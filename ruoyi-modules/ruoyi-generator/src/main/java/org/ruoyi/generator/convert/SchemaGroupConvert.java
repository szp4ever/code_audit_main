package org.ruoyi.generator.convert;

import org.mapstruct.Mapper;
import org.ruoyi.generator.domain.SchemaGroup;
import org.ruoyi.generator.domain.bo.SchemaGroupBo;
import org.ruoyi.generator.domain.vo.SchemaGroupVo;

@Mapper(componentModel = "spring")
public interface SchemaGroupConvert {
    SchemaGroup toEntity(SchemaGroupBo bo);
    SchemaGroupVo toVo(SchemaGroup entity);
    SchemaGroup voToEntity(SchemaGroupVo vo);
}
