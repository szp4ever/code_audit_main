package org.ruoyi.system.service;


import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.system.domain.bo.SysTemplateBo;
import org.ruoyi.system.domain.vo.SysTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * 模板配置Service接口
 *
 * @author RuoYi-AI
 */
public interface ISysTemplateService {

    /**
     * 查询模板配置
     */
    SysTemplateVo queryById(Long templateId);

    /**
     * 查询模板配置列表
     */
    TableDataInfo<SysTemplateVo> queryPageList(SysTemplateBo bo, PageQuery pageQuery);

    /**
     * 查询模板配置列表
     */
    List<SysTemplateVo> queryList(SysTemplateBo bo);

    /**
     * 新增模板配置
     */
    Boolean insertByBo(SysTemplateBo bo);

    /**
     * 修改模板配置
     */
    Boolean updateByBo(SysTemplateBo bo);

    /**
     * 校验并批量删除模板配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}