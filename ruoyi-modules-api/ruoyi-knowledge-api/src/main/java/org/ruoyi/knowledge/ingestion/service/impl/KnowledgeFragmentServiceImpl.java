package org.ruoyi.knowledge.ingestion.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeFragment;
import org.ruoyi.knowledge.curation.domain.KnowledgeItem;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeFragmentBo;
import org.ruoyi.knowledge.ingestion.enums.ProcessingStatus;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeAttachVo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeFragmentPageVo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeFragmentVo;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeAttachMapper;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeFragmentMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemFragmentMapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.ingestion.service.IKnowledgeFragmentService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识片段Service业务层处理
 *
 * @author ageerle
 * @date 2025-04-08
 */
@RequiredArgsConstructor
@Service
public class KnowledgeFragmentServiceImpl implements IKnowledgeFragmentService {

    private final KnowledgeFragmentMapper baseMapper;
    private final KnowledgeAttachMapper attachMapper;
    private final KnowledgeItemMapper knowledgeItemMapper;
    private final KnowledgeItemFragmentMapper itemFragmentMapper;

    /**
     * 查询知识片段
     */
    @Override
    public KnowledgeFragmentVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询知识片段列表（支持关联查询附件信息和分面统计）
     */
    @Override
    public TableDataInfo<KnowledgeFragmentVo> queryPageList(KnowledgeFragmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeFragment> lqw = buildQueryWrapper(bo);
        
        //处理排序：优先使用bo中的orderBy
        String orderBy = bo.getOrderBy();
        String order = bo.getOrder();
        
        //如果bo中没有排序信息，尝试从pageQuery获取
        if (StringUtils.isBlank(orderBy) && StringUtils.isNotBlank(pageQuery.getOrderByColumn())) {
            orderBy = pageQuery.getOrderByColumn();
            order = pageQuery.getIsAsc();
        }
        
        //构建Page对象（实体类型，selectVoPage需要实体类型的分页对象）
        Page<KnowledgeFragment> page = pageQuery.build();
        
        //应用排序（relevance 在内存中排序，此处用 idx 作为占位）
        if (StringUtils.isNotBlank(orderBy)) {
            boolean isAsc = "asc".equalsIgnoreCase(order);
            //清除PageQuery的默认排序，使用手动排序
            page.orders().clear();
            
            if ("relevance".equalsIgnoreCase(orderBy)) {
                // 相关程度在查询后内存排序，此处用 idx 保证分页稳定
                lqw.orderByAsc(KnowledgeFragment::getIdx);
            } else if ("idx".equals(orderBy)) {
                lqw.orderBy(true, isAsc, KnowledgeFragment::getIdx);
            } else if ("create_time".equals(orderBy) || "createTime".equals(orderBy)) {
                lqw.orderBy(true, isAsc, KnowledgeFragment::getCreateTime);
            } else if ("update_time".equals(orderBy) || "updateTime".equals(orderBy)) {
                lqw.orderBy(true, isAsc, KnowledgeFragment::getUpdateTime);
            } else if ("content_length".equals(orderBy) || "contentLength".equals(orderBy)) {
                //按内容长度排序（使用SQL函数LENGTH）
                String orderDirection = isAsc ? "ASC" : "DESC";
                lqw.last("ORDER BY LENGTH(content) " + orderDirection);
            } else {
                //其他字段：使用PageQuery的排序机制
                page.addOrder(isAsc ? OrderItem.asc(orderBy) : OrderItem.desc(orderBy));
            }
        } else {
            //默认按索引排序
            page.orders().clear();
            lqw.orderByAsc(KnowledgeFragment::getIdx);
        }
        
        Page<KnowledgeFragmentVo> result = baseMapper.selectVoPage(page, lqw);
        
        //填充附件信息
        if (CollUtil.isNotEmpty(result.getRecords())) {
            fillAttachmentInfo(result.getRecords());
            fillItemInfo(result.getRecords());
        }
        
        // 有搜索关键词且按相关程度排序时，对结果按内容匹配度排序
        if ("relevance".equalsIgnoreCase(orderBy) && StringUtils.isNotBlank(bo.getSearchKeyword())) {
            sortByRelevance(result.getRecords(), bo.getSearchKeyword());
        }
        
        //如果需要分面统计，返回KnowledgeFragmentPageVo
        //否则返回普通的TableDataInfo
        return TableDataInfo.build(result);
    }
    
