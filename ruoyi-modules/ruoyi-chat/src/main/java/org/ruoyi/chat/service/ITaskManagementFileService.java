package org.ruoyi.chat.service;

import jakarta.servlet.http.HttpServletResponse;
import org.ruoyi.chat.domain.TaskManagementFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 任务管理文件Service接口
 *
 * @author ruoyi
 */
public interface ITaskManagementFileService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param taskId 任务ID（可选）
     * @return 文件信息
     */
    TaskManagementFile uploadFile(MultipartFile file, Long taskId);

    /**
     * 批量上传文件（支持文件夹上传）
     *
     * @param files 文件数组
     * @param relativePaths 相对路径数组（与files对应，可为null）
     * @param taskId 任务ID（可选）
     * @return 文件信息列表
     */
    List<TaskManagementFile> uploadFiles(MultipartFile[] files, String[] relativePaths, Long taskId);

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @param response HTTP响应
     */
    void downloadFile(Long fileId, HttpServletResponse response);

    /**
     * 根据任务ID查询文件列表
     *
     * @param taskId 任务ID
     * @param fileCategory 文件类别（input/output）
     * @return 文件列表
     */
    List<TaskManagementFile> selectFilesByTaskId(Long taskId, String fileCategory);
}








