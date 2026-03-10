package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysOss;
import org.ruoyi.system.domain.bo.SysOssBo;
import org.ruoyi.system.domain.vo.SysOssVo;

@Mapper(componentModel = "spring")
public interface SysOssConvert {
    SysOss toEntity(SysOssBo bo);
    SysOssVo toVo(SysOss entity);
    SysOss voToEntity(SysOssVo vo);
}
