package org.ruoyi.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.ProjectManagementTag;

import java.util.List;

/**
 * 项目管理标签Mapper接口
 *
 * @author ruoyi
 */
public interface ProjectManagementTagMapper extends BaseMapper<ProjectManagementTag> {

    /**
     * 根据项目ID查询标签列表
     *
     * @param projectId 项目ID
     * @return 标签名称列表
     */
    List<String> selectTagsByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据项目ID删除标签
     *
     * @param projectId 项目ID
     * @return 结果
     */
    int deleteTagsByProjectId(@Param("projectId") Long projectId);

    /**
     * 批量插入标签
     *
     * @param tags 标签列表
     * @return 结果
     */
    int batchInsertTags(@Param("tags") List<ProjectManagementTag> tags);
}

