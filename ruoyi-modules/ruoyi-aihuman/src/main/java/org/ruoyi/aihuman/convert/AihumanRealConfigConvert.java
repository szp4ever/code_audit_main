package org.ruoyi.aihuman.convert;

import org.mapstruct.Mapper;
import org.ruoyi.aihuman.domain.AihumanRealConfig;
import org.ruoyi.aihuman.domain.bo.AihumanRealConfigBo;
import org.ruoyi.aihuman.domain.vo.AihumanRealConfigVo;

@Mapper(componentModel = "spring")
public interface AihumanRealConfigConvert {
    AihumanRealConfig toEntity(AihumanRealConfigBo bo);
    AihumanRealConfigVo toVo(AihumanRealConfig entity);
    AihumanRealConfig voToEntity(AihumanRealConfigVo vo);
}
