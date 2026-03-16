package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysDept;
import org.ruoyi.system.domain.bo.SysDeptBo;
import org.ruoyi.system.domain.vo.SysDeptVo;

@Mapper(componentModel = "spring")
public interface SysDeptConvert {
    SysDept toEntity(SysDeptBo bo);
    SysDeptVo toVo(SysDept entity);
    SysDept voToEntity(SysDeptVo vo);
}
