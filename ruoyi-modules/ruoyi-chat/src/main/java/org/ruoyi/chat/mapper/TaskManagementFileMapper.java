package org.ruoyi.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagementFile;

import java.util.List;

/**
 * 任务管理文件Mapper接口
 *
 * @author ruoyi
 */
public interface TaskManagementFileMapper extends BaseMapper<TaskManagementFile> {

    /**
     * 根据任务ID查询文件列表
     *
     * @param taskId 任务ID
     * @param fileCategory 文件类别（input/output）
     * @return 文件列表
     */
    List<TaskManagementFile> selectFilesByTaskId(@Param("taskId") Long taskId, @Param("fileCategory") String fileCategory);

    /**
     * 批量插入文件
     *
     * @param files 文件列表
     * @return 结果
     */
    int batchInsertFiles(@Param("files") List<TaskManagementFile> files);

    /**
     * 根据文件ID查询文件
     *
     * @param id 文件ID
     * @return 文件信息
     */
    TaskManagementFile selectFileById(Long id);

    /**
     * 根据任务ID删除文件
     *
     * @param taskId 任务ID
     * @return 结果
     */
    int deleteFilesByTaskId(Long taskId);
}







