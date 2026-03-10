package org.ruoyi.knowledge.curation.service;

import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.curation.domain.vo.ItemFragmentDetailVo;

import java.util.List;

/**
 * 知识条目-片段关联 Service
 */
public interface IKnowledgeItemFragmentService {

    /**
     * 为条目关联一个片段（人工）
     */
    KnowledgeItemFragment associate(String itemUuid, Long fragmentId);

    /**
     * 为条目批量关联片段（AI 建议）
     */
    List<KnowledgeItemFragment> batchAssociate(String itemUuid, List<Long> fragmentIds, Double relevanceScore);

    /**
     * 取消条目与片段的关联
     */
    void disassociate(String itemUuid, Long fragmentId);

    /**
     * 取消条目的所有片段关联
     */
    void disassociateAll(String itemUuid);

    /**
     * 查询条目关联的所有片段关联记录
     */
    List<KnowledgeItemFragment> listByItemUuid(String itemUuid);

    /**
     * 查询条目关联的片段详情（含 content、documentName 等，用于前端展示）
     */
    List<ItemFragmentDetailVo> listByItemUuidWithDetails(String itemUuid);

    /**
     * 查询片段关联的所有条目关联记录
     */
    List<KnowledgeItemFragment> listByFragmentId(Long fragmentId);

    /**
     * 检查关联是否已存在
     */
    boolean exists(String itemUuid, Long fragmentId);

    /**
     * 统计条目关联的片段数量
     */
    long countByItemUuid(String itemUuid);

    /**
     * 统计片段关联的条目数量
     */
    long countByFragmentId(Long fragmentId);
}
