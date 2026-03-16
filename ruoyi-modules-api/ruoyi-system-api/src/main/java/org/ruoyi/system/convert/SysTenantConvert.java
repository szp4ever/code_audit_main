package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.SysTenant;
import org.ruoyi.system.domain.bo.SysTenantBo;
import org.ruoyi.system.domain.vo.SysTenantVo;
import org.ruoyi.system.domain.vo.TenantListVo;

@Mapper(componentModel = "spring")
public interface SysTenantConvert {
    SysTenant toEntity(SysTenantBo bo);
    SysTenantVo toVo(SysTenant entity);
    SysTenant voToEntity(SysTenantVo vo);
    TenantListVo toListVo(SysTenantVo vo);
}
