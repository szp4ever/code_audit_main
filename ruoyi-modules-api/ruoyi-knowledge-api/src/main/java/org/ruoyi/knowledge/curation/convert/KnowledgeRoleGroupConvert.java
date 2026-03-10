package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeRoleGroup;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeRoleGroupBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeRoleGroupVo;

@Mapper(componentModel = "spring")
public interface KnowledgeRoleGroupConvert {
    KnowledgeRoleGroup toEntity(KnowledgeRoleGroupBo bo);
    KnowledgeRoleGroupVo toVo(KnowledgeRoleGroup entity);
    KnowledgeRoleGroup voToEntity(KnowledgeRoleGroupVo vo);
}
