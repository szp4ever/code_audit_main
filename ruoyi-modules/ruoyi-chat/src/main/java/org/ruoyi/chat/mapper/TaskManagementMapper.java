package org.ruoyi.chat.mapper; // [修复] 包名修正

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.vo.TaskDurationStatItem;
import org.ruoyi.chat.domain.vo.TaskManagementVo;
import org.ruoyi.core.mapper.BaseMapperPlus;

import java.util.List;
import java.util.Map;

/**
 * 任务管理Mapper接口
 */
@Mapper
public interface TaskManagementMapper extends BaseMapperPlus<TaskManagement, TaskManagementVo> {

    TaskManagement selectTaskManagementById(@Param("id") Long id);

    List<TaskManagement> selectTaskManagementList(TaskManagement taskManagement);

    List<Map<String, Object>> selectStatusStats();

    List<Map<String, Object>> selectTypeStats();

    List<TaskDurationStatItem> selectDurationStats(@Param("timeRange") String timeRange);
}