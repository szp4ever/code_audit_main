package org.ruoyi.chat.mapper; // [修复] 包名修正

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagementFile;
import org.ruoyi.chat.domain.vo.TaskManagementFileVo;
import org.ruoyi.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 任务管理文件Mapper接口
 */
@Mapper
public interface TaskManagementFileMapper extends BaseMapperPlus<TaskManagementFile, TaskManagementFileVo> {

    List<TaskManagementFile> selectFilesByTaskId(@Param("taskId") Long taskId, @Param("fileCategory") String fileCategory);

    int batchInsertFiles(@Param("files") List<TaskManagementFile> files);

    int deleteFilesByTaskId(Long taskId);
}