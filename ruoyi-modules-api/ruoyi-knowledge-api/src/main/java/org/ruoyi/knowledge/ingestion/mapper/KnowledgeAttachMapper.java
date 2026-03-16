package org.ruoyi.knowledge.ingestion.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeAttachVo;

/**
 * 知识库附件Mapper接口
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Mapper
public interface KnowledgeAttachMapper extends BaseMapperPlus<KnowledgeAttach, KnowledgeAttachVo> {

}
