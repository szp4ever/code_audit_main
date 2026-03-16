package org.ruoyi.knowledge.cwe.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.cwe.domain.CweReference;
import org.ruoyi.knowledge.cwe.domain.bo.CweReferenceBo;
import org.ruoyi.knowledge.cwe.domain.vo.CweReferenceVo;

@Mapper(componentModel = "spring")
public interface CweReferenceConvert {
    CweReference toEntity(CweReferenceBo bo);
    CweReferenceVo toVo(CweReference entity);
    CweReference voToEntity(CweReferenceVo vo);
}
