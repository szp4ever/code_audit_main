package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.common.log.event.OperLogEvent;
import org.ruoyi.system.domain.SysOperLog;
import org.ruoyi.system.domain.bo.SysOperLogBo;
import org.ruoyi.system.domain.vo.SysOperLogVo;

@Mapper(componentModel = "spring")
public interface SysOperLogConvert {
    SysOperLog toEntity(SysOperLogBo bo);
    SysOperLogVo toVo(SysOperLog entity);
    SysOperLog voToEntity(SysOperLogVo vo);
    OperLogEvent toEvent(SysOperLogBo bo);
    SysOperLogBo toBo(OperLogEvent event);
}
