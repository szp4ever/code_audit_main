package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeRoleRelation;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeRoleRelationBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeRoleRelationVo;

@Mapper(componentModel = "spring")
public interface KnowledgeRoleRelationConvert {
    KnowledgeRoleRelation toEntity(KnowledgeRoleRelationBo bo);
    KnowledgeRoleRelationVo toVo(KnowledgeRoleRelation entity);
    KnowledgeRoleRelation voToEntity(KnowledgeRoleRelationVo vo);
}
