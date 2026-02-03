package org.ruoyi.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.TaskManagementFile;
import org.ruoyi.chat.domain.TaskManagementIssue;
import org.ruoyi.chat.domain.vo.TaskIssueResponse;
import org.ruoyi.chat.mapper.TaskManagementFileMapper;
import org.ruoyi.chat.mapper.TaskManagementIssueMapper;
import org.ruoyi.chat.mapper.TaskManagementMapper;
import org.ruoyi.chat.service.ITaskProcessingService;
import org.ruoyi.common.json.utils.JsonUtils;
import org.ruoyi.common.oss.core.OssClient;
import org.ruoyi.common.oss.entity.UploadResult;
import org.ruoyi.common.oss.factory.OssFactory;
import org.ruoyi.system.domain.SysTemplate;
import org.ruoyi.system.mapper.SysTemplateMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 报告文件结果包装类
 */
class ReportFileResult {
    private final byte[] fileBytes;
    private final String contentType;

    public ReportFileResult(byte[] fileBytes, String contentType) {
        this.fileBytes = fileBytes;
        this.contentType = contentType;
    }

    public byte[] getFileBytes() { return fileBytes; }
    public String getContentType() { return contentType; }
}

/**
 * 任务处理Service业务层重构版
 * 增加了深度追踪（TRACE）日志
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskProcessingServiceImpl implements ITaskProcessingService {

    private final TaskManagementMapper taskManagementMapper;
    private final TaskManagementFileMapper taskManagementFileMapper;
    private final TaskManagementIssueMapper taskManagementIssueMapper;
    private final SysTemplateMapper sysTemplateMapper;

    private static final String FLASK_PROCESS_API_URL = "http://127.0.0.1:5004/api/process";
    private static final String FLASK_REPORT_API_URL = "http://127.0.0.1:5004/api/report";
    private static final int REQUEST_TIMEOUT = 600;

    @Override
    @Async("taskProcessingExecutor")
    public void processTask(TaskManagement taskManagement) {
        Long taskId = taskManagement.getId();
        log.info(">>>>>> [TRACE-START] 开始处理任务，任务ID: {} <<<<<<", taskId);

        try {
            updateTaskStatus(taskId, "in_progress");

            TaskManagement freshTask = taskManagementMapper.selectById(taskId);
            if (freshTask != null) {
                taskManagement = freshTask;
            }

            List<TaskManagementFile> inputFiles = taskManagementFileMapper.selectFilesByTaskId(taskId, "input");
            log.info("[TRACE] 获取到输入文件记录数: {}", CollUtil.size(inputFiles));

            if (CollUtil.isEmpty(inputFiles)) {
                log.warn("[TRACE] 任务没有输入文件，终止处理。任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }

            // 第一步：调用Flask接口进行诊断
            String responseJson = callFlaskProcessApi(freshTask != null ? freshTask : taskManagement, inputFiles);

            if (StrUtil.isBlank(responseJson)) {
                log.error("[TRACE] Flask Process 接口返回空，任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }

            // 解析并保存Issues
            TaskIssueResponse issueResponse = saveIssuesFromResponse(taskId, responseJson);

            if (issueResponse == null || issueResponse.getData() == null) {
                log.error("[TRACE] 解析 Issues 失败或 Data 为空，任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }

            // 第二步：调用Flask生成报告文件
            ReportFileResult reportResult = callFlaskReportApi(taskId, issueResponse);

            if (reportResult == null || reportResult.getFileBytes() == null || reportResult.getFileBytes().length == 0) {
                log.error("[TRACE] Flask Report 接口生成文件失败，任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }

            // 上传报告至OSS
            saveReportFile(taskId, reportResult.getFileBytes(), reportResult.getContentType());

            log.info(">>>>>> [TRACE-SUCCESS] 任务处理完成，任务ID: {} <<<<<<", taskId);
            updateTaskStatus(taskId, "completed");

        } catch (Exception e) {
            log.error(">>>>>> [TRACE-ERROR] 任务处理发生异常，任务ID: {} <<<<<<", taskId, e);
            updateTaskStatus(taskId, "cancelled");
        }
    }

    private String callFlaskProcessApi(TaskManagement taskManagement, List<TaskManagementFile> inputFiles) {
        log.info("[TRACE-API] 开始构建 Multipart 请求，接口地址: {}", FLASK_PROCESS_API_URL);
        try {
            WebClient webClient = WebClient.builder().baseUrl(FLASK_PROCESS_API_URL).build();
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            OssClient storage = OssFactory.instance();

            // 1. 处理模板
            String finalDescription = taskManagement.getDescription();
            if (taskManagement.getTemplateId() != null) {
                SysTemplate template = sysTemplateMapper.selectById(taskManagement.getTemplateId());
                if (template != null) {
                    log.info("[TRACE] 识别到关联模板: {}, 类型: {}", template.getTemplateName(), template.getTemplateType());

                    if ("1".equals(template.getTemplateType())) {
                        String promptContent = template.getTemplateContent();
                        finalDescription = StrUtil.isBlank(finalDescription) ? promptContent : finalDescription + "\n\n" + promptContent;
                        log.info("[TRACE] 已装载文本模板");
                    } else if ("2".equals(template.getTemplateType()) && StrUtil.isNotBlank(template.getFilePath())) {
                        String objectKey = cleanOssPath(template.getFilePath());
                        log.info("[TRACE] 正在读取 Word 模板，Key: {}", objectKey);
                        try (InputStream is = storage.getObjectContent(objectKey)) {
                            byte[] tplBytes = IoUtil.readBytes(is);
                            log.info("[TRACE] 模板读取成功，字节数: {}", tplBytes.length);
                            bodyBuilder.part("template_file", tplBytes)
                                    .filename("template.docx")
                                    .contentType(MediaType.APPLICATION_OCTET_STREAM);
                        }
                    }
                }
            }

            bodyBuilder.part("task_type", taskManagement.getTaskType());
            bodyBuilder.part("description", StrUtil.nullToEmpty(finalDescription));

            // 2. 装载审计文件
            log.info("[TRACE] 准备读取代码文件，总数: {}", inputFiles.size());
            for (TaskManagementFile file : inputFiles) {
                String fileKey = cleanOssPath(file.getUrl());
                log.info("[TRACE] 正在读取文件: {}, Key: {}", file.getName(), fileKey);
                try (InputStream is = storage.getObjectContent(fileKey)) {
                    byte[] fileBytes = IoUtil.readBytes(is);
                    log.info("[TRACE] 文件读取成功: {}, 字节数: {}", file.getName(), fileBytes.length);
                    bodyBuilder.part("files", fileBytes)
                            .filename(file.getName())
                            .contentType(MediaType.APPLICATION_OCTET_STREAM);
                }
            }

            log.info("[TRACE] 请求体构建完成，正在发送请求...");
            String result = webClient.post().uri("")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                    .block();

            log.info("[TRACE] Flask 处理接口响应成功，JSON长度: {}", StrUtil.length(result));
            return result;

        } catch (Exception e) {
            log.error("[TRACE-API] 调用 Process 接口异常: {}", e.getMessage());
            throw new RuntimeException("Flask Process 接口调用失败", e);
        }
    }

    private ReportFileResult callFlaskReportApi(Long taskId, TaskIssueResponse issueResponse) {
        log.info("[TRACE-REPORT] 开始构建报告生成请求，任务ID: {}", taskId);
        try {
            WebClient webClient = WebClient.builder().baseUrl(FLASK_REPORT_API_URL).build();

            String reportMd = issueResponse.getData().getReportMarkdown();
            log.info("[TRACE-REPORT] 待发送报告正文 (Markdown) 长度: {}", StrUtil.length(reportMd));

            String issuesJson = JsonUtils.toJsonString(issueResponse.getData());

            ResponseEntity<byte[]> response = webClient.post().uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(issuesJson)
                    .retrieve()
                    .toEntity(byte[].class)
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                    .block();

            if (response == null || response.getBody() == null) {
                throw new RuntimeException("Flask Report 接口响应为空");
            }

            String contentType = response.getHeaders().getFirst("Content-Type");
            log.info("[TRACE-REPORT] 报告生成成功，文件大小: {}", response.getBody().length);

            return new ReportFileResult(response.getBody(),
                    StrUtil.blankToDefault(contentType, "application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

        } catch (Exception e) {
            log.error("[TRACE-REPORT] 调用 Report 接口异常: {}", e.getMessage());
            throw new RuntimeException("Flask Report 接口调用失败", e);
        }
    }

    private TaskIssueResponse saveIssuesFromResponse(Long taskId, String responseJson) {
        log.info("[TRACE-JSON] 开始解析响应并入库，任务ID: {}", taskId);
        try {
            TaskIssueResponse response = JsonUtils.parseObject(responseJson, TaskIssueResponse.class);
            if (response == null || response.getCode() != 200) {
                log.warn("[TRACE-JSON] Flask 返回状态码非200: {}", response != null ? response.getMsg() : "null");
                return null;
            }

            TaskIssueResponse.TaskIssueData data = response.getData();
            List<TaskIssueResponse.TaskIssueItem> issues = data.getIssues();

            // 清理旧数据
            taskManagementIssueMapper.deleteIssuesByTaskId(taskId);

            if (CollUtil.isNotEmpty(issues)) {
                List<TaskManagementIssue> issueList = new ArrayList<>();
                for (TaskIssueResponse.TaskIssueItem item : issues) {
                    TaskManagementIssue entity = new TaskManagementIssue();
                    entity.setTaskId(taskId);
                    entity.setFileName(item.getFileName());
                    entity.setIssueName(item.getIssueName());
                    entity.setSeverity(item.getSeverity());
                    entity.setLineNumber(item.getLineNumber());
                    entity.setDescription(item.getDescription());
                    entity.setFixSuggestion(item.getFixSuggestion());
                    entity.setDelFlag("0");
                    issueList.add(entity);
                }
                taskManagementIssueMapper.batchInsertIssues(issueList);
                log.info("[TRACE-JSON] 数据库入库成功，数量: {}", issueList.size());
            }
            return response;
        } catch (Exception e) {
            log.error("[TRACE-JSON] 数据处理失败: {}", e.getMessage());
            throw new RuntimeException("Issue 数据入库失败", e);
        }
    }

    private void saveReportFile(Long taskId, byte[] fileBytes, String contentType) {
        log.info("[TRACE-OSS] 正在保存报告文件至 OSS...");
        OssClient storage = OssFactory.instance();
        String fileName = "task_" + taskId + "_report_" + System.currentTimeMillis() + ".docx";
        UploadResult result = storage.upload(fileBytes, fileName, contentType);

        log.info("[TRACE-OSS] 报告上传成功，URL: {}", result.getUrl());

        TaskManagementFile outputFile = new TaskManagementFile();
        outputFile.setTaskId(taskId);
        outputFile.setName(fileName);
        outputFile.setUrl(result.getUrl());
        outputFile.setSize((long) fileBytes.length);
        outputFile.setType(contentType);
        outputFile.setFileCategory("output");
        outputFile.setUploadTime(new Date());
        outputFile.setDelFlag("0");
        taskManagementFileMapper.insert(outputFile);
    }

    /**
     * 核心修复：清洗 OSS 路径，确保提取纯净 Object Key
     */
    private String cleanOssPath(String path) {
        if (StrUtil.isBlank(path)) return "";
        // URL 解码处理 %3A/%2F 等
        String decodedPath = URLUtil.decode(path);
        // 寻找 Bucket 标识，截取之后的部分并移除签名参数
        String bucketFlag = "/ruoyi/";
        if (decodedPath.contains(bucketFlag)) {
            String key = decodedPath.substring(decodedPath.indexOf(bucketFlag) + bucketFlag.length());
            return key.contains("?") ? key.substring(0, key.indexOf("?")) : key;
        }
        return decodedPath;
    }

    private void updateTaskStatus(Long taskId, String status) {
        TaskManagement task = new TaskManagement();
        task.setId(taskId);
        task.setStatus(status);
        taskManagementMapper.updateById(task);
    }
}