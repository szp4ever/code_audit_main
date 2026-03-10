package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysTemplate;
import org.ruoyi.system.domain.bo.SysTemplateBo;
import org.ruoyi.system.domain.vo.SysTemplateVo;

@Mapper(componentModel = "spring")
public interface SysTemplateConvert {
    SysTemplate toEntity(SysTemplateBo bo);
    SysTemplateVo toVo(SysTemplate entity);
    SysTemplate voToEntity(SysTemplateVo vo);
}
