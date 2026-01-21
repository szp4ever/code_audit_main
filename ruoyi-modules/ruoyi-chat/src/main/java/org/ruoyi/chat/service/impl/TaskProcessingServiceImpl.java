package org.ruoyi.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
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
import org.springframework.http.MediaType;
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
 * 报告文件结果
 */
class ReportFileResult {
    private byte[] fileBytes;
    private String contentType;
    
    public ReportFileResult(byte[] fileBytes, String contentType) {
        this.fileBytes = fileBytes;
        this.contentType = contentType;
    }
    
    public byte[] getFileBytes() {
        return fileBytes;
    }
    
    public String getContentType() {
        return contentType;
    }
}

/**
 * 任务处理Service业务层处理
 * 调用Python Flask接口处理任务
 *
 * @author ruoyi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskProcessingServiceImpl implements ITaskProcessingService {

    private final TaskManagementMapper taskManagementMapper;
    private final TaskManagementFileMapper taskManagementFileMapper;
    private final TaskManagementIssueMapper taskManagementIssueMapper;
    
    // Flask接口地址
    private static final String FLASK_PROCESS_API_URL = "http://127.0.0.1:5004/api/process";
    private static final String FLASK_REPORT_API_URL = "http://127.0.0.1:5004/api/report";
    
    // 请求超时时间（秒）
    private static final int REQUEST_TIMEOUT = 300; // 5分钟超时

    /**
     * 处理任务（异步执行）
     * 调用Flask接口处理任务，并保存返回的文件
     * 使用taskProcessingExecutor线程池，确保任务在后台持续执行
     *
     * @param taskManagement 任务信息
     */
    @Override
    @Async("taskProcessingExecutor")
    public void processTask(TaskManagement taskManagement) {
        Long taskId = taskManagement.getId();
        log.info("========== 开始处理任务，任务ID: {} ==========", taskId);
        
        try {
            // 更新任务状态为进行中
            updateTaskStatus(taskId, "in_progress");
            
            // 获取任务的输入文件
            List<TaskManagementFile> inputFiles = taskManagementFileMapper.selectFilesByTaskId(taskId, "input");
            if (inputFiles == null || inputFiles.isEmpty()) {
                log.warn("任务没有输入文件，跳过处理。任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }
            
            // 第一步：调用Flask接口获取issues
            String responseJson = callFlaskProcessApi(taskManagement, inputFiles);
            
            if (responseJson == null || responseJson.isEmpty()) {
                log.warn("Flask接口返回的数据为空，任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }
            
            // 解析JSON响应并保存issues
            TaskIssueResponse issueResponse = saveIssuesFromResponse(taskId, responseJson);
            
            if (issueResponse == null || issueResponse.getData() == null) {
                log.warn("解析issues失败，任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }
            
            // 第二步：调用Flask报告接口生成报告文件
            ReportFileResult reportResult = callFlaskReportApi(taskId, issueResponse);
            
            if (reportResult == null || reportResult.getFileBytes() == null || reportResult.getFileBytes().length == 0) {
                log.warn("Flask报告接口返回的文件为空，任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }
            
            // 保存报告文件到OSS
            saveReportFile(taskId, reportResult.getFileBytes(), reportResult.getContentType());
            
            log.info("任务处理完成，任务ID: {}", taskId);
            
            // 更新任务状态为已完成
            updateTaskStatus(taskId, "completed");
            
        } catch (Exception e) {
            log.error("任务处理失败，任务ID: {}", taskId, e);
            // 更新任务状态为已取消（或保持进行中，根据业务需求）
            updateTaskStatus(taskId, "cancelled");
        }
    }

    /**
     * 调用Flask处理接口（第一步：获取issues）
     *
     * @param taskManagement 任务信息
     * @param inputFiles 输入文件列表
     * @return 返回的JSON字符串
     */
    private String callFlaskProcessApi(TaskManagement taskManagement, List<TaskManagementFile> inputFiles) {
        log.info("========== 第一步：开始调用Flask处理接口，任务ID: {}, 任务类型: {}, 文件数量: {} ==========", 
                taskManagement.getId(), taskManagement.getTaskType(), inputFiles.size());
        
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(FLASK_PROCESS_API_URL)
                    .build();
            
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            
            // 添加任务类型
            bodyBuilder.part("task_type", taskManagement.getTaskType());
            
            // 添加任务描述
            if (taskManagement.getDescription() != null && !taskManagement.getDescription().isEmpty()) {
                bodyBuilder.part("description", taskManagement.getDescription());
                log.info("添加任务描述到Flask请求: {}", taskManagement.getDescription());
            }
            
            // 添加所有输入文件
            OssClient storage = OssFactory.instance();
            for (int i = 0; i < inputFiles.size(); i++) {
                TaskManagementFile file = inputFiles.get(i);
                log.info("准备上传文件到Flask: {}", file.getName());
                
                // 从OSS下载文件
                InputStream fileInputStream = storage.getObjectContent(file.getUrl());
                byte[] fileBytes = IoUtil.readBytes(fileInputStream);
                fileInputStream.close();
                
                // 添加到multipart请求
                bodyBuilder.part("files", fileBytes)
                        .filename(file.getName())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM);
                
                log.info("文件已添加到请求: {}", file.getName());
            }
            
            // 发送请求
            log.info("发送请求到Flask处理接口: {}", FLASK_PROCESS_API_URL);
            String responseJson = webClient.post()
                    .uri("")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                    .block();
            
            log.info("Flask处理接口调用成功，返回JSON长度: {} 字符", responseJson != null ? responseJson.length() : 0);
            if (responseJson != null && responseJson.length() > 0) {
                log.debug("Flask返回的JSON内容: {}", responseJson);
            }
            return responseJson;
            
        } catch (Exception e) {
            log.error("调用Flask处理接口失败", e);
            throw new RuntimeException("调用Flask处理接口失败: " + e.getMessage(), e);
        }
    }

    /**
     * 调用Flask报告接口（第二步：生成报告文件）
     *
     * @param taskId 任务ID
     * @param issueResponse issues响应数据
     * @return 返回的报告文件结果（包含文件字节数组和Content-Type）
     */
    private ReportFileResult callFlaskReportApi(Long taskId, TaskIssueResponse issueResponse) {
        log.info("========== 第二步：开始调用Flask报告接口，任务ID: {} ==========", taskId);
        
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(FLASK_REPORT_API_URL)
                    .build();
            
            // 将issues数据转换为JSON字符串
            String issuesJson = JsonUtils.toJsonString(issueResponse.getData());
            log.info("准备发送issues数据到报告接口，数据长度: {} 字符", issuesJson.length());
            log.debug("Issues JSON内容: {}", issuesJson);
            
            // 发送POST请求，将issues数据作为JSON body，并获取响应头和响应体
            org.springframework.http.ResponseEntity<byte[]> responseEntity = webClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(issuesJson)
                    .retrieve()
                    .toEntity(byte[].class)
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                    .block();
            
            if (responseEntity == null || responseEntity.getBody() == null) {
                throw new RuntimeException("Flask报告接口响应为空");
            }
            
            // 获取Content-Type响应头
            String contentType = responseEntity.getHeaders().getFirst("Content-Type");
            if (contentType == null || contentType.isEmpty()) {
                // 如果没有Content-Type，尝试从Content-Disposition中获取，或者使用默认值
                String contentDisposition = responseEntity.getHeaders().getFirst("Content-Disposition");
                if (contentDisposition != null && contentDisposition.contains(".docx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                } else {
                    // 默认使用docx，因为用户说返回的是docx文件
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                }
            }
            
            log.info("Flask报告接口返回的Content-Type: {}", contentType);
            
            // 读取响应体
            byte[] reportFileBytes = responseEntity.getBody();
            
            log.info("Flask报告接口调用成功，返回文件大小: {} bytes", reportFileBytes != null ? reportFileBytes.length : 0);
            return new ReportFileResult(reportFileBytes, contentType);
            
        } catch (Exception e) {
            log.error("调用Flask报告接口失败，任务ID: {}", taskId, e);
            throw new RuntimeException("调用Flask报告接口失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析Flask返回的JSON响应并保存issues到数据库
     *
     * @param taskId 任务ID
     * @param responseJson Flask返回的JSON字符串
     * @return 解析后的响应对象
     */
    private TaskIssueResponse saveIssuesFromResponse(Long taskId, String responseJson) {
        log.info("开始解析Flask返回的JSON并保存issues，任务ID: {}", taskId);
        
        try {
            // 解析JSON响应
            TaskIssueResponse response = JsonUtils.parseObject(responseJson, TaskIssueResponse.class);
            
            if (response == null) {
                log.warn("解析Flask响应失败，响应为空，任务ID: {}", taskId);
                return null;
            }
            
            if (response.getCode() == null || response.getCode() != 200) {
                log.warn("Flask接口返回错误，code: {}, msg: {}, 任务ID: {}", 
                        response.getCode(), response.getMsg(), taskId);
                return null;
            }
            
            TaskIssueResponse.TaskIssueData data = response.getData();
            if (data == null) {
                log.warn("Flask响应数据为空，任务ID: {}", taskId);
                return null;
            }
            
            List<TaskIssueResponse.TaskIssueItem> issues = data.getIssues();
            if (CollUtil.isEmpty(issues)) {
                log.info("Flask返回的issues列表为空，任务ID: {}", taskId);
                return response; // 即使issues为空，也返回response对象
            }
            
            String fileName = data.getFileName();
            Integer totalIssues = data.getTotalIssues();
            
            log.info("解析到issues数据，任务ID: {}, 文件名: {}, 总数量: {}, 实际数量: {}", 
                    taskId, fileName, totalIssues, issues.size());
            
            // 转换为数据库实体并批量保存
            List<TaskManagementIssue> issueList = new ArrayList<>();
            for (TaskIssueResponse.TaskIssueItem item : issues) {
                TaskManagementIssue issue = new TaskManagementIssue();
                issue.setTaskId(taskId);
                issue.setFileName(fileName);
                issue.setIssueName(item.getIssueName());
                issue.setSeverity(item.getSeverity());
                issue.setLineNumber(item.getLineNumber());
                issue.setDescription(item.getDescription());
                issue.setFixSuggestion(item.getFixSuggestion());
                issue.setDelFlag("0");
                
                issueList.add(issue);
            }
            
            // 批量插入issues
            if (CollUtil.isNotEmpty(issueList)) {
                int insertCount = taskManagementIssueMapper.batchInsertIssues(issueList);
                log.info("Issues保存成功，任务ID: {}, 保存数量: {}", taskId, insertCount);
            }
            
            return response;
            
        } catch (Exception e) {
            log.error("解析并保存issues失败，任务ID: {}", taskId, e);
            throw new RuntimeException("解析并保存issues失败: " + e.getMessage(), e);
        }
    }

    /**
     * 保存报告文件到OSS
     *
     * @param taskId 任务ID
     * @param fileBytes 文件字节数组
     * @param contentType Content-Type响应头
     */
    private void saveReportFile(Long taskId, byte[] fileBytes, String contentType) {
        log.info("开始保存报告文件到OSS，任务ID: {}, 文件大小: {} bytes, Content-Type: {}", 
                taskId, fileBytes.length, contentType);
        
        try {
            OssClient storage = OssFactory.instance();
            
            // 根据Content-Type确定文件扩展名
            String fileExtension = getFileExtensionFromContentType(contentType);
            log.info("根据Content-Type推断的文件扩展名: {}", fileExtension);
            
            // 生成文件名（使用任务ID和时间戳）
            String fileName = "task_" + taskId + "_report_" + System.currentTimeMillis() + "." + fileExtension;
            
            // 上传到OSS
            UploadResult uploadResult = storage.upload(fileBytes, fileName, contentType);
            
            log.info("报告文件上传成功，URL: {}, 文件名: {}", uploadResult.getUrl(), uploadResult.getFilename());
            
            // 保存文件信息到数据库
            TaskManagementFile outputFile = new TaskManagementFile();
            outputFile.setTaskId(taskId);
            outputFile.setName(fileName);
            outputFile.setUrl(uploadResult.getUrl());
            outputFile.setSize((long) fileBytes.length);
            outputFile.setType(contentType);
            outputFile.setFileCategory("output");
            outputFile.setUploadTime(new Date());
            outputFile.setDelFlag("0");
            
            taskManagementFileMapper.insert(outputFile);
            
            log.info("报告文件信息已保存到数据库，文件ID: {}", outputFile.getId());
            
        } catch (Exception e) {
            log.error("保存报告文件失败，任务ID: {}", taskId, e);
            throw new RuntimeException("保存报告文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据Content-Type获取文件扩展名
     *
     * @param contentType Content-Type响应头
     * @return 文件扩展名（不含点号）
     */
    private String getFileExtensionFromContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return "docx"; // 默认使用docx
        }
        
        // 移除可能的参数（如charset）
        String mainType = contentType.split(";")[0].trim().toLowerCase();
        
        // 根据Content-Type映射文件扩展名
        switch (mainType) {
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            case "application/msword":
                return "docx";
            case "application/pdf":
                return "pdf";
            case "application/vnd.ms-excel":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return "xlsx";
            case "text/plain":
                return "txt";
            case "text/html":
                return "html";
            default:
                log.warn("未知的Content-Type: {}，使用默认扩展名docx", contentType);
                return "docx";
        }
    }

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param status 状态
     */
    private void updateTaskStatus(Long taskId, String status) {
        try {
            TaskManagement task = new TaskManagement();
            task.setId(taskId);
            task.setStatus(status);
            taskManagementMapper.updateById(task);
            log.info("任务状态已更新，任务ID: {}, 状态: {}", taskId, status);
        } catch (Exception e) {
            log.error("更新任务状态失败，任务ID: {}, 状态: {}", taskId, status, e);
        }
    }
}

