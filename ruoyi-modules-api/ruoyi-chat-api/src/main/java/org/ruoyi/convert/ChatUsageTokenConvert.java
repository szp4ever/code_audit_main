package org.ruoyi.convert;

import org.mapstruct.Mapper;
import org.ruoyi.domain.ChatUsageToken;
import org.ruoyi.domain.bo.ChatUsageTokenBo;
import org.ruoyi.domain.vo.ChatUsageTokenVo;

@Mapper(componentModel = "spring")
public interface ChatUsageTokenConvert {
    ChatUsageToken toEntity(ChatUsageTokenBo bo);
    ChatUsageTokenVo toVo(ChatUsageToken entity);
    ChatUsageToken voToEntity(ChatUsageTokenVo vo);
}
