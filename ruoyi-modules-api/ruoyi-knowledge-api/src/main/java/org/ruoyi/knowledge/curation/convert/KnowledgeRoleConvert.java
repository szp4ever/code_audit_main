package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeRole;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeRoleBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeRoleVo;

@Mapper(componentModel = "spring")
public interface KnowledgeRoleConvert {
    KnowledgeRole toEntity(KnowledgeRoleBo bo);
    KnowledgeRoleVo toVo(KnowledgeRole entity);
    KnowledgeRole voToEntity(KnowledgeRoleVo vo);
}
