package org.ruoyi.chat.service.impl; // 包名保持 org.ruoyi.chat.service.impl 以匹配您的目录结构

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.*;
import org.ruoyi.chat.domain.vo.*;
import org.ruoyi.chat.mapper.*;
import org.ruoyi.chat.service.ITaskManagementService;
import org.ruoyi.chat.service.ITaskProcessingService;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.system.domain.SysTemplate;
import org.ruoyi.system.mapper.SysTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务管理Service业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskManagementServiceImpl implements ITaskManagementService {

    private final TaskManagementMapper taskManagementMapper;
    private final TaskManagementFileMapper taskManagementFileMapper;
    private final TaskManagementTagMapper taskManagementTagMapper;
    private final TaskManagementIssueMapper taskManagementIssueMapper;
    private final SysTemplateMapper sysTemplateMapper;

    @Autowired
    @Lazy
    private ITaskProcessingService taskProcessingService;

    // Python Flask 服务的地址
    private static final String FLASK_API_URL = "http://127.0.0.1:5004/api/process";

    // 注入文件上传路径
    @Value("${ruoyi.profile:D:/ruoyi/uploadPath}")
    private String profile;

    /**
     * 创建任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(TaskManagement taskManagement) {
        if (ObjectUtil.isEmpty(taskManagement.getStatus())) {
            taskManagement.setStatus("pending");
        }

        taskManagementMapper.insert(taskManagement);
        Long taskId = taskManagement.getId();

        log.info("========== 创建任务开始 | ID: {} | 模板ID: {} ==========", taskId, taskManagement.getTemplateId());

        if (CollUtil.isNotEmpty(taskManagement.getTags())) {
            List<TaskManagementTag> tags = new ArrayList<>();
            for (String tagName : taskManagement.getTags()) {
                TaskManagementTag tag = new TaskManagementTag();
                tag.setTaskId(taskId);
                tag.setTagName(tagName);
                tag.setCreateTime(new Date());
                tags.add(tag);
            }
            taskManagementTagMapper.batchInsertTags(tags);
        }

        if (CollUtil.isNotEmpty(taskManagement.getInputFiles())) {
            for (TaskManagementFile file : taskManagement.getInputFiles()) {
                Long fileId = file.getId();
                if (fileId == null) continue;

                TaskManagementFile updateFile = new TaskManagementFile();
                updateFile.setId(fileId);
                updateFile.setTaskId(taskId);
                updateFile.setFileCategory("input");

                taskManagementFileMapper.updateById(updateFile);
            }
        }

        // 异步调用
        taskManagement.setId(taskId);
        try {
            taskProcessingService.processTask(taskManagement);
        } catch (Exception e) {
            log.error("提交任务处理请求失败", e);
        }

        return taskId;
    }

    /**
     * 执行分析任务
     */
    @Override
    public void runAnalysisTask(Long taskId) {
        log.info("========== 开始执行任务分析，TaskId: {} ==========", taskId);

        TaskManagement task = taskManagementMapper.selectById(taskId);
        if (task == null) {
            log.error("错误：在数据库中找不到任务 ID: {}", taskId);
            return;
        }

        // --- [调试日志] 打印关键数据 ---
        log.info("当前任务 TemplateID: {}", task.getTemplateId());
        log.info("当前任务 Description: {}", task.getDescription());
        // ----------------------------

        updateTaskStatus(taskId, "in_progress");

        List<TaskManagementFile> inputFiles = taskManagementFileMapper.selectFilesByTaskId(taskId, "input");
        if (CollUtil.isEmpty(inputFiles)) {
            log.error("任务 {} 缺少输入文件", taskId);
            updateTaskStatus(taskId, "failed");
            return;
        }

        try {
            String finalDescription = task.getDescription();

            // 检查是否有模板ID
            if (task.getTemplateId() != null) {
                log.info("检测到关联模板 ID: {}，准备查询模板信息...", task.getTemplateId());
                SysTemplate template = sysTemplateMapper.selectById(task.getTemplateId());

                if (template != null) {
                    log.info("获取到模板: {}, 类型: {}", template.getTemplateName(), template.getTemplateType());

                    // 情况 A: 文本模板 (Type=1)
                    if ("1".equals(template.getTemplateType())) {
                        String promptContent = template.getTemplateContent();
                        if (StrUtil.isNotBlank(promptContent)) {
                            log.info(">>> 应用文本模板内容，长度: {}", promptContent.length());
                            if (StrUtil.isBlank(finalDescription)) {
                                finalDescription = promptContent;
                            } else {
                                finalDescription = finalDescription + "\n\n【补充指令】\n" + promptContent;
                            }
                        } else {
                            log.warn("模板内容为空，跳过拼接");
                        }
                    }
                    // 情况 B: Word模板 (Type=2)
                    else if ("2".equals(template.getTemplateType())) {
                        log.info("这是 Word 模板，将作为文件上传");
                        // Word 模板逻辑不需要修改 finalDescription
                    }
                } else {
                    log.error("未找到 ID 为 {} 的模板数据", task.getTemplateId());
                }
            } else {
                log.warn("注意：任务的 templateId 为空，不会应用任何模板。请检查 TaskManagement 实体类是否包含 templateId 字段。");
            }

            log.info(">>> 发送给 Flask 的最终 Description: {}", finalDescription);

            // 2. 构建 HTTP 请求
            HttpRequest request = HttpRequest.post(FLASK_API_URL)
                    .form("task_type", task.getTaskType())
                    .form("description", finalDescription) // 确保这里传入的是最新的 finalDescription
                    .timeout(600000);

            // 处理 Word 模板文件上传
            if (task.getTemplateId() != null) {
                SysTemplate template = sysTemplateMapper.selectById(task.getTemplateId());
                if (template != null && "2".equals(template.getTemplateType())) {
                    String templatePath = convertUrlToLocalPath(template.getFilePath());
                    File templateFile = new File(templatePath);
                    if (templateFile.exists()) {
                        request.form("template_file", templateFile);
                    }
                }
            }

            // 3. 添加代码文件
            boolean hasValidFile = false;
            for (TaskManagementFile file : inputFiles) {
                String localPath = convertUrlToLocalPath(file.getUrl());
                File codeFile = new File(localPath);
                if (codeFile.exists()) {
                    request.form("files", codeFile);
                    hasValidFile = true;
                } else {
                    log.warn("代码文件不存在: {}", localPath);
                }
            }

            if (!hasValidFile) {
                log.error("没有有效的物理文件可供分析");
                updateTaskStatus(taskId, "failed");
                return;
            }

            // 4. 发送请求
            HttpResponse response = request.execute();

            // ... 后续处理逻辑保持不变 ...
            if (response.isOk()) {
                JSONObject json = JSONUtil.parseObj(response.body());
                if (json.getInt("code") == 200) {
                    JSONObject data = json.getJSONObject("data");
                    JSONArray issues = data.getJSONArray("issues");
                    String reportMd = data.getStr("report_markdown"); // 获取 AI 生成的报告

                    log.info("AI 分析完成，漏洞数: {}", issues.size());

                    saveIssuesToDb(taskId, issues);
                    generateFinalReport(task, issues); // 注意：这里如果想用 reportMd 生成 Word，需要修改 generateFinalReport
                } else {
                    log.error("AI 服务错误: {}", json.getStr("msg"));
                    updateTaskStatus(taskId, "failed");
                }
            } else {
                log.error("AI 服务连接失败: {}", response.getStatus());
                updateTaskStatus(taskId, "failed");
            }

        } catch (Exception e) {
            log.error("任务分析异常", e);
            updateTaskStatus(taskId, "failed");
        }
    }

    /**
     * 生成报告
     */
    private void generateFinalReport(TaskManagement task, JSONArray issues) {
        Long templateId = task.getTemplateId();
        if (templateId == null) {
            updateTaskStatus(task.getId(), "completed");
            return;
        }

        SysTemplate template = sysTemplateMapper.selectById(templateId);
        if (template == null) {
            log.error("模板 ID: {} 不存在", templateId);
            updateTaskStatus(task.getId(), "completed");
            return;
        }

        // 兼容 Word 模板与文本模板逻辑
        String templatePath = "";
        if ("2".equals(template.getTemplateType())) {
            // Word 模板 - 继续生成报告
            templatePath = convertUrlToLocalPath(template.getFilePath());
        } else {
            // [修改] 文本模板 (Type=1)
            // 文本模板已在分析阶段作为 Prompt 使用，此处不需要生成 Word 报告
            // 视为正常完成
            log.info("当前任务使用的是文本模板(提示词)，已在分析阶段应用，跳过 Word 报告生成");
            updateTaskStatus(task.getId(), "completed");
            return;
        }

        if (!FileUtil.exist(templatePath)) {
            log.error("Word 模板文件不存在: {}", templatePath);
            updateTaskStatus(task.getId(), "completed");
            return;
        }

        String outputDir = this.profile + "/reports";
        if (!FileUtil.exist(outputDir)) {
            FileUtil.mkdir(outputDir);
        }
        String outputFileName = "Report_" + task.getId() + "_" + System.currentTimeMillis() + ".docx";
        String outputPath = outputDir + "/" + outputFileName;

        try {
            List<Map<String, Object>> vulnList = new ArrayList<>();
            for (int i = 0; i < issues.size(); i++) {
                JSONObject issue = issues.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                map.put("issue_name", issue.getStr("issue_name", "未知漏洞"));
                map.put("severity", issue.getStr("severity", "Low"));
                map.put("line_number", issue.getStr("line_number", "-"));
                map.put("description", issue.getStr("description", ""));
                map.put("fix_suggestion", issue.getStr("fix_suggestion", ""));
                vulnList.add(map);
            }

            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            Configure config = Configure.builder().bind("vulnerabilityList", policy).build();

            XWPFTemplate.compile(templatePath, config).render(
                    new HashMap<String, Object>() {{
                        put("taskName", task.getTitle());
                        put("taskType", task.getTaskType());
                        put("totalIssues", String.valueOf(issues.size()));
                        put("scanTime", new Date().toString());
                        put("vulnerabilityList", vulnList);
                    }}
            ).writeToFile(outputPath);

            TaskManagementFile outputFile = new TaskManagementFile();
            outputFile.setTaskId(task.getId());
            outputFile.setFileCategory("output");
            outputFile.setName(outputFileName);
            outputFile.setUrl("/profile/reports/" + outputFileName);
            outputFile.setSize(FileUtil.size(new File(outputPath)));
            outputFile.setType("docx");
            outputFile.setUploadTime(new Date());

            taskManagementFileMapper.insert(outputFile);
            updateTaskStatus(task.getId(), "completed");

        } catch (Exception e) {
            log.error("生成报告失败", e);
            updateTaskStatus(task.getId(), "completed");
        }
    }

    private void saveIssuesToDb(Long taskId, JSONArray issues) {
        taskManagementIssueMapper.delete(
                new LambdaQueryWrapper<TaskManagementIssue>().eq(TaskManagementIssue::getTaskId, taskId)
        );

        for (int i = 0; i < issues.size(); i++) {
            JSONObject json = issues.getJSONObject(i);
            TaskManagementIssue issue = new TaskManagementIssue();
            issue.setTaskId(taskId);
            issue.setIssueName(json.getStr("issue_name"));
            issue.setSeverity(json.getStr("severity"));
            issue.setLineNumber(json.getStr("line_number"));
            issue.setDescription(json.getStr("description"));
            issue.setFixSuggestion(json.getStr("fix_suggestion"));
            issue.setCreateTime(new Date());
            taskManagementIssueMapper.insert(issue);
        }
    }

    private void updateTaskStatus(Long taskId, String status) {
        TaskManagement tm = new TaskManagement();
        tm.setId(taskId);
        tm.setStatus(status);
        tm.setUpdateTime(new Date());
        taskManagementMapper.updateById(tm);
    }

    private String convertUrlToLocalPath(String url) {
        if (StrUtil.isEmpty(url)) return "";
        return this.profile + StrUtil.subAfter(url, "/profile", false);
    }

    // --- 查询相关方法 ---

    @Override
    public TableDataInfo<TaskManagementVo> selectTaskList(TaskManagement taskManagement, PageQuery pageQuery) {
        LambdaQueryWrapper<TaskManagement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(taskManagement.getStatus()), TaskManagement::getStatus, taskManagement.getStatus())
                .eq(ObjectUtil.isNotEmpty(taskManagement.getPriority()), TaskManagement::getPriority, taskManagement.getPriority())
                .eq(ObjectUtil.isNotEmpty(taskManagement.getTaskType()), TaskManagement::getTaskType, taskManagement.getTaskType())
                .eq(TaskManagement::getDelFlag, "0")
                .orderByDesc(TaskManagement::getCreateTime);

        Page<TaskManagement> page = taskManagementMapper.selectPage(pageQuery.build(), wrapper);

        List<TaskManagementVo> voList = page.getRecords().stream().map(task -> {
            TaskManagementVo vo = convertToVo(task);
            loadFiles(vo, task.getId());
            vo.setTags(taskManagementTagMapper.selectTagsByTaskId(task.getId()));
            return vo;
        }).collect(Collectors.toList());

        return new TableDataInfo<>(voList, page.getTotal());
    }

    @Override
    public TaskManagementVo selectTaskById(Long id) {
        TaskManagement task = taskManagementMapper.selectTaskManagementById(id);
        if (task == null || "1".equals(task.getDelFlag())) {
            return null;
        }
        TaskManagementVo vo = convertToVo(task);
        loadFiles(vo, id);
        vo.setTags(taskManagementTagMapper.selectTagsByTaskId(id));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(TaskManagement taskManagement) {
        int result = taskManagementMapper.updateById(taskManagement);
        if (CollUtil.isNotEmpty(taskManagement.getTags())) {
            taskManagementTagMapper.deleteTagsByTaskId(taskManagement.getId());
            List<TaskManagementTag> tags = new ArrayList<>();
            for (String tagName : taskManagement.getTags()) {
                TaskManagementTag tag = new TaskManagementTag();
                tag.setTaskId(taskManagement.getId());
                tag.setTagName(tagName);
                tag.setCreateTime(new Date());
                tags.add(tag);
            }
            taskManagementTagMapper.batchInsertTags(tags);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTaskById(Long id) {
        TaskManagement task = new TaskManagement();
        task.setId(id);
        task.setDelFlag("1");
        return taskManagementMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int retryTaskById(Long id) {
        TaskManagement updateTask = new TaskManagement();
        updateTask.setId(id);
        updateTask.setStatus("pending");
        updateTask.setUpdateTime(new Date());
        int updateCount = taskManagementMapper.updateById(updateTask);

        TaskManagement latestTask = taskManagementMapper.selectTaskManagementById(id);
        try {
            taskProcessingService.processTask(latestTask);
        } catch (Exception e) {
            log.error("提交任务重试请求失败", e);
        }
        return updateCount;
    }

    private TaskManagementVo convertToVo(TaskManagement task) {
        TaskManagementVo vo = new TaskManagementVo();
        vo.setId(task.getId());
        vo.setProjectId(task.getProjectId());
        vo.setTitle(task.getTitle());
        vo.setDescription(task.getDescription());
        vo.setPriority(task.getPriority());
        vo.setTaskType(task.getTaskType());
        vo.setStatus(task.getStatus());
        vo.setCreatedAt(task.getCreateTime());
        vo.setUpdatedAt(task.getUpdateTime());

        // 1. 设置模板ID
        vo.setTemplateId(task.getTemplateId());

        // 2. [新增] 设置模板名称
        if (task.getTemplateId() != null) {
            // 使用 sysTemplateMapper 查询名称
            SysTemplate template = sysTemplateMapper.selectById(task.getTemplateId());
            if (template != null) {
                vo.setTemplateName(template.getTemplateName());
            } else {
                vo.setTemplateName("未知模板 (ID:" + task.getTemplateId() + ")");
            }
        }

        return vo;
    }

    private void loadFiles(TaskManagementVo vo, Long taskId) {
        List<TaskManagementFile> inputFiles = taskManagementFileMapper.selectFilesByTaskId(taskId, "input");
        vo.setInputFiles(convertFileListToVo(inputFiles));
        List<TaskManagementFile> outputFiles = taskManagementFileMapper.selectFilesByTaskId(taskId, "output");
        vo.setOutputFiles(convertFileListToVo(outputFiles));
    }

    private List<TaskManagementFileVo> convertFileListToVo(List<TaskManagementFile> files) {
        if (CollUtil.isEmpty(files)) return new ArrayList<>();
        return files.stream().map(this::convertFileToVo).collect(Collectors.toList());
    }

    private TaskManagementFileVo convertFileToVo(TaskManagementFile file) {
        if (file == null) return null;
        TaskManagementFileVo vo = new TaskManagementFileVo();
        vo.setId(file.getId() != null ? String.valueOf(file.getId()) : null);
        vo.setName(file.getName());
        vo.setUrl(file.getUrl());
        vo.setSize(file.getSize());
        vo.setType(file.getType());
        vo.setUploadTime(file.getUploadTime());
        return vo;
    }

    @Override
    public Map<String, Integer> getStatusStats() {
        List<Map<String, Object>> statsList = taskManagementMapper.selectStatusStats();
        if (CollUtil.isEmpty(statsList)) return new HashMap<>();
        return statsList.stream().collect(Collectors.toMap(item -> String.valueOf(item.get("key")), item -> ((Number) item.get("value")).intValue()));
    }

    @Override
    public Map<String, Integer> getTypeStats() {
        List<Map<String, Object>> statsList = taskManagementMapper.selectTypeStats();
        if (CollUtil.isEmpty(statsList)) return new HashMap<>();
        return statsList.stream().collect(Collectors.toMap(item -> String.valueOf(item.get("key")), item -> ((Number) item.get("value")).intValue()));
    }

    @Override
    public List<TaskDurationStatItem> getDurationStats(String timeRange) {
        if (timeRange == null || timeRange.isEmpty()) timeRange = "day";
        if (!timeRange.equals("day") && !timeRange.equals("hour") && !timeRange.equals("week")) timeRange = "day";
        return taskManagementMapper.selectDurationStats(timeRange);
    }

    @Override
    public TaskVulnerabilityDetailVo getTaskVulnerabilities(Long taskId) {
        TaskManagement task = taskManagementMapper.selectById(taskId);
        if (task == null) return null;

        List<TaskManagementIssue> issues = taskManagementIssueMapper.selectIssuesByTaskId(taskId);
        List<Map<String, Object>> severityStats = taskManagementIssueMapper.selectSeverityCountByTaskId(taskId);
        Map<String, Integer> severityCount = new HashMap<>();
        if (CollUtil.isNotEmpty(severityStats)) {
            for (Map<String, Object> stat : severityStats) {
                String severity = String.valueOf(stat.get("severity")).toLowerCase();
                Integer count = ((Number) stat.get("count")).intValue();
                severityCount.put(severity, count);
            }
        }

        TaskVulnerabilityDetailVo detailVo = new TaskVulnerabilityDetailVo();
        detailVo.setTaskId(taskId);
        detailVo.setTaskTitle(task.getTitle());
        detailVo.setTotalCount(issues != null ? issues.size() : 0);
        detailVo.setSeverityCount(severityCount);

        List<VulnerabilityVo> vulnerabilityList = new ArrayList<>();
        if (CollUtil.isNotEmpty(issues)) {
            for (TaskManagementIssue issue : issues) {
                VulnerabilityVo vulnVo = convertIssueToVulnerabilityVo(issue);
                vulnerabilityList.add(vulnVo);
            }
        }
        detailVo.setVulnerabilities(vulnerabilityList);
        return detailVo;
    }

    private VulnerabilityVo convertIssueToVulnerabilityVo(TaskManagementIssue issue) {
        VulnerabilityVo vo = new VulnerabilityVo();
        vo.setId(issue.getId());
        vo.setTaskId(issue.getTaskId());
        vo.setTitle(issue.getIssueName());
        vo.setDescription(issue.getDescription());

        String severity = issue.getSeverity();
        if (severity != null) vo.setSeverity(severity.toLowerCase());
        else vo.setSeverity("low");

        vo.setFilePath(issue.getFileName());

        String lineNumberStr = issue.getLineNumber();
        if (lineNumberStr != null && !lineNumberStr.isEmpty()) {
            try {
                if (lineNumberStr.contains("-")) {
                    String firstNumber = lineNumberStr.split("-")[0].trim();
                    vo.setLineNumber(Integer.parseInt(firstNumber));
                } else {
                    vo.setLineNumber(Integer.parseInt(lineNumberStr.trim()));
                }
            } catch (NumberFormatException e) {
                log.warn("无法解析行号: {}", lineNumberStr);
                vo.setLineNumber(null);
            }
        } else {
            vo.setLineNumber(null);
        }

        vo.setCodeSnippet(null);
        vo.setFixSuggestion(issue.getFixSuggestion());
        vo.setCategory(null);
        vo.setCreatedAt(issue.getCreateTime());

        return vo;
    }
}