package org.ruoyi.convert;

import org.mapstruct.Mapper;
import org.ruoyi.domain.McpInfo;
import org.ruoyi.domain.bo.McpInfoBo;
import org.ruoyi.domain.vo.McpInfoVo;

@Mapper(componentModel = "spring")
public interface McpInfoConvert {
    McpInfo toEntity(McpInfoBo bo);
    McpInfoVo toVo(McpInfo entity);
    McpInfo voToEntity(McpInfoVo vo);
}
