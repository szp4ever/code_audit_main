package org.ruoyi.knowledge.cwe.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.cwe.domain.CweClusterMapping;
import org.ruoyi.knowledge.cwe.domain.vo.CweClusterMappingVo;

@Mapper(componentModel = "spring")
public interface CweClusterMappingConvert {
    CweClusterMappingVo toVo(CweClusterMapping entity);
    CweClusterMapping voToEntity(CweClusterMappingVo vo);
}
