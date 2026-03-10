package org.ruoyi.knowledge.curation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.SpringUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import cn.hutool.http.HttpStatus;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.curation.domain.KnowledgeItem;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemTag;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemVulnerabilityType;
import org.ruoyi.knowledge.curation.domain.KnowledgeTag;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeItemBo;
import org.ruoyi.knowledge.curation.domain.vo.ClusterWithItemsVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweClusterMappingVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweClusterVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweReferenceVo;
import org.ruoyi.knowledge.curation.domain.vo.FacetStatsVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemPageVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeInfoVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeTagVo;
import org.ruoyi.knowledge.curation.domain.vo.VulnerabilityDistributionVo;
import org.ruoyi.knowledge.curation.domain.vo.CweDistributionItemVo;
import org.ruoyi.knowledge.curation.domain.vo.SeverityDistributionItemVo;
import org.ruoyi.knowledge.cwe.domain.CweReference;
import org.ruoyi.knowledge.curation.domain.vo.BatchDeleteResultVo;
import org.ruoyi.knowledge.curation.domain.vo.BatchUpdateResultVo;
import org.ruoyi.knowledge.curation.domain.vo.DeleteFailureVo;
import org.ruoyi.knowledge.curation.domain.vo.UpdateFailureVo;
import org.ruoyi.knowledge.curation.domain.vo.ExportPreviewVo;
import org.ruoyi.knowledge.curation.domain.bo.BatchUpdateRequestBo;
import org.ruoyi.knowledge.curation.domain.bo.ExportPreviewRequestBo;
import org.ruoyi.knowledge.curation.domain.bo.ExportRequestBo;
import org.ruoyi.knowledge.shared.utils.CvssScoreCalculator;
import org.ruoyi.knowledge.cwe.mapper.CweClusterMapper;
import org.ruoyi.knowledge.cwe.mapper.CweClusterMappingMapper;
import org.ruoyi.knowledge.cwe.mapper.CweReferenceMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemFragmentMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemTagMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemVulnerabilityTypeMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeTagMapper;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeFragmentMapper;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeAttachProcessMapper;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeAttachMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeInfoMapper;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeFragment;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttachProcess;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;
import org.ruoyi.knowledge.curation.domain.KnowledgeInfo;
import org.ruoyi.knowledge.ingestion.enums.ProcessingStatus;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemHistoryService;
import org.ruoyi.system.service.ISysOssService;
import org.ruoyi.system.domain.vo.SysOssVo;
import org.ruoyi.common.oss.core.OssClient;
import org.ruoyi.common.oss.factory.OssFactory;
import org.ruoyi.system.service.ISysUserService;
import org.ruoyi.system.domain.vo.SysUserVo;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Collections;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 知识条目Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KnowledgeItemServiceImpl implements IKnowledgeItemService {

    private final KnowledgeItemMapper baseMapper;
    private final KnowledgeItemFragmentMapper itemFragmentMapper;
    private final KnowledgeItemVulnerabilityTypeMapper vulnerabilityTypeMapper;
    private final KnowledgeItemTagMapper itemTagMapper;
    private final KnowledgeTagMapper knowledgeTagMapper;
    private final CweClusterMapper cweClusterMapper;
    private final CweClusterMappingMapper cweClusterMappingMapper;
    private final CweReferenceMapper cweReferenceMapper;
    private final KnowledgeFragmentMapper fragmentMapper;
    private final KnowledgeAttachProcessMapper attachProcessMapper;
    private final KnowledgeAttachMapper attachMapper;
    private final KnowledgeInfoMapper knowledgeInfoMapper;
    private final ISysOssService sysOssService;
    private final ISysUserService sysUserService;
    private final IKnowledgeItemHistoryService knowledgeItemHistoryService;
    @Override
    public KnowledgeItemVo queryById(Long id) {
        KnowledgeItemVo vo = baseMapper.selectVoById(id);
        if (vo != null && StringUtils.isNotBlank(vo.getItemUuid())) {
            loadVulnerabilityTypes(vo);
            fillUserNames(Collections.singletonList(vo));
        }
        return vo;
    }

    @Override
    public KnowledgeItemVo queryByItemUuid(String itemUuid) {
        KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
        if (entity == null) {
            return null;
        }
        KnowledgeItemVo vo = baseMapper.selectVoById(entity.getId());
        if (vo != null) {
            loadVulnerabilityTypes(vo);
            fillUserNames(Collections.singletonList(vo));
        }
        return vo;
    }

    /**
     * 加载漏洞类型列表
     */
    private void loadVulnerabilityTypes(KnowledgeItemVo vo) {
        if (StringUtils.isNotBlank(vo.getItemUuid())) {
            List<String> cweIds = vulnerabilityTypeMapper.selectCweIdsByItemUuid(vo.getItemUuid());
            vo.setVulnerabilityTypes(cweIds);
            if (CollectionUtils.isNotEmpty(cweIds) && cweIds.size() == 1) {
                vo.setVulnerabilityType(cweIds.get(0));
            }
            List<String> tagNames = loadTagNamesByItemUuid(vo.getItemUuid());
            vo.setTags(tagNames);
        }
    }
    private List<String> loadTagNamesByItemUuid(String itemUuid) {
        if (StringUtils.isBlank(itemUuid)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
        itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
        List<KnowledgeItemTag> itemTags = itemTagMapper.selectList(itemTagLqw);
        if (CollectionUtils.isEmpty(itemTags)) {
            return new ArrayList<>();
        }
        List<Long> tagIds = itemTags.stream()
            .map(KnowledgeItemTag::getTagId)
            .collect(Collectors.toList());
        LambdaQueryWrapper<KnowledgeTag> tagLqw = Wrappers.lambdaQuery();
        tagLqw.in(KnowledgeTag::getId, tagIds);
        List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagLqw);
        return tags.stream()
            .map(KnowledgeTag::getTagName)
            .collect(Collectors.toList());
    }

    /**
     * 批量填充片段数量
     */
    private void fillFragmentCounts(List<KnowledgeItemVo> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        List<String> itemUuids = items.stream()
            .map(KnowledgeItemVo::getItemUuid)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .collect(Collectors.toList());
        if (itemUuids.isEmpty()) {
            return;
        }
        // 通过 N:M 中间表统计每个条目关联的片段数
        Map<String, Integer> fragmentCountMap = new HashMap<>();
        List<KnowledgeItemFragment> rels = itemFragmentMapper.selectList(
            Wrappers.<KnowledgeItemFragment>lambdaQuery()
                .in(KnowledgeItemFragment::getItemUuid, itemUuids)
        );
        fragmentCountMap = rels.stream()
        .collect(Collectors.groupingBy(
                KnowledgeItemFragment::getItemUuid,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
        ));
        for (KnowledgeItemVo item : items) {
            if (StringUtils.isNotBlank(item.getItemUuid()) && fragmentCountMap.containsKey(item.getItemUuid())) {
                item.setFragmentCount(fragmentCountMap.get(item.getItemUuid()));
            } else {
                item.setFragmentCount(0);
            }
        }
    }

    /**
     * 为列表数据填充创建人/更新人名称，供前端展示「创建人」列使用。
     * 仅在分页查询（列表页）中调用，避免对所有 queryList 调用增加不必要的负担。
     */
    private void fillUserNames(List<KnowledgeItemVo> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        Set<Long> userIds = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getCreateBy() != null) {
                userIds.add(item.getCreateBy());
            }
            if (item.getUpdateBy() != null) {
                userIds.add(item.getUpdateBy());
            }
        }
        if (userIds.isEmpty()) {
            return;
        }
        Map<Long, String> userMap = new HashMap<>();
        for (Long userId : userIds) {
            SysUserVo user = sysUserService.selectUserById(userId);
            if (user != null && StringUtils.isNotBlank(user.getUserName())) {
                userMap.put(userId, user.getUserName());
            }
        }
        for (KnowledgeItemVo item : items) {
            if (item.getCreateBy() != null) {
                item.setCreateByName(userMap.get(item.getCreateBy()));
            }
            if (item.getUpdateBy() != null) {
                item.setUpdateByName(userMap.get(item.getUpdateBy()));
            }
        }
    }

    @Override
    public TableDataInfo<KnowledgeItemVo> queryPageList(KnowledgeItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        Page<KnowledgeItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        
        if (result.getRecords() != null) {
            result.getRecords().forEach(this::loadVulnerabilityTypes);
            fillFragmentCounts(result.getRecords());
            fillUserNames(result.getRecords());
            if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
                sortByRelevance(result.getRecords(), bo.getSearchKeyword());
            }
        }
        FacetStatsVo facetStats = calculateFacetStats(bo);
        List<ClusterWithItemsVo> groupedByClusters = calculateGroupedByClusters(facetStats, bo.getVulnerabilityTypeKeyword());
        KnowledgeItemPageVo pageVo = new KnowledgeItemPageVo();
        pageVo.setTotal(result.getTotal());
        pageVo.setRows(result.getRecords());
        pageVo.setCode(HttpStatus.HTTP_OK);
        pageVo.setMsg("查询成功");
        pageVo.setFacetStats(facetStats);
        pageVo.setGroupedByClusters(groupedByClusters);
        return pageVo;
    }

    @Override
    public List<KnowledgeItemVo> queryList(KnowledgeItemBo bo) {
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        List<KnowledgeItemVo> list = baseMapper.selectVoList(lqw);
        // 加载漏洞类型列表和片段数量
        if (list != null) {
            list.forEach(this::loadVulnerabilityTypes);
            fillFragmentCounts(list);
        }
        return list;
    }

    private LambdaQueryWrapper<KnowledgeItem> buildQueryWrapper(KnowledgeItemBo bo) {
        LambdaQueryWrapper<KnowledgeItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeItem::getKid, bo.getKid());
        if (bo.getItemUuids() != null) {
            if (CollectionUtils.isNotEmpty(bo.getItemUuids())) {
                lqw.in(KnowledgeItem::getItemUuid, bo.getItemUuids());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        } else {
            lqw.eq(StringUtils.isNotBlank(bo.getItemUuid()), KnowledgeItem::getItemUuid, bo.getItemUuid());
        }
        if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
            String keyword = bo.getSearchKeyword();
            lqw.and(wrapper -> wrapper
                .like(KnowledgeItem::getTitle, keyword)
                .or()
                .like(KnowledgeItem::getSummary, keyword)
                .or()
                .like(KnowledgeItem::getProblemDescription, keyword)
                .or()
                .like(KnowledgeItem::getFixSolution, keyword)
            );
        } else {
            lqw.like(StringUtils.isNotBlank(bo.getTitle()), KnowledgeItem::getTitle, bo.getTitle());
            lqw.like(StringUtils.isNotBlank(bo.getSummary()), KnowledgeItem::getSummary, bo.getSummary());
            lqw.like(StringUtils.isNotBlank(bo.getProblemDescription()), KnowledgeItem::getProblemDescription, bo.getProblemDescription());
        }
        lqw.eq(StringUtils.isNotBlank(bo.getVulnerabilityType()), KnowledgeItem::getVulnerabilityType, bo.getVulnerabilityType());
        lqw.eq(StringUtils.isNotBlank(bo.getLanguage()), KnowledgeItem::getLanguage, bo.getLanguage());
        lqw.eq(StringUtils.isNotBlank(bo.getSeverity()), KnowledgeItem::getSeverity, bo.getSeverity());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), KnowledgeItem::getStatus, bo.getStatus());
        if (bo.getLanguages() != null) {
            if (CollectionUtils.isNotEmpty(bo.getLanguages())) {
                lqw.in(KnowledgeItem::getLanguage, bo.getLanguages());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getSeverities() != null) {
            if (CollectionUtils.isNotEmpty(bo.getSeverities())) {
                lqw.in(KnowledgeItem::getSeverity, bo.getSeverities());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getStatuses() != null) {
            if (CollectionUtils.isNotEmpty(bo.getStatuses())) {
                lqw.in(KnowledgeItem::getStatus, bo.getStatuses());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getTags() != null) {
            if (CollectionUtils.isNotEmpty(bo.getTags())) {
                LambdaQueryWrapper<KnowledgeTag> tagQueryWrapper = Wrappers.lambdaQuery();
                tagQueryWrapper.in(KnowledgeTag::getTagName, bo.getTags());
                List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagQueryWrapper);
                if (CollectionUtils.isNotEmpty(tags)) {
                    List<Long> tagIds = tags.stream()
                            .map(KnowledgeTag::getId)
                            .collect(Collectors.toList());
                    LambdaQueryWrapper<KnowledgeItemTag> itemTagQueryWrapper = Wrappers.lambdaQuery();
                    itemTagQueryWrapper.select(KnowledgeItemTag::getItemUuid)
                            .in(KnowledgeItemTag::getTagId, tagIds);
                    List<KnowledgeItemTag> itemTags = itemTagMapper.selectList(itemTagQueryWrapper);
                    if (CollectionUtils.isNotEmpty(itemTags)) {
                        List<String> itemUuids = itemTags.stream()
                                .map(KnowledgeItemTag::getItemUuid)
                                .distinct()
                                .collect(Collectors.toList());
                        lqw.in(KnowledgeItem::getItemUuid, itemUuids);
                    } else {
                        lqw.isNull(KnowledgeItem::getItemUuid);
                    }
                } else {
                    lqw.isNull(KnowledgeItem::getItemUuid);
                }
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getVulnerabilityTypes() != null) {
            if (CollectionUtils.isNotEmpty(bo.getVulnerabilityTypes())) {
                LambdaQueryWrapper<KnowledgeItemVulnerabilityType> vulnLqw = Wrappers.lambdaQuery();
                vulnLqw.select(KnowledgeItemVulnerabilityType::getItemUuid)
                        .in(KnowledgeItemVulnerabilityType::getCweId, bo.getVulnerabilityTypes());
                List<KnowledgeItemVulnerabilityType> vulnList = vulnerabilityTypeMapper.selectList(vulnLqw);
                if (CollectionUtils.isNotEmpty(vulnList)) {
                    List<String> itemUuids = vulnList.stream()
                            .map(KnowledgeItemVulnerabilityType::getItemUuid)
                            .distinct()
                            .collect(Collectors.toList());
                    lqw.in(KnowledgeItem::getItemUuid, itemUuids);
                } else {
                    lqw.isNull(KnowledgeItem::getItemUuid);
                }
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssScoreMin() != null) {
            lqw.ge(KnowledgeItem::getCvssScore, bo.getCvssScoreMin());
        }
        if (bo.getCvssScoreMax() != null) {
            lqw.le(KnowledgeItem::getCvssScore, bo.getCvssScoreMax());
        }
        if (bo.getCvssAttackVector() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssAttackVector())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssAttackVector()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "AV:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssAttackComplexity() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssAttackComplexity())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssAttackComplexity()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "AC:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssPrivilegesRequired() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssPrivilegesRequired())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssPrivilegesRequired()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "PR:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssUserInteraction() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssUserInteraction())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssUserInteraction()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "UI:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssScope() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssScope())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssScope()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "S:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssConfidentiality() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssConfidentiality())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssConfidentiality()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "C:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssIntegrity() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssIntegrity())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssIntegrity()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "I:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssAvailability() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssAvailability())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssAvailability()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "A:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCreateTimeStart() != null) {
            lqw.ge(KnowledgeItem::getCreateTime, bo.getCreateTimeStart());
        }
        if (bo.getCreateTimeEnd() != null) {
            lqw.le(KnowledgeItem::getCreateTime, bo.getCreateTimeEnd());
        }
        if (bo.getUpdateTimeStart() != null) {
            lqw.ge(KnowledgeItem::getUpdateTime, bo.getUpdateTimeStart());
        }
        if (bo.getUpdateTimeEnd() != null) {
            lqw.le(KnowledgeItem::getUpdateTime, bo.getUpdateTimeEnd());
        }
        
        // 过滤掉未完成处理的新建条目：只过滤那些在 llmCreatedItems 中且关联的处理任务未完成的条目
        // 通过 N:M 中间表 knowledge_item_fragment 关联片段和处理任务
        String completedStatus = ProcessingStatus.COMPLETED.getCode();
        String notExistsSql = "NOT EXISTS (" +
            "SELECT 1 FROM knowledge_item_fragment kif " +
            "INNER JOIN knowledge_fragment f ON f.id = kif.fragment_id " +
            "INNER JOIN knowledge_attach_process p ON f.doc_id = p.doc_id " +
            "WHERE kif.item_uuid = knowledge_item.item_uuid " +
            "AND f.del_flag = '0' " +
            "AND p.current_status != '" + completedStatus + "' " +
            "AND JSON_SEARCH(p.status_data, 'one', knowledge_item.item_uuid, NULL, '$.llmCreatedItems[*].itemUuid') IS NOT NULL)";
        
        lqw.apply(notExistsSql);
        
        return lqw;
    }

    private void sortByRelevance(List<KnowledgeItemVo> items, String keyword) {
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

    private double calculateRelevanceScore(KnowledgeItemVo item, String[] keywords) {
        double score = 0.0;
        for (String keyword : keywords) {
            score += countMatches(item.getTitle(), keyword) * 4.0;
            score += countMatches(item.getSummary(), keyword) * 2.0;
            score += countMatches(item.getProblemDescription(), keyword) * 2.0;
            score += countMatches(item.getFixSolution(), keyword) * 1.0;
            if (item.getTitle() != null && item.getTitle().toLowerCase().startsWith(keyword)) {
                score += 10.0;
            }
            if (item.getTitle() != null && item.getTitle().toLowerCase().equals(keyword)) {
                score += 20.0;
            }
        }
        return score;
    }

    private int countMatches(String text, String keyword) {
        if (text == null || keyword == null) {
            return 0;
        }
        String lowerText = text.toLowerCase();
        int count = 0;
        int index = 0;
        while ((index = lowerText.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }
        return count;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(KnowledgeItemBo bo) {
        KnowledgeItem add = MapstructUtils.convert(bo, KnowledgeItem.class);
        if (StringUtils.isBlank(add.getItemUuid())) {
            add.setItemUuid(UUID.randomUUID().toString().replace("-", ""));
        }
        if (CollectionUtils.isNotEmpty(bo.getVulnerabilityTypes()) && StringUtils.isBlank(add.getVulnerabilityType())) {
            add.setVulnerabilityType(bo.getVulnerabilityTypes().get(0));
        }
        //显式设置 createBy，确保即使自动填充失效也能正确设置
        Long currentUserId = LoginHelper.getUserId();
        if (currentUserId != null && add.getCreateBy() == null) {
            add.setCreateBy(currentUserId);
        }
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            bo.setItemUuid(add.getItemUuid());
            saveVulnerabilityTypes(add.getItemUuid(), bo.getVulnerabilityTypes());
            saveItemTags(add.getItemUuid(), bo.getTags());
            if (StringUtils.isNotBlank(add.getKid()) && !isItemInUncompletedProcess(add.getItemUuid())) {
                baseMapper.updateKnowledgeItemCount(add.getKid());
                updateKnowledgeDataSize(add.getKid());
            }
            // 创建初始版本快照（v1）
            knowledgeItemHistoryService.createVersionSnapshot(add.getItemUuid(), "create", "创建条目");
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(KnowledgeItemBo bo) {
        if (StringUtils.isBlank(bo.getItemUuid())) {
            throw new ServiceException("知识条目UUID不能为空");
        }
        KnowledgeItem existing = baseMapper.selectByItemUuid(bo.getItemUuid());
        if (existing == null) {
            throw new ServiceException("知识条目不存在");
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        if (!isSuperAdmin && !Objects.equals(existing.getCreateBy(), currentUserId)) {
            throw new ServiceException("无权限编辑此知识条目，仅作者或管理员可以编辑");
        }
        KnowledgeItem update = MapstructUtils.convert(bo, KnowledgeItem.class);
        update.setId(existing.getId());
        update.setItemUuid(existing.getItemUuid());
        if (CollectionUtils.isNotEmpty(bo.getVulnerabilityTypes()) && StringUtils.isBlank(update.getVulnerabilityType())) {
            update.setVulnerabilityType(bo.getVulnerabilityTypes().get(0));
        }
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag && StringUtils.isNotBlank(update.getItemUuid())) {
            saveVulnerabilityTypes(update.getItemUuid(), bo.getVulnerabilityTypes());
            saveItemTags(update.getItemUuid(), bo.getTags());
            if (StringUtils.isNotBlank(existing.getKid()) && !isItemInUncompletedProcess(update.getItemUuid())) {
                baseMapper.updateKnowledgeItemCount(existing.getKid());
                updateKnowledgeDataSize(existing.getKid());
            }
            // 根据状态变化记录版本快照
            String changeType = "update";
            if (!Objects.equals(existing.getStatus(), update.getStatus())) {
                if ("published".equals(update.getStatus())) {
                    changeType = "publish";
                } else if ("archived".equals(update.getStatus())) {
                    changeType = "archive";
                }
            }
            knowledgeItemHistoryService.createVersionSnapshot(update.getItemUuid(), changeType, null);
        }
        return flag;
    }

    /**
     * 保存漏洞类型关联关系
     */
    private void saveVulnerabilityTypes(String itemUuid, List<String> vulnerabilityTypes) {
        if (StringUtils.isBlank(itemUuid)) {
            return;
        }
        vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
        if (CollectionUtils.isNotEmpty(vulnerabilityTypes)) {
            // 查询所有可用的CWE ID（从数据库获取实际格式）
            List<CweReferenceVo> allCwes = cweReferenceMapper.selectVoList(Wrappers.lambdaQuery());
            // 创建映射：标准化格式 -> 数据库实际格式
            Map<String, String> cweIdMap = new HashMap<>();
            for (CweReferenceVo cwe : allCwes) {
                if (StringUtils.isNotBlank(cwe.getCweId())) {
                    String dbCweId = cwe.getCweId();
                    // 创建多个可能的匹配键
                    String normalized = dbCweId.trim().toUpperCase();
                    cweIdMap.put(normalized, dbCweId);
                    if (normalized.startsWith("CWE-")) {
                        String numericPart = normalized.replace("CWE-", "");
                        cweIdMap.put(numericPart, dbCweId);
                        cweIdMap.put("CWE-" + numericPart, dbCweId);
                    }
                }
            }
            
            String tenantId = LoginHelper.getTenantId();
            Long tenantIdLong = 0L;
            if (StringUtils.isNotBlank(tenantId)) {
                try {
                    tenantIdLong = Long.parseLong(tenantId);
                } catch (NumberFormatException e) {
                    tenantIdLong = 0L;
                }
            }
            final Long finalTenantId = tenantIdLong;
            List<KnowledgeItemVulnerabilityType> list = vulnerabilityTypes.stream()
                    .filter(StringUtils::isNotBlank)
                    .map(cweId -> {
                        // 标准化输入格式用于匹配
                        String normalized = cweId.trim().toUpperCase();
                        String matchedCweId = null;
                        
                        // 尝试精确匹配
                        if (cweIdMap.containsKey(normalized)) {
                            matchedCweId = cweIdMap.get(normalized);
                        } else if (normalized.startsWith("CWE-")) {
                            // 尝试数字部分匹配
                            String numericPart = normalized.replace("CWE-", "");
                            if (cweIdMap.containsKey(numericPart)) {
                                matchedCweId = cweIdMap.get(numericPart);
                            }
                        } else {
                            // 尝试添加CWE-前缀匹配
                            String withPrefix = "CWE-" + normalized;
                            if (cweIdMap.containsKey(withPrefix)) {
                                matchedCweId = cweIdMap.get(withPrefix);
                            } else if (cweIdMap.containsKey(normalized)) {
                                matchedCweId = cweIdMap.get(normalized);
                            }
                        }
                        
                        return matchedCweId;
                    })
                    .filter(cweId -> cweId != null)
                    .distinct()
                    .map(cweId -> {
                        KnowledgeItemVulnerabilityType item = new KnowledgeItemVulnerabilityType();
                        item.setItemUuid(itemUuid);
                        item.setCweId(cweId);
                        item.setTenantId(finalTenantId);
                        return item;
                    })
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(list)) {
                vulnerabilityTypeMapper.insertBatch(list);
            } else if (CollectionUtils.isNotEmpty(vulnerabilityTypes)) {
                log.warn("[保存漏洞类型] 所有CWE ID验证失败，未保存任何漏洞类型关联，itemUuid={}, 输入的CWE IDs={}", 
                    itemUuid, vulnerabilityTypes);
            }
        }
    }

    private void saveItemTags(String itemUuid, List<String> tagNames) {
        if (StringUtils.isBlank(itemUuid)) {
            return;
        }
        LambdaQueryWrapper<KnowledgeItemTag> delLqw = Wrappers.lambdaQuery();
        delLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
        itemTagMapper.delete(delLqw);
        if (CollectionUtils.isNotEmpty(tagNames)) {
            LambdaQueryWrapper<KnowledgeTag> tagLqw = Wrappers.lambdaQuery();
            tagLqw.in(KnowledgeTag::getTagName, tagNames);
            List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagLqw);
            if (CollectionUtils.isNotEmpty(tags)) {
                String tenantId = LoginHelper.getTenantId();
                Long tenantIdLong = 0L;
                if (StringUtils.isNotBlank(tenantId)) {
                    try {
                        tenantIdLong = Long.parseLong(tenantId);
                    } catch (NumberFormatException e) {
                        tenantIdLong = 0L;
                    }
                }
                final Long finalTenantId = tenantIdLong;
                List<KnowledgeItemTag> itemTags = tags.stream()
                        .map(tag -> {
                            KnowledgeItemTag itemTag = new KnowledgeItemTag();
                            itemTag.setItemUuid(itemUuid);
                            itemTag.setTagId(tag.getId());
                            itemTag.setTenantId(finalTenantId);
                            return itemTag;
                        })
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(itemTags)) {
                    for (KnowledgeItemTag itemTag : itemTags) {
                        itemTagMapper.insert(itemTag);
                    }
                }
            }
        }
    }

    private boolean isItemInUncompletedProcess(String itemUuid) {
        if (StringUtils.isBlank(itemUuid)) {
            return false;
        }
        String completedStatus = ProcessingStatus.COMPLETED.getCode();
        // 通过 N:M 中间表查该条目关联的片段
        List<KnowledgeItemFragment> rels = itemFragmentMapper.selectList(
            Wrappers.<KnowledgeItemFragment>lambdaQuery()
                .eq(KnowledgeItemFragment::getItemUuid, itemUuid)
        );
        if (CollectionUtils.isEmpty(rels)) {
            return false;
        }
        List<Long> fragmentIds = rels.stream().map(KnowledgeItemFragment::getFragmentId).collect(Collectors.toList());
        List<KnowledgeFragment> fragments = fragmentMapper.selectBatchIds(fragmentIds);
        if (CollectionUtils.isEmpty(fragments)) {
            return false;
        }
        for (KnowledgeFragment fragment : fragments) {
            List<KnowledgeAttachProcess> processes = attachProcessMapper.selectList(
                Wrappers.<KnowledgeAttachProcess>lambdaQuery()
                    .eq(KnowledgeAttachProcess::getDocId, fragment.getDocId())
                    .ne(KnowledgeAttachProcess::getCurrentStatus, completedStatus)
            );
            if (CollectionUtils.isNotEmpty(processes)) {
                for (KnowledgeAttachProcess process : processes) {
                    String statusData = process.getStatusData();
                    if (StringUtils.isNotBlank(statusData)) {
                        try {
                            JSONObject statusDataObj = JSON.parseObject(statusData);
                            JSONArray llmCreatedItems = statusDataObj.getJSONArray("llmCreatedItems");
                            if (llmCreatedItems != null) {
                                for (int i = 0; i < llmCreatedItems.size(); i++) {
                                    JSONObject item = llmCreatedItems.getJSONObject(i);
                                    if (item != null && itemUuid.equals(item.getString("itemUuid"))) {
                                        return true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.warn("解析statusData失败: processId={}, error={}", process.getId(), e.getMessage());
                        }
                    }
                }
            }
        }
        return false;
    }

    private void validEntityBeforeSave(KnowledgeItem entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<KnowledgeItem> items = baseMapper.selectBatchIds(ids);
        Set<String> affectedKids = items.stream()
                .map(KnowledgeItem::getKid)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        List<String> itemUuids = items.stream()
                .map(KnowledgeItem::getItemUuid)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(itemUuids)) {
            for (String itemUuid : itemUuids) {
                vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
                itemFragmentMapper.deleteByItemUuid(itemUuid);
                LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
                itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
                itemTagMapper.delete(itemTagLqw);
            }
        }
        boolean flag = baseMapper.deleteBatchIds(ids) > 0;
        if (flag) {
            // 批量删除后更新所有受影响知识库的统计字段
            for (String kid : affectedKids) {
                baseMapper.updateKnowledgeItemCount(kid);
                updateKnowledgeDataSize(kid);
            }
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByItemUuid(String itemUuid) {
        KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
        if (entity == null) {
            throw new ServiceException("知识条目不存在");
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        if (!isSuperAdmin && !Objects.equals(entity.getCreateBy(), currentUserId)) {
            throw new ServiceException("无权限删除此知识条目，仅作者或管理员可以删除");
        }
        String kid = entity.getKid();
        vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
        itemFragmentMapper.deleteByItemUuid(itemUuid);
        LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
        itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
        itemTagMapper.delete(itemTagLqw);
        boolean flag = baseMapper.deleteById(entity.getId()) > 0;
        if (flag && StringUtils.isNotBlank(kid)) {
            baseMapper.updateKnowledgeItemCount(kid);
            updateKnowledgeDataSize(kid);
        }
        return flag;
    }

    private void parseCvssVector(String cvssVector, FacetStatsVo stats) {
        if (StringUtils.isBlank(cvssVector)) {
            return;
        }
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    String metric = kv[0].trim();
                    String value = kv[1].trim();
                    switch (metric) {
                        case "AV":
                            stats.getCvssAttackVector().merge(value, 1L, Long::sum);
                            break;
                        case "AC":
                            stats.getCvssAttackComplexity().merge(value, 1L, Long::sum);
                            break;
                        case "PR":
                            stats.getCvssPrivilegesRequired().merge(value, 1L, Long::sum);
                            break;
                        case "UI":
                            stats.getCvssUserInteraction().merge(value, 1L, Long::sum);
                            break;
                        case "S":
                            stats.getCvssScope().merge(value, 1L, Long::sum);
                            break;
                        case "C":
                            stats.getCvssConfidentiality().merge(value, 1L, Long::sum);
                            break;
                        case "I":
                            stats.getCvssIntegrity().merge(value, 1L, Long::sum);
                            break;
                        case "A":
                            stats.getCvssAvailability().merge(value, 1L, Long::sum);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public FacetStatsVo calculateFacetStats(KnowledgeItemBo bo) {
        FacetStatsVo stats = new FacetStatsVo();
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        List<KnowledgeItemVo> allItems = baseMapper.selectVoList(lqw);
        if (allItems != null) {
            allItems.forEach(this::loadVulnerabilityTypes);
            for (KnowledgeItemVo item : allItems) {
                if (StringUtils.isNotBlank(item.getSeverity())) {
                    stats.getSeverities().merge(item.getSeverity(), 1L, Long::sum);
                }
                if (StringUtils.isNotBlank(item.getLanguage())) {
                    stats.getLanguages().merge(item.getLanguage(), 1L, Long::sum);
                }
                if (StringUtils.isNotBlank(item.getStatus())) {
                    stats.getStatuses().merge(item.getStatus(), 1L, Long::sum);
                }
                if (CollectionUtils.isNotEmpty(item.getVulnerabilityTypes())) {
                    for (String type : item.getVulnerabilityTypes()) {
                        stats.getVulnerabilityTypes().merge(type, 1L, Long::sum);
                    }
                }
                if (StringUtils.isNotBlank(item.getCvssVector())) {
                    parseCvssVector(item.getCvssVector(), stats);
                }
            }
            List<String> itemUuids = allItems.stream()
                    .map(KnowledgeItemVo::getItemUuid)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(itemUuids)) {
                LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
                itemTagLqw.in(KnowledgeItemTag::getItemUuid, itemUuids);
                List<KnowledgeItemTag> itemTags = itemTagMapper.selectList(itemTagLqw);
                if (CollectionUtils.isNotEmpty(itemTags)) {
                    List<Long> tagIds = itemTags.stream()
                            .map(KnowledgeItemTag::getTagId)
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tagIds)) {
                        LambdaQueryWrapper<KnowledgeTag> tagLqw = Wrappers.lambdaQuery();
                        tagLqw.in(KnowledgeTag::getId, tagIds);
                        List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagLqw);
                        Map<Long, String> tagIdToNameMap = tags.stream()
                                .collect(Collectors.toMap(KnowledgeTag::getId, KnowledgeTag::getTagName));
                        for (KnowledgeItemTag itemTag : itemTags) {
                            String tagName = tagIdToNameMap.get(itemTag.getTagId());
                            if (StringUtils.isNotBlank(tagName)) {
                                stats.getTags().merge(tagName, 1L, Long::sum);
                            }
                        }
                    }
                }
            }
        }
        return stats;
    }

    private List<ClusterWithItemsVo> calculateGroupedByClusters(FacetStatsVo facetStats, String vulnerabilityTypeKeyword) {
        List<ClusterWithItemsVo> result = new ArrayList<>();
        boolean hasData = facetStats != null && facetStats.getVulnerabilityTypes() != null && 
                         !facetStats.getVulnerabilityTypes().isEmpty();
        Set<String> availableCwes = hasData ? facetStats.getVulnerabilityTypes().keySet() : null;
        String keyword = vulnerabilityTypeKeyword != null ? vulnerabilityTypeKeyword.toLowerCase().trim() : "";
        List<CweClusterVo> clusters = cweClusterMapper.selectByClusterMethod("kmeans");
        if (clusters == null || clusters.isEmpty()) {
            return result;
        }
        Map<String, String> cweDisplayNames = new HashMap<>();
        if (StringUtils.isNotBlank(keyword)) {
            List<CweReferenceVo> allCweRefs = cweReferenceMapper.selectVoList(Wrappers.lambdaQuery());
            for (CweReferenceVo ref : allCweRefs) {
                String displayName = ref.getCweId();
                if (StringUtils.isNotBlank(ref.getNameZh())) {
                    displayName = ref.getCweId() + ": " + ref.getNameZh();
                }
                cweDisplayNames.put(ref.getCweId(), displayName);
            }
        }
        for (CweClusterVo cluster : clusters) {
            ClusterWithItemsVo vo = new ClusterWithItemsVo();
            vo.setId(cluster.getId());
            vo.setClusterId(cluster.getClusterId());
            vo.setClusterMethod(cluster.getClusterMethod());
            vo.setClusterName(cluster.getClusterNameEn());
            vo.setClusterNameZh(cluster.getClusterNameZh());
            vo.setClusterDescription(cluster.getDescription());
            vo.setClusterDescriptionZh(cluster.getDescription());
            List<CweClusterMappingVo> mappings = cweClusterMappingMapper.selectByClusterIdAndMethod(
                cluster.getClusterId(), cluster.getClusterMethod()
            );
            List<String> cwes = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(mappings)) {
                for (CweClusterMappingVo mapping : mappings) {
                    String cweId = mapping.getCweId();
                    if (StringUtils.isNotBlank(keyword)) {
                        String displayName = cweDisplayNames.getOrDefault(cweId, cweId).toLowerCase();
                        if (!displayName.contains(keyword) && !cweId.toLowerCase().contains(keyword)) {
                            continue;
                        }
                    }
                    cwes.add(cweId);
                }
            }
            vo.setCwes(cwes);
            vo.setCount(cwes.size());
            long itemCount = 0L;
            if (facetStats != null && facetStats.getVulnerabilityTypes() != null) {
                for (String cweId : cwes) {
                    itemCount += facetStats.getVulnerabilityTypes().getOrDefault(cweId, 0L);
                }
            }
            vo.setItemCount(itemCount);
            result.add(vo);
        }
        return result;
    }

    public void updateKnowledgeDataSize(String kid) {
        if (StringUtils.isBlank(kid)) {
            return;
        }
        try {
            List<KnowledgeAttach> attaches = attachMapper.selectList(
                Wrappers.<KnowledgeAttach>lambdaQuery()
                    .eq(KnowledgeAttach::getKid, kid)
            );
            if (CollectionUtils.isEmpty(attaches)) {
                updateDataSizeToZero(kid);
                return;
            }
            Set<String> uncompletedDocIds = getUncompletedDocIds();
            long totalSize = 0L;
            OssClient storage = OssFactory.instance();
            List<Long> ossIds = attaches.stream()
                .map(KnowledgeAttach::getOssId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            Map<Long, SysOssVo> ossMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(ossIds)) {
                List<SysOssVo> ossList = sysOssService.listByIds(ossIds);
                ossMap = ossList.stream()
                    .collect(Collectors.toMap(SysOssVo::getOssId, oss -> oss, (a, b) -> a));
            }
            for (KnowledgeAttach attach : attaches) {
                if (uncompletedDocIds.contains(attach.getDocId())) {
                    continue;
                }
                if (attach.getOssId() == null) {
                    continue;
                }
                SysOssVo oss = ossMap.get(attach.getOssId());
                if (oss == null || StringUtils.isBlank(oss.getUrl())) {
                    continue;
                }
                try {
                    String url = oss.getUrl();
                    String objectPath;
                    if (StringUtils.isNotBlank(oss.getFileName())) {
                        objectPath = oss.getFileName();
                    } else if (url.startsWith("http://") || url.startsWith("https://")) {
                        try {
                            URI uri = new URI(url);
                            String path = uri.getPath().startsWith("/") ? uri.getPath().substring(1) : uri.getPath();
                            String baseUrl = storage.getUrl();
                            String basePath = baseUrl.replace("http://", "").replace("https://", "");
                            if (path.startsWith(basePath + "/")) {
                                objectPath = path.substring(basePath.length() + 1);
                            } else {
                                String[] parts = path.split("/", 2);
                                if (parts.length > 1) {
                                    objectPath = parts[1];
                                } else {
                                    objectPath = path;
                                }
                            }
                        } catch (URISyntaxException e) {
                            String baseUrl = storage.getUrl();
                            objectPath = url.replace(baseUrl + "/", "").replace(baseUrl, "");
                            String bucketName = storage.getConfigKey();
                            if (objectPath.startsWith(bucketName + "/")) {
                                objectPath = objectPath.substring(bucketName.length() + 1);
                            }
                        }
                    } else {
                        objectPath = url;
                    }
                    ObjectMetadata metadata = storage.getObjectMetadata(objectPath);
                    if (metadata != null && metadata.getContentLength() > 0) {
                        long fileSize = metadata.getContentLength();
                        totalSize += fileSize;
                    }
                } catch (Exception e) {
                    log.warn("[KnowledgeItemServiceImpl] 获取文件大小失败: ossId={}, docId={}, error={}", attach.getOssId(), attach.getDocId(), e.getMessage());
                }
            }
            baseMapper.updateKnowledgeDataSize(kid, totalSize);
        } catch (Exception e) {
            log.error("[KnowledgeItemServiceImpl] 更新知识库存储大小失败: kid={}, error={}", kid, e.getMessage(), e);
        }
    }

    private Set<String> getUncompletedDocIds() {
        List<KnowledgeAttachProcess> processes = attachProcessMapper.selectList(
            Wrappers.<KnowledgeAttachProcess>lambdaQuery()
                .ne(KnowledgeAttachProcess::getCurrentStatus, ProcessingStatus.COMPLETED.getCode())
        );
        return processes.stream()
            .map(KnowledgeAttachProcess::getDocId)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
    }

    private void updateDataSizeToZero(String kid) {
        baseMapper.updateKnowledgeDataSize(kid, 0L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchDeleteResultVo batchDeleteByItemUuids(List<String> itemUuids) {
        if (CollectionUtils.isEmpty(itemUuids)) {
            return BatchDeleteResultVo.builder()
                .successCount(0)
                .failedCount(0)
                .failures(new ArrayList<>())
                .build();
        }
        if (itemUuids.size() > 1000) {
            throw new ServiceException("单次最多删除1000条");
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        List<DeleteFailureVo> failures = new ArrayList<>();
        int successCount = 0;
        Set<String> affectedKids = new java.util.HashSet<>();
        for (String itemUuid : itemUuids) {
            try {
                KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
                if (entity == null) {
                    failures.add(DeleteFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("条目不存在")
                        .errorCode("RESOURCE_NOT_FOUND")
                        .build());
                    continue;
                }
                if (!isSuperAdmin && !Objects.equals(entity.getCreateBy(), currentUserId)) {
                    failures.add(DeleteFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("无删除权限")
                        .errorCode("PERMISSION_DENIED")
                        .build());
                    continue;
                }
                String kid = entity.getKid();
                vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
                itemFragmentMapper.deleteByItemUuid(itemUuid);
                LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
                itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
                itemTagMapper.delete(itemTagLqw);
                boolean flag = baseMapper.deleteById(entity.getId()) > 0;
                if (flag) {
                    successCount++;
                    if (StringUtils.isNotBlank(kid)) {
                        affectedKids.add(kid);
                    }
                } else {
                    failures.add(DeleteFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("删除失败")
                        .errorCode("DELETE_FAILED")
                        .build());
                }
            } catch (Exception e) {
                log.error("批量删除条目失败: itemUuid={}, error={}", itemUuid, e.getMessage(), e);
                failures.add(DeleteFailureVo.builder()
                    .itemUuid(itemUuid)
                    .reason("删除异常: " + e.getMessage())
                    .errorCode("EXCEPTION")
                    .build());
            }
        }
        for (String kid : affectedKids) {
            baseMapper.updateKnowledgeItemCount(kid);
            updateKnowledgeDataSize(kid);
        }
        return BatchDeleteResultVo.builder()
            .successCount(successCount)
            .failedCount(failures.size())
            .failures(failures)
            .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchUpdateResultVo batchUpdateByItemUuids(BatchUpdateRequestBo request) {
        if (CollectionUtils.isEmpty(request.getItemUuids())) {
            return BatchUpdateResultVo.builder()
                .successCount(0)
                .failedCount(0)
                .failures(new ArrayList<>())
                .build();
        }
        if (request.getItemUuids().size() > 1000) {
            throw new ServiceException("单次最多更新1000条");
        }
        String field = request.getField();
        Object value = request.getValue();
        if (StringUtils.isBlank(field) || value == null) {
            throw new ServiceException("字段名和值不能为空");
        }
        Set<String> allowedFields = new HashSet<>();
        allowedFields.add("language");
        allowedFields.add("severity");
        allowedFields.add("status");
        allowedFields.add("tags");
        allowedFields.add("riskAttackVector");
        allowedFields.add("riskComplexity");
        allowedFields.add("riskPrivileges");
        allowedFields.add("riskUserInteraction");
        if (!allowedFields.contains(field)) {
            throw new ServiceException("不允许批量更新字段: " + field);
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        List<UpdateFailureVo> failures = new ArrayList<>();
        int successCount = 0;
        Set<String> affectedKids = new HashSet<>();
        for (String itemUuid : request.getItemUuids()) {
            try {
                KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
                if (entity == null) {
                    failures.add(UpdateFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("条目不存在")
                        .errorCode("RESOURCE_NOT_FOUND")
                        .build());
                    continue;
                }
                if (!isSuperAdmin && !Objects.equals(entity.getCreateBy(), currentUserId)) {
                    failures.add(UpdateFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("无编辑权限")
                        .errorCode("PERMISSION_DENIED")
                        .build());
                    continue;
                }
                updateSingleItemField(entity, field, value);
                boolean flag = baseMapper.updateById(entity) > 0;
                if (flag) {
                    successCount++;
                    if (StringUtils.isNotBlank(entity.getKid())) {
                        affectedKids.add(entity.getKid());
                    }
                    // 批量更新也记录版本快照
                    String changeType = "update";
                    if ("status".equals(field)) {
                        String statusVal = value.toString();
                        if ("published".equals(statusVal)) {
                            changeType = "publish";
                        } else if ("archived".equals(statusVal)) {
                            changeType = "archive";
                        }
                    }
                    String reason = "批量更新字段 " + field + " 为 " + String.valueOf(value);
                    knowledgeItemHistoryService.createVersionSnapshot(entity.getItemUuid(), changeType, reason);
                } else {
                    failures.add(UpdateFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("更新失败")
                        .errorCode("UPDATE_FAILED")
                        .build());
                }
            } catch (Exception e) {
                log.error("批量更新条目失败: itemUuid={}, field={}, error={}", itemUuid, field, e.getMessage(), e);
                failures.add(UpdateFailureVo.builder()
                    .itemUuid(itemUuid)
                    .reason("更新异常: " + e.getMessage())
                    .errorCode("EXCEPTION")
                    .build());
            }
        }
        for (String kid : affectedKids) {
            baseMapper.updateKnowledgeItemCount(kid);
            updateKnowledgeDataSize(kid);
        }
        return BatchUpdateResultVo.builder()
            .successCount(successCount)
            .failedCount(failures.size())
            .failures(failures)
            .build();
    }

    private void updateSingleItemField(KnowledgeItem entity, String field, Object value) {
        switch (field) {
            case "language":
                entity.setLanguage(value != null ? value.toString() : null);
                break;
            case "severity":
                entity.setSeverity(value != null ? value.toString() : null);
                break;
            case "status":
                entity.setStatus(value != null ? value.toString() : null);
                if ("published".equals(value) && entity.getPublishTime() == null) {
                    entity.setPublishTime(new java.util.Date());
                } else if ("archived".equals(value) && entity.getArchiveTime() == null) {
                    entity.setArchiveTime(new java.util.Date());
                }
                break;
            case "tags":
                if (value instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> tagNames = (List<String>) value;
                    saveItemTags(entity.getItemUuid(), tagNames);
                }
                break;
            case "riskAttackVector":
            case "riskComplexity":
            case "riskPrivileges":
            case "riskUserInteraction":
                updateCvssFields(entity, field, value);
                break;
            default:
                throw new ServiceException("不支持的字段: " + field);
        }
    }

    private void updateCvssFields(KnowledgeItem entity, String field, Object value) {
        String attackVector = null;
        String attackComplexity = null;
        String privilegesRequired = null;
        String userInteraction = null;
        String cvssVector = entity.getCvssVector();
        if ("riskAttackVector".equals(field)) {
            attackVector = value != null ? value.toString() : null;
            attackComplexity = parseCvssField(cvssVector, "AC");
            privilegesRequired = parseCvssField(cvssVector, "PR");
            userInteraction = parseCvssField(cvssVector, "UI");
        } else if ("riskComplexity".equals(field)) {
            attackComplexity = value != null ? value.toString() : null;
            attackVector = parseCvssField(cvssVector, "AV");
            privilegesRequired = parseCvssField(cvssVector, "PR");
            userInteraction = parseCvssField(cvssVector, "UI");
        } else if ("riskPrivileges".equals(field)) {
            privilegesRequired = value != null ? value.toString() : null;
            attackVector = parseCvssField(cvssVector, "AV");
            attackComplexity = parseCvssField(cvssVector, "AC");
            userInteraction = parseCvssField(cvssVector, "UI");
        } else if ("riskUserInteraction".equals(field)) {
            userInteraction = value != null ? value.toString() : null;
            attackVector = parseCvssField(cvssVector, "AV");
            attackComplexity = parseCvssField(cvssVector, "AC");
            privilegesRequired = parseCvssField(cvssVector, "PR");
        }
        if (StringUtils.isNotBlank(attackVector) && StringUtils.isNotBlank(attackComplexity)
            && StringUtils.isNotBlank(privilegesRequired) && StringUtils.isNotBlank(userInteraction)) {
            List<String> impact = parseCvssImpact(cvssVector);
            if (impact.isEmpty()) {
                impact.add("C");
                impact.add("I");
                impact.add("A");
            }
            java.math.BigDecimal cvssScore = CvssScoreCalculator.calculateCvssScore(
                attackVector, attackComplexity, privilegesRequired, userInteraction, impact);
            if (cvssScore != null) {
                entity.setCvssScore(cvssScore);
            }
            //severity字段只保存用户手动设置的值，不保存CVSS计算出的severity
            //CVSS计算出的severity仅用于前端显示，不存储到数据库
            StringBuilder cvssBuilder = new StringBuilder("CVSS:4.0");
            cvssBuilder.append("/AV:").append(attackVector);
            cvssBuilder.append("/AC:").append(attackComplexity);
            cvssBuilder.append("/AT:N");
            cvssBuilder.append("/PR:").append(privilegesRequired);
            cvssBuilder.append("/UI:").append(userInteraction);
            boolean hasC = impact.contains("C");
            boolean hasI = impact.contains("I");
            boolean hasA = impact.contains("A");
            cvssBuilder.append("/VC:").append(hasC ? "H" : "N");
            cvssBuilder.append("/VI:").append(hasI ? "H" : "N");
            cvssBuilder.append("/VA:").append(hasA ? "H" : "N");
            cvssBuilder.append("/SC:N/SI:N/SA:N");
            String newCvssVector = cvssBuilder.toString();
            if (newCvssVector.length() > 255) {
                newCvssVector = newCvssVector.substring(0, 255);
            }
            entity.setCvssVector(newCvssVector);
            entity.setCvssVersion("4.0");
        }
    }

    private String parseCvssField(String cvssVector, String metric) {
        if (StringUtils.isBlank(cvssVector)) {
            return null;
        }
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.startsWith(metric + ":")) {
                return part.substring(metric.length() + 1);
            }
        }
        return null;
    }

    private String getCvssComponentLabel(String cvssVector, String metric) {
        if (StringUtils.isBlank(cvssVector)) {
            return "";
        }
        String value = parseCvssField(cvssVector, metric);
        if (StringUtils.isBlank(value)) {
            return "";
        }
        Map<String, String> metricLabels = new HashMap<>();
        metricLabels.put("AV:N", "网络");
        metricLabels.put("AV:A", "网络相邻");
        metricLabels.put("AV:L", "本地");
        metricLabels.put("AV:P", "物理");
        metricLabels.put("AC:L", "低");
        metricLabels.put("AC:H", "高");
        metricLabels.put("PR:N", "无");
        metricLabels.put("PR:L", "低");
        metricLabels.put("PR:H", "高");
        metricLabels.put("UI:N", "无");
        metricLabels.put("UI:R", "必需");
        metricLabels.put("UI:A", "活跃");
        metricLabels.put("VC:H", "高");
        metricLabels.put("VC:L", "低");
        metricLabels.put("VC:N", "无");
        metricLabels.put("VI:H", "高");
        metricLabels.put("VI:L", "低");
        metricLabels.put("VI:N", "无");
        metricLabels.put("VA:H", "高");
        metricLabels.put("VA:L", "低");
        metricLabels.put("VA:N", "无");
        String key = metric + ":" + value;
        return metricLabels.getOrDefault(key, value);
    }

    private List<String> parseCvssImpact(String cvssVector) {
        List<String> impact = new ArrayList<>();
        if (StringUtils.isBlank(cvssVector)) {
            return impact;
        }
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.startsWith("VC:") && "H".equals(part.substring(3))) {
                impact.add("C");
            } else if (part.startsWith("VI:") && "H".equals(part.substring(3))) {
                impact.add("I");
            } else if (part.startsWith("VA:") && "H".equals(part.substring(3))) {
                impact.add("A");
            }
        }
        return impact;
    }

    // ========== Export delegation ==========

    /**
     * 导出预览
     */
    @Override
    public ExportPreviewVo exportPreview(ExportPreviewRequestBo request) {
        return SpringUtils.getBean(KnowledgeItemExportDataService.class).exportPreview(request);
    }

    /**
     * 导出（Excel/PDF）
     */
    @Override
    public void export(ExportRequestBo request, HttpServletResponse response) throws IOException {
        SpringUtils.getBean(KnowledgeItemExportDataService.class).export(request, response);
    }

    @Override
    public VulnerabilityDistributionVo getVulnerabilityDistribution(String kid, Integer topN) {
        if (StringUtils.isBlank(kid)) {
            throw new ServiceException("知识库ID不能为空");
        }
        if (topN == null || topN <= 0) {
            topN = 10;
        }

        VulnerabilityDistributionVo result = new VulnerabilityDistributionVo();
        result.setTopN(topN);

        // 总条目数
        long totalItems = baseMapper.selectCount(
            Wrappers.<KnowledgeItem>lambdaQuery()
                .eq(KnowledgeItem::getKid, kid)
                .eq(KnowledgeItem::getDelFlag, "0")
        );
        result.setTotalItems(totalItems);

        // 有 CWE 分类的条目数
        Long itemsWithCwe = vulnerabilityTypeMapper.selectItemsWithCweCountByKid(kid);
        itemsWithCwe = itemsWithCwe != null ? itemsWithCwe : 0L;
        result.setItemsWithCwe(itemsWithCwe);
        result.setItemsWithoutCwe(totalItems - itemsWithCwe);
        result.setCoveragePercent(totalItems > 0
            ? Math.round(itemsWithCwe * 10000.0 / totalItems) / 100.0
            : 0.0);

        // 按 CWE 类型分布（SQL 聚合）
        List<CweDistributionItemVo> allCweDistribution = vulnerabilityTypeMapper.selectCweDistributionByKid(kid);
        if (allCweDistribution == null) {
            allCweDistribution = new ArrayList<>();
        }

        // 计算百分比
        long totalCweAssociations = allCweDistribution.stream()
            .mapToLong(item -> item.getCount() != null ? item.getCount() : 0L)
            .sum();
        for (CweDistributionItemVo item : allCweDistribution) {
            long count = item.getCount() != null ? item.getCount() : 0L;
            item.setPercentage(totalCweAssociations > 0
                ? Math.round(count * 10000.0 / totalCweAssociations) / 100.0
                : 0.0);
        }

        // TopN + Others
        if (allCweDistribution.size() <= topN) {
            result.setDistribution(allCweDistribution);
            result.setOthersItem(null);
        } else {
            List<CweDistributionItemVo> topList = new ArrayList<>(allCweDistribution.subList(0, topN));
            List<CweDistributionItemVo> tailList = allCweDistribution.subList(topN, allCweDistribution.size());

            long othersCount = tailList.stream()
                .mapToLong(item -> item.getCount() != null ? item.getCount() : 0L)
                .sum();
            double othersPercentage = totalCweAssociations > 0
                ? Math.round(othersCount * 10000.0 / totalCweAssociations) / 100.0
                : 0.0;

            CweDistributionItemVo othersItem = CweDistributionItemVo.builder()
                .cweId("OTHERS")
                .cweName("Others (" + tailList.size() + " types)")
                .cweNameZh("其他（" + tailList.size() + " 种类型）")
                .count(othersCount)
                .percentage(othersPercentage)
                .build();

            result.setDistribution(topList);
            result.setOthersItem(othersItem);
        }

        // 按严重级别分布（SQL 聚合）
        List<SeverityDistributionItemVo> severityDistribution = vulnerabilityTypeMapper.selectSeverityDistributionByKid(kid);
        if (severityDistribution == null) {
            severityDistribution = new ArrayList<>();
        }
        long totalSeverityCount = severityDistribution.stream()
            .mapToLong(item -> item.getCount() != null ? item.getCount() : 0L)
            .sum();
        for (SeverityDistributionItemVo item : severityDistribution) {
            long count = item.getCount() != null ? item.getCount() : 0L;
            item.setPercentage(totalSeverityCount > 0
                ? Math.round(count * 10000.0 / totalSeverityCount) / 100.0
                : 0.0);
        }
        result.setBySeverity(severityDistribution);

        return result;
    }
}
