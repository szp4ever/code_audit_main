package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysDictType;
import org.ruoyi.system.domain.bo.SysDictTypeBo;
import org.ruoyi.system.domain.vo.SysDictTypeVo;

@Mapper(componentModel = "spring")
public interface SysDictTypeConvert {
    SysDictType toEntity(SysDictTypeBo bo);
    SysDictTypeVo toVo(SysDictType entity);
    SysDictType voToEntity(SysDictTypeVo vo);
}
