package org.ruoyi.service;

import org.ruoyi.domain.enums.ProcessingStatus;
import org.ruoyi.domain.vo.KnowledgeAttachProcessVo;

import java.util.List;
import java.util.Map;

/**
 * 附件处理状态管理服务接口
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
public interface IAttachProcessService {

    /**
     * 创建处理任务
     *
     * @param attachId 附件ID
     * @param docId    文档ID
     * @return 处理任务ID（processId）
     */
    String createProcess(Long attachId, String docId);

    /**
     * 更新状态
     *
     * @param processId  处理任务ID
     * @param status      新状态
     * @param statusData  状态数据（可为null）
     */
    void updateStatus(String processId, ProcessingStatus status, Object statusData);

    /**
     * 更新进度（不改变状态，仅更新进度和状态数据）
     * 用于在处理循环中实时更新细粒度进度
     *
     * @param processId  处理任务ID
     * @param statusData  状态数据（包含 currentIndex, totalCount 等进度信息）
     */
    void updateProgress(String processId, Object statusData);

    /**
     * 获取当前状态
     *
     * @param processId 处理任务ID
     * @return 处理状态视图对象
     */
    KnowledgeAttachProcessVo getCurrentStatus(String processId);

    /**
     * 批量获取处理状态
     *
     * @param processIds 处理任务ID列表
     * @return 处理状态视图对象列表
     */
    List<KnowledgeAttachProcessVo> getAttachProcessStatusBatch(List<String> processIds);

    /**
     * 用户确认匹配结果
     *
     * @param processId 处理任务ID
     * @param decisions 匹配决策列表
     */
    void confirmMatching(String processId, List<MatchingDecision> decisions);

    /**
     * 用户确认新条目
     *
     * @param processId     处理任务ID
     * @param modifications 条目修改列表
     */
    void confirmItems(String processId, List<ItemModification> modifications);

    /**
     * 保存草稿（实时保存用户修改）
     *
     * @param processId   处理任务ID
     * @param partialData 部分数据（只包含需要更新的字段）
     */
    void saveDraft(String processId, Map<String, Object> partialData);

    /**
     * 回退到上一状态
     *
     * @param processId   处理任务ID
     * @param targetStatus 目标状态
     */
    void rollback(String processId, ProcessingStatus targetStatus);

    /**
     * 尝试获取分布式锁（进入审阅界面时调用）
     *
     * @param processId 处理任务ID
     * @return 是否成功获取锁
     */
    boolean tryLock(String processId);

    /**
     * 取消处理任务
     *
     * @param processId 处理任务ID
     */
    void cancelProcess(String processId);

    /**
     * 匹配决策
     */
    class MatchingDecision {
        private Integer chunkIndex;
        private String fid;
        private String decision; // keep | change | create_new
        private String selectedItemUuid; // 用户选择的条目UUID（如果decision为change）

        // Getters and Setters
        public Integer getChunkIndex() {
            return chunkIndex;
        }

        public void setChunkIndex(Integer chunkIndex) {
            this.chunkIndex = chunkIndex;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getDecision() {
            return decision;
        }

        public void setDecision(String decision) {
            this.decision = decision;
        }

        public String getSelectedItemUuid() {
            return selectedItemUuid;
        }

        public void setSelectedItemUuid(String selectedItemUuid) {
            this.selectedItemUuid = selectedItemUuid;
        }
    }

    /**
     * 条目修改
     */
    class ItemModification {
        private String itemUuid;
        private Map<String, Object> modifiedFields; // 用户修改的字段

        // Getters and Setters
        public String getItemUuid() {
            return itemUuid;
        }

        public void setItemUuid(String itemUuid) {
            this.itemUuid = itemUuid;
        }

        public Map<String, Object> getModifiedFields() {
            return modifiedFields;
        }

        public void setModifiedFields(Map<String, Object> modifiedFields) {
            this.modifiedFields = modifiedFields;
        }
    }
}
