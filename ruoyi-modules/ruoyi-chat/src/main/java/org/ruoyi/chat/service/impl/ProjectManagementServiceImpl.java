package org.ruoyi.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.ProjectManagement;
import org.ruoyi.chat.domain.ProjectManagementTag;
import org.ruoyi.chat.domain.vo.ProjectManagementVo;
import org.ruoyi.chat.mapper.ProjectManagementMapper;
import org.ruoyi.chat.mapper.ProjectManagementTagMapper;
import org.ruoyi.chat.service.IProjectManagementService;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目管理Service业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectManagementServiceImpl implements IProjectManagementService {

    private final ProjectManagementMapper projectManagementMapper;
    private final ProjectManagementTagMapper projectManagementTagMapper;

    /**
     * 创建项目
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProject(ProjectManagement projectManagement) {
        // 设置默认状态
        if (ObjectUtil.isEmpty(projectManagement.getStatus())) {
            projectManagement.setStatus("active");
        }
        
        // 处理前端传来的创建人信息
        // 如果前端传了 createdById，则设置到 createById
        if (ObjectUtil.isNotEmpty(projectManagement.getCreatedById())) {
            projectManagement.setCreateById(projectManagement.getCreatedById());
        }
        // 如果前端传了 createdBy（String，用户名），则设置到 createByName
        if (ObjectUtil.isNotEmpty(projectManagement.getCreatedBy())) {
            projectManagement.setCreateByName(projectManagement.getCreatedBy());
        }
        
        // 处理前端传来的创建部门信息
        // 直接存储前端传来的字符串，不进行任何转换或查询
        // 优先使用createdByDept（前端传入的字段名）
        if (ObjectUtil.isNotEmpty(projectManagement.getCreatedByDept())) {
            projectManagement.setCreateDeptName(projectManagement.getCreatedByDept());
            log.info("从createdByDept设置createDeptName: {}", projectManagement.getCreatedByDept());
        }
        // 如果createdByDept为空，尝试使用createDeptId（如果前端传了这个字段）
        else if (ObjectUtil.isNotEmpty(projectManagement.getCreateDeptId())) {
            // 将Long类型的createDeptId转换为String
            projectManagement.setCreateDeptName(String.valueOf(projectManagement.getCreateDeptId()));
            log.info("从createDeptId设置createDeptName: {}", projectManagement.getCreateDeptId());
        }
        // 如果createDeptId也为空，但createDeptName不为空（前端直接传了createDept），则使用createDeptName
        // 注意：我们在ProjectManagement中使用createDeptName字段（String类型），直接存储前端传来的字符串
        
        log.info("========== 创建项目开始 ==========");
        log.info("项目名称: {}", projectManagement.getName());
        log.info("创建人: {}", projectManagement.getCreateByName());
        log.info("创建人ID: {}", projectManagement.getCreateById());
        log.info("创建部门 (createDeptName): {}", projectManagement.getCreateDeptName());
        log.info("前端传来的createdByDept: {}", projectManagement.getCreatedByDept());
        log.info("前端传来的createDeptId: {}", projectManagement.getCreateDeptId());
        
        // 如果createDeptName仍然为null，记录警告
        if (projectManagement.getCreateDeptName() == null) {
            log.warn("警告：createDeptName字段为null，可能前端没有传入部门信息");
        } else {
            log.info("createDeptName字段已设置，值: {}", projectManagement.getCreateDeptName());
        }
        
        // 插入项目主表（使用自定义的insertProjectManagement方法，支持createdBy字段）
        projectManagementMapper.insertProjectManagement(projectManagement);
        Long projectId = projectManagement.getId();
        
        log.info("项目ID（自增主键）: {}", projectId);

        // 保存标签
        if (CollUtil.isNotEmpty(projectManagement.getTags())) {
            List<ProjectManagementTag> tags = new ArrayList<>();
            for (String tagName : projectManagement.getTags()) {
                ProjectManagementTag tag = new ProjectManagementTag();
                tag.setProjectId(projectId);
                tag.setTagName(tagName);
                tag.setCreateTime(new Date());
                tags.add(tag);
            }
            projectManagementTagMapper.batchInsertTags(tags);
            log.info("标签保存成功，数量: {}", tags.size());
        }

        return projectId;
    }

    /**
     * 查询项目列表
     */
    @Override
    public TableDataInfo<ProjectManagementVo> selectProjectList(ProjectManagement projectManagement, PageQuery pageQuery) {
        // 构建查询条件
        LambdaQueryWrapper<ProjectManagement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectManagement::getDelFlag, "0");
        
        if (ObjectUtil.isNotEmpty(projectManagement.getStatus())) {
            wrapper.eq(ProjectManagement::getStatus, projectManagement.getStatus());
        }
        
        if (ObjectUtil.isNotEmpty(projectManagement.getName())) {
            wrapper.like(ProjectManagement::getName, projectManagement.getName());
        }
        
        if (ObjectUtil.isNotEmpty(projectManagement.getCreateById())) {
            wrapper.eq(ProjectManagement::getCreateById, projectManagement.getCreateById());
        }
        
        wrapper.orderByDesc(ProjectManagement::getCreateTime);
        
        // 使用自定义查询方法，避免MyBatis-Plus自动映射BaseEntity的createBy字段（Long类型）
        // 先查询总数
        Long total = projectManagementMapper.selectCount(wrapper);
        // 构建分页查询
        Page<ProjectManagement> page = pageQuery.build();
        // 查询列表（使用自定义方法，使用ResultMap正确映射createByName字段）
        List<ProjectManagement> list = projectManagementMapper.selectProjectManagementList(projectManagement);
        // 手动分页
        int start = (int) ((page.getCurrent() - 1) * page.getSize());
        int end = Math.min(start + (int) page.getSize(), list.size());
        List<ProjectManagement> pagedList = start < list.size() ? list.subList(start, end) : new ArrayList<>();
        // 手动构建Page对象
        Page<ProjectManagement> resultPage = new Page<>(page.getCurrent(), page.getSize(), total);
        resultPage.setRecords(pagedList);
        
        // 转换为VO并加载统计信息
        List<ProjectManagementVo> voList = resultPage.getRecords().stream()
                .map(this::convertToVo)
                .collect(Collectors.toList());
        
        // 加载统计信息
        for (ProjectManagementVo vo : voList) {
            loadStatistics(vo, vo.getId());
        }
        
        return new TableDataInfo<>(voList, resultPage.getTotal());
    }

    /**
     * 查询项目详情
     */
    @Override
    public ProjectManagementVo selectProjectById(Long id) {
        ProjectManagement project = projectManagementMapper.selectProjectManagementById(id);
        if (project == null) {
            return null;
        }
        
        ProjectManagementVo vo = convertToVo(project);
        
        // 加载标签
        loadTags(vo, id);
        
        // 加载统计信息
        loadStatistics(vo, id);
        
        return vo;
    }

    /**
     * 更新项目
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProject(ProjectManagement projectManagement) {
        int result = projectManagementMapper.updateById(projectManagement);
        
        // 更新标签
        if (CollUtil.isNotEmpty(projectManagement.getTags())) {
            // 删除旧标签
            projectManagementTagMapper.deleteTagsByProjectId(projectManagement.getId());
            // 插入新标签
            List<ProjectManagementTag> tags = new ArrayList<>();
            for (String tagName : projectManagement.getTags()) {
                ProjectManagementTag tag = new ProjectManagementTag();
                tag.setProjectId(projectManagement.getId());
                tag.setTagName(tagName);
                tag.setCreateTime(new Date());
                tags.add(tag);
            }
            projectManagementTagMapper.batchInsertTags(tags);
        }
        
        return result;
    }

    /**
     * 删除项目
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProjectById(Long id) {
        // 逻辑删除
        ProjectManagement project = new ProjectManagement();
        project.setId(id);
        project.setDelFlag("1");
        return projectManagementMapper.updateById(project);
    }

    /**
     * 获取项目的任务列表
     */
    @Override
    public TableDataInfo<Map<String, Object>> getProjectTasks(Long projectId, String status, PageQuery pageQuery) {
        List<Map<String, Object>> taskList = projectManagementMapper.selectTasksByProjectId(projectId, status);
        
        // 手动分页
        int total = taskList.size();
        // 安全获取分页参数，如果为null则使用默认值
        Integer pageNum = pageQuery.getPageNum();
        Integer pageSize = pageQuery.getPageSize();
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<Map<String, Object>> pagedList = start < total ? taskList.subList(start, end) : new ArrayList<>();
        
        return new TableDataInfo<>(pagedList, (long) total);
    }

    /**
     * 获取项目统计信息
     */
    @Override
    public Map<String, Object> getProjectStatistics(Long projectId) {
        Map<String, Object> statistics = projectManagementMapper.selectProjectStatistics(projectId);
        
        // 确保所有字段都有值
        if (statistics == null) {
            statistics = new HashMap<>();
        }
        
        statistics.putIfAbsent("taskCount", 0);
        statistics.putIfAbsent("completedTaskCount", 0);
        statistics.putIfAbsent("inProgressTaskCount", 0);
        statistics.putIfAbsent("pendingTaskCount", 0);
        statistics.putIfAbsent("cancelledTaskCount", 0);
        
        return statistics;
    }

    /**
     * 转换为VO
     */
    private ProjectManagementVo convertToVo(ProjectManagement project) {
        ProjectManagementVo vo = new ProjectManagementVo();
        vo.setId(project.getId());
        vo.setName(project.getName());
        vo.setDescription(project.getDescription());
        vo.setStatus(project.getStatus());
        vo.setCreatedBy(project.getCreateByName() != null ? project.getCreateByName() : "");
        vo.setCreatedById(project.getCreateById());
        vo.setCreatedByDept(project.getCreateDeptName());
        vo.setCreatedAt(project.getCreateTime());
        vo.setUpdatedAt(project.getUpdateTime());
        return vo;
    }

    /**
     * 加载标签
     */
    private void loadTags(ProjectManagementVo vo, Long projectId) {
        List<String> tagNames = projectManagementTagMapper.selectTagsByProjectId(projectId);
        if (tagNames == null) {
            tagNames = new ArrayList<>();
        }
        vo.setTags(tagNames);
    }

    /**
     * 加载统计信息
     */
    private void loadStatistics(ProjectManagementVo vo, Long projectId) {
        Integer taskCount = projectManagementMapper.countTasksByProjectId(projectId);
        Integer completedTaskCount = projectManagementMapper.countCompletedTasksByProjectId(projectId);
        
        vo.setTaskCount(taskCount != null ? taskCount : 0);
        vo.setCompletedTaskCount(completedTaskCount != null ? completedTaskCount : 0);
    }
}

