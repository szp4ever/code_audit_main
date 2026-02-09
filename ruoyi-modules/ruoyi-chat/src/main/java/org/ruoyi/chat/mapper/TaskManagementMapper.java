package org.ruoyi.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.vo.TaskDurationStatItem;
import org.ruoyi.chat.domain.vo.TaskMonthlyCountItem;
import org.ruoyi.chat.domain.vo.TaskQuarterlyStatsItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 导入 MP 的 Page


import java.util.List;
import java.util.Map;

/**
 * 任务管理Mapper接口
 *
 * @author ruoyi
 */
@Mapper
public interface TaskManagementMapper extends BaseMapper<TaskManagement> {

    /**
     * 查询任务详情（包含文件和标签）
     *
     * @param id 任务ID
     * @return 任务信息
     */
    TaskManagement selectTaskManagementById(Long id);

    List<TaskManagement> selectTaskManagementList(@Param("page") Page<TaskManagement> page, @Param("task") TaskManagement task);

    /**
     * 插入任务
     *
     * @param taskManagement 任务信息
     * @return 结果
     */
    int insertTaskManagement(TaskManagement taskManagement);

    /**
     * 更新任务
     *
     * @param taskManagement 任务信息
     * @return 结果
     */
    int updateTaskManagement(TaskManagement taskManagement);

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @return 结果
     */
    int deleteTaskManagementById(Long id);

    /**
     * 统计任务状态分布
     *
     * @return 状态统计结果 List<Map<key: status, value: count>>
     */
    List<Map<String, Object>> selectStatusStats();

    /**
     * 统计任务类型分布
     *
     * @return 类型统计结果 List<Map<key: taskType, value: count>>
     */
    List<Map<String, Object>> selectTypeStats();

    /**
     * 统计任务耗时
     *
     * @param timeRange 时间范围：day|hour|week
     * @return 耗时统计结果
     */
    List<TaskDurationStatItem> selectDurationStats(@Param("timeRange") String timeRange);

    /**
     * 统计月度任务
     */
    List<TaskMonthlyCountItem> selectTaskMonthlyCount(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    /**
     * 统计季度信息
     * @param year
     * @return
     */
    List<TaskQuarterlyStatsItem> selectTaskQuarterlyStats(@Param("year") String year);

    /**
     * 按任务状态统计任务数量
     * @param status
     * @return
     */
    Integer countTaskByStatus(@Param("status") String status);
}