    /**
     * 查询知识片段列表（带分面统计）
     */
    public KnowledgeFragmentPageVo queryPageListWithFacetStats(KnowledgeFragmentBo bo, PageQuery pageQuery) {
        TableDataInfo<KnowledgeFragmentVo> tableData = queryPageList(bo, pageQuery);
        
        KnowledgeFragmentPageVo pageVo = new KnowledgeFragmentPageVo();
        pageVo.setTotal(tableData.getTotal());
        pageVo.setRows(tableData.getRows());
        pageVo.setCode(tableData.getCode());
        pageVo.setMsg(tableData.getMsg());
        
        //计算分面统计
        Map<String, Long> facetStats = calculateAttachmentFacetStats(bo);
        pageVo.setFacetStats(facetStats);
        
        return pageVo;
    }

    /**
     * 查询知识片段列表
     */
    @Override
    public List<KnowledgeFragmentVo> queryList(KnowledgeFragmentBo bo) {
        LambdaQueryWrapper<KnowledgeFragment> lqw = buildQueryWrapper(bo);
        List<KnowledgeFragmentVo> fragments = baseMapper.selectVoList(lqw);
        if (CollUtil.isNotEmpty(fragments)) {
            fillAttachmentInfo(fragments);
            fillItemInfo(fragments);
        }
        return fragments;
    }

