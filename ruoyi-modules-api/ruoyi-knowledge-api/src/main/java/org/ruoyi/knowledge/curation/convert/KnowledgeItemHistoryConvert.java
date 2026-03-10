package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemHistory;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemHistoryVo;

@Mapper(componentModel = "spring")
public interface KnowledgeItemHistoryConvert {
    KnowledgeItemHistoryVo toVo(KnowledgeItemHistory entity);
    KnowledgeItemHistory voToEntity(KnowledgeItemHistoryVo vo);
}
