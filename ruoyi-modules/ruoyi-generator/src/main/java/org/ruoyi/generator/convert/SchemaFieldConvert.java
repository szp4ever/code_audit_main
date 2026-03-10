package org.ruoyi.generator.convert;

import org.mapstruct.Mapper;
import org.ruoyi.generator.domain.SchemaField;
import org.ruoyi.generator.domain.bo.SchemaFieldBo;
import org.ruoyi.generator.domain.vo.SchemaFieldVo;

@Mapper(componentModel = "spring")
public interface SchemaFieldConvert {
    SchemaField toEntity(SchemaFieldBo bo);
    SchemaFieldVo toVo(SchemaField entity);
    SchemaField voToEntity(SchemaFieldVo vo);
}
