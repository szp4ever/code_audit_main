package org.ruoyi.knowledge.enrichment.service;

import java.util.List;

/**
 * 重复条目检测服务 — 扫描知识库中的条目，找出可能重复的条目对。
 * <p>
 * 检测策略：
 * 1. 标题相似度（编辑距离 / Jaccard）
 * 2. 内容向量相似度
 * 3. CWE + 漏洞类型 + 语言的组合匹配
 */
public interface IDuplicateDetectionService {

    /**
     * 检测知识库中的重复条目
     *
     * @param kid       知识库 ID
     * @param threshold 相似度阈值（0-1）
     * @return 重复对列表
     */
    List<DuplicatePair> detectDuplicates(String kid, double threshold);

    /**
     * 合并两个重复条目（保留 A，将 B 的片段关联转移到 A，然后归档 B）
     *
     * @param keepUuid    保留的条目 UUID
     * @param archiveUuid 归档的条目 UUID
     */
    void mergeItems(String keepUuid, String archiveUuid);

    /**
     * 重复对 DTO
     */
    record DuplicatePair(
        String itemUuidA,
        String titleA,
        String itemUuidB,
        String titleB,
        double titleSimilarity,
        double contentSimilarity,
        boolean sameCwe,
        boolean sameVulnType,
        double overallScore,
        String reason
    ) {}
}
