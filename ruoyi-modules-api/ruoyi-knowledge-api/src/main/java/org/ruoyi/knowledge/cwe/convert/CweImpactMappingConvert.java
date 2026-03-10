package org.ruoyi.knowledge.cwe.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.cwe.domain.CweImpactMapping;
import org.ruoyi.knowledge.cwe.domain.vo.CweImpactMappingVo;

@Mapper(componentModel = "spring")
public interface CweImpactMappingConvert {
    CweImpactMappingVo toVo(CweImpactMapping entity);
    CweImpactMapping voToEntity(CweImpactMappingVo vo);
}
