package org.ruoyi.aihuman.convert;

import org.mapstruct.Mapper;
import org.ruoyi.aihuman.domain.AihumanConfig;
import org.ruoyi.aihuman.domain.bo.AihumanConfigBo;
import org.ruoyi.aihuman.domain.vo.AihumanConfigVo;

@Mapper(componentModel = "spring")
public interface AihumanConfigConvert {
    AihumanConfig toEntity(AihumanConfigBo bo);
    AihumanConfigVo toVo(AihumanConfig entity);
    AihumanConfig voToEntity(AihumanConfigVo vo);
}
