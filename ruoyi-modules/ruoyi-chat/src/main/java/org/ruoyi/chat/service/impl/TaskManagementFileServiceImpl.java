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
            
            response.setContentType(file.getType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, len);
            }
            response.getOutputStream().flush();
            inputStream.close();
        } catch (Exception e) {
            log.error("文件下载失败", e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
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

