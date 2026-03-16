package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeItem;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeItemBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;

@Mapper(componentModel = "spring")
public interface KnowledgeItemConvert {
    KnowledgeItem toEntity(KnowledgeItemBo bo);
    KnowledgeItemVo toVo(KnowledgeItem entity);
    KnowledgeItem voToEntity(KnowledgeItemVo vo);
}
