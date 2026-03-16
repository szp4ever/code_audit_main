package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysNoticeState;
import org.ruoyi.system.domain.bo.SysNoticeStateBo;
import org.ruoyi.system.domain.vo.SysNoticeStateVo;

@Mapper(componentModel = "spring")
public interface SysNoticeStateConvert {
    SysNoticeState toEntity(SysNoticeStateBo bo);
    SysNoticeStateVo toVo(SysNoticeState entity);
    SysNoticeState voToEntity(SysNoticeStateVo vo);
}
