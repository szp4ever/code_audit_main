package org.ruoyi.convert;

import org.mapstruct.Mapper;
import org.ruoyi.domain.ChatSession;
import org.ruoyi.domain.bo.ChatSessionBo;
import org.ruoyi.domain.vo.ChatSessionVo;

@Mapper(componentModel = "spring")
public interface ChatSessionConvert {
    ChatSession toEntity(ChatSessionBo bo);
    ChatSessionVo toVo(ChatSession entity);
    ChatSession voToEntity(ChatSessionVo vo);
}
