package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ruoyi.system.domain.SysTenantPackage;
import org.ruoyi.system.domain.bo.SysTenantPackageBo;
import org.ruoyi.system.domain.vo.SysTenantPackageVo;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SysTenantPackageConvert {

    @Mapping(target = "menuIds", source = "menuIds", qualifiedByName = "longArrayToString")
    SysTenantPackage toEntity(SysTenantPackageBo bo);

    SysTenantPackageVo toVo(SysTenantPackage entity);

    SysTenantPackage voToEntity(SysTenantPackageVo vo);

    @Named("longArrayToString")
    default String longArrayToString(Long[] menuIds) {
        if (menuIds == null || menuIds.length == 0) {
            return null;
        }
        return Arrays.stream(menuIds)
            .map(String::valueOf)
            .collect(Collectors.joining(","));
    }
}
