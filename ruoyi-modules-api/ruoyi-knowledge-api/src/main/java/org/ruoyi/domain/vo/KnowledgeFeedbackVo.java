package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeFeedback;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识反馈视图对象 knowledge_feedback
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KnowledgeFeedback.class)
public class KnowledgeFeedbackVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 反馈UUID
     */
    @ExcelProperty(value = "反馈UUID")
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
    @ExcelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;

    /**
     * 代码语言
     */
    @ExcelProperty(value = "代码语言")
    private String language;

    /**
     * 反馈类型
     */
    @ExcelProperty(value = "反馈类型")
    private String feedbackType;

    /**
     * 文件路径
     */
    @ExcelProperty(value = "文件路径")
    private String filePath;

    /**
     * 文件名
     */
    @ExcelProperty(value = "文件名")
    private String fileName;

    /**
     * 行号
     */
    @ExcelProperty(value = "行号")
    private Integer lineNumber;

    /**
     * 代码片段
     */
    private String codeSnippet;

    /**
     * 上下文代码
     */
    private String contextCode;

    /**
     * 用户评论
     */
    @ExcelProperty(value = "用户评论")
    private String userComment;

    /**
     * 修正后的代码
     */
    private String correctedCode;

    /**
     * 修复建议
     */
    private String fixSuggestion;

    /**
     * 目标知识条目UUID
     */
    private String targetItemUuid;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 转换后的知识条目UUID
     */
    private String convertedItemUuid;

    /**
     * 审核通过时间
     */
    @ExcelProperty(value = "审核通过时间")
    private java.util.Date approveTime;

    /**
     * 审核人ID
     */
    @ExcelProperty(value = "审核人ID")
    private Long approveBy;

    /**
     * 驳回原因
     */
    @ExcelProperty(value = "驳回原因")
    private String rejectReason;

    /**
     * 删除标志
     */
    private String delFlag;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private java.util.Date updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 更新人
     */
    private Long updateBy;
}
