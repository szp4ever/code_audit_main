package org.ruoyi.knowledge.curation.service;

import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemHistoryVo;
import org.ruoyi.knowledge.curation.domain.vo.VersionDiffVo;

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

    /**
     * 计算两个版本之间的 diff
     */
    VersionDiffVo diffVersions(String itemUuid, Integer fromVersion, Integer toVersion);

    /**
     * 恢复到指定版本（非破坏性：生成新版本）
     */
    Boolean restoreToVersion(String itemUuid, Integer targetVersion, String reason);
}
