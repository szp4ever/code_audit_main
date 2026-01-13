package org.ruoyi.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.ProjectManagement;

import java.util.List;
import java.util.Map;

/**
 * 项目管理Mapper接口
 *
 * @author ruoyi
 */
public interface ProjectManagementMapper extends BaseMapper<ProjectManagement> {

    /**
     * 查询项目详情（包含标签和统计信息）
     *
     * @param id 项目ID
     * @return 项目信息
     */
    ProjectManagement selectProjectManagementById(Long id);

    /**
     * 查询项目列表（包含统计信息）
     *
     * @param projectManagement 查询条件
     * @return 项目列表
     */
    List<ProjectManagement> selectProjectManagementList(ProjectManagement projectManagement);

    /**
     * 插入项目
     *
     * @param projectManagement 项目信息
     * @return 结果
     */
    int insertProjectManagement(ProjectManagement projectManagement);

    /**
     * 更新项目
     *
     * @param projectManagement 项目信息
     * @return 结果
     */
    int updateProjectManagement(ProjectManagement projectManagement);

    /**
     * 删除项目（逻辑删除）
     *
     * @param id 项目ID
     * @return 结果
     */
    int deleteProjectManagementById(Long id);

    /**
     * 统计项目的任务数量
     *
     * @param projectId 项目ID
     * @return 任务数量
     */
    Integer countTasksByProjectId(@Param("projectId") Long projectId);

    /**
     * 统计项目的已完成任务数量
     *
     * @param projectId 项目ID
     * @return 已完成任务数量
     */
    Integer countCompletedTasksByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据项目ID查询任务列表
     *
     * @param projectId 项目ID
     * @param status 任务状态（可选）
     * @return 任务列表
     */
    List<Map<String, Object>> selectTasksByProjectId(@Param("projectId") Long projectId, @Param("status") String status);

    /**
     * 获取项目统计信息
     *
     * @param projectId 项目ID
     * @return 统计信息 Map包含：taskCount, completedTaskCount, inProgressTaskCount, pendingTaskCount, cancelledTaskCount
     */
    Map<String, Object> selectProjectStatistics(@Param("projectId") Long projectId);
}

