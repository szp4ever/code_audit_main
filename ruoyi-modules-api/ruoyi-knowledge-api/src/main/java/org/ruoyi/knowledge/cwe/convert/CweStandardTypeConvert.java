package org.ruoyi.knowledge.cwe.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.cwe.domain.CweStandardType;
import org.ruoyi.knowledge.cwe.domain.bo.CweStandardTypeBo;
import org.ruoyi.knowledge.cwe.domain.vo.CweStandardTypeVo;

@Mapper(componentModel = "spring")
public interface CweStandardTypeConvert {
    CweStandardType toEntity(CweStandardTypeBo bo);
    CweStandardTypeVo toVo(CweStandardType entity);
    CweStandardType voToEntity(CweStandardTypeVo vo);
}
