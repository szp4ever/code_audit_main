package org.ruoyi.knowledge.cwe.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.cwe.domain.CweStandardMapping;
import org.ruoyi.knowledge.cwe.domain.vo.CweStandardMappingVo;

@Mapper(componentModel = "spring")
public interface CweStandardMappingConvert {
    CweStandardMappingVo toVo(CweStandardMapping entity);
    CweStandardMapping voToEntity(CweStandardMappingVo vo);
}
