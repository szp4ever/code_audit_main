package org.ruoyi.service;


import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeInfoBo;
import org.ruoyi.domain.bo.KnowledgeInfoUploadBo;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.domain.vo.KnowledgeAttachVo;

import java.util.Collection;
import java.util.List;

/**
 * 知识库Service接口
 *
 * @author ageerle
 * @date 2025-04-08
 */
public interface IKnowledgeInfoService {

    /**
     * 查询知识库
     */
    KnowledgeInfoVo queryById(Long id);

    /**
     * 根据kid查询知识库
     */
    KnowledgeInfoVo queryByKid(String kid);

    /**
     * 查询知识库列表
     */
    TableDataInfo<KnowledgeInfoVo> queryPageList(KnowledgeInfoBo bo, PageQuery pageQuery);

    /**
     * 查询知识库列表
     */
    TableDataInfo<KnowledgeInfoVo> queryPageListByRole(KnowledgeInfoBo bo, PageQuery pageQuery);

    /**
     * 查询知识库列表
     */
    List<KnowledgeInfoVo> queryList(KnowledgeInfoBo bo);

    /**
     * 新增知识库
     */
    Boolean insertByBo(KnowledgeInfoBo bo);

    /**
     * 修改知识库
     */
    Boolean updateByBo(KnowledgeInfoBo bo);

    /**
     * 校验并批量删除知识库信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 新增知识库
     */
    void saveOne(KnowledgeInfoBo bo);

    /**
     * 删除知识库
     */
    void removeKnowledge(String kid);

    /**
     * 批量删除知识库
     */
    void removeKnowledgeBatch(List<String> kids);

    /**
     * 上传附件
     */
    void upload(KnowledgeInfoUploadBo bo) throws Exception;
    
    /**
     * 上传并创建附件记录（同步，快速完成）
     * 只读取文件内容并保存到数据库，创建processId，立即返回
     */
    KnowledgeAttachVo uploadAndCreateAttach(KnowledgeInfoUploadBo bo) throws Exception;
    
    /**
     * 异步处理附件（解析、分块、匹配、创建条目、向量化等）
     */
    void processAttachAsync(Long attachId, String docId, String kid, Boolean autoCreateItems, Boolean autoClassify);
    
    /**
     * 刷新所有知识库的统计字段（条目数、片段数、存储大小）
     * 不更新update_time字段
     */
    void refreshAllKnowledgeStatistics();
}
