package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.KnowledgeAttachProcess;
import org.ruoyi.domain.vo.KnowledgeAttachProcessVo;

/**
 * 附件处理状态Mapper接口
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Mapper
public interface KnowledgeAttachProcessMapper extends BaseMapperPlus<KnowledgeAttachProcess, KnowledgeAttachProcessVo> {

    /**
     * 使用JSON_SET部分更新status_data字段
     * @param id 处理任务ID
     * @param jsonPath JSON路径（如 $.matchingResults[0].userDecision）
     * @param jsonValue JSON值（需要是有效的JSON字符串）
     */
    @Update("UPDATE knowledge_attach_process SET status_data = JSON_SET(COALESCE(status_data, '{}'), #{jsonPath}, CAST(#{jsonValue} AS JSON)), updated_at = NOW() WHERE id = #{id}")
    int updateStatusDataJsonSet(@Param("id") Long id, @Param("jsonPath") String jsonPath, @Param("jsonValue") String jsonValue);
}
