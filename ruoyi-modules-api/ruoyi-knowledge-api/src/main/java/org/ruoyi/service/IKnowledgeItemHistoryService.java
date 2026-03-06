package org.ruoyi.service;

import org.ruoyi.domain.vo.KnowledgeItemHistoryVo;

import java.util.List;

/**
 * 知识条目版本历史Service接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
public interface IKnowledgeItemHistoryService {

    /**
     * 根据itemUuid查询版本历史列表
     */
    List<KnowledgeItemHistoryVo> queryByItemUuid(String itemUuid);

    /**
     * 根据itemUuid和version查询版本历史
     */
    KnowledgeItemHistoryVo queryByItemUuidAndVersion(String itemUuid, Integer version);

    /**
     * 创建版本快照
     */
    Boolean createVersionSnapshot(String itemUuid, String changeType, String changeReason);
}
