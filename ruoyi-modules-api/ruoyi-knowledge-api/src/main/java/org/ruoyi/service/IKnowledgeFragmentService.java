package org.ruoyi.service;


import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeFragmentBo;
import org.ruoyi.domain.vo.KnowledgeFragmentPageVo;
import org.ruoyi.domain.vo.KnowledgeFragmentVo;

import java.util.Collection;
import java.util.List;

/**
 * 知识片段Service接口
 *
 * @author ageerle
 * @date 2025-04-08
 */
public interface IKnowledgeFragmentService {

    /**
     * 查询知识片段
     */
    KnowledgeFragmentVo queryById(Long id);

    /**
     * 查询知识片段列表
     */
    TableDataInfo<KnowledgeFragmentVo> queryPageList(KnowledgeFragmentBo bo, PageQuery pageQuery);

    /**
     * 查询知识片段列表（带分面统计）
     */
    default KnowledgeFragmentPageVo queryPageListWithFacetStats(KnowledgeFragmentBo bo, PageQuery pageQuery) {
        TableDataInfo<KnowledgeFragmentVo> tableData = queryPageList(bo, pageQuery);
        KnowledgeFragmentPageVo pageVo = new KnowledgeFragmentPageVo();
        pageVo.setTotal(tableData.getTotal());
        pageVo.setRows(tableData.getRows());
        pageVo.setCode(tableData.getCode());
        pageVo.setMsg(tableData.getMsg());
        pageVo.setFacetStats(new java.util.HashMap<>());
        return pageVo;
    }

    /**
     * 查询知识片段列表
     */
    List<KnowledgeFragmentVo> queryList(KnowledgeFragmentBo bo);

    /**
     * 新增知识片段
     */
    Boolean insertByBo(KnowledgeFragmentBo bo);

    /**
     * 修改知识片段
     */
    Boolean updateByBo(KnowledgeFragmentBo bo);

    /**
     * 校验并批量删除知识片段信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
