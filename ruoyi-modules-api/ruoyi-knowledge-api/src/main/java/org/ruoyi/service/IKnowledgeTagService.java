package org.ruoyi.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeTagBo;
import org.ruoyi.domain.vo.KnowledgeTagVo;

import java.util.Collection;
import java.util.List;

/**
 * 知识标签Service接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
public interface IKnowledgeTagService {

    /**
     * 查询知识标签
     */
    KnowledgeTagVo queryById(Long id);

    /**
     * 查询知识标签列表
     */
    TableDataInfo<KnowledgeTagVo> queryPageList(KnowledgeTagBo bo, PageQuery pageQuery);

    /**
     * 查询知识标签列表
     */
    List<KnowledgeTagVo> queryList(KnowledgeTagBo bo);

    /**
     * 根据itemUuid查询标签列表
     */
    List<KnowledgeTagVo> queryByItemUuid(String itemUuid);

    /**
     * 新增知识标签
     */
    Boolean insertByBo(KnowledgeTagBo bo);

    /**
     * 修改知识标签
     */
    Boolean updateByBo(KnowledgeTagBo bo);

    /**
     * 校验并批量删除知识标签信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 为知识条目添加标签
     */
    Boolean addTagToItem(String itemUuid, Long tagId);

    /**
     * 移除知识条目标签
     */
    Boolean removeTagFromItem(String itemUuid, Long tagId);
}
