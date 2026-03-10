package org.ruoyi.generator.convert;

import org.mapstruct.Mapper;
import org.ruoyi.generator.domain.Schema;
import org.ruoyi.generator.domain.bo.SchemaBo;
import org.ruoyi.generator.domain.vo.SchemaVo;

@Mapper(componentModel = "spring")
public interface SchemaConvert {
    Schema toEntity(SchemaBo bo);
    SchemaVo toVo(Schema entity);
    Schema voToEntity(SchemaVo vo);
}
