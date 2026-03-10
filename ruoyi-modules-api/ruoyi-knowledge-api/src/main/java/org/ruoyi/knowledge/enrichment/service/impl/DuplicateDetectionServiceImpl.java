package org.ruoyi.knowledge.enrichment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeItemBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemMapper;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemFragmentService;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.ruoyi.knowledge.enrichment.service.IDuplicateDetectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 重复条目检测服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DuplicateDetectionServiceImpl implements IDuplicateDetectionService {

    private final IKnowledgeItemService knowledgeItemService;
    private final IKnowledgeItemFragmentService knowledgeItemFragmentService;
    private final KnowledgeItemMapper knowledgeItemMapper;

    /** 各维度权重 */
    private static final double WEIGHT_TITLE = 0.40;
    private static final double WEIGHT_CONTENT = 0.25;
    private static final double WEIGHT_CWE = 0.15;
    private static final double WEIGHT_VULN_TYPE = 0.10;
    private static final double WEIGHT_LANGUAGE = 0.10;

    @Override
    public List<DuplicatePair> detectDuplicates(String kid, double threshold) {
        log.info("detectDuplicates: 开始检测重复条目, kid={}, threshold={}", kid, threshold);

        // 1. 加载知识库中所有条目
        KnowledgeItemBo queryBo = new KnowledgeItemBo();
        queryBo.setKid(kid);
        List<KnowledgeItemVo> items = knowledgeItemService.queryList(queryBo);

        if (CollUtil.isEmpty(items) || items.size() < 2) {
            log.info("detectDuplicates: 条目数不足，无法检测重复, kid={}, count={}",
                kid, items == null ? 0 : items.size());
            return Collections.emptyList();
        }

        List<DuplicatePair> results = new ArrayList<>();

        // 2. 两两比较
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                KnowledgeItemVo a = items.get(i);
                KnowledgeItemVo b = items.get(j);

                // 标题相似度
                double titleSim = levenshteinSimilarity(
                    StrUtil.blankToDefault(a.getTitle(), ""),
                    StrUtil.blankToDefault(b.getTitle(), "")
                );

                // 内容相似度（基于摘要的 Jaccard）
                double contentSim = jaccardSimilarity(
                    StrUtil.blankToDefault(a.getSummary(), ""),
                    StrUtil.blankToDefault(b.getSummary(), "")
                );

                // CWE 匹配
                boolean sameCwe = hasSameElement(a.getVulnerabilityTypes(), b.getVulnerabilityTypes());
                if (!sameCwe) {
                    // 回退到单值字段比较
                    sameCwe = StrUtil.isNotBlank(a.getVulnerabilityType())
                        && StrUtil.equals(a.getVulnerabilityType(), b.getVulnerabilityType());
                }

                // 漏洞类型匹配（使用单值 vulnerabilityType 字段的文本相似度）
                boolean sameVulnType = false;
                if (StrUtil.isNotBlank(a.getVulnerabilityType()) && StrUtil.isNotBlank(b.getVulnerabilityType())) {
                    sameVulnType = levenshteinSimilarity(a.getVulnerabilityType(), b.getVulnerabilityType()) > 0.8;
                }

                // 语言匹配
                boolean sameLanguage = StrUtil.isNotBlank(a.getLanguage())
                    && StrUtil.equalsIgnoreCase(a.getLanguage(), b.getLanguage());

                // 加权总分
                double overallScore = WEIGHT_TITLE * titleSim
                    + WEIGHT_CONTENT * contentSim
                    + WEIGHT_CWE * (sameCwe ? 1.0 : 0.0)
                    + WEIGHT_VULN_TYPE * (sameVulnType ? 1.0 : 0.0)
                    + WEIGHT_LANGUAGE * (sameLanguage ? 1.0 : 0.0);

                if (overallScore >= threshold) {
                    String reason = buildReason(titleSim, contentSim, sameCwe, sameVulnType, sameLanguage);
                    results.add(new DuplicatePair(
                        a.getItemUuid(), a.getTitle(),
                        b.getItemUuid(), b.getTitle(),
                        titleSim, contentSim,
                        sameCwe, sameVulnType,
                        overallScore, reason
                    ));
                }
            }
        }

        // 按总分降序排序
        results.sort(Comparator.comparingDouble(DuplicatePair::overallScore).reversed());

        log.info("detectDuplicates: 检测完成, kid={}, 条目数={}, 重复对数={}",
            kid, items.size(), results.size());

        return results;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeItems(String keepUuid, String archiveUuid) {
        log.info("mergeItems: 开始合并条目, keep={}, archive={}", keepUuid, archiveUuid);

        // 1. 验证两个条目都存在
        KnowledgeItemVo keepItem = knowledgeItemService.queryByItemUuid(keepUuid);
        KnowledgeItemVo archiveItem = knowledgeItemService.queryByItemUuid(archiveUuid);

        if (keepItem == null) {
            throw new IllegalArgumentException("保留条目不存在: " + keepUuid);
        }
        if (archiveItem == null) {
            throw new IllegalArgumentException("归档条目不存在: " + archiveUuid);
        }

        // 2. 获取归档条目的所有片段关联
        List<KnowledgeItemFragment> archiveFragments = knowledgeItemFragmentService.listByItemUuid(archiveUuid);

        if (CollUtil.isNotEmpty(archiveFragments)) {
            // 获取保留条目已有的片段 ID
            List<KnowledgeItemFragment> keepFragments = knowledgeItemFragmentService.listByItemUuid(keepUuid);
            Set<Long> keepFragmentIds = keepFragments.stream()
                .map(KnowledgeItemFragment::getFragmentId)
                .collect(Collectors.toSet());

            // 转移不重复的片段关联
            List<Long> toTransfer = archiveFragments.stream()
                .map(KnowledgeItemFragment::getFragmentId)
                .filter(fid -> !keepFragmentIds.contains(fid))
                .collect(Collectors.toList());

            if (CollUtil.isNotEmpty(toTransfer)) {
                knowledgeItemFragmentService.batchAssociate(keepUuid, toTransfer, null);
                log.info("mergeItems: 转移了 {} 个片段关联到条目 {}", toTransfer.size(), keepUuid);
            }

            // 移除归档条目的所有关联
            knowledgeItemFragmentService.disassociateAll(archiveUuid);
        }

        // 3. 将归档条目状态设为 archived
        KnowledgeItemBo archiveBo = new KnowledgeItemBo();
        archiveBo.setId(archiveItem.getId());
        archiveBo.setItemUuid(archiveUuid);
        archiveBo.setKid(archiveItem.getKid());
        archiveBo.setTitle(archiveItem.getTitle());
        archiveBo.setStatus("archived");
        knowledgeItemService.updateByBo(archiveBo);

        log.info("mergeItems: 合并完成, 保留条目=[{}]({}), 归档条目=[{}]({})",
            keepItem.getTitle(), keepUuid, archiveItem.getTitle(), archiveUuid);
    }

    // ======================== 私有方法 ========================

    /**
     * 基于 Levenshtein 编辑距离计算字符串相似度（0-1）
     */
    private double levenshteinSimilarity(String a, String b) {
        if (a.equals(b)) {
            return 1.0;
        }
        if (a.isEmpty() || b.isEmpty()) {
            return 0.0;
        }

        int lenA = a.length();
        int lenB = b.length();

        // 使用两行滚动数组优化空间
        int[] prev = new int[lenB + 1];
        int[] curr = new int[lenB + 1];

        for (int j = 0; j <= lenB; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= lenA; i++) {
            curr[0] = i;
            for (int j = 1; j <= lenB; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(
                    Math.min(curr[j - 1] + 1, prev[j] + 1),
                    prev[j - 1] + cost
                );
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        int distance = prev[lenB];
        int maxLen = Math.max(lenA, lenB);
        return 1.0 - (double) distance / maxLen;
    }

    /**
     * 基于词级 Jaccard 系数计算文本相似度（0-1）
     */
    private double jaccardSimilarity(String a, String b) {
        if (StrUtil.isBlank(a) || StrUtil.isBlank(b)) {
            return 0.0;
        }
        Set<String> setA = new HashSet<>(Arrays.asList(a.split("\\s+")));
        Set<String> setB = new HashSet<>(Arrays.asList(b.split("\\s+")));

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        if (union.isEmpty()) {
            return 0.0;
        }
        return (double) intersection.size() / union.size();
    }

    /**
     * 检查两个列表是否有共同元素
     */
    private boolean hasSameElement(List<String> listA, List<String> listB) {
        if (CollUtil.isEmpty(listA) || CollUtil.isEmpty(listB)) {
            return false;
        }
        Set<String> setA = new HashSet<>(listA);
        return listB.stream().anyMatch(setA::contains);
    }

    /**
     * 构建可读的重复原因说明
     */
    private String buildReason(double titleSim, double contentSim,
                               boolean sameCwe, boolean sameVulnType, boolean sameLanguage) {
        List<String> reasons = new ArrayList<>();
        if (titleSim > 0.7) {
            reasons.add(String.format("标题相似度 %.0f%%", titleSim * 100));
        }
        if (contentSim > 0.5) {
            reasons.add(String.format("内容相似度 %.0f%%", contentSim * 100));
        }
        if (sameCwe) {
            reasons.add("相同CWE");
        }
        if (sameVulnType) {
            reasons.add("相同漏洞类型");
        }
        if (sameLanguage) {
            reasons.add("相同语言");
        }
        return reasons.isEmpty() ? "综合评分达标" : String.join("、", reasons);
    }
}
