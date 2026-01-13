package org.ruoyi.chat.service.impl;

import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.TaskManagementFile;
import org.ruoyi.chat.mapper.TaskManagementFileMapper;
import org.ruoyi.chat.mapper.TaskManagementMapper;
import org.ruoyi.chat.service.ITaskProcessingService;
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
import java.util.Date;
import java.util.List;

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
    
    // Flask接口地址
    private static final String FLASK_API_URL = "http://127.0.0.1:5004/api/process";
    
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
            
            // 调用Flask接口
            byte[] resultFileBytes = callFlaskApi(taskManagement, inputFiles);
            
            if (resultFileBytes == null || resultFileBytes.length == 0) {
                log.warn("Flask接口返回的文件为空，任务ID: {}", taskId);
                updateTaskStatus(taskId, "completed");
                return;
            }
            
            // 保存返回的文件到OSS
            TaskManagementFile outputFile = saveOutputFile(taskId, resultFileBytes);
            
            log.info("任务处理完成，任务ID: {}, 输出文件ID: {}", taskId, outputFile.getId());
            
            // 更新任务状态为已完成
            updateTaskStatus(taskId, "completed");
            
        } catch (Exception e) {
            log.error("任务处理失败，任务ID: {}", taskId, e);
            // 更新任务状态为已取消（或保持进行中，根据业务需求）
            updateTaskStatus(taskId, "cancelled");
        }
    }

    /**
     * 调用Flask接口
     *
     * @param taskManagement 任务信息
     * @param inputFiles 输入文件列表
     * @return 返回的文件字节数组
     */
    private byte[] callFlaskApi(TaskManagement taskManagement, List<TaskManagementFile> inputFiles) {
        log.info("开始调用Flask接口，任务ID: {}, 任务类型: {}, 文件数量: {}", 
                taskManagement.getId(), taskManagement.getTaskType(), inputFiles.size());
        
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(FLASK_API_URL)
                    .build();
            
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            
            // 添加任务类型
            bodyBuilder.part("task_type", taskManagement.getTaskType());
            
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
            log.info("发送请求到Flask接口: {}", FLASK_API_URL);
            byte[] responseBytes = webClient.post()
                    .uri("")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                    .block();
            
            log.info("Flask接口调用成功，返回文件大小: {} bytes", responseBytes != null ? responseBytes.length : 0);
            return responseBytes;
            
        } catch (Exception e) {
            log.error("调用Flask接口失败", e);
            throw new RuntimeException("调用Flask接口失败: " + e.getMessage(), e);
        }
    }

    /**
     * 保存输出文件到OSS
     *
     * @param taskId 任务ID
     * @param fileBytes 文件字节数组
     * @return 保存的文件对象
     */
    private TaskManagementFile saveOutputFile(Long taskId, byte[] fileBytes) {
        log.info("开始保存输出文件到OSS，任务ID: {}, 文件大小: {} bytes", taskId, fileBytes.length);
        
        try {
            OssClient storage = OssFactory.instance();
            
            // 生成文件名（使用任务ID和时间戳）
            String fileName = "task_" + taskId + "_result_" + System.currentTimeMillis() + ".txt";
            
            // 上传到OSS
            UploadResult uploadResult = storage.upload(fileBytes, fileName, "text/plain");
            
            log.info("输出文件上传成功，URL: {}, 文件名: {}", uploadResult.getUrl(), uploadResult.getFilename());
            
            // 保存文件信息到数据库
            TaskManagementFile outputFile = new TaskManagementFile();
            outputFile.setTaskId(taskId);
            outputFile.setName(fileName);
            outputFile.setUrl(uploadResult.getUrl());
            outputFile.setSize((long) fileBytes.length);
            outputFile.setType("text/plain");
            outputFile.setFileCategory("output");
            outputFile.setUploadTime(new Date());
            outputFile.setDelFlag("0");
            
            taskManagementFileMapper.insert(outputFile);
            
            log.info("输出文件信息已保存到数据库，文件ID: {}", outputFile.getId());
            
            return outputFile;
            
        } catch (Exception e) {
            log.error("保存输出文件失败", e);
            throw new RuntimeException("保存输出文件失败: " + e.getMessage(), e);
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

