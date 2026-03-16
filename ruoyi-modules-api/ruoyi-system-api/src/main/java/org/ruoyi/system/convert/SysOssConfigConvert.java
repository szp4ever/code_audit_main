package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysOssConfig;
import org.ruoyi.system.domain.bo.SysOssConfigBo;
import org.ruoyi.system.domain.vo.SysOssConfigVo;

@Mapper(componentModel = "spring")
public interface SysOssConfigConvert {
    SysOssConfig toEntity(SysOssConfigBo bo);
    SysOssConfigVo toVo(SysOssConfig entity);
    SysOssConfig voToEntity(SysOssConfigVo vo);
}
