package org.ruoyi.knowledge.enrichment.service;

import java.util.List;

/**
 * 片段智能匹配服务 — 为已有条目推荐相关的未关联片段。
 * <p>
 * 使用向量相似度搜索 + LLM 二次确认的两阶段匹配策略：
 * 1. 向量搜索：用条目的标题+摘要作为 query，在向量库中搜索相似片段
 * 2. LLM 确认：对候选片段调用 LLM 判断是否真正相关
 */
public interface IFragmentMatchService {

    /**
     * 为指定条目推荐关联片段
     *
     * @param itemUuid 条目 UUID
     * @param kid      知识库 ID（限定搜索范围）
     * @param limit    最大返回数量
     * @return 匹配结果列表
     */
    List<MatchResult> matchFragments(String itemUuid, String kid, int limit);

    /**
     * 批量匹配 — 为知识库中所有未关联片段的条目推荐片段
     *
     * @param kid   知识库 ID
     * @param limit 每个条目最大推荐数
     * @return 所有匹配结果
     */
    List<MatchResult> batchMatch(String kid, int limit);

    /**
     * 匹配结果 DTO
     */
    record MatchResult(
        String itemUuid,
        String itemTitle,
        Long fragmentId,
        String fragmentPreview,
        double vectorSimilarity,
        double llmConfidence,
        String matchReason
    ) {}
}
