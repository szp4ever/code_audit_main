package org.ruoyi.system.convert;

import org.mapstruct.Mapper;
import org.ruoyi.system.domain.ChatConfig;
import org.ruoyi.system.domain.bo.ChatConfigBo;
import org.ruoyi.system.domain.vo.ChatConfigVo;

@Mapper(componentModel = "spring")
public interface ChatConfigConvert {
    ChatConfig toEntity(ChatConfigBo bo);
    ChatConfigVo toVo(ChatConfig entity);
    ChatConfig voToEntity(ChatConfigVo vo);
}
