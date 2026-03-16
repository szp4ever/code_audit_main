package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysConfig;
import org.ruoyi.system.domain.bo.SysConfigBo;
import org.ruoyi.system.domain.vo.SysConfigVo;

@Mapper(componentModel = "spring")
public interface SysConfigConvert {
    SysConfig toEntity(SysConfigBo bo);
    SysConfigVo toVo(SysConfig entity);
    SysConfig voToEntity(SysConfigVo vo);
}
