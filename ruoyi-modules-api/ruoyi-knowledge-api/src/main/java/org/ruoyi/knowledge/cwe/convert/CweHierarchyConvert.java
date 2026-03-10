package org.ruoyi.knowledge.cwe.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.cwe.domain.CweHierarchy;
import org.ruoyi.knowledge.cwe.domain.vo.CweHierarchyVo;

@Mapper(componentModel = "spring")
public interface CweHierarchyConvert {
    CweHierarchyVo toVo(CweHierarchy entity);
    CweHierarchy voToEntity(CweHierarchyVo vo);
}
