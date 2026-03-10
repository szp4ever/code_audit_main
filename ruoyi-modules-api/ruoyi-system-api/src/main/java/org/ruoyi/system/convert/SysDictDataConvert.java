package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysDictData;
import org.ruoyi.system.domain.bo.SysDictDataBo;
import org.ruoyi.system.domain.vo.SysDictDataVo;

@Mapper(componentModel = "spring")
public interface SysDictDataConvert {
    SysDictData toEntity(SysDictDataBo bo);
    SysDictDataVo toVo(SysDictData entity);
    SysDictData voToEntity(SysDictDataVo vo);
}
