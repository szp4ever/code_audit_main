package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 知识反馈对象 knowledge_feedback
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_feedback")
public class KnowledgeFeedback extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 反馈UUID
     */
    private String feedbackUuid;

    /**
     * 检测结果UUID（关联 detection_result.result_uuid，独立反馈入口可为空）
     */
    private String resultUuid;

    /**
     * 任务ID（关联 task_management.id，独立反馈入口可为空）
     */
    private Long taskId;

    /**
     * 项目ID（关联 project_management.id）
     */
    private Long projectId;

    /**
     * 项目名称快照（冗余字段，即使任务被删除也能追溯）
     */
    private String projectName;

    /**
     * 代码语言快照（冗余字段，记录当时检测的语言）
     */
    private String language;

    /**
     * 反馈类型（false_positive-误报、fix_recommendation-修复建议、supplement-补充描述）
     */
    private String feedbackType;

    /**
     * 出问题的文件路径
     */
    private String filePath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 行号
     */
    private Integer lineNumber;

    /**
     * 出问题的代码片段（原始代码）
     */
    private String codeSnippet;

    /**
     * 上下文代码
     */
    private String contextCode;

    /**
     * 用户评论
     */
    private String userComment;

    /**
     * 修正后的代码（如果是误报，提供正确代码）
     */
    private String correctedCode;

    /**
     * 修复建议（如果是修复建议）
     */
    private String fixSuggestion;

    /**
     * 目标知识条目UUID（如果是对现有知识的修正，关联 knowledge_item.item_uuid）
     */
    private String targetItemUuid;

    /**
     * 状态（pending-待处理、approved-已采纳入库、rejected-驳回）
     */
    private String status;

    /**
     * 转换后的知识条目UUID（关联 knowledge_item.item_uuid，status=approved时才有值）
     */
    private String convertedItemUuid;

    /**
     * 审核通过时间
     */
    private java.util.Date approveTime;

    /**
     * 审核人ID
     */
    private Long approveBy;

    /**
     * 驳回原因（status=rejected时填写）
     */
    private String rejectReason;

    /**
     * 删除标志
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 租户ID
     */
    private Long tenantId;
}
