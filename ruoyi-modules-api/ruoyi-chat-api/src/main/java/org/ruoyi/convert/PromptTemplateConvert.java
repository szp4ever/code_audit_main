package org.ruoyi.convert;

import org.mapstruct.Mapper;
import org.ruoyi.domain.PromptTemplate;
import org.ruoyi.domain.bo.PromptTemplateBo;
import org.ruoyi.domain.vo.PromptTemplateVo;

@Mapper(componentModel = "spring")
public interface PromptTemplateConvert {
    PromptTemplate toEntity(PromptTemplateBo bo);
    PromptTemplateVo toVo(PromptTemplate entity);
    PromptTemplate voToEntity(PromptTemplateVo vo);
}
