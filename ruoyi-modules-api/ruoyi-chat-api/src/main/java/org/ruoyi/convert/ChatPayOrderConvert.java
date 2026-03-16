package org.ruoyi.convert;

import org.mapstruct.Mapper;
import org.ruoyi.domain.ChatPayOrder;
import org.ruoyi.domain.bo.ChatPayOrderBo;
import org.ruoyi.domain.vo.ChatPayOrderVo;

@Mapper(componentModel = "spring")
public interface ChatPayOrderConvert {
    ChatPayOrder toEntity(ChatPayOrderBo bo);
    ChatPayOrderVo toVo(ChatPayOrder entity);
    ChatPayOrder voToEntity(ChatPayOrderVo vo);
}
