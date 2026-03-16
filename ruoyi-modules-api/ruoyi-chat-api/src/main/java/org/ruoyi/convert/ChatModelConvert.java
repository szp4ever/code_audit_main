package org.ruoyi.convert;

import org.mapstruct.Mapper;
import org.ruoyi.domain.ChatModel;
import org.ruoyi.domain.bo.ChatModelBo;
import org.ruoyi.domain.vo.ChatModelVo;

@Mapper(componentModel = "spring")
public interface ChatModelConvert {
    ChatModel toEntity(ChatModelBo bo);
    ChatModelVo toVo(ChatModel entity);
    ChatModel voToEntity(ChatModelVo vo);
}
