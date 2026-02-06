package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.KnowledgeFeedback;

/**
 * 知识反馈业务对象 knowledge_feedback
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KnowledgeFeedback.class, reverseConvertGenerate = false)
public class KnowledgeFeedbackBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 反馈UUID
     */
    private String feedbackUuid;

    /**
     * 检测结果UUID
     */
    private String resultUuid;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 反馈类型
     */
    @NotBlank(message = "反馈类型不能为空", groups = {AddGroup.class})
    private String feedbackType;

    /**
     * 出问题的文件路径
     */
    private String filePath;

    /**
     * 用户评论
     */
    private String userComment;

    /**
     * 目标知识条目UUID
     */
    private String targetItemUuid;

    /**
     * 状态
     */
    private String status;
}
