package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysNotice;
import org.ruoyi.system.domain.bo.SysNoticeBo;
import org.ruoyi.system.domain.vo.SysNoticeVo;

@Mapper(componentModel = "spring")
public interface SysNoticeConvert {
    SysNotice toEntity(SysNoticeBo bo);
    SysNoticeVo toVo(SysNotice entity);
    SysNotice voToEntity(SysNoticeVo vo);
}
