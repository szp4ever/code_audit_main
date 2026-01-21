package org.ruoyi.chat.service.impl;

import cn.hutool.core.io.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.TaskManagementFile;
import org.ruoyi.chat.mapper.TaskManagementFileMapper;
import org.ruoyi.chat.service.ITaskManagementFileService;
import org.ruoyi.common.core.constant.CacheNames;
import org.ruoyi.common.json.utils.JsonUtils;
import org.ruoyi.common.oss.constant.OssConstant;
import org.ruoyi.common.oss.core.OssClient;
import org.ruoyi.common.oss.entity.UploadResult;
import org.ruoyi.common.oss.factory.OssFactory;
import org.ruoyi.common.oss.properties.OssProperties;
import org.ruoyi.common.core.utils.file.FileUtils;
import org.ruoyi.common.core.utils.file.MimeTypeUtils;
import org.ruoyi.common.redis.utils.CacheUtils;
import org.ruoyi.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 任务管理文件Service业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskManagementFileServiceImpl implements ITaskManagementFileService {

    private final TaskManagementFileMapper taskManagementFileMapper;

    /**
     * 上传文件
     */
    @Override
    public TaskManagementFile uploadFile(MultipartFile file, Long taskId) {
        try {
            // 打印文件信息
            String originalFilename = file.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFilename);
            long fileSize = file.getSize();
            String contentType = file.getContentType();
            
            log.info("========== 任务管理文件上传开始 ==========");
            log.info("文件名: {}", originalFilename);
            log.info("文件后缀: {}", suffix);
            log.info("文件大小: {} bytes ({} KB)", fileSize, fileSize / 1024);
            log.info("文件类型: {}", contentType);
            log.info("任务ID: {}", taskId);
            
            // 获取OSS配置信息
            String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
            log.info("OSS配置Key: {}", configKey);
            
            if (configKey != null) {
                String configJson = CacheUtils.get(CacheNames.SYS_OSS_CONFIG, configKey);
                if (configJson != null) {
                    OssProperties properties = JsonUtils.parseObject(configJson, OssProperties.class);
                    log.info("========== OSS配置信息 ==========");
                    log.info("访问站点(endpoint): {}", properties.getEndpoint());
                    log.info("自定义域名(domain): {}", properties.getDomain());
                    log.info("前缀(prefix): {}", properties.getPrefix());
                    log.info("ACCESS_KEY: {}", properties.getAccessKey() != null ? "***已配置***" : "未配置");
                    log.info("SECRET_KEY: {}", properties.getSecretKey() != null ? "***已配置***" : "未配置");
                    log.info("存储空间名(bucketName): {}", properties.getBucketName());
                    log.info("存储区域(region): {}", properties.getRegion());
                    log.info("是否HTTPS(isHttps): {}", properties.getIsHttps());
                    log.info("桶权限类型(accessPolicy): {}", properties.getAccessPolicy());
                } else {
                    log.warn("OSS配置信息未找到，configKey: {}", configKey);
                }
            }
            
            OssClient storage = OssFactory.instance();
            log.info("OSS客户端获取成功，ConfigKey: {}", storage.getConfigKey());
            log.info("OSS访问URL: {}", storage.getUrl());
            
            // 上传文件
            log.info("开始上传文件到OSS...");
            UploadResult uploadResult = storage.uploadSuffix(file.getBytes(), suffix, contentType);
            log.info("文件上传成功！");
            log.info("上传后的文件URL: {}", uploadResult.getUrl());
            log.info("上传后的文件名: {}", uploadResult.getFilename());
            
            TaskManagementFile taskFile = new TaskManagementFile();
            taskFile.setName(originalFilename);
            taskFile.setUrl(uploadResult.getUrl());
            taskFile.setSize(fileSize);
            taskFile.setType(contentType);
            taskFile.setFileCategory("input");
            taskFile.setUploadTime(new Date());
            taskFile.setDelFlag("0");
            
            if (taskId != null) {
                taskFile.setTaskId(taskId);
            }
            
            taskManagementFileMapper.insert(taskFile);
            log.info("文件信息已保存到数据库，文件ID: {}", taskFile.getId());
            log.info("========== 任务管理文件上传完成 ==========");
            return taskFile;
        } catch (Exception e) {
            log.error("========== 文件上传失败 ==========");
            log.error("错误信息: {}", e.getMessage());
            log.error("错误类型: {}", e.getClass().getName());
            if (e.getCause() != null) {
                log.error("根本原因: {}", e.getCause().getMessage());
            }
            log.error("完整异常堆栈:", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 批量上传文件（支持文件夹上传）
     */
    @Override
    public List<TaskManagementFile> uploadFiles(MultipartFile[] files, String[] relativePaths, Long taskId) {
        log.info("========== 批量上传文件开始 ==========");
        log.info("文件数量: {}", files != null ? files.length : 0);
        log.info("任务ID: {}", taskId);
        
        if (files == null || files.length == 0) {
            throw new RuntimeException("文件列表不能为空");
        }
        
        // 验证所有文件是否为代码文件类型
        List<String> invalidFiles = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String filename = file.getOriginalFilename();
                if (filename != null && !FileUtils.isValidFileExtention(file, MimeTypeUtils.CODE_FILE_EXTENSION)) {
                    invalidFiles.add(filename);
                }
            }
        }
        
        // 如果有不支持的文件类型，抛出异常并列出所有不支持的文件
        if (!invalidFiles.isEmpty()) {
            String errorMessage = String.format(
                "上传失败：以下文件不是代码文件类型，仅支持代码文件上传。不支持的文件：%s。支持的代码文件类型包括：Java、JavaScript、TypeScript、Python、C/C++、C#、Go、Rust、PHP、Ruby、Swift、Kotlin、Scala、HTML/CSS、配置文件、脚本文件、SQL等。",
                String.join("、", invalidFiles)
            );
            log.error("文件类型验证失败: {}", errorMessage);
            throw new RuntimeException(errorMessage);
        }
        
        List<TaskManagementFile> uploadedFiles = new java.util.ArrayList<>();
        OssClient storage = OssFactory.instance();
        
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file == null || file.isEmpty()) {
                log.warn("跳过空文件，索引: {}", i);
                continue;
            }
            
            try {
                String originalFilename = file.getOriginalFilename();
                String relativePath = (relativePaths != null && i < relativePaths.length) 
                        ? relativePaths[i] : null;
                
                // 如果没有提供相对路径，使用文件名作为默认值
                if (relativePath == null || relativePath.trim().isEmpty()) {
                    relativePath = originalFilename;
                }
                
                String suffix = FileUtil.getSuffix(originalFilename);
                long fileSize = file.getSize();
                String contentType = file.getContentType();
                
                log.info("正在上传文件[{}]: {}", i + 1, originalFilename);
                log.info("相对路径: {}", relativePath);
                log.info("文件大小: {} bytes ({} KB)", fileSize, fileSize / 1024);
                
                // 上传文件到OSS
                UploadResult uploadResult = storage.uploadSuffix(file.getBytes(), suffix, contentType);
                
                // 创建文件对象
                TaskManagementFile taskFile = new TaskManagementFile();
                taskFile.setName(originalFilename);
                taskFile.setRelativePath(relativePath);
                taskFile.setUrl(uploadResult.getUrl());
                taskFile.setSize(fileSize);
                taskFile.setType(contentType);
                taskFile.setFileCategory("input");
                taskFile.setUploadTime(new Date());
                taskFile.setDelFlag("0");
                
                if (taskId != null) {
                    taskFile.setTaskId(taskId);
                }
                
                // 保存到数据库
                taskManagementFileMapper.insert(taskFile);
                uploadedFiles.add(taskFile);
                
                log.info("文件上传成功[{}]: ID={}, URL={}", i + 1, taskFile.getId(), uploadResult.getUrl());
            } catch (Exception e) {
                log.error("文件上传失败[{}]: {}", i + 1, file.getOriginalFilename(), e);
                // 继续上传其他文件，不中断整个批量上传过程
                // 如果希望任何文件失败都停止，可以取消注释下面的代码
                // throw new RuntimeException("批量上传文件失败: " + e.getMessage(), e);
            }
        }
        
        log.info("批量上传完成，成功: {}/{}", uploadedFiles.size(), files.length);
        log.info("========== 批量上传文件结束 ==========");
        
        if (uploadedFiles.isEmpty()) {
            throw new RuntimeException("所有文件上传失败");
        }
        
        return uploadedFiles;
    }

    /**
     * 下载文件
     */
    @Override
    public void downloadFile(Long fileId, HttpServletResponse response) {
        TaskManagementFile file = taskManagementFileMapper.selectById(fileId);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }
        
        try {
            OssClient storage = OssFactory.instance();
            InputStream inputStream = storage.getObjectContent(file.getUrl());
            
            // 设置Content-Type，如果数据库中没有则根据文件扩展名推断
            String contentType = file.getType();
            if (contentType == null || contentType.isEmpty()) {
                String fileName = file.getName();
                String extension = FileUtil.getSuffix(fileName).toLowerCase();
                contentType = getContentTypeByExtension(extension);
                log.info("文件类型为空，根据扩展名推断: {} -> {}", extension, contentType);
            }
            
            response.setContentType(contentType);
            log.info("设置Content-Type: {}", contentType);
            
            // 设置文件名，处理中文文件名编码问题
            String fileName = file.getName();
            String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8")
                    .replace("+", "%20"); // 空格编码为%20而不是+
            
            // 同时设置标准Content-Disposition和RFC 5987格式（支持中文）
            response.setHeader("Content-Disposition", 
                    "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
            
            // 设置文件大小（如果已知）
            if (file.getSize() != null) {
                response.setContentLengthLong(file.getSize());
            }
            
            log.info("开始下载文件: {}, Content-Type: {}, 大小: {} bytes", 
                    fileName, contentType, file.getSize());
            
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, len);
            }
            response.getOutputStream().flush();
            inputStream.close();
            
            log.info("文件下载完成: {}", fileName);
        } catch (Exception e) {
            log.error("文件下载失败，文件ID: {}, 文件名: {}", fileId, file.getName(), e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 根据文件扩展名获取Content-Type
     *
     * @param extension 文件扩展名（不含点号，如docx、pdf）
     * @return Content-Type
     */
    private String getContentTypeByExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "application/octet-stream";
        }
        
        switch (extension.toLowerCase()) {
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "doc":
                return "application/msword";
            case "pdf":
                return "application/pdf";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xls":
                return "application/vnd.ms-excel";
            case "txt":
                return "text/plain";
            case "html":
            case "htm":
                return "text/html";
            case "json":
                return "application/json";
            case "xml":
                return "application/xml";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 根据任务ID查询文件列表
     */
    @Override
    public List<TaskManagementFile> selectFilesByTaskId(Long taskId, String fileCategory) {
        return taskManagementFileMapper.selectFilesByTaskId(taskId, fileCategory);
    }
}