    private LambdaQueryWrapper<KnowledgeFragment> buildQueryWrapper(KnowledgeFragmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KnowledgeFragment> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeFragment::getKid, bo.getKid());
        // 通过 N:M 中间表过滤"某个条目关联的片段"
        if (StringUtils.isNotBlank(bo.getItemUuid())) {
            List<KnowledgeItemFragment> rels = itemFragmentMapper.selectList(
                Wrappers.<KnowledgeItemFragment>lambdaQuery()
                    .eq(KnowledgeItemFragment::getItemUuid, bo.getItemUuid())
            );
            if (CollUtil.isNotEmpty(rels)) {
                List<Long> fragmentIds = rels.stream().map(KnowledgeItemFragment::getFragmentId).collect(Collectors.toList());
                lqw.in(KnowledgeFragment::getId, fragmentIds);
            } else {
                // 没有关联片段，返回空结果
                lqw.eq(KnowledgeFragment::getId, -1L);
            }
        }
        lqw.eq(StringUtils.isNotBlank(bo.getDocId()), KnowledgeFragment::getDocId, bo.getDocId());
        lqw.eq(StringUtils.isNotBlank(bo.getFid()), KnowledgeFragment::getFid, bo.getFid());
        lqw.eq(bo.getIdx() != null, KnowledgeFragment::getIdx, bo.getIdx());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), KnowledgeFragment::getContent, bo.getContent());
        
        //搜索关键词（内容模糊匹配）
        if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
            lqw.like(KnowledgeFragment::getContent, bo.getSearchKeyword());
        }
        
        //附件筛选（多选）
        if (CollUtil.isNotEmpty(bo.getDocIds())) {
            lqw.in(KnowledgeFragment::getDocId, bo.getDocIds());
        }
        
        lqw.eq(KnowledgeFragment::getDelFlag, "0");
        
        //过滤掉未完成处理的片段：只过滤那些关联的处理任务未完成的片段
        //保留：没有关联处理任务的片段（旧数据）、处理任务已完成的片段
        //如果includeIncomplete为true，则不进行过滤（用于审阅页面等场景）
        if (bo.getIncludeIncomplete() == null || !bo.getIncludeIncomplete()) {
            String completedStatus = ProcessingStatus.COMPLETED.getCode();
            //过滤掉关联的处理任务未完成的片段
            String notExistsSql = "NOT EXISTS (SELECT 1 FROM knowledge_attach_process p " +
                "WHERE p.doc_id = knowledge_fragment.doc_id " +
                "AND p.current_status != '" + completedStatus + "' " +
                "AND p.del_flag = '0')";
            lqw.apply(notExistsSql);
        }
        
        return lqw;
    }
    
    /**
     * 按关键词相关程度排序（片段仅有 content 字段参与评分）
     */
    private void sortByRelevance(List<KnowledgeFragmentVo> items, String keyword) {
        if (items == null || items.isEmpty() || StringUtils.isBlank(keyword)) {
            return;
        }
        String lowerKeyword = keyword.toLowerCase().trim();
        String[] keywords = lowerKeyword.split("\\s+");
        items.sort((a, b) -> {
            double scoreA = calculateRelevanceScore(a, keywords);
            double scoreB = calculateRelevanceScore(b, keywords);
            return Double.compare(scoreB, scoreA);
        });
    }
    
    private double calculateRelevanceScore(KnowledgeFragmentVo fragment, String[] keywords) {
        double score = 0.0;
        String content = fragment.getContent();
        if (content == null) return 0;
        String lowerContent = content.toLowerCase();
        for (String keyword : keywords) {
            score += countMatches(content, keyword) * 2.0;
            if (lowerContent.startsWith(keyword)) score += 5.0;
            if (lowerContent.equals(keyword)) score += 10.0;
        }
        return score;
    }
    
    private int countMatches(String text, String keyword) {
        if (text == null || keyword == null) return 0;
        String lowerText = text.toLowerCase();
        int count = 0;
        int index = 0;
        while ((index = lowerText.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }
        return count;
    }
    
    /**
     * 填充附件信息
     */
    private void fillAttachmentInfo(List<KnowledgeFragmentVo> fragments) {
        if (CollUtil.isEmpty(fragments)) {
            return;
        }
        
        //提取所有docId（去重）
        List<String> docIds = fragments.stream()
            .map(KnowledgeFragmentVo::getDocId)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .collect(Collectors.toList());
        
        if (docIds.isEmpty()) {
            return;
        }
        
        //批量查询附件信息
        List<KnowledgeAttach> attaches = attachMapper.selectList(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .in(KnowledgeAttach::getDocId, docIds)
        );
        
        //构建docId到附件的映射
        Map<String, KnowledgeAttach> attachMap = attaches.stream()
            .collect(Collectors.toMap(KnowledgeAttach::getDocId, attach -> attach, (a, b) -> a));
        
        //填充附件信息到片段VO
        for (KnowledgeFragmentVo fragment : fragments) {
            if (StringUtils.isNotBlank(fragment.getDocId()) && attachMap.containsKey(fragment.getDocId())) {
                KnowledgeAttach attach = attachMap.get(fragment.getDocId());
                fragment.setDocName(attach.getDocName());
                fragment.setAttachId(attach.getId());
                fragment.setDocType(attach.getDocType());
            }
        }
    }
    
    /**
     * 填充知识条目信息
     */
    private void fillItemInfo(List<KnowledgeFragmentVo> fragments) {
        if (CollUtil.isEmpty(fragments)) {
            return;
        }
        
        //提取所有itemUuid（去重）
        List<String> itemUuids = fragments.stream()
            .map(KnowledgeFragmentVo::getItemUuid)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .collect(Collectors.toList());
        
        if (itemUuids.isEmpty()) {
            return;
        }
        
        //批量查询知识条目信息
        List<KnowledgeItem> items = knowledgeItemMapper.selectList(
            Wrappers.<KnowledgeItem>lambdaQuery()
                .in(KnowledgeItem::getItemUuid, itemUuids)
                .eq(KnowledgeItem::getDelFlag, "0")
        );
        
        //构建itemUuid到条目的映射
        Map<String, KnowledgeItem> itemMap = items.stream()
            .collect(Collectors.toMap(KnowledgeItem::getItemUuid, item -> item, (a, b) -> a));
        
        //填充条目信息到片段VO
        for (KnowledgeFragmentVo fragment : fragments) {
            if (StringUtils.isNotBlank(fragment.getItemUuid()) && itemMap.containsKey(fragment.getItemUuid())) {
                KnowledgeItem item = itemMap.get(fragment.getItemUuid());
                fragment.setItemTitle(item.getTitle());
            }
        }
    }
    
    /**
     * 计算附件分布统计（基于所有符合条件的片段，应用搜索和筛选条件）
     */
    private Map<String, Long> calculateAttachmentFacetStats(KnowledgeFragmentBo bo) {
        Map<String, Long> stats = new HashMap<>();
        
        //构建查询条件（应用搜索和筛选，但不应用附件筛选，以便统计所有可能的附件）
        LambdaQueryWrapper<KnowledgeFragment> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeFragment::getKid, bo.getKid());
        // 通过 N:M 中间表过滤"某个条目关联的片段"
        if (StringUtils.isNotBlank(bo.getItemUuid())) {
            List<KnowledgeItemFragment> rels = itemFragmentMapper.selectList(
                Wrappers.<KnowledgeItemFragment>lambdaQuery()
                    .eq(KnowledgeItemFragment::getItemUuid, bo.getItemUuid())
            );
            if (CollUtil.isNotEmpty(rels)) {
                List<Long> fragmentIds = rels.stream().map(KnowledgeItemFragment::getFragmentId).collect(Collectors.toList());
                lqw.in(KnowledgeFragment::getId, fragmentIds);
            } else {
                lqw.eq(KnowledgeFragment::getId, -1L);
            }
        }
        
        //应用搜索关键词（但不应用附件筛选，以便统计所有附件）
        if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
            lqw.like(KnowledgeFragment::getContent, bo.getSearchKeyword());
        }
        
        lqw.eq(KnowledgeFragment::getDelFlag, "0");
        
        //应用相同的过滤逻辑（过滤未完成处理的片段）
        if (bo.getIncludeIncomplete() == null || !bo.getIncludeIncomplete()) {
            String completedStatus = ProcessingStatus.COMPLETED.getCode();
            String notExistsSql = "NOT EXISTS (SELECT 1 FROM knowledge_attach_process p " +
                "WHERE p.doc_id = knowledge_fragment.doc_id " +
                "AND p.current_status != '" + completedStatus + "' " +
                "AND p.del_flag = '0')";
            lqw.apply(notExistsSql);
        }
        
        //查询所有符合条件的片段（不分页）
        List<KnowledgeFragment> fragments = baseMapper.selectList(lqw);
        
        if (CollUtil.isEmpty(fragments)) {
            return stats;
        }
        
        //提取所有docId
        List<String> docIds = fragments.stream()
            .map(KnowledgeFragment::getDocId)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .collect(Collectors.toList());
        
        if (docIds.isEmpty()) {
            return stats;
        }
        
        //批量查询附件信息
        List<KnowledgeAttach> attaches = attachMapper.selectList(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .in(KnowledgeAttach::getDocId, docIds)
        );
        
        //构建docId到附件名称的映射
        Map<String, String> docIdToNameMap = attaches.stream()
            .collect(Collectors.toMap(KnowledgeAttach::getDocId, KnowledgeAttach::getDocName, (a, b) -> a));
        
        //计算附件分布统计（基于所有符合条件的片段）
        for (KnowledgeFragment fragment : fragments) {
            if (StringUtils.isNotBlank(fragment.getDocId())) {
                String docName = docIdToNameMap.getOrDefault(fragment.getDocId(), fragment.getDocId());
                stats.merge(docName, 1L, Long::sum);
            }
        }
        
        return stats;
    }

    /**
     * 新增知识片段
     */
    @Override
    public Boolean insertByBo(KnowledgeFragmentBo bo) {
        KnowledgeFragment add = MapstructUtils.convert(bo, KnowledgeFragment.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改知识片段
     */
    @Override
    public Boolean updateByBo(KnowledgeFragmentBo bo) {
        KnowledgeFragment update = MapstructUtils.convert(bo, KnowledgeFragment.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KnowledgeFragment entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除知识片段
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
