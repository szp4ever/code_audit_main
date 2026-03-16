package org.ruoyi.knowledge.ingestion.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.knowledge.ingestion.loader.ResourceLoader;
import org.ruoyi.knowledge.ingestion.loader.ResourceLoaderFactory;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.file.FileUtils;
import org.ruoyi.common.oss.core.OssClient;
import org.ruoyi.common.oss.factory.OssFactory;

import java.util.Collections;
import java.util.HashMap;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeFragment;
import org.ruoyi.knowledge.curation.domain.KnowledgeItem;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemTag;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeAttachBo;
import org.ruoyi.knowledge.ingestion.domain.bo.StoreEmbeddingBo;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeAttachVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeInfoVo;
import org.ruoyi.knowledge.ingestion.domain.vo.MatchResultVo;
import org.ruoyi.system.domain.vo.SysOssVo;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttachProcess;
import org.ruoyi.knowledge.ingestion.enums.ProcessingStatus;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeAttachMapper;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeAttachProcessMapper;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeFragmentMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeInfoMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemFragmentMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemVulnerabilityTypeMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemTagMapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import com.alibaba.fastjson2.JSON;
import org.ruoyi.service.IChatModelService;
import org.ruoyi.knowledge.ingestion.service.IKnowledgeAttachService;
import org.ruoyi.knowledge.curation.service.IKnowledgeInfoService;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.springframework.dao.DuplicateKeyException;
import org.ruoyi.knowledge.ingestion.service.VectorStoreService;
import org.ruoyi.system.service.ISysOssService;
import org.ruoyi.knowledge.shared.utils.FileUploadValidator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashSet;

