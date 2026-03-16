package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysRole;
import org.ruoyi.system.domain.bo.SysRoleBo;
import org.ruoyi.system.domain.vo.SysRoleVo;

@Mapper(componentModel = "spring")
public interface SysRoleConvert {
    SysRole toEntity(SysRoleBo bo);
    SysRoleVo toVo(SysRole entity);
    SysRole voToEntity(SysRoleVo vo);
}
