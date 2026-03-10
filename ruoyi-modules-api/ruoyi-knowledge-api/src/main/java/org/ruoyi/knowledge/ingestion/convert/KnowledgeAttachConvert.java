package org.ruoyi.knowledge.ingestion.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeAttachBo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeAttachVo;

@Mapper(componentModel = "spring")
public interface KnowledgeAttachConvert {
    KnowledgeAttach toEntity(KnowledgeAttachBo bo);
    KnowledgeAttachVo toVo(KnowledgeAttach entity);
    KnowledgeAttach voToEntity(KnowledgeAttachVo vo);
}
