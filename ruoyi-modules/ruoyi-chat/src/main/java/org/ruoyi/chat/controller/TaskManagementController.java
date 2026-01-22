package org.ruoyi.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.TaskManagementFile;
import org.ruoyi.chat.domain.vo.TaskManagementVo;
import org.ruoyi.chat.domain.vo.TaskVulnerabilityDetailVo;
import org.ruoyi.chat.service.ITaskManagementFileService;
import org.ruoyi.chat.service.ITaskManagementService;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 任务管理Controller
 *
 * @author ruoyi
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/task")
@Tag(name = "任务管理", description = "任务管理相关接口")
public class TaskManagementController extends BaseController {

    private final ITaskManagementService taskManagementService;
    private final ITaskManagementFileService taskManagementFileService;

    /**
     * 创建任务
     */
    @Operation(summary = "创建任务")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<Long> create(@RequestBody TaskManagement taskManagement) {
        log.info("========== Controller层：接收创建任务请求 ==========");
        log.info("任务标题: {}", taskManagement.getTitle());
        log.info("任务类型: {}", taskManagement.getTaskType());
        log.info("优先级: {}", taskManagement.getPriority());
        
        // 打印输入文件信息
        if (taskManagement.getInputFiles() != null && !taskManagement.getInputFiles().isEmpty()) {
            log.info("输入文件数量: {}", taskManagement.getInputFiles().size());
            for (int i = 0; i < taskManagement.getInputFiles().size(); i++) {
                TaskManagementFile file = taskManagement.getInputFiles().get(i);
                log.info("文件[{}] - ID: {}, Name: {}, URL: {}", 
                        i + 1, file.getId(), file.getName(), file.getUrl());
            }
        } else {
            log.info("没有输入文件");
        }
        
        // 打印标签信息
        if (taskManagement.getTags() != null && !taskManagement.getTags().isEmpty()) {
            log.info("标签数量: {}, 标签列表: {}", taskManagement.getTags().size(), taskManagement.getTags());
        }
        
        Long taskId = taskManagementService.createTask(taskManagement);
        log.info("任务创建成功，返回任务ID: {}", taskId);
        return R.ok(taskId);
    }

    /**
     * 查询任务列表
     */
    @Operation(summary = "查询任务列表")
    @GetMapping("/list")
    public TableDataInfo<TaskManagementVo> list(TaskManagement taskManagement, PageQuery pageQuery) {
        return taskManagementService.selectTaskList(taskManagement, pageQuery);
    }

    /**
     * 获取任务详情
     */
    @Operation(summary = "获取任务详情")
    @GetMapping("/detail/{id}")
    public R<TaskManagementVo> getInfo(@PathVariable Long id) {
        TaskManagementVo taskVo = taskManagementService.selectTaskById(id);
        return R.ok(taskVo);
    }

    /**
     * 更新任务
     */
    @Operation(summary = "更新任务")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{id}")
    public R<Void> edit(@PathVariable Long id, @RequestBody TaskManagement taskManagement) {
        taskManagement.setId(id);
        taskManagementService.updateTask(taskManagement);
        return R.ok();
    }

    /**
     * 删除任务
     */
    @Operation(summary = "删除任务")
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{id}")
    public R<Void> remove(@PathVariable Long id) {
        taskManagementService.deleteTaskById(id);
        return R.ok();
    }

    /**
     * 重试任务
     */
    @Operation(summary = "重试任务")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @PostMapping("/retry/{id}")
    public R<Void> retryTask(@PathVariable Long id) {
        System.out.println("retryTaskController");
        taskManagementService.retryTaskById(id);
        return R.ok();
    }

    /**
     * 上传文件
     */
    @Operation(summary = "上传任务文件")
    @Log(title = "任务文件", businessType = BusinessType.INSERT)
    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<TaskManagementFile> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "taskId", required = false) Long taskId) {
        TaskManagementFile taskFile = taskManagementFileService.uploadFile(file, taskId);
        return R.ok(taskFile);
    }

    /**
     * 批量上传文件（支持文件夹上传）
     */
    @Operation(summary = "批量上传任务文件（支持文件夹上传）")
    @Log(title = "任务文件", businessType = BusinessType.INSERT)
    @PostMapping(value = "/file/uploadBatch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<List<TaskManagementFile>> uploadFiles(
            @RequestPart("files") MultipartFile[] files,
            @RequestParam(value = "relativePaths", required = false) String[] relativePaths,
            @RequestParam(value = "taskId", required = false) Long taskId) {
        List<TaskManagementFile> uploadedFiles = taskManagementFileService.uploadFiles(files, relativePaths, taskId);
        return R.ok(uploadedFiles);
    }

    /**
     * 下载文件
     */
    @Operation(summary = "下载任务文件")
    @GetMapping("/file/download/{fileId}")
    public void downloadFile(@PathVariable Long fileId, HttpServletResponse response) {
        taskManagementFileService.downloadFile(fileId, response);
    }

    /**
     * 获取任务漏洞详情
     */
    @Operation(summary = "获取任务漏洞详情")
    @GetMapping("/vulnerabilities/{taskId}")
    public R<TaskVulnerabilityDetailVo> getTaskVulnerabilities(@PathVariable Long taskId) {
        TaskVulnerabilityDetailVo detail = taskManagementService.getTaskVulnerabilities(taskId);
        return R.ok(detail);
    }
}





