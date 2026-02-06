package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.KnowledgeFeedback;
import org.ruoyi.domain.vo.KnowledgeFeedbackVo;

import java.util.List;

/**
 * 知识反馈Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface KnowledgeFeedbackMapper extends BaseMapperPlus<KnowledgeFeedback, KnowledgeFeedbackVo> {

    /**
     * 根据feedbackUuid查询反馈
     *
     * @param feedbackUuid 反馈UUID
     * @return KnowledgeFeedback
     */
    KnowledgeFeedback selectByFeedbackUuid(@Param("feedbackUuid") String feedbackUuid);

    /**
     * 根据status查询反馈列表
     *
     * @param status 状态
     * @return 反馈列表
     */
    List<KnowledgeFeedbackVo> selectByStatus(@Param("status") String status);
}
