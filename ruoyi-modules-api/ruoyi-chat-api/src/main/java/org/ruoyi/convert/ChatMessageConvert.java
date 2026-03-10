package org.ruoyi.convert;

import org.mapstruct.Mapper;
import org.ruoyi.domain.ChatMessage;
import org.ruoyi.domain.bo.ChatMessageBo;
import org.ruoyi.domain.vo.ChatMessageVo;

@Mapper(componentModel = "spring")
public interface ChatMessageConvert {
    ChatMessage toEntity(ChatMessageBo bo);
    ChatMessageVo toVo(ChatMessage entity);
    ChatMessage voToEntity(ChatMessageVo vo);
}
