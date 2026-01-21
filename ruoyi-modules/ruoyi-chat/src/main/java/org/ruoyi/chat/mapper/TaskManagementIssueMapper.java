package org.ruoyi.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagementIssue;

import java.util.List;
import java.util.Map;

/**
 * 任务管理Issue Mapper接口
 *
 * @author ruoyi
 */
public interface TaskManagementIssueMapper extends BaseMapper<TaskManagementIssue> {
    
    /**
     * 根据任务ID查询Issue列表
     *
     * @param taskId 任务ID
     * @return Issue列表
     */
    List<TaskManagementIssue> selectIssuesByTaskId(@Param("taskId") Long taskId);
    
    /**
     * 批量插入Issue
     *
     * @param issues Issue列表
     * @return 插入数量
     */
    int batchInsertIssues(@Param("issues") List<TaskManagementIssue> issues);
    
    /**
     * 根据任务ID删除Issue（逻辑删除）
     *
     * @param taskId 任务ID
     * @return 删除数量
     */
    int deleteIssuesByTaskId(@Param("taskId") Long taskId);
    
    /**
     * 根据任务ID统计各严重程度的Issue数量
     *
     * @param taskId 任务ID
     * @return 统计结果 List<Map<severity, count>>
     */
    List<Map<String, Object>> selectSeverityCountByTaskId(@Param("taskId") Long taskId);
}

