package org.ruoyi.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeFeedbackBo;
import org.ruoyi.domain.vo.KnowledgeFeedbackVo;

import java.util.Collection;
import java.util.List;

/**
 * 知识反馈Service接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
public interface IKnowledgeFeedbackService {

    /**
     * 查询知识反馈
     */
    KnowledgeFeedbackVo queryById(Long id);

    /**
     * 根据feedbackUuid查询反馈
     */
    KnowledgeFeedbackVo queryByFeedbackUuid(String feedbackUuid);

    /**
     * 查询知识反馈列表
     */
    TableDataInfo<KnowledgeFeedbackVo> queryPageList(KnowledgeFeedbackBo bo, PageQuery pageQuery);

    /**
     * 根据status查询反馈列表
     */
    List<KnowledgeFeedbackVo> queryByStatus(String status);

    /**
     * 新增知识反馈
     */
    Boolean insertByBo(KnowledgeFeedbackBo bo);

    /**
     * 修改知识反馈
     */
    Boolean updateByBo(KnowledgeFeedbackBo bo);

    /**
     * 审核通过反馈
     */
    Boolean approveFeedback(String feedbackUuid, String targetKid, Boolean autoPublish);

    /**
     * 驳回反馈
     */
    Boolean rejectFeedback(String feedbackUuid, String rejectReason);

    /**
     * 校验并批量删除知识反馈信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
