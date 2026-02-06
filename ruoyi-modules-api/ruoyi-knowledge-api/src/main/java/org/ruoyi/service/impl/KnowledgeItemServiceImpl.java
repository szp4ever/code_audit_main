package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.SpringUtils;
import org.ruoyi.common.core.service.DictService;
import org.ruoyi.common.satoken.utils.LoginHelper;
import cn.hutool.http.HttpStatus;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.KnowledgeItem;
import org.ruoyi.domain.KnowledgeItemTag;
import org.ruoyi.domain.KnowledgeItemVulnerabilityType;
import org.ruoyi.domain.KnowledgeTag;
import org.ruoyi.domain.bo.KnowledgeItemBo;
import org.ruoyi.domain.vo.ClusterWithItemsVo;
import org.ruoyi.domain.vo.CweClusterMappingVo;
import org.ruoyi.domain.vo.CweClusterVo;
import org.ruoyi.domain.vo.CweReferenceVo;
import org.ruoyi.domain.vo.FacetStatsVo;
import org.ruoyi.domain.vo.KnowledgeItemPageVo;
import org.ruoyi.domain.vo.KnowledgeItemVo;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.domain.vo.KnowledgeTagVo;
import org.ruoyi.domain.CweReference;
import org.ruoyi.domain.vo.BatchDeleteResultVo;
import org.ruoyi.domain.vo.BatchUpdateResultVo;
import org.ruoyi.domain.vo.DeleteFailureVo;
import org.ruoyi.domain.vo.UpdateFailureVo;
import org.ruoyi.domain.vo.ExportPreviewVo;
import org.ruoyi.domain.vo.FieldInfoVo;
import org.ruoyi.domain.bo.BatchUpdateRequestBo;
import org.ruoyi.domain.bo.ExportPreviewRequestBo;
import org.ruoyi.domain.bo.ExportRequestBo;
import org.ruoyi.domain.bo.ExcelOptionsBo;
import org.ruoyi.utils.CvssScoreCalculator;
import org.ruoyi.mapper.CweClusterMapper;
import org.ruoyi.mapper.CweClusterMappingMapper;
import org.ruoyi.mapper.CweReferenceMapper;
import org.ruoyi.mapper.KnowledgeItemMapper;
import org.ruoyi.mapper.KnowledgeItemTagMapper;
import org.ruoyi.mapper.KnowledgeItemVulnerabilityTypeMapper;
import org.ruoyi.mapper.KnowledgeTagMapper;
import org.ruoyi.mapper.KnowledgeFragmentMapper;
import org.ruoyi.mapper.KnowledgeAttachProcessMapper;
import org.ruoyi.mapper.KnowledgeAttachMapper;
import org.ruoyi.mapper.KnowledgeInfoMapper;
import org.ruoyi.domain.KnowledgeFragment;
import org.ruoyi.domain.KnowledgeAttachProcess;
import org.ruoyi.domain.KnowledgeAttach;
import org.ruoyi.domain.KnowledgeInfo;
import org.ruoyi.domain.enums.ProcessingStatus;
import org.ruoyi.service.IKnowledgeItemService;
import org.ruoyi.system.service.ISysOssService;
import org.ruoyi.system.service.ISysUserService;
import org.ruoyi.system.service.ISysDeptService;
import org.ruoyi.system.domain.vo.SysOssVo;
import org.ruoyi.system.domain.vo.SysUserVo;
import org.ruoyi.system.domain.vo.SysDeptVo;
import org.ruoyi.common.oss.core.OssClient;
import org.ruoyi.common.oss.factory.OssFactory;
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
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.Head;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import java.awt.Color;

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
    private final ISysDeptService sysDeptService;

    @Override
    public KnowledgeItemVo queryById(Long id) {
        KnowledgeItemVo vo = baseMapper.selectVoById(id);
        if (vo != null && StringUtils.isNotBlank(vo.getItemUuid())) {
            loadVulnerabilityTypes(vo);
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
        Map<String, Integer> fragmentCountMap = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .in(KnowledgeFragment::getItemUuid, itemUuids)
                .eq(KnowledgeFragment::getDelFlag, "0")
        ).stream()
        .collect(Collectors.groupingBy(
            KnowledgeFragment::getItemUuid,
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

    @Override
    public TableDataInfo<KnowledgeItemVo> queryPageList(KnowledgeItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        Page<KnowledgeItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        
        if (result.getRecords() != null) {
            result.getRecords().forEach(this::loadVulnerabilityTypes);
            fillFragmentCounts(result.getRecords());
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
        
        // 过滤掉未完成处理的新建条目：只过滤那些在llmCreatedItems中且关联的处理任务未完成的条目
        // 保留：没有关联片段的条目（手动创建）、片段没有关联处理任务的条目（旧数据）、处理任务已完成的条目、已有条目（不在llmCreatedItems中）
        String completedStatus = ProcessingStatus.COMPLETED.getCode();
        
        // 只过滤新建条目：检查该条目是否在某个未完成任务的llmCreatedItems中
        String notExistsSql = "NOT EXISTS (SELECT 1 FROM knowledge_fragment f " +
            "INNER JOIN knowledge_attach_process p ON f.doc_id = p.doc_id " +
            "WHERE f.item_uuid = knowledge_item.item_uuid " +
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
            saveVulnerabilityTypes(add.getItemUuid(), bo.getVulnerabilityTypes());
            saveItemTags(add.getItemUuid(), bo.getTags());
            if (StringUtils.isNotBlank(add.getKid()) && !isItemInUncompletedProcess(add.getItemUuid())) {
                baseMapper.updateKnowledgeItemCount(add.getKid());
                updateKnowledgeDataSize(add.getKid());
            }
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
        List<KnowledgeFragment> fragments = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .eq(KnowledgeFragment::getItemUuid, itemUuid)
                .eq(KnowledgeFragment::getDelFlag, "0")
        );
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

    private FacetStatsVo calculateFacetStats(KnowledgeItemBo bo) {
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

    @Override
    public ExportPreviewVo exportPreview(ExportPreviewRequestBo request) {
        int previewLimit = Math.min(10, Math.max(5, (int) Math.min(getExportDataCount(request), 10)));
        List<KnowledgeItemVo> sampleData = getExportData(request, previewLimit);
        long totalCount = getExportDataCount(request);
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        long estimatedFileSize = estimateFileSize(sampleData, request.getFormat(), fieldInfos.size());
        int estimatedTime = estimateTime(totalCount, request.getFormat());
        String previewHtml = null;
        if ("pdf".equals(request.getFormat())) {
            previewHtml = generatePreviewHtml(sampleData, fieldInfos, request.getFieldFormats());
        }
        return ExportPreviewVo.builder()
            .sampleData(convertToMapList(sampleData, request.getSelectedFields(), request.getExpandedFields(), request.getFieldFormats()))
            .totalCount(totalCount)
            .selectedFields(fieldInfos)
            .estimatedFileSize(estimatedFileSize)
            .estimatedTime(estimatedTime)
            .previewHtml(previewHtml)
            .build();
    }

    @Override
    public void export(ExportRequestBo request, HttpServletResponse response) {
        try {
            List<KnowledgeItemVo> data = getExportData(request, null);
            String fileName = StringUtils.isNotBlank(request.getFileName()) 
                ? request.getFileName() 
                : generateDefaultFileName(request.getFormat());
            if ("excel".equals(request.getFormat())) {
                exportToExcel(data, request, fileName, response);
            } else if ("pdf".equals(request.getFormat())) {
                exportToPdf(data, request, fileName, response);
            } else {
                throw new ServiceException("不支持的导出格式: " + request.getFormat());
            }
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new ServiceException("导出失败: " + e.getMessage());
        }
    }

    private List<KnowledgeItemVo> getExportData(ExportPreviewRequestBo request, Integer limit) {
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            bo.setItemUuids(request.getItemUuids());
            List<KnowledgeItemVo> list = queryList(bo);
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
            PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            TableDataInfo<KnowledgeItemVo> pageData = queryPageList(bo, pageQuery);
            List<KnowledgeItemVo> list = pageData.getRows();
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        } else {
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            List<KnowledgeItemVo> list = queryList(bo);
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        }
    }

    private long getExportDataCount(ExportPreviewRequestBo request) {
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            return request.getItemUuids().size();
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageSize() != null) {
            return request.getPageSize();
        }
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if (request.getFilters() != null) {
            bo = request.getFilters();
        }
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        return baseMapper.selectCount(lqw);
    }

    private List<FieldInfoVo> buildFieldInfos(List<String> selectedFields, Map<String, List<String>> expandedFields) {
        Map<String, String> fieldLabels = getFieldLabels();
        List<FieldInfoVo> fieldInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(selectedFields)) {
            for (String field : selectedFields) {
                //过滤掉已废弃的字段
                if ("vulnerabilityType".equals(field)) {
                    continue;
                }
                fieldInfos.add(FieldInfoVo.builder()
                    .key(field)
                    .label(fieldLabels.getOrDefault(field, field))
                    .type("base")
                    .build());
                if (expandedFields != null && expandedFields.containsKey(field)) {
                    for (String expandedField : expandedFields.get(field)) {
                        fieldInfos.add(FieldInfoVo.builder()
                            .key(expandedField)
                            .label(fieldLabels.getOrDefault(expandedField, expandedField))
                            .type("expanded")
                            .parentField(field)
                            .build());
                    }
                }
            }
        }
        return fieldInfos;
    }

    private Map<String, String> getFieldLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("title", "标题");
        labels.put("summary", "摘要");
        labels.put("problemDescription", "问题描述");
        labels.put("fixSolution", "修复方案");
        labels.put("exampleCode", "示例代码");
        labels.put("referenceLink", "参考链接");
        labels.put("severity", "风险等级");
        labels.put("vulnerabilityTypes", "漏洞类型");
        labels.put("vulnerabilityTypeName", "漏洞类型名称（中文）");
        labels.put("vulnerabilityTypeNameEn", "漏洞类型名称（英文）");
        labels.put("vulnerabilityTypeDescription", "漏洞类型描述（中文）");
        labels.put("vulnerabilityTypeDescriptionEn", "漏洞类型描述（英文）");
        labels.put("language", "编程语言");
        labels.put("cvssScore", "CVSS评分");
        labels.put("cvssAttackVector", "CVSS攻击方式");
        labels.put("cvssAttackComplexity", "CVSS利用复杂度");
        labels.put("cvssPrivilegesRequired", "CVSS权限需求");
        labels.put("cvssUserInteraction", "CVSS用户交互");
        labels.put("cvssConfidentialityImpact", "CVSS机密性影响");
        labels.put("cvssIntegrityImpact", "CVSS完整性影响");
        labels.put("cvssAvailabilityImpact", "CVSS可用性影响");
        labels.put("status", "状态");
        labels.put("tags", "标签");
        labels.put("fragmentCount", "片段数量");
        labels.put("createTime", "创建时间");
        labels.put("updateTime", "更新时间");
        labels.put("createBy", "创建人");
        labels.put("updateBy", "更新人");
        labels.put("kid", "知识库");
        return labels;
    }

    private long estimateFileSize(List<KnowledgeItemVo> sampleData, String format, int fieldCount) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return 0;
        }
        long avgSize = sampleData.stream()
            .mapToLong(item -> estimateItemSize(item))
            .sum() / sampleData.size();
        return avgSize * fieldCount * 2;
    }

    private long estimateItemSize(KnowledgeItemVo item) {
        long size = 0;
        if (StringUtils.isNotBlank(item.getTitle())) size += item.getTitle().length();
        if (StringUtils.isNotBlank(item.getSummary())) size += item.getSummary().length();
        if (StringUtils.isNotBlank(item.getProblemDescription())) size += item.getProblemDescription().length();
        if (StringUtils.isNotBlank(item.getFixSolution())) size += item.getFixSolution().length();
        if (StringUtils.isNotBlank(item.getExampleCode())) size += item.getExampleCode().length();
        return size;
    }

    private int estimateTime(long totalCount, String format) {
        int baseTime = "excel".equals(format) ? 1 : 2;
        return (int) Math.max(1, baseTime + totalCount / 1000);
    }

    private String generatePreviewHtml(List<KnowledgeItemVo> sampleData, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return "<p>暂无数据</p>";
        }
        List<String> selectedFields = fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList());
        Map<String, List<String>> expandedFields = extractExpandedFields(fieldInfos);
        Map<String, CweReferenceVo> cweMap = buildCweMap(sampleData, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(sampleData, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(sampleData, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(sampleData, selectedFields, expandedFields);
        StringBuilder html = new StringBuilder("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        html.append("<thead><tr>");
        for (FieldInfoVo fieldInfo : fieldInfos) {
            html.append("<th>").append(escapeHtml(fieldInfo.getLabel())).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (KnowledgeItemVo item : sampleData) {
            html.append("<tr>");
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String value = getFieldValue(item, fieldInfo.getKey(), cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                html.append("<td>").append(escapeHtml(value)).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table>");
        return html.toString();
    }

    private Map<String, List<String>> extractExpandedFields(List<FieldInfoVo> fieldInfos) {
        Map<String, List<String>> expandedFields = new HashMap<>();
        if (CollectionUtils.isEmpty(fieldInfos)) {
            return expandedFields;
        }
        for (FieldInfoVo fieldInfo : fieldInfos) {
            if ("expanded".equals(fieldInfo.getType()) && StringUtils.isNotBlank(fieldInfo.getParentField())) {
                expandedFields.computeIfAbsent(fieldInfo.getParentField(), k -> new ArrayList<>())
                    .add(fieldInfo.getKey());
            }
        }
        return expandedFields;
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    private String getFieldValue(KnowledgeItemVo item, String fieldKey, Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap, Map<String, String> knowledgeBaseMap, Map<String, String> fieldFormats) {
        DictService dictService = SpringUtils.getBean(DictService.class);
        switch (fieldKey) {
            case "title": return StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : "";
            case "summary": return StringUtils.isNotBlank(item.getSummary()) ? item.getSummary() : "";
            case "problemDescription": return StringUtils.isNotBlank(item.getProblemDescription()) ? item.getProblemDescription() : "";
            case "fixSolution": return StringUtils.isNotBlank(item.getFixSolution()) ? item.getFixSolution() : "";
            case "exampleCode": return StringUtils.isNotBlank(item.getExampleCode()) ? item.getExampleCode() : "";
            case "referenceLink": return StringUtils.isNotBlank(item.getReferenceLink()) ? item.getReferenceLink() : "";
            case "severity": 
                if (StringUtils.isBlank(item.getSeverity())) return "";
                return dictService.getDictLabel("knowledge_severity", item.getSeverity());
            case "vulnerabilityTypes": 
                String vulnFormat = fieldFormats != null ? fieldFormats.get("vulnerabilityTypes") : null;
                return formatVulnerabilityTypes(item.getVulnerabilityTypes(), cweMap, vulnFormat);
            case "vulnerabilityTypeName":
                String vulnType = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnType != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnType);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                        return cwe.getNameZh();
                    }
                }
                return "";
            case "vulnerabilityTypeNameEn":
                String vulnTypeEn = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeEn != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeEn);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getNameEn())) {
                        return cwe.getNameEn();
                    }
                }
                return "";
            case "vulnerabilityTypeDescription":
                String vulnTypeDesc = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeDesc != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeDesc);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getDescriptionZh())) {
                        return cwe.getDescriptionZh();
                    }
                }
                return "";
            case "vulnerabilityTypeDescriptionEn":
                String vulnTypeDescEn = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeDescEn != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeDescEn);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getDescriptionEn())) {
                        return cwe.getDescriptionEn();
                    }
                }
                return "";
            case "language": 
                if (StringUtils.isBlank(item.getLanguage())) return "";
                return dictService.getDictLabel("knowledge_language", item.getLanguage());
            case "cvssScore": return item.getCvssScore() != null ? item.getCvssScore().toString() : "";
            case "cvssAttackVector": return getCvssComponentLabel(item.getCvssVector(), "AV");
            case "cvssAttackComplexity": return getCvssComponentLabel(item.getCvssVector(), "AC");
            case "cvssPrivilegesRequired": return getCvssComponentLabel(item.getCvssVector(), "PR");
            case "cvssUserInteraction": return getCvssComponentLabel(item.getCvssVector(), "UI");
            case "cvssConfidentialityImpact": return getCvssComponentLabel(item.getCvssVector(), "VC");
            case "cvssIntegrityImpact": return getCvssComponentLabel(item.getCvssVector(), "VI");
            case "cvssAvailabilityImpact": return getCvssComponentLabel(item.getCvssVector(), "VA");
            case "status": 
                if (StringUtils.isBlank(item.getStatus())) return "";
                return dictService.getDictLabel("knowledge_item_status", item.getStatus());
            case "tags": 
                String tagFormat = fieldFormats != null ? fieldFormats.get("tags") : null;
                return formatTags(item.getTags(), tagMap, tagFormat);
            case "fragmentCount": return item.getFragmentCount() != null ? item.getFragmentCount().toString() : "";
            case "createTime": return item.getCreateTime() != null ? formatDateTime(item.getCreateTime()) : "";
            case "updateTime": return item.getUpdateTime() != null ? formatDateTime(item.getUpdateTime()) : "";
            case "createBy":
                if (item.getCreateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getCreateBy(), "");
                }
                return "";
            case "kid":
                if (StringUtils.isNotBlank(item.getKid()) && knowledgeBaseMap != null) {
                    return knowledgeBaseMap.getOrDefault(item.getKid(), "");
                }
                return "";
            case "createByName":
                if (item.getCreateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getCreateBy(), "");
                }
                return "";
            case "updateBy":
                if (item.getUpdateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getUpdateBy(), "");
                }
                return "";
            case "updateByName":
                if (item.getUpdateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getUpdateBy(), "");
                }
                return "";
            default: return "";
        }
    }

    private String formatDateTime(java.util.Date date) {
        if (date == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private String formatDataSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    private String parseCvssVector(String cvssVector) {
        if (StringUtils.isBlank(cvssVector)) {
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
        metricLabels.put("AT:N", "无");
        metricLabels.put("AT:P", "存在");
        metricLabels.put("VC:H", "高");
        metricLabels.put("VC:L", "低");
        metricLabels.put("VC:N", "无");
        metricLabels.put("VI:H", "高");
        metricLabels.put("VI:L", "低");
        metricLabels.put("VI:N", "无");
        metricLabels.put("VA:H", "高");
        metricLabels.put("VA:L", "低");
        metricLabels.put("VA:N", "无");
        metricLabels.put("SC:H", "高");
        metricLabels.put("SC:L", "低");
        metricLabels.put("SC:N", "无");
        metricLabels.put("SI:H", "高");
        metricLabels.put("SI:L", "低");
        metricLabels.put("SI:N", "无");
        metricLabels.put("SA:H", "高");
        metricLabels.put("SA:L", "低");
        metricLabels.put("SA:N", "无");
        Map<String, String> metricNames = new HashMap<>();
        metricNames.put("AV", "攻击方式");
        metricNames.put("AC", "利用复杂度");
        metricNames.put("PR", "权限需求");
        metricNames.put("UI", "用户交互");
        metricNames.put("AT", "攻击要求");
        metricNames.put("VC", "机密性影响");
        metricNames.put("VI", "完整性影响");
        metricNames.put("VA", "可用性影响");
        metricNames.put("SC", "后续机密性影响");
        metricNames.put("SI", "后续完整性影响");
        metricNames.put("SA", "后续可用性影响");
        List<String> components = new ArrayList<>();
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    String metric = kv[0].trim();
                    String value = kv[1].trim();
                    String metricName = metricNames.getOrDefault(metric, metric);
                    String label = metricLabels.getOrDefault(metric + ":" + value, value);
                    components.add(metricName + "：" + label);
                }
            }
        }
        return String.join("；", components);
    }

    private List<Map<String, Object>> convertToMapList(List<KnowledgeItemVo> items, List<String> selectedFields, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        List<FieldInfoVo> fieldInfos = buildFieldInfos(selectedFields, expandedFields);
        Map<String, CweReferenceVo> cweMap = buildCweMap(items, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(items, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(items, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(items, selectedFields, expandedFields);
        List<Map<String, Object>> result = new ArrayList<>();
        for (KnowledgeItemVo item : items) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String fieldKey = fieldInfo.getKey();
                Object fieldValue = getFieldValue(item, fieldKey, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                map.put(fieldKey, fieldValue);
            }
            result.add(map);
        }
        return result;
    }

    private Map<String, CweReferenceVo> buildCweMap(List<KnowledgeItemVo> items, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        Map<String, CweReferenceVo> cweMap = new HashMap<>();
        boolean needCwe = false;
        if (expandedFields != null && (expandedFields.containsKey("vulnerabilityType") || expandedFields.containsKey("vulnerabilityTypes"))) {
            needCwe = true;
        }
        if (fieldFormats != null && (fieldFormats.containsKey("vulnerabilityTypes") || fieldFormats.containsKey("vulnerabilityType"))) {
            String format = fieldFormats.get("vulnerabilityTypes");
            if (format == null) format = fieldFormats.get("vulnerabilityType");
            if (format != null) {
                needCwe = true;
            }
        }
        if (!needCwe) {
            return cweMap;
        }
        Set<String> cweIds = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getVulnerabilityTypes() != null) {
                cweIds.addAll(item.getVulnerabilityTypes());
            }
            if (StringUtils.isNotBlank(item.getVulnerabilityType())) {
                cweIds.add(item.getVulnerabilityType());
            }
        }
        if (!cweIds.isEmpty()) {
            List<CweReferenceVo> cweList = cweReferenceMapper.selectVoList(
                Wrappers.<CweReference>lambdaQuery().in(CweReference::getCweId, cweIds)
            );
            for (CweReferenceVo cwe : cweList) {
                cweMap.put(cwe.getCweId(), cwe);
            }
        }
        return cweMap;
    }

    private Map<String, KnowledgeTagVo> buildTagMap(List<KnowledgeItemVo> items, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        Map<String, KnowledgeTagVo> tagMap = new HashMap<>();
        boolean needTag = false;
        if (expandedFields != null && expandedFields.containsKey("tags")) {
            needTag = true;
        }
        if (fieldFormats != null && fieldFormats.containsKey("tags")) {
            String format = fieldFormats.get("tags");
            if (format != null && "full".equals(format)) {
                needTag = true;
            }
        }
        if (!needTag) {
            return tagMap;
        }
        Set<String> tagNames = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getTags() != null) {
                tagNames.addAll(item.getTags());
            }
        }
        if (!tagNames.isEmpty()) {
            List<KnowledgeTagVo> tagList = knowledgeTagMapper.selectVoList(
                Wrappers.<KnowledgeTag>lambdaQuery().in(KnowledgeTag::getTagName, tagNames)
            );
            for (KnowledgeTagVo tag : tagList) {
                tagMap.put(tag.getTagName(), tag);
            }
        }
        return tagMap;
    }

    private Map<Long, String> buildUserMap(List<KnowledgeItemVo> items, List<String> selectedFields) {
        Map<Long, String> userMap = new HashMap<>();
        if (selectedFields == null) {
            return userMap;
        }
        boolean needUser = selectedFields.contains("createBy") || selectedFields.contains("updateBy");
        if (!needUser) {
            return userMap;
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
        if (!userIds.isEmpty()) {
            for (Long userId : userIds) {
                SysUserVo user = sysUserService.selectUserById(userId);
                if (user != null && StringUtils.isNotBlank(user.getUserName())) {
                    userMap.put(userId, user.getUserName());
                }
            }
        }
        return userMap;
    }


    private Map<String, String> buildKnowledgeBaseMap(List<KnowledgeItemVo> items, List<String> selectedFields, Map<String, List<String>> expandedFields) {
        Map<String, String> knowledgeBaseMap = new HashMap<>();
        //如果选择了kid字段，需要构建映射
        boolean needKnowledgeBase = (selectedFields != null && selectedFields.contains("kid")) 
            || (expandedFields != null && expandedFields.containsKey("kid"));
        if (!needKnowledgeBase) {
            return knowledgeBaseMap;
        }
        Set<String> kids = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (StringUtils.isNotBlank(item.getKid())) {
                kids.add(item.getKid());
            }
        }
        if (!kids.isEmpty()) {
            for (String kid : kids) {
                KnowledgeInfoVo info = knowledgeInfoMapper.selectVoByKid(kid);
                if (info != null && StringUtils.isNotBlank(info.getKname())) {
                    knowledgeBaseMap.put(kid, info.getKname());
                }
            }
        }
        return knowledgeBaseMap;
    }

    private String formatVulnerabilityTypes(List<String> vulnerabilityTypes, Map<String, CweReferenceVo> cweMap, String format) {
        if (CollectionUtils.isEmpty(vulnerabilityTypes)) {
            return "";
        }
        List<String> formatted = new ArrayList<>();
        for (String cweId : vulnerabilityTypes) {
            CweReferenceVo cwe = cweMap != null ? cweMap.get(cweId) : null;
            if (format == null || "name_only".equals(format)) {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            } else if ("id_name".equals(format)) {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cweId + ": " + cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            } else if ("full".equals(format)) {
                StringBuilder sb = new StringBuilder();
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    sb.append(cwe.getNameZh());
                    if (StringUtils.isNotBlank(cwe.getDescriptionZh())) {
                        String desc = cwe.getDescriptionZh();
                        if (desc.length() > 50) {
                            desc = desc.substring(0, 50) + "...";
                        }
                        sb.append("（").append(desc).append("）");
                    }
                } else {
                    sb.append(cweId);
                }
                formatted.add(sb.toString());
            } else {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            }
        }
        return String.join(", ", formatted);
    }

    private String formatTags(List<String> tags, Map<String, KnowledgeTagVo> tagMap, String format) {
        if (CollectionUtils.isEmpty(tags)) {
            return "";
        }
        List<String> formatted = new ArrayList<>();
        for (String tagName : tags) {
            KnowledgeTagVo tag = tagMap != null ? tagMap.get(tagName) : null;
            if (format == null || "name_only".equals(format)) {
                formatted.add(tagName);
            } else if ("full".equals(format)) {
                StringBuilder sb = new StringBuilder(tagName);
                if (tag != null) {
                    if (StringUtils.isNotBlank(tag.getTagCategory())) {
                        sb.append(" [").append(tag.getTagCategory()).append("]");
                    }
                    if (StringUtils.isNotBlank(tag.getDescription())) {
                        String desc = tag.getDescription();
                        if (desc.length() > 30) {
                            desc = desc.substring(0, 30) + "...";
                        }
                        sb.append("（").append(desc).append("）");
                    }
                }
                formatted.add(sb.toString());
            } else {
                formatted.add(tagName);
            }
        }
        return String.join(", ", formatted);
    }

    private String generateDefaultFileName(String format) {
        String extension = "excel".equals(format) ? "xlsx" : "pdf";
        return "知识条目导出_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "." + extension;
    }

    private void exportToExcel(List<KnowledgeItemVo> data, ExportRequestBo request, String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        List<Map<String, Object>> exportData = convertToMapList(data, request.getSelectedFields(), request.getExpandedFields(), request.getFieldFormats());
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        //构建表头：List<List<String>>，外层List的每个元素代表一列，内层List的每个元素代表该列的表头行（通常只有一行）
        List<List<String>> headers = new ArrayList<>();
        for (FieldInfoVo fieldInfo : fieldInfos) {
            List<String> columnHeader = new ArrayList<>();
            columnHeader.add(fieldInfo.getLabel());
            headers.add(columnHeader);
        }
        //构建数据行：List<List<Object>>，外层List的每个元素代表一行数据，内层List的每个元素代表一列的值
        List<List<Object>> rows = new ArrayList<>();
        for (Map<String, Object> item : exportData) {
            List<Object> row = new ArrayList<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                row.add(item.getOrDefault(fieldInfo.getKey(), ""));
            }
            rows.add(row);
        }
        OutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream).build();
            ExcelOptionsBo excelOptions = request.getExcelOptions();
            boolean freezeHeader = excelOptions != null && Boolean.TRUE.equals(excelOptions.getFreezeHeader());
            boolean includeFilter = excelOptions != null && Boolean.TRUE.equals(excelOptions.getIncludeFilter());
            boolean conditionalFormatting = excelOptions != null && Boolean.TRUE.equals(excelOptions.getConditionalFormatting());
            ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet(0, "知识条目")
                .head(headers)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(createExcelStyleStrategy());
            if (freezeHeader || includeFilter) {
                sheetBuilder.registerWriteHandler(new FreezeAndFilterHandler(freezeHeader, includeFilter, fieldInfos.size()));
            }
            if (conditionalFormatting) {
                sheetBuilder.registerWriteHandler(new SeverityConditionalFormatHandler(fieldInfos, true));
            }
            WriteSheet writeSheet = sheetBuilder.build();
            excelWriter.write(rows, writeSheet);
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new RuntimeException("导出Excel异常: " + e.getMessage(), e);
        } finally {
            if (excelWriter != null) {
                try {
                    excelWriter.finish();
                } catch (Exception e) {
                    //忽略finish时的异常，避免掩盖原始异常
                }
            }
        }
    }

    //创建Excel样式策略：表头深色背景+白色文字，交替行颜色
    private HorizontalCellStyleStrategy createExcelStyleStrategy() {
        //表头样式：深蓝色背景，白色文字
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headWriteCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headWriteCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headWriteCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setColor(IndexedColors.WHITE.getIndex());
        headWriteFont.setBold(true);
        headWriteFont.setFontName("微软雅黑");
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //偶数行样式：白色背景
        WriteCellStyle evenRowStyle = new WriteCellStyle();
        evenRowStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        evenRowStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        evenRowStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        evenRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        evenRowStyle.setWrapped(true);
        WriteFont evenRowFont = new WriteFont();
        evenRowFont.setFontName("微软雅黑");
        evenRowFont.setFontHeightInPoints((short) 11);
        evenRowStyle.setWriteFont(evenRowFont);
        //奇数行样式：浅灰色背景
        WriteCellStyle oddRowStyle = new WriteCellStyle();
        oddRowStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        oddRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        oddRowStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        oddRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        oddRowStyle.setWrapped(true);
        WriteFont oddRowFont = new WriteFont();
        oddRowFont.setFontName("微软雅黑");
        oddRowFont.setFontHeightInPoints((short) 11);
        oddRowStyle.setWriteFont(oddRowFont);
        List<WriteCellStyle> contentStyleList = new ArrayList<>();
        contentStyleList.add(evenRowStyle);
        contentStyleList.add(oddRowStyle);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentStyleList);
    }
    //冻结表头和筛选器处理器
    private static class FreezeAndFilterHandler implements SheetWriteHandler {
        private final boolean freezeHeader;
        private final boolean includeFilter;
        private final int columnCount;
        public FreezeAndFilterHandler(boolean freezeHeader, boolean includeFilter, int columnCount) {
            this.freezeHeader = freezeHeader;
            this.includeFilter = includeFilter;
            this.columnCount = columnCount;
        }
        @Override
        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
            Sheet sheet = writeSheetHolder.getSheet();
            if (freezeHeader) {
                sheet.createFreezePane(0, 1, 0, 1);
            }
            if (includeFilter && columnCount > 0) {
                CellRangeAddress filterRange = new CellRangeAddress(0, 0, 0, columnCount - 1);
                sheet.setAutoFilter(filterRange);
            }
        }
    }
    //条件格式处理器：风险等级字段颜色
    private static class SeverityConditionalFormatHandler implements CellWriteHandler {
        private final List<FieldInfoVo> fieldInfos;
        private final boolean enabled;
        public SeverityConditionalFormatHandler(List<FieldInfoVo> fieldInfos, boolean enabled) {
            this.fieldInfos = fieldInfos;
            this.enabled = enabled;
        }
        @Override
        public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                     List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            if (!enabled || isHead || cell == null) {
                return;
            }
            int columnIndex = cell.getColumnIndex();
            if (columnIndex >= fieldInfos.size()) {
                return;
            }
            FieldInfoVo fieldInfo = fieldInfos.get(columnIndex);
            if (!"severity".equals(fieldInfo.getKey()) && !"severityLabel".equals(fieldInfo.getKey())) {
                return;
            }
            String cellValue = cell.getStringCellValue();
            if (StringUtils.isBlank(cellValue)) {
                return;
            }
            CellStyle cellStyle = writeSheetHolder.getSheet().getWorkbook().createCellStyle();
            if (cell.getCellStyle() != null) {
                cellStyle.cloneStyleFrom(cell.getCellStyle());
            }
            //使用自定义RGB颜色匹配设计文档要求
            if (cellStyle instanceof XSSFCellStyle) {
                XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
                XSSFFont xssfFont = ((org.apache.poi.xssf.usermodel.XSSFWorkbook) writeSheetHolder.getSheet().getWorkbook()).createFont();
                xssfFont.setFontName("微软雅黑");
                xssfFont.setFontHeightInPoints((short) 11);
                if (cellValue.contains("高") || cellValue.contains("high") || cellValue.contains("High")) {
                    xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 230, 230), null));
                    xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    xssfFont.setColor(new XSSFColor(new Color(204, 0, 0), null));
                } else if (cellValue.contains("中") || cellValue.contains("medium") || cellValue.contains("Medium")) {
                    xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 244, 230), null));
                    xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    xssfFont.setColor(new XSSFColor(new Color(230, 115, 0), null));
                } else if (cellValue.contains("低") || cellValue.contains("low") || cellValue.contains("Low")) {
                    xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(230, 247, 230), null));
                    xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    xssfFont.setColor(new XSSFColor(new Color(0, 102, 0), null));
                }
                xssfCellStyle.setFont(xssfFont);
            } else {
                //HSSF格式使用IndexedColors
                Font font = writeSheetHolder.getSheet().getWorkbook().createFont();
                font.setFontName("微软雅黑");
                font.setFontHeightInPoints((short) 11);
                if (cellValue.contains("高") || cellValue.contains("high") || cellValue.contains("High")) {
                    cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    font.setColor(IndexedColors.DARK_RED.getIndex());
                } else if (cellValue.contains("中") || cellValue.contains("medium") || cellValue.contains("Medium")) {
                    cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    font.setColor(IndexedColors.ORANGE.getIndex());
                } else if (cellValue.contains("低") || cellValue.contains("low") || cellValue.contains("Low")) {
                    cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    font.setColor(IndexedColors.DARK_GREEN.getIndex());
                }
                cellStyle.setFont(font);
            }
            cell.setCellStyle(cellStyle);
        }
    }
    private void exportToPdf(List<KnowledgeItemVo> data, ExportRequestBo request, String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        String html = generatePreviewHtml(data, buildFieldInfos(request.getSelectedFields(), request.getExpandedFields()), request.getFieldFormats());
        response.getWriter().write(html);
        response.getWriter().flush();
    }
}
