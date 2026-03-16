package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysUser;
import org.ruoyi.system.domain.bo.SysUserBo;
import org.ruoyi.system.domain.vo.SysUserVo;
import org.ruoyi.system.domain.vo.SysUserExportVo;

@Mapper(componentModel = "spring")
public interface SysUserConvert {
    SysUser toEntity(SysUserBo bo);
    SysUserVo toVo(SysUser entity);
    SysUser voToEntity(SysUserVo vo);
    SysUserExportVo toExportVo(SysUserVo vo);
}
