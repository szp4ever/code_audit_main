package org.ruoyi.chat.service;

import org.ruoyi.chat.domain.TaskManagement;

/**
 * 任务处理Service接口
 * 用于调用Python Flask接口处理任务
 *
 * @author ruoyi
 */
public interface ITaskProcessingService {

    /**
     * 处理任务
     * 调用Flask接口处理任务，并保存返回的文件
     *
     * @param taskManagement 任务信息
     */
    void processTask(TaskManagement taskManagement);
}




