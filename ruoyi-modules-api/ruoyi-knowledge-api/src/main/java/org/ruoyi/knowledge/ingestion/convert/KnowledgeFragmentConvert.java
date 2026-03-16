package org.ruoyi.knowledge.ingestion.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeFragment;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeFragmentBo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeFragmentVo;

@Mapper(componentModel = "spring")
public interface KnowledgeFragmentConvert {
    KnowledgeFragment toEntity(KnowledgeFragmentBo bo);
    KnowledgeFragmentVo toVo(KnowledgeFragment entity);
    KnowledgeFragment voToEntity(KnowledgeFragmentVo vo);
}
