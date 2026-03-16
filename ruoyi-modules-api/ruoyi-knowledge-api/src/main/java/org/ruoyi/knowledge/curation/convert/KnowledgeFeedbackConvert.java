package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeFeedback;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeFeedbackBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeFeedbackVo;

@Mapper(componentModel = "spring")
public interface KnowledgeFeedbackConvert {
    KnowledgeFeedback toEntity(KnowledgeFeedbackBo bo);
    KnowledgeFeedbackVo toVo(KnowledgeFeedback entity);
    KnowledgeFeedback voToEntity(KnowledgeFeedbackVo vo);
}
