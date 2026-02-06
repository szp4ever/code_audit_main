package org.ruoyi.service;

import jakarta.servlet.http.HttpServletResponse;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.BatchUpdateRequestBo;
import org.ruoyi.domain.bo.ExportPreviewRequestBo;
import org.ruoyi.domain.bo.ExportRequestBo;
import org.ruoyi.domain.bo.KnowledgeItemBo;
import org.ruoyi.domain.vo.BatchDeleteResultVo;
import org.ruoyi.domain.vo.BatchUpdateResultVo;
import org.ruoyi.domain.vo.ExportPreviewVo;
import org.ruoyi.domain.vo.KnowledgeItemVo;

import java.util.Collection;
import java.util.List;

/**
 * 知识条目Service接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
public interface IKnowledgeItemService {

    /**
     * 查询知识条目
     */
    KnowledgeItemVo queryById(Long id);

    /**
     * 根据itemUuid查询知识条目
     */
    KnowledgeItemVo queryByItemUuid(String itemUuid);

    /**
     * 查询知识条目列表
     */
    TableDataInfo<KnowledgeItemVo> queryPageList(KnowledgeItemBo bo, PageQuery pageQuery);

    /**
     * 查询知识条目列表
     */
    List<KnowledgeItemVo> queryList(KnowledgeItemBo bo);

    /**
     * 新增知识条目
     */
    Boolean insertByBo(KnowledgeItemBo bo);

    /**
     * 修改知识条目
     */
    Boolean updateByBo(KnowledgeItemBo bo);

    /**
     * 校验并批量删除知识条目信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据itemUuid删除知识条目
     */
    Boolean deleteByItemUuid(String itemUuid);

    /**
     * 更新知识库的存储大小（基于附件大小统计）
     */
    void updateKnowledgeDataSize(String kid);

    /**
     * 批量删除知识条目（基于itemUuid）
     */
    BatchDeleteResultVo batchDeleteByItemUuids(List<String> itemUuids);

    /**
     * 批量更新知识条目（基于itemUuid）
     */
    BatchUpdateResultVo batchUpdateByItemUuids(BatchUpdateRequestBo request);

    /**
     * 导出预览
     */
    ExportPreviewVo exportPreview(ExportPreviewRequestBo request);

    /**
     * 导出知识条目
     */
    void export(ExportRequestBo request, HttpServletResponse response);
}
