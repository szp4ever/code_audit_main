package org.ruoyi.aihuman.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ruoyi.aihuman.domain.AihumanInfo;
import org.ruoyi.aihuman.domain.bo.AihumanInfoBo;
import org.ruoyi.aihuman.domain.vo.AihumanInfoVo;

@Mapper(componentModel = "spring")
public interface AihumanInfoConvert {

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    AihumanInfo toEntity(AihumanInfoBo bo);

    AihumanInfoVo toVo(AihumanInfo entity);

    AihumanInfo voToEntity(AihumanInfoVo vo);
}
