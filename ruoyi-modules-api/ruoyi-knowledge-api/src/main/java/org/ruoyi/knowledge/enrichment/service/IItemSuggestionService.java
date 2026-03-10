package org.ruoyi.knowledge.enrichment.service;

import java.util.List;

/**
 * 条目建议服务 — 从未关联的片段中生成知识条目草稿建议。
 * <p>
 * 工作流程：
 * 1. 选取一批未关联条目的片段
 * 2. 对每个片段调用 LLM 提取结构化数据
 * 3. 将提取结果包装为"建议"（ItemSuggestion），等待人工审核
 * 4. 人工确认后，创建正式的 KnowledgeItem 并关联片段
 */
public interface IItemSuggestionService {

    /**
     * 从指定片段列表生成条目建议
     *
     * @param fragmentIds 片段 ID 列表
     * @param kid         知识库 ID
     * @return 生成的建议列表
     */
    List<ItemSuggestion> generateSuggestions(List<Long> fragmentIds, String kid);

    /**
     * 接受建议 — 创建正式条目并关联片段
     *
     * @param suggestionId 建议 ID
     * @return 创建的条目 UUID
     */
    String acceptSuggestion(String suggestionId);

    /**
     * 拒绝建议
     *
     * @param suggestionId 建议 ID
     * @param reason       拒绝原因（可选）
     */
    void rejectSuggestion(String suggestionId, String reason);

    /**
     * 查询待审核的建议列表
     *
     * @param kid 知识库 ID
     * @return 待审核建议列表
     */
    List<ItemSuggestion> listPendingSuggestions(String kid);

    /**
     * 条目建议 DTO
     */
    record ItemSuggestion(
        String suggestionId,
        Long fragmentId,
        String fragmentContent,
        String kid,
        String suggestedTitle,
        String suggestedSummary,
        String suggestedVulnerabilityType,
        String suggestedSeverity,
        String suggestedProblemDescription,
        String suggestedFixSuggestion,
        String suggestedExampleCode,
        List<String> suggestedTags,
        double confidence,
        String status // pending / accepted / rejected
    ) {}
}
