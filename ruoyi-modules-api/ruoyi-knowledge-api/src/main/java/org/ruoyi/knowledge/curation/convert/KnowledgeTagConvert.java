package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeTag;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeTagBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeTagVo;

@Mapper(componentModel = "spring")
public interface KnowledgeTagConvert {
    KnowledgeTag toEntity(KnowledgeTagBo bo);
    KnowledgeTagVo toVo(KnowledgeTag entity);
    KnowledgeTag voToEntity(KnowledgeTagVo vo);
}
