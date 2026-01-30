package org.ruoyi.chat.domain.vo;

import lombok.Data;

/**
 * 任务耗时统计项
 *
 * @author ruoyi
 */
@Data
public class TaskRealTimeCountVO {
    // 执行中任务数量（status = in_progress）
    private Integer inProgressCount;
    // 排队中任务数量（status = pending）
    private Integer pendingCount;
    // 已完成任务数量（status = complete）
    private Integer completeCount;
}


