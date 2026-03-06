package org.ruoyi.service;

import org.ruoyi.domain.bo.ExtractionContext;
import org.ruoyi.domain.bo.ExtractedItemData;

import java.util.List;

/**
 * 知识条目提取服务接口
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
public interface IKnowledgeItemExtractionService {

    /**
     * 从片段内容提取结构化信息
     *
     * @param chunkContent 片段内容
     * @param context     提取上下文（包含约束信息）
     * @return 提取的结构化数据
     */
    ExtractedItemData extractFromChunk(String chunkContent, ExtractionContext context);

    /**
     * 批量提取
     *
     * @param chunkContents 片段内容列表
     * @param context       提取上下文
     * @return 提取的结构化数据列表
     */
    List<ExtractedItemData> extractBatch(List<String> chunkContents, ExtractionContext context);

    /**
     * 获取提取进度（异步调用时）
     *
     * @param taskId 任务ID
     * @return 提取进度
     */
    ExtractionProgress getProgress(String taskId);

    /**
     * 提取进度
     */
    class ExtractionProgress {
        private String taskId;
        private int total;
        private int completed;
        private int failed;

        // Getters and Setters
        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public int getFailed() {
            return failed;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }
    }
}