/**
 * 知识库附件Service业务层处理
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KnowledgeAttachServiceImpl implements IKnowledgeAttachService {

    private final KnowledgeAttachMapper baseMapper;
    private final KnowledgeFragmentMapper fragmentMapper;
    private final KnowledgeItemMapper knowledgeItemMapper;
    private final KnowledgeInfoMapper knowledgeInfoMapper;
    private final KnowledgeAttachProcessMapper attachProcessMapper;
    private final KnowledgeItemFragmentMapper itemFragmentMapper;
    private final KnowledgeItemVulnerabilityTypeMapper vulnerabilityTypeMapper;
    private final KnowledgeItemTagMapper itemTagMapper;
    private final ISysOssService sysOssService;
    private final VectorStoreService vectorStoreService;
    private final ResourceLoaderFactory resourceLoaderFactory;
    private final IKnowledgeInfoService knowledgeInfoService;
    private final IChatModelService chatModelService;
    private final org.ruoyi.knowledge.ingestion.service.IAttachProcessService attachProcessService;
    private final IKnowledgeItemService knowledgeItemService;

    /**
     * 查询知识库附件（填充扩展字段）
     */
    @Override
    public KnowledgeAttachVo queryById(Long id) {
        KnowledgeAttachVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            fillExtendedFields(Collections.singletonList(vo));
        }
        return vo;
    }

    /**
     * 查询知识库附件列表（填充扩展字段）
     */
    @Override
    public TableDataInfo<KnowledgeAttachVo> queryPageList(KnowledgeAttachBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeAttach> lqw = buildQueryWrapper(bo);
        Page<KnowledgeAttachVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        if (result.getRecords().isEmpty()) {
            return TableDataInfo.build(result);
        }
        fillExtendedFields(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 查询知识库附件列表
     */
    @Override
    public List<KnowledgeAttachVo> queryList(KnowledgeAttachBo bo) {
        LambdaQueryWrapper<KnowledgeAttach> lqw = buildQueryWrapper(bo);
        List<KnowledgeAttachVo> vos = baseMapper.selectVoList(lqw);
        if (CollUtil.isNotEmpty(vos)) {
            fillExtendedFields(vos);
        }
        return vos;
    }
    
    /**
     * 填充扩展字段（文件大小、片段数量、关联知识条目）
     */
    private void fillExtendedFields(List<KnowledgeAttachVo> vos) {
        if (CollUtil.isEmpty(vos)) {
            return;
        }
        
        List<String> docIds = vos.stream()
            .map(KnowledgeAttachVo::getDocId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
        
        if (docIds.isEmpty()) {
            return;
        }
        
        List<Long> ossIds = vos.stream()
            .map(KnowledgeAttachVo::getOssId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
        
        Map<Long, SysOssVo> ossMap = new HashMap<>();
        if (!ossIds.isEmpty()) {
            List<SysOssVo> ossList = sysOssService.listByIds(ossIds);
            ossMap = ossList.stream()
                .collect(Collectors.toMap(SysOssVo::getOssId, oss -> oss, (a, b) -> a));
        }
        
        // 查询processId映射
        Map<String, String> docIdToProcessIdMap = new HashMap<>();
        if (!docIds.isEmpty()) {
            List<KnowledgeAttachProcess> processes = attachProcessMapper.selectList(
                Wrappers.<KnowledgeAttachProcess>lambdaQuery()
                    .in(KnowledgeAttachProcess::getDocId, docIds)
            );
            docIdToProcessIdMap = processes.stream()
                .collect(Collectors.toMap(
                    KnowledgeAttachProcess::getDocId,
                    p -> String.valueOf(p.getId()),
                    (a, b) -> a
                ));
            // 注：如需处理状态跟踪，可在此扩展
        }
        
        // 设置processId到vo
        for (KnowledgeAttachVo vo : vos) {
            if (vo.getDocId() != null && docIdToProcessIdMap.containsKey(vo.getDocId())) {
                vo.setProcessId(docIdToProcessIdMap.get(vo.getDocId()));
            }
        }
        
        Map<String, Integer> fragmentCountMap = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .in(KnowledgeFragment::getDocId, docIds)
                .eq(KnowledgeFragment::getDelFlag, "0")
        ).stream()
        .collect(Collectors.groupingBy(
            KnowledgeFragment::getDocId,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
        ));
        
        // 通过 N:M 中间表查询各文档关联的条目 UUID
        List<Long> fragmentIds = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .in(KnowledgeFragment::getDocId, docIds)
                .eq(KnowledgeFragment::getDelFlag, "0")
                .select(KnowledgeFragment::getId)
        ).stream().map(KnowledgeFragment::getId).collect(Collectors.toList());

        Set<String> itemUuids = new HashSet<>();
        if (!fragmentIds.isEmpty()) {
            itemFragmentMapper.selectList(
                Wrappers.<KnowledgeItemFragment>lambdaQuery()
                    .in(KnowledgeItemFragment::getFragmentId, fragmentIds)
            ).forEach(rel -> itemUuids.add(rel.getItemUuid()));
        }

        Map<String, KnowledgeItem> itemMap = new HashMap<>();
        if (!itemUuids.isEmpty()) {
            List<KnowledgeItem> items = knowledgeItemMapper.selectList(
                Wrappers.<KnowledgeItem>lambdaQuery()
                    .in(KnowledgeItem::getItemUuid, itemUuids)
                    .eq(KnowledgeItem::getDelFlag, "0")
            );
            itemMap = items.stream()
                .collect(Collectors.toMap(KnowledgeItem::getItemUuid, item -> item, (a, b) -> a));
        }
        
        // 通过 N:M 中间表构建 docId → itemUuids 映射（用于 fillExtendedFields）
        List<KnowledgeFragment> docFragments = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .in(KnowledgeFragment::getDocId, docIds)
                .eq(KnowledgeFragment::getDelFlag, "0")
                .select(KnowledgeFragment::getId, KnowledgeFragment::getDocId)
        );
        Map<Long, String> fragmentIdToDocId = docFragments.stream()
            .collect(Collectors.toMap(KnowledgeFragment::getId, KnowledgeFragment::getDocId, (a, b) -> a));

        Map<String, List<String>> docIdToItemUuidsMap = new HashMap<>();
        if (!fragmentIdToDocId.isEmpty()) {
            itemFragmentMapper.selectList(
                Wrappers.<KnowledgeItemFragment>lambdaQuery()
                    .in(KnowledgeItemFragment::getFragmentId, fragmentIdToDocId.keySet())
            ).forEach(rel -> {
                String docId = fragmentIdToDocId.get(rel.getFragmentId());
                if (docId != null) {
                    docIdToItemUuidsMap.computeIfAbsent(docId, k -> new ArrayList<>())
                        .add(rel.getItemUuid());
                }
            });
        }
        
        Map<String, KnowledgeAttach> attachMap = new HashMap<>();
        if (!docIds.isEmpty()) {
            List<KnowledgeAttach> attaches = baseMapper.selectList(
                Wrappers.<KnowledgeAttach>lambdaQuery()
                    .in(KnowledgeAttach::getDocId, docIds)
            );
            attachMap = attaches.stream()
                .collect(Collectors.toMap(KnowledgeAttach::getDocId, attach -> attach, (a, b) -> a));
        }
        
        OssClient storage = OssFactory.instance();

        // ── 批量获取文件大小（一次 LIST 替代逐个 HEAD） ──
        Map<String, String> docIdToObjectPath = new HashMap<>();
        for (KnowledgeAttachVo vo : vos) {
            if (vo.getOssId() == null) continue;
            SysOssVo oss = ossMap.get(vo.getOssId());
            if (oss == null || oss.getUrl() == null) continue;
            try {
                String objectPath = resolveObjectPath(oss, storage);
                if (objectPath != null && !objectPath.isEmpty()) {
                    docIdToObjectPath.put(vo.getDocId(), objectPath);
                }
            } catch (Exception e) {
                log.warn("解析对象路径失败: docId={}, url={}, error={}", vo.getDocId(), oss.getUrl(), e.getMessage());
            }
        }

        Map<String, Long> objectSizeMap = Collections.emptyMap();
        if (!docIdToObjectPath.isEmpty()) {
            try {
                objectSizeMap = storage.batchGetObjectSizes(docIdToObjectPath.values());
            } catch (Exception e) {
                log.warn("批量获取文件大小失败，回退到逐个查询: {}", e.getMessage());
                objectSizeMap = new HashMap<>();
                for (Map.Entry<String, String> entry : docIdToObjectPath.entrySet()) {
                    try {
                        ObjectMetadata meta = storage.getObjectMetadata(entry.getValue());
                        objectSizeMap.put(entry.getValue(), meta.getContentLength());
                    } catch (Exception ex) {
                        log.warn("获取文件大小失败: docId={}, path={}", entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        for (KnowledgeAttachVo vo : vos) {
            String objectPath = docIdToObjectPath.get(vo.getDocId());
            if (objectPath != null) {
                vo.setFileSize(objectSizeMap.get(objectPath));
            } else {
                vo.setFileSize(null);
            }
            
            vo.setFragmentCount(fragmentCountMap.getOrDefault(vo.getDocId(), 0));
            
            List<String> itemUuidList = docIdToItemUuidsMap.get(vo.getDocId());
            if (CollUtil.isNotEmpty(itemUuidList)) {
                List<String> itemTitles = new ArrayList<>();
                int matchedCount = 0;
                int newItemCount = 0;
                KnowledgeAttach attach = attachMap.get(vo.getDocId());
                if (attach != null && attach.getCreateTime() != null) {
                    long attachCreateTime = attach.getCreateTime().getTime();
                    for (String itemUuid : itemUuidList) {
                        KnowledgeItem item = itemMap.get(itemUuid);
                        if (item != null) {
                            itemTitles.add(item.getTitle());
                            if (item.getCreateTime() != null) {
                                long itemCreateTime = item.getCreateTime().getTime();
                                long timeDiff = Math.abs(itemCreateTime - attachCreateTime);
                                if (timeDiff > 300000) {
                                    matchedCount++;
                                } else {
                                    newItemCount++;
                                }
                            } else {
                                newItemCount++;
                            }
                        }
                    }
                } else {
                    for (String itemUuid : itemUuidList) {
                        KnowledgeItem item = itemMap.get(itemUuid);
                        if (item != null) {
                            itemTitles.add(item.getTitle());
                        }
                    }
                }
                vo.setItemUuids(itemUuidList);
                vo.setItemTitles(itemTitles);
                if (!itemUuidList.isEmpty()) {
                    vo.setItemUuid(itemUuidList.get(0));
                    if (!itemTitles.isEmpty()) {
                        vo.setItemTitle(itemTitles.get(0));
                    }
                }
                if (matchedCount > 0 || newItemCount > 0) {
                    MatchResultVo matchResult = new MatchResultVo();
                    matchResult.setMatchedCount(matchedCount);
                    matchResult.setNewItemCount(newItemCount);
                    vo.setMatchResult(matchResult);
                }
            } else {
                vo.setItemUuid(null);
                vo.setItemTitle(null);
                vo.setItemUuids(new ArrayList<>());
                vo.setItemTitles(new ArrayList<>());
            }
            
            String processId = docIdToProcessIdMap.get(vo.getDocId());
            if (StringUtils.isNotBlank(processId)) {
                vo.setProcessId(processId);
            }
        }
    }

    /**
     * 从 SysOssVo 解析出 MinIO 对象路径（去掉 endpoint/bucket 前缀）
     */
    private String resolveObjectPath(SysOssVo oss, OssClient storage) {
        if (StringUtils.isNotBlank(oss.getFileName())) {
            return oss.getFileName();
        }
        String url = oss.getUrl();
        if (url.startsWith("http://") || url.startsWith("https://")) {
            try {
                java.net.URI uri = new java.net.URI(url);
                String path = uri.getPath();
                if (path != null && path.startsWith("/")) {
                    path = path.substring(1);
                }
                // 去掉桶名前缀（如 "ruoyi/2026/03/07/xxx.pdf" → "2026/03/07/xxx.pdf"）
                if (path != null) {
                    String[] parts = path.split("/", 2);
                    if (parts.length > 1) {
                        return parts[1];
                    }
                    return path;
                }
            } catch (java.net.URISyntaxException e) {
                String baseUrl = storage.getUrl();
                String objectPath = url.replace(baseUrl + "/", "").replace(baseUrl, "");
                String bucketName = storage.getConfigKey();
                if (objectPath.startsWith(bucketName + "/")) {
                    objectPath = objectPath.substring(bucketName.length() + 1);
                }
                return objectPath;
            }
        }
        return url;
    }

    private LambdaQueryWrapper<KnowledgeAttach> buildQueryWrapper(KnowledgeAttachBo bo) {
        LambdaQueryWrapper<KnowledgeAttach> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeAttach::getKid, bo.getKid());
        lqw.eq(StringUtils.isNotBlank(bo.getDocId()), KnowledgeAttach::getDocId, bo.getDocId());
        lqw.like(StringUtils.isNotBlank(bo.getDocName()), KnowledgeAttach::getDocName, bo.getDocName());
        lqw.eq(StringUtils.isNotBlank(bo.getDocType()), KnowledgeAttach::getDocType, bo.getDocType());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), KnowledgeAttach::getContent, bo.getContent());

        if (bo.getCreateTimeStart() != null) {
            lqw.ge(KnowledgeAttach::getCreateTime, bo.getCreateTimeStart());
        }
        if (bo.getCreateTimeEnd() != null) {
            lqw.le(KnowledgeAttach::getCreateTime, bo.getCreateTimeEnd());
        }

        if (CollUtil.isNotEmpty(bo.getCreateByList())) {
            lqw.in(KnowledgeAttach::getCreateBy, bo.getCreateByList());
        }

        if (bo.getItemCountMin() != null || bo.getItemCountMax() != null) {
            // 通过 N:M 中间表统计关联条目数
            StringBuilder subQuery = new StringBuilder(
                "doc_id IN (" +
                "SELECT kf.doc_id FROM knowledge_fragment kf " +
                "INNER JOIN knowledge_item_fragment kif ON kif.fragment_id = kf.id " +
                "WHERE kf.del_flag = '0' " +
                "GROUP BY kf.doc_id"
            );
            subQuery.append(" HAVING");
            boolean hasCondition = false;
            if (bo.getItemCountMin() != null) {
                subQuery.append(" COUNT(DISTINCT kif.item_uuid) >= ").append(bo.getItemCountMin());
                hasCondition = true;
            }
            if (bo.getItemCountMax() != null) {
                if (hasCondition) subQuery.append(" AND");
                subQuery.append(" COUNT(DISTINCT kif.item_uuid) <= ").append(bo.getItemCountMax());
            }
            subQuery.append(")");
            lqw.apply(subQuery.toString());
        }

        // 默认只显示处理完成的文档（不包含未完成/处理中的）
        if (bo.getIncludeIncomplete() == null || !bo.getIncludeIncomplete()) {
            String completedStatus = ProcessingStatus.COMPLETED.getCode();
            // 只查询有处理记录且状态为 COMPLETED 的附件
            String existsCompletedSql = "EXISTS (SELECT 1 FROM knowledge_attach_process p " +
                "WHERE p.doc_id = knowledge_attach.doc_id " +
                "AND p.current_status = '" + completedStatus + "' " +
                "AND p.del_flag = '0')";
            lqw.apply(existsCompletedSql);
        }

        return lqw;
    }

    /**
     * 获取附件条目数量分布统计（用于智能分箱）
     */
    @Override
    public List<Integer> getItemCountDistribution(KnowledgeAttachBo bo) {
        LambdaQueryWrapper<KnowledgeAttach> lqw = buildQueryWrapper(bo);
        List<KnowledgeAttachVo> allAttaches = baseMapper.selectVoList(lqw);
        
        if (CollUtil.isEmpty(allAttaches)) {
            return new ArrayList<>();
        }
        
        List<String> docIds = allAttaches.stream()
            .map(KnowledgeAttachVo::getDocId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
        
        if (docIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 通过 N:M 中间表构建 docId → itemUuids 映射
        List<KnowledgeFragment> docFragments2 = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .in(KnowledgeFragment::getDocId, docIds)
                .eq(KnowledgeFragment::getDelFlag, "0")
                .select(KnowledgeFragment::getId, KnowledgeFragment::getDocId)
        );
        Map<Long, String> fragIdToDocId2 = docFragments2.stream()
            .collect(Collectors.toMap(KnowledgeFragment::getId, KnowledgeFragment::getDocId, (a, b) -> a));

        Map<String, List<String>> docIdToItemUuidsMap = new HashMap<>();
        if (!fragIdToDocId2.isEmpty()) {
            itemFragmentMapper.selectList(
                Wrappers.<KnowledgeItemFragment>lambdaQuery()
                    .in(KnowledgeItemFragment::getFragmentId, fragIdToDocId2.keySet())
            ).forEach(rel -> {
                String docId = fragIdToDocId2.get(rel.getFragmentId());
                if (docId != null) {
                    docIdToItemUuidsMap.computeIfAbsent(docId, k -> new ArrayList<>())
                        .add(rel.getItemUuid());
                }
            });
        }
        
        return allAttaches.stream()
            .map(attach -> {
                List<String> itemUuids = docIdToItemUuidsMap.getOrDefault(attach.getDocId(), new ArrayList<>());
                return itemUuids.size();
            })
            .collect(Collectors.toList());
    }

    /**
     * 获取附件分面统计（筛选选项和计数）
     */
    @Override
    public org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo getFacetStats(KnowledgeAttachBo bo) {
        org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo result = new org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo();
        
        KnowledgeAttachBo optionsBo = new KnowledgeAttachBo();
        optionsBo.setKid(bo.getKid());
        LambdaQueryWrapper<KnowledgeAttach> optionsLqw = buildQueryWrapperForOptions(optionsBo);
        List<KnowledgeAttach> allAttachEntities = baseMapper.selectList(optionsLqw);
        if (CollUtil.isNotEmpty(allAttachEntities)) {
            Set<String> docTypes = new HashSet<>();
            Set<String> creators = new HashSet<>();
            List<Integer> itemCounts = new ArrayList<>();
            List<String> docIds = allAttachEntities.stream()
                .map(KnowledgeAttach::getDocId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            
            Map<String, Integer> itemCountMap = new HashMap<>();
            if (!docIds.isEmpty()) {
                // 通过 N:M 中间表统计各文档关联的条目数
                List<KnowledgeFragment> frags = fragmentMapper.selectList(
                    Wrappers.<KnowledgeFragment>lambdaQuery()
                        .in(KnowledgeFragment::getDocId, docIds)
                        .eq(KnowledgeFragment::getDelFlag, "0")
                        .select(KnowledgeFragment::getId, KnowledgeFragment::getDocId)
                );
                Map<Long, String> fIdToDocId = frags.stream()
                    .collect(Collectors.toMap(KnowledgeFragment::getId, KnowledgeFragment::getDocId, (a, b) -> a));
                if (!fIdToDocId.isEmpty()) {
                    Map<String, Set<String>> docIdToItemUuids = new HashMap<>();
                    itemFragmentMapper.selectList(
                        Wrappers.<KnowledgeItemFragment>lambdaQuery()
                            .in(KnowledgeItemFragment::getFragmentId, fIdToDocId.keySet())
                    ).forEach(rel -> {
                        String docId = fIdToDocId.get(rel.getFragmentId());
                        if (docId != null) {
                            docIdToItemUuids.computeIfAbsent(docId, k -> new HashSet<>()).add(rel.getItemUuid());
                        }
                    });
                    docIdToItemUuids.forEach((docId, uuids) -> itemCountMap.put(docId, uuids.size()));
                }
            }
            
            for (KnowledgeAttach attach : allAttachEntities) {
                if (StringUtils.isNotBlank(attach.getDocType())) {
                    docTypes.add(attach.getDocType());
                }
                if (attach.getCreateBy() != null) {
                    creators.add(String.valueOf(attach.getCreateBy()));
                }
                int itemCount = itemCountMap.getOrDefault(attach.getDocId(), 0);
                itemCounts.add(itemCount);
            }
            
            result.getOptions().setDocTypes(docTypes);
            result.getOptions().setCreators(creators);
            
            if (!itemCounts.isEmpty()) {
                List<org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo> ranges = generateItemCountRanges(itemCounts);
                result.getOptions().setItemCountRanges(ranges);
            }
        }
        
        LambdaQueryWrapper<KnowledgeAttach> countsLqw = buildQueryWrapper(bo);
        List<KnowledgeAttach> filteredAttachEntities = baseMapper.selectList(countsLqw);
        if (CollUtil.isNotEmpty(filteredAttachEntities)) {
            List<String> filteredDocIds = filteredAttachEntities.stream()
                .map(KnowledgeAttach::getDocId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            
            Map<String, Integer> itemCountMap = new HashMap<>();
            if (!filteredDocIds.isEmpty()) {
                // 通过 N:M 中间表统计各文档关联的条目数（用于 getFacetStats counts）
                List<KnowledgeFragment> filteredFrags = fragmentMapper.selectList(
                    Wrappers.<KnowledgeFragment>lambdaQuery()
                        .in(KnowledgeFragment::getDocId, filteredDocIds)
                        .eq(KnowledgeFragment::getDelFlag, "0")
                        .select(KnowledgeFragment::getId, KnowledgeFragment::getDocId)
                );
                Map<Long, String> fIdToDocIdFiltered = filteredFrags.stream()
                    .collect(Collectors.toMap(KnowledgeFragment::getId, KnowledgeFragment::getDocId, (a, b) -> a));
                if (!fIdToDocIdFiltered.isEmpty()) {
                    Map<String, Set<String>> docIdToUuids = new HashMap<>();
                    itemFragmentMapper.selectList(
                        Wrappers.<KnowledgeItemFragment>lambdaQuery()
                            .in(KnowledgeItemFragment::getFragmentId, fIdToDocIdFiltered.keySet())
                    ).forEach(rel -> {
                        String docId = fIdToDocIdFiltered.get(rel.getFragmentId());
                        if (docId != null) {
                            docIdToUuids.computeIfAbsent(docId, k -> new HashSet<>()).add(rel.getItemUuid());
                        }
                    });
                    docIdToUuids.forEach((docId, uuids) -> itemCountMap.put(docId, uuids.size()));
                }
            }
            
            for (KnowledgeAttach attach : filteredAttachEntities) {
                if (StringUtils.isNotBlank(attach.getDocType())) {
                    result.getCounts().getDocTypes().merge(attach.getDocType(), 1L, Long::sum);
                }
                if (attach.getCreateBy() != null) {
                    result.getCounts().getCreators().merge(String.valueOf(attach.getCreateBy()), 1L, Long::sum);
                }
                int itemCount = itemCountMap.getOrDefault(attach.getDocId(), 0);
                String rangeKey = getItemCountRangeKey(itemCount, result.getOptions().getItemCountRanges());
                if (rangeKey != null) {
                    result.getCounts().getItemCounts().merge(rangeKey, 1L, Long::sum);
                }
            }
        }
        
        return result;
    }

    /**
     * 构建查询条件（用于获取筛选选项，只应用基础筛选，排除状态和条目数量筛选）
     */
    private LambdaQueryWrapper<KnowledgeAttach> buildQueryWrapperForOptions(KnowledgeAttachBo bo) {
        LambdaQueryWrapper<KnowledgeAttach> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeAttach::getKid, bo.getKid());
        lqw.eq(StringUtils.isNotBlank(bo.getDocId()), KnowledgeAttach::getDocId, bo.getDocId());
        lqw.like(StringUtils.isNotBlank(bo.getDocName()), KnowledgeAttach::getDocName, bo.getDocName());
        
        if (bo.getCreateTimeStart() != null) {
            lqw.ge(KnowledgeAttach::getCreateTime, bo.getCreateTimeStart());
        }
        if (bo.getCreateTimeEnd() != null) {
            lqw.le(KnowledgeAttach::getCreateTime, bo.getCreateTimeEnd());
        }
        
        //应用相同的过滤逻辑（过滤未完成处理的附件）
        if (bo.getIncludeIncomplete() == null || !bo.getIncludeIncomplete()) {
            String completedStatus = ProcessingStatus.COMPLETED.getCode();
            String notExistsSql = "NOT EXISTS (SELECT 1 FROM knowledge_attach_process p " +
                "WHERE p.doc_id = knowledge_attach.doc_id " +
                "AND p.current_status != '" + completedStatus + "' " +
                "AND p.del_flag = '0')";
            lqw.apply(notExistsSql);
        }
        
        return lqw;
    }

    /**
     * 生成条目数量范围（使用Freedman-Diaconis规则和equiprobable分布）
     */
    private List<org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo> generateItemCountRanges(List<Integer> itemCounts) {
        if (CollUtil.isEmpty(itemCounts)) {
            return new ArrayList<>();
        }
        
        List<Integer> sortedCounts = new ArrayList<>(itemCounts);
        Collections.sort(sortedCounts);
        
        int zeroCount = (int) sortedCounts.stream().filter(c -> c == 0).count();
        List<Integer> nonZeroCounts = sortedCounts.stream().filter(c -> c > 0).collect(Collectors.toList());
        
        List<org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo> ranges = new ArrayList<>();
        
        if (zeroCount > 0) {
            org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo zeroRange = new org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo();
            zeroRange.setLabel("0个条目");
            zeroRange.setMin(0);
            zeroRange.setMax(0);
            ranges.add(zeroRange);
        }
        
        if (nonZeroCounts.isEmpty()) {
            return ranges;
        }
        
        int optimalBins = calculateOptimalBins(nonZeroCounts);
        int targetRanges = Math.max(3, Math.min(optimalBins, 12));
        int itemsPerRange = (int) Math.ceil((double) nonZeroCounts.size() / targetRanges);
        
        for (int i = 0; i < targetRanges; i++) {
            int startIdx = i * itemsPerRange;
            int endIdx = Math.min((i + 1) * itemsPerRange, nonZeroCounts.size());
            
            if (startIdx >= nonZeroCounts.size()) break;
            
            int rangeMin = nonZeroCounts.get(startIdx);
            int rangeMax = nonZeroCounts.get(endIdx - 1);
            
            org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo range = new org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo();
            if (rangeMin == rangeMax) {
                range.setLabel(rangeMin + "个条目");
            } else {
                range.setLabel(rangeMin + "-" + rangeMax + "个条目");
            }
            range.setMin(rangeMin);
            range.setMax(rangeMax);
            ranges.add(range);
        }
        
        return ranges;
    }

    /**
     * 计算最优bin数量（Freedman-Diaconis规则）
     */
    private int calculateOptimalBins(List<Integer> data) {
        if (data.isEmpty() || data.size() == 1) return 1;
        
        Collections.sort(data);
        int min = data.get(0);
        int max = data.get(data.size() - 1);
        int range = max - min;
        
        if (range == 0) return 1;
        
        int q1Index = (int) Math.ceil(0.25 * data.size()) - 1;
        int q3Index = (int) Math.ceil(0.75 * data.size()) - 1;
        q1Index = Math.max(0, Math.min(q1Index, data.size() - 1));
        q3Index = Math.max(0, Math.min(q3Index, data.size() - 1));
        
        int q1 = data.get(q1Index);
        int q3 = data.get(q3Index);
        int iqr = q3 - q1;
        
        if (iqr == 0) {
            int binWidth = (int) Math.ceil((double) range / Math.cbrt(data.size()));
            return Math.max(2, Math.min(10, binWidth > 0 ? (int) Math.ceil((double) range / binWidth) : 1));
        }
        
        double binWidth = 2.0 * iqr / Math.cbrt(data.size());
        int bins = (int) Math.ceil((double) range / binWidth);
        
        return Math.max(3, Math.min(12, bins));
    }

    /**
     * 获取条目数量对应的范围键
     */
    private String getItemCountRangeKey(int itemCount, List<org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo> ranges) {
        for (org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo.ItemCountRangeVo range : ranges) {
            if (itemCount >= range.getMin() && itemCount <= range.getMax()) {
                return range.getMin() == range.getMax() ? String.valueOf(range.getMin()) : range.getMin() + "-" + range.getMax();
            }
        }
        return null;
    }

    /**
     * 新增知识库附件（幂等设计）
     * <p>
     * 最佳实践：
     * 1. 同名文件检查存在竞态条件，改为数据库唯一索引 + 异常捕获实现幂等
     * 2. 重复上传同名文件时，返回已存在的附件记录而不是抛异常
     * 3. 数据库应建立唯一索引: (kid, doc_name)
     *
     * @param bo 附件业务对象
     * @return true: 新增成功; false: 记录已存在，返回已有记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(KnowledgeAttachBo bo) {
        // 先尝试查询是否已存在同名文件
        KnowledgeAttach existing = baseMapper.selectOne(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getKid, bo.getKid())
                .eq(KnowledgeAttach::getDocName, bo.getDocName())
                .last("LIMIT 1")
        );
        if (existing != null) {
            log.info("知识库 {} 下已存在同名文件 {}，幂等返回已有记录", bo.getKid(), bo.getDocName());
            bo.setId(existing.getId());
            bo.setDocId(existing.getDocId());
            return true;
        }

        KnowledgeAttach add = MapstructUtils.convert(bo, KnowledgeAttach.class);

        try {
            // 尝试直接插入，依赖数据库唯一索引保证幂等性
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
                log.info("成功新增附件: kid={}, docName={}", add.getKid(), add.getDocName());
            }
            return flag;
        } catch (DuplicateKeyException e) {
            // 并发场景下其他事务已插入，查询并返回已有记录
            log.info("并发插入检测到重复文件，知识库 {} 下文件 {} 已存在，幂等处理", bo.getKid(), bo.getDocName());
            KnowledgeAttach concurrent = baseMapper.selectOne(
                Wrappers.<KnowledgeAttach>lambdaQuery()
                    .eq(KnowledgeAttach::getKid, bo.getKid())
                    .eq(KnowledgeAttach::getDocName, bo.getDocName())
                    .last("LIMIT 1")
            );
            if (concurrent != null) {
                bo.setId(concurrent.getId());
                bo.setDocId(concurrent.getDocId());
            }
            return true;
        }
    }

    /**
     * 修改知识库附件
     */
    @Override
    public Boolean updateByBo(KnowledgeAttachBo bo) {
        KnowledgeAttach update = MapstructUtils.convert(bo, KnowledgeAttach.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验（幂等设计：重名检查移至 insertByBo，避免重复抛异常）
     * <p>
     * 最佳实践：
     * 1. 重名检查由 insertByBo 方法通过 DuplicateKeyException 捕获实现幂等
     * 2. 保留附件数量上限检查（业务规则限制）
     * 3. 基础数据完整性校验保留
     */
    private void validEntityBeforeSave(KnowledgeAttach entity) {
        if (entity == null) {
            throw new ServiceException("附件数据不能为空");
        }

        // 验证知识库ID
        if (StringUtils.isBlank(entity.getKid())) {
            throw new ServiceException("知识库ID不能为空");
        }

        // 验证文档名称
        if (StringUtils.isBlank(entity.getDocName())) {
            throw new ServiceException("文档名称不能为空");
        }

        // 检查单知识库总附件量限制（业务规则，非幂等问题）
        long totalCount = baseMapper.selectCount(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getKid, entity.getKid())
        );
        if (totalCount >= FileUploadValidator.getMaxAttachmentsPerKnowledgeBase()) {
            throw new ServiceException(String.format("该知识库附件数量已达上限（%d个），请先删除部分附件", FileUploadValidator.getMaxAttachmentsPerKnowledgeBase()));
        }
    }

    /**
     * 批量删除知识库附件
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        // 使用 deleteByIds 替代废弃的 deleteBatchIds
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeKnowledgeAttach(String docId) {
        KnowledgeAttach attach = baseMapper.selectOne(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getDocId, docId)
                .last("LIMIT 1")
        );
        
        String kid = null;
        Long ossId = null;
        
        if (attach != null) {
            kid = attach.getKid();
            ossId = attach.getOssId();
        }
        
        // 尝试删除向量数据（失败不影响数据库记录删除）
        if (kid != null) {
            try {
                vectorStoreService.removeByDocId(docId, kid);
            } catch (Exception e) {
                log.warn("删除向量数据失败: docId={}, kid={}, error={}", docId, kid, e.getMessage());
            }
        }
        
        // 尝试删除OSS文件（失败不影响数据库记录删除）
        if (ossId != null) {
            try {
                sysOssService.deleteWithValidByIds(Collections.singletonList(ossId), false);
            } catch (Exception e) {
                log.warn("删除文件失败: ossId={}, error={}", ossId, e.getMessage());
            }
        }
        
        // 删除LLM创建的知识条目并取消处理任务（必须在删除处理任务记录之前）
        if (attach != null) {
            try {
                List<KnowledgeAttachProcess> processes = attachProcessMapper.selectList(
                    Wrappers.<KnowledgeAttachProcess>lambdaQuery()
                        .eq(KnowledgeAttachProcess::getDocId, docId)
                );
                for (KnowledgeAttachProcess process : processes) {
                    deleteLlmCreatedItems(process);
                    String processId = String.valueOf(process.getId());
                    try {
                        attachProcessService.cancelProcess(processId);
                        log.info("取消处理任务: processId={}, docId={}", processId, docId);
                    } catch (Exception e) {
                        log.warn("取消处理任务失败: processId={}, docId={}, error={}", processId, docId, e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.warn("处理任务清理失败: docId={}, error={}", docId, e.getMessage());
            }
        }
        
        // 删除数据库记录（核心操作，必须执行）
        Map<String, Object> processMap = new HashMap<>();
        processMap.put("doc_id", docId);
        attachProcessMapper.deleteByMap(processMap);
        
        Map<String, Object> map = new HashMap<>();
        map.put("doc_id", docId);
        fragmentMapper.deleteByMap(map);
        baseMapper.deleteByMap(map);
        
        // 更新知识库统计字段（删除附件后）
        if (StringUtils.isNotBlank(kid)) {
            try {
                knowledgeItemMapper.updateKnowledgeItemCount(kid);
                knowledgeItemMapper.updateKnowledgeFragmentCount(kid);
                knowledgeItemService.updateKnowledgeDataSize(kid);
            } catch (Exception e) {
                log.warn("更新知识库统计字段失败: kid={}, error={}", kid, e.getMessage());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeKnowledgeAttachByProcessId(String processId) {
        org.ruoyi.knowledge.ingestion.domain.KnowledgeAttachProcess process = attachProcessMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            return;
        }

        // 删除LLM创建的知识条目（必须在删除处理任务之前）
        try {
            deleteLlmCreatedItems(process);
        } catch (Exception e) {
            log.warn("删除LLM创建的知识条目失败: processId={}, error={}", processId, e.getMessage());
        }

        String docId = process.getDocId();
        if (StringUtils.isBlank(docId)) {
            return;
        }

        removeKnowledgeAttach(docId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeKnowledgeAttachByKidAndName(String kid, String docName) {
        // 对文件名进行清理，确保与存储时的文件名一致
        String sanitizedDocName = FileUploadValidator.sanitizeFileName(docName);

        KnowledgeAttach attach = baseMapper.selectOne(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getKid, kid)
                .eq(KnowledgeAttach::getDocName, sanitizedDocName)
                .last("LIMIT 1")
        );

        if (attach == null) {
            log.warn("删除附件时未找到记录: kid={}, docName={} (sanitized={})", kid, docName, sanitizedDocName);
            return;
        }

        String docId = attach.getDocId();
        removeKnowledgeAttach(docId);
    }

    /**
     * 下载附件文件
     */
    @Override
    public void downloadAttach(Long id, HttpServletResponse response) throws IOException {
        KnowledgeAttachVo attach = queryById(id);
        if (attach == null) {
            throw new ServiceException("附件不存在");
        }
        
        if (attach.getOssId() == null) {
            throw new ServiceException("附件文件不存在");
        }
        
        SysOssVo oss = sysOssService.getById(attach.getOssId());
        if (oss == null || oss.getUrl() == null) {
            throw new ServiceException("文件数据不存在");
        }
        
        OssClient storage = OssFactory.instance();
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
        
        FileUtils.setAttachmentResponseHeader(response, attach.getDocName());
        String contentType = getContentType(attach.getDocType());
        response.setContentType(contentType);
        
        try (InputStream inputStream = storage.getObjectContent(objectPath)) {
            int available = inputStream.available();
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
            response.getOutputStream().flush();
            
            log.info("附件下载成功: id={}, docId={}, docName={}, userId={}", 
                id, attach.getDocId(), attach.getDocName(), LoginHelper.getUserId());
        } catch (Exception e) {
            log.error("文件下载失败: id={}, error={}", id, e.getMessage());
            throw new ServiceException("文件下载失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据文件类型获取 Content-Type
     */
    private String getContentType(String docType) {
        if (docType == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        Map<String, String> contentTypeMap = Map.of(
            "pdf", "application/pdf",
            "doc", "application/msword",
            "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "xls", "application/vnd.ms-excel",
            "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "txt", "text/plain",
            "md", "text/markdown"
        );
        return contentTypeMap.getOrDefault(docType.toLowerCase(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
    
    /**
     * 重新处理附件（重新解析、分块、向量化）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reprocessAttach(String docId) {
        KnowledgeAttach attach = baseMapper.selectOne(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getDocId, docId)
                .last("LIMIT 1")
        );
        
        if (attach == null) {
            throw new ServiceException("附件不存在: docId=" + docId);
        }
        
        String kid = attach.getKid();
        
        try {
            vectorStoreService.removeByDocId(docId, kid);
        } catch (Exception e) {
            log.warn("删除向量数据失败: docId={}, kid={}, error={}", docId, kid, e.getMessage());
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("doc_id", docId);
        fragmentMapper.deleteByMap(map);
        
        if (attach.getOssId() == null) {
            throw new ServiceException("附件文件不存在，无法重新处理");
        }
        
        try {
            MultipartFile file = sysOssService.downloadByFile(attach.getOssId());
            List<String> chunkList = new ArrayList<>();
            String content = "";
            ResourceLoader resourceLoader = resourceLoaderFactory.getLoaderByFileType(attach.getDocType());
            List<String> fids = new ArrayList<>();
            
            content = resourceLoader.getContent(file.getInputStream());
            chunkList = resourceLoader.getChunkList(content, kid);
            List<KnowledgeFragment> knowledgeFragmentList = new ArrayList<>();
            if (CollUtil.isNotEmpty(chunkList)) {
                for (int i = 0; i < chunkList.size(); i++) {
                    String fid = RandomUtil.randomString(10);
                    fids.add(fid);
                    KnowledgeFragment knowledgeFragment = new KnowledgeFragment();
                    knowledgeFragment.setKid(kid);
                    knowledgeFragment.setDocId(docId);
                    knowledgeFragment.setFid(fid);
                    knowledgeFragment.setIdx(i);
                    knowledgeFragment.setContent(chunkList.get(i));
                    knowledgeFragment.setCreateTime(new Date());
                    knowledgeFragmentList.add(knowledgeFragment);
                }
            }
            fragmentMapper.insertBatch(knowledgeFragmentList);
            
            // 更新知识库片段数
            if (StringUtils.isNotBlank(kid)) {
                try {
                    knowledgeItemMapper.updateKnowledgeFragmentCount(kid);
                } catch (Exception e) {
                    log.warn("更新知识库片段数失败: kid={}, error={}", kid, e.getMessage());
                }
            }
            
            attach.setContent(content);
            attach.setUpdateTime(new Date());
            baseMapper.updateById(attach);
            
            org.ruoyi.knowledge.curation.domain.KnowledgeInfo knowledgeInfo = knowledgeInfoMapper.selectByKid(kid);
            if (knowledgeInfo == null) {
                throw new ServiceException("知识库不存在: kid=" + kid);
            }
            KnowledgeInfoVo knowledgeInfoVo = knowledgeInfoService.queryById(knowledgeInfo.getId());
            ChatModelVo chatModelVo = chatModelService.selectModelByName(knowledgeInfoVo.getEmbeddingModelName());
            if (chatModelVo == null) {
                chatModelVo = chatModelService.selectModelByCategoryWithHighestPriority("vector");
            }
            StoreEmbeddingBo storeEmbeddingBo = new StoreEmbeddingBo();
            storeEmbeddingBo.setKid(kid);
            storeEmbeddingBo.setDocId(docId);
            storeEmbeddingBo.setFids(fids);
            storeEmbeddingBo.setChunkList(chunkList);
            storeEmbeddingBo.setVectorStoreName(knowledgeInfoVo.getVectorModelName());
            storeEmbeddingBo.setEmbeddingModelName(knowledgeInfoVo.getEmbeddingModelName());
            storeEmbeddingBo.setApiKey(chatModelVo.getApiKey());
            storeEmbeddingBo.setBaseUrl(chatModelVo.getApiHost());
            vectorStoreService.storeEmbeddings(storeEmbeddingBo);
            
            attach.setUpdateTime(new Date());
            baseMapper.updateById(attach);
            
            // 同步更新知识库数据量
            if (StringUtils.isNotBlank(kid)) {
                try {
                    knowledgeItemService.updateKnowledgeDataSize(kid);
                    log.info("重新处理附件后知识库数据量已同步: docId={}, kid={}", docId, kid);
                } catch (Exception syncEx) {
                    log.warn("重新处理附件后知识库数据量同步失败（非致命）: docId={}, kid={}, error={}", docId, kid, syncEx.getMessage());
                }
            }
            
            log.info("重新处理附件成功: docId={}, kid={}", docId, kid);
        } catch (Exception e) {
            log.error("重新处理附件失败: docId={}, error={}", docId, e.getMessage());
            throw new ServiceException("重新处理附件失败: " + e.getMessage());
        }
    }

    @Override
    public String translationByFile(MultipartFile file, String targetLanguage) {
        return "接口开发中!";
    }

    /**
     * 删除LLM创建的知识条目
     * 仅删除statusData中llmCreatedItems记录的条目，不删除匹配到的已有条目
     */
    private void deleteLlmCreatedItems(KnowledgeAttachProcess process) {
        if (process == null) {
            return;
        }
        String statusDataStr = process.getStatusData();
        if (StringUtils.isBlank(statusDataStr) || statusDataStr.trim().isEmpty() || statusDataStr.equals("{}")) {
            return;
        }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> statusData = JSON.parseObject(statusDataStr, Map.class);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> llmCreatedItems = (List<Map<String, Object>>) statusData.getOrDefault("llmCreatedItems", new ArrayList<>());
            if (CollUtil.isEmpty(llmCreatedItems)) {
                return;
            }
            Set<String> itemUuidsToDelete = new HashSet<>();
            Set<String> affectedKids = new HashSet<>();
            for (Map<String, Object> itemData : llmCreatedItems) {
                String itemUuid = (String) itemData.get("itemUuid");
                if (StringUtils.isNotBlank(itemUuid)) {
                    itemUuidsToDelete.add(itemUuid);
                }
            }
            if (CollUtil.isEmpty(itemUuidsToDelete)) {
                return;
            }
            for (String itemUuid : itemUuidsToDelete) {
                try {
                    KnowledgeItem item = knowledgeItemMapper.selectByItemUuid(itemUuid);
                    if (item == null) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(item.getKid())) {
                        affectedKids.add(item.getKid());
                    }
                    vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
                    itemFragmentMapper.deleteByItemUuid(itemUuid);
                    LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
                    itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
                    itemTagMapper.delete(itemTagLqw);
                    knowledgeItemMapper.deleteById(item.getId());
                    log.info("删除LLM创建的知识条目: itemUuid={}, processId={}", itemUuid, process.getId());
                } catch (Exception e) {
                    log.warn("删除知识条目失败: itemUuid={}, processId={}, error={}", itemUuid, process.getId(), e.getMessage());
                }
            }
            // 更新统计字段
            for (String kid : affectedKids) {
                try {
                    knowledgeItemMapper.updateKnowledgeItemCount(kid);
                    knowledgeItemService.updateKnowledgeDataSize(kid);
                } catch (Exception e) {
                    log.warn("更新知识库统计字段失败: kid={}, error={}", kid, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("解析statusData失败: processId={}, error={}", process.getId(), e.getMessage());
        }
    }
}
