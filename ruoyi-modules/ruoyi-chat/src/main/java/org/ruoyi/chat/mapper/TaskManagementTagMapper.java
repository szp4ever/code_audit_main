package org.ruoyi.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.TaskManagementTag;

import java.util.List;

/**
 * 任务管理标签Mapper接口
 *
 * @author ruoyi
 */
public interface TaskManagementTagMapper extends BaseMapper<TaskManagementTag> {

    /**
     * 根据任务ID查询标签列表
     *
     * @param taskId 任务ID
     * @return 标签列表
     */
    List<String> selectTagsByTaskId(Long taskId);

    /**
     * 批量插入标签
     *
     * @param tags 标签列表
     * @return 结果
     */
    int batchInsertTags(@Param("tags") List<TaskManagementTag> tags);

    /**
     * 根据任务ID删除标签
     *
     * @param taskId 任务ID
     * @return 结果
     */
    int deleteTagsByTaskId(Long taskId);
}







