package org.ruoyi.service;


import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeAttachBo;
import org.ruoyi.domain.vo.KnowledgeAttachVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * 知识库附件Service接口
 *
 * @author ageerle
 * @date 2025-04-08
 */
public interface IKnowledgeAttachService {

    /**
     * 查询知识库附件
     */
    KnowledgeAttachVo queryById(Long id);

    /**
     * 查询知识库附件列表
     */
    TableDataInfo<KnowledgeAttachVo> queryPageList(KnowledgeAttachBo bo, PageQuery pageQuery);

    /**
     * 查询知识库附件列表
     */
    List<KnowledgeAttachVo> queryList(KnowledgeAttachBo bo);

    /**
     * 新增知识库附件
     */
    Boolean insertByBo(KnowledgeAttachBo bo);

    /**
     * 修改知识库附件
     */
    Boolean updateByBo(KnowledgeAttachBo bo);

    /**
     * 校验并批量删除知识库附件信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 删除知识附件（通过docId）
     */
    void removeKnowledgeAttach(String docId);

    /**
     * 删除知识附件（通过processId）
     * 用于上传过程中删除任务，此时可能还没有docId
     */
    void removeKnowledgeAttachByProcessId(String processId);

    /**
     * 删除知识附件（通过kid和docName）
     * 最保险的删除方式，因为文件名是用户最直观的标识，且始终存在于任务对象中
     */
    void removeKnowledgeAttachByKidAndName(String kid, String docName);

    /**
     * 下载附件文件
     *
     * @param id 附件主键ID
     * @param response HTTP响应
     */
    void downloadAttach(Long id, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException;

    /**
     * 重新处理附件（重新解析、分块、向量化）
     *
     * @param docId 文档ID
     */
    void reprocessAttach(String docId);

    /**
     * 翻译文件
     *
     * @param file           文件
     * @param targetLanguage 目标语音
     */
    String translationByFile(MultipartFile file, String targetLanguage);

    /**
     * 获取附件条目数量分布统计（用于智能分箱）
     *
     * @param bo 查询条件
     * @return 条目数量列表
     */
    List<Integer> getItemCountDistribution(KnowledgeAttachBo bo);

    /**
     * 获取附件分面统计（筛选选项和计数）
     * 筛选选项基于原始数据（不受筛选影响），统计计数基于筛选结果（动态更新）
     *
     * @param bo 查询条件（用于计数）
     * @return 分面统计
     */
    org.ruoyi.domain.vo.AttachFacetStatsVo getFacetStats(KnowledgeAttachBo bo);
}
