package org.ruoyi.chat.mapper; // [修复] 包名修正

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagementTag;
import org.ruoyi.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 任务管理标签Mapper接口
 */
@Mapper
public interface TaskManagementTagMapper extends BaseMapperPlus<TaskManagementTag, TaskManagementTag> {

    List<String> selectTagsByTaskId(Long taskId);

    int batchInsertTags(@Param("tags") List<TaskManagementTag> tags);

    int deleteTagsByTaskId(Long taskId);
}