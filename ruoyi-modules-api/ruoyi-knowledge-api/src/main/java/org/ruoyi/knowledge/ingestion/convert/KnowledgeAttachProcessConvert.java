package org.ruoyi.knowledge.ingestion.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttachProcess;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeAttachProcessBo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeAttachProcessVo;

@Mapper(componentModel = "spring")
public interface KnowledgeAttachProcessConvert {
    KnowledgeAttachProcess toEntity(KnowledgeAttachProcessBo bo);
    KnowledgeAttachProcessVo toVo(KnowledgeAttachProcess entity);
    KnowledgeAttachProcess voToEntity(KnowledgeAttachProcessVo vo);
}
