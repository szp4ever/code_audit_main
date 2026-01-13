package org.ruoyi.chat.service;

import org.ruoyi.chat.domain.ProjectManagement;
import org.ruoyi.chat.domain.vo.ProjectManagementVo;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;

import java.util.Map;

/**
 * 项目管理Service接口
 *
 * @author ruoyi
 */
public interface IProjectManagementService {

    /**
     * 创建项目
     *
     * @param projectManagement 项目信息
     * @return 项目ID
     */
    Long createProject(ProjectManagement projectManagement);

    /**
     * 查询项目列表
     *
     * @param projectManagement 查询条件
     * @param pageQuery 分页参数
     * @return 项目列表
     */
    TableDataInfo<ProjectManagementVo> selectProjectList(ProjectManagement projectManagement, PageQuery pageQuery);

    /**
     * 查询项目详情
     *
     * @param id 项目ID
     * @return 项目详情
     */
    ProjectManagementVo selectProjectById(Long id);

    /**
     * 更新项目
     *
     * @param projectManagement 项目信息
     * @return 结果
     */
    int updateProject(ProjectManagement projectManagement);

    /**
     * 删除项目
     *
     * @param id 项目ID
     * @return 结果
     */
    int deleteProjectById(Long id);

    /**
     * 获取项目的任务列表
     *
     * @param projectId 项目ID
     * @param status 任务状态（可选）
     * @param pageQuery 分页参数
     * @return 任务列表
     */
    TableDataInfo<Map<String, Object>> getProjectTasks(Long projectId, String status, PageQuery pageQuery);

    /**
     * 获取项目统计信息
     *
     * @param projectId 项目ID
     * @return 统计信息 Map包含：taskCount, completedTaskCount, inProgressTaskCount, pendingTaskCount, cancelledTaskCount
     */
    Map<String, Object> getProjectStatistics(Long projectId);
}

