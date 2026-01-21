package org.ruoyi.chat.service;

import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.vo.TaskDurationStatItem;
import org.ruoyi.chat.domain.vo.TaskManagementVo;
import org.ruoyi.chat.domain.vo.TaskVulnerabilityDetailVo;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;

import java.util.List;
import java.util.Map;

/**
 * 任务管理Service接口
 *
 * @author ruoyi
 */
public interface ITaskManagementService {

    /**
     * 创建任务
     *
     * @param taskManagement 任务信息
     * @return 任务ID
     */
    Long createTask(TaskManagement taskManagement);

    /**
     * 查询任务列表
     *
     * @param taskManagement 查询条件
     * @param pageQuery 分页参数
     * @return 任务列表
     */
    TableDataInfo<TaskManagementVo> selectTaskList(TaskManagement taskManagement, PageQuery pageQuery);

    /**
     * 查询任务详情
     *
     * @param id 任务ID
     * @return 任务详情
     */
    TaskManagementVo selectTaskById(Long id);

    /**
     * 更新任务
     *
     * @param taskManagement 任务信息
     * @return 结果
     */
    int updateTask(TaskManagement taskManagement);

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @return 结果
     */
    int deleteTaskById(Long id);

    /**
     * 获取任务状态统计
     *
     * @return 状态统计结果 Map<status, count>
     */
    Map<String, Integer> getStatusStats();

    /**
     * 获取任务类型统计
     *
     * @return 类型统计结果 Map<taskType, count>
     */
    Map<String, Integer> getTypeStats();

    /**
     * 获取任务耗时统计
     *
     * @param timeRange 时间范围：day|hour|week
     * @return 耗时统计结果
     */
    List<TaskDurationStatItem> getDurationStats(String timeRange);

    /**
     * 获取任务漏洞详情
     *
     * @param taskId 任务ID
     * @return 任务漏洞详情
     */
    TaskVulnerabilityDetailVo getTaskVulnerabilities(Long taskId);
}






