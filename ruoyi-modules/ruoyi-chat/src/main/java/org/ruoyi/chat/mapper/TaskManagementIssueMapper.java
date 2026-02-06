package org.ruoyi.chat.mapper; // [修复] 包名修正

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagementIssue;
import org.ruoyi.core.mapper.BaseMapperPlus;

import java.util.List;
import java.util.Map;

/**
 * 任务管理Issue Mapper接口
 */
@Mapper
public interface TaskManagementIssueMapper extends BaseMapperPlus<TaskManagementIssue, TaskManagementIssue> {

    List<TaskManagementIssue> selectIssuesByTaskId(@Param("taskId") Long taskId);

    int batchInsertIssues(@Param("issues") List<TaskManagementIssue> issues);

    int deleteIssuesByTaskId(@Param("taskId") Long taskId);

    // [补充] 之前的 Service 代码用到了这个删除方法
    default int deleteByTaskId(Long taskId) {
        return this.deleteIssuesByTaskId(taskId);
    }

    List<Map<String, Object>> selectSeverityCountByTaskId(@Param("taskId") Long taskId);
}