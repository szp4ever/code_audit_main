package org.ruoyi.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.ProjectManagement;
import org.ruoyi.chat.domain.vo.ProjectManagementVo;
import org.ruoyi.chat.service.IProjectManagementService;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 项目管理Controller
 *
 * @author ruoyi
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
@Tag(name = "项目管理", description = "项目管理相关接口")
public class ProjectManagementController extends BaseController {

    private final IProjectManagementService projectManagementService;

    /**
     * 创建项目
     */
    @Operation(summary = "创建项目")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<Long> create(@RequestBody ProjectManagement projectManagement) {
        log.info("========== Controller层：接收创建项目请求 ==========");
        log.info("项目名称: {}", projectManagement.getName());
        log.info("项目状态: {}", projectManagement.getStatus());
        
        // 打印标签信息
        if (projectManagement.getTags() != null && !projectManagement.getTags().isEmpty()) {
            log.info("标签数量: {}, 标签列表: {}", projectManagement.getTags().size(), projectManagement.getTags());
        }
        
        Long projectId = projectManagementService.createProject(projectManagement);
        log.info("项目创建成功，返回项目ID: {}", projectId);
        return R.ok(projectId);
    }

    /**
     * 查询项目列表
     */
    @Operation(summary = "查询项目列表")
    @GetMapping("/list")
    public TableDataInfo<ProjectManagementVo> list(ProjectManagement projectManagement, PageQuery pageQuery) {
        return projectManagementService.selectProjectList(projectManagement, pageQuery);
    }

    /**
     * 获取项目详情
     */
    @Operation(summary = "获取项目详情")
    @GetMapping("/detail/{id}")
    public R<ProjectManagementVo> getInfo(@PathVariable Long id) {
        ProjectManagementVo projectVo = projectManagementService.selectProjectById(id);
        return R.ok(projectVo);
    }

    /**
     * 更新项目
     */
    @Operation(summary = "更新项目")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{id}")
    public R<Void> edit(@PathVariable Long id, @RequestBody ProjectManagement projectManagement) {
        projectManagement.setId(id);
        projectManagementService.updateProject(projectManagement);
        return R.ok();
    }

    /**
     * 删除项目
     */
    @Operation(summary = "删除项目")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{id}")
    public R<Void> remove(@PathVariable Long id) {
        projectManagementService.deleteProjectById(id);
        return R.ok();
    }

    /**
     * 获取项目的任务列表
     */
    @Operation(summary = "获取项目的任务列表")
    @GetMapping("/{projectId}/tasks")
    public TableDataInfo<Map<String, Object>> getProjectTasks(
            @PathVariable Long projectId,
            @RequestParam(value = "status", required = false) String status,
            PageQuery pageQuery) {
        return projectManagementService.getProjectTasks(projectId, status, pageQuery);
    }

    /**
     * 获取项目统计信息
     */
    @Operation(summary = "获取项目统计信息")
    @GetMapping("/{projectId}/statistics")
    public R<Map<String, Object>> getProjectStatistics(@PathVariable Long projectId) {
        Map<String, Object> statistics = projectManagementService.getProjectStatistics(projectId);
        return R.ok(statistics);
    }
}

