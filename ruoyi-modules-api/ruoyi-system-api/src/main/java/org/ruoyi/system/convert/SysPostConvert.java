package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysPost;
import org.ruoyi.system.domain.bo.SysPostBo;
import org.ruoyi.system.domain.vo.SysPostVo;

@Mapper(componentModel = "spring")
public interface SysPostConvert {
    SysPost toEntity(SysPostBo bo);
    SysPostVo toVo(SysPost entity);
    SysPost voToEntity(SysPostVo vo);
}
