package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysLogininfor;
import org.ruoyi.system.domain.bo.SysLogininforBo;
import org.ruoyi.system.domain.vo.SysLogininforVo;

@Mapper(componentModel = "spring")
public interface SysLogininforConvert {
    SysLogininfor toEntity(SysLogininforBo bo);
    SysLogininforVo toVo(SysLogininfor entity);
    SysLogininfor voToEntity(SysLogininforVo vo);
}
