package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeInfo;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeInfoBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeInfoVo;

@Mapper(componentModel = "spring")
public interface KnowledgeInfoConvert {
    KnowledgeInfo toEntity(KnowledgeInfoBo bo);
    KnowledgeInfoVo toVo(KnowledgeInfo entity);
    KnowledgeInfo voToEntity(KnowledgeInfoVo vo);
}
