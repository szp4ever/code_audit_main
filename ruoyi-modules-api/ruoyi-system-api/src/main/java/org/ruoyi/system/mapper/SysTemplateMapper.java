package org.ruoyi.system.mapper;

// [关键修改] 请检查这个包名，标准的 RuoYi-Vue-Plus 路径如下：
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.system.domain.SysTemplate;
import org.ruoyi.system.domain.vo.SysTemplateVo;
import org.apache.ibatis.annotations.Mapper; // 建议加上 @Mapper 注解以防扫描不到

/**
 * 模板配置Mapper接口
 * 继承 BaseMapperPlus<实体, VO> 以支持 selectVoPage
 */
@Mapper
public interface SysTemplateMapper extends BaseMapperPlus<SysTemplate, SysTemplateVo> {

}