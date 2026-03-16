package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysMenu;
import org.ruoyi.system.domain.bo.SysMenuBo;
import org.ruoyi.system.domain.vo.SysMenuVo;

@Mapper(componentModel = "spring")
public interface SysMenuConvert {
    SysMenu toEntity(SysMenuBo bo);
    SysMenuVo toVo(SysMenu entity);
    SysMenu voToEntity(SysMenuVo vo);
}
