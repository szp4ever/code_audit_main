package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 检测结果对象 detection_result
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("detection_result")
public class DetectionResult extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 检测结果UUID
     */
    private String resultUuid;

    /**
     * 任务ID（关联 task_management.id）
     */
    private Long taskId;

    /**
     * 项目ID（关联 project_management.id）
     */
    private Long projectId;

    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 漏洞类型
     */
    private String vulnerabilityType;

    /**
     * 风险等级
     */
    private String severity;

    /**
     * CVSS 向量字符串
     */
    private String cvssVector;

    /**
     * CVSS 数值分数（0.0-10.0）
     */
    private BigDecimal cvssScore;

    /**
     * CVSS 版本号（如 4.0）
     */
    private String cvssVersion;

    /**
     * 代码语言
     */
    private String language;

    /**
     * 文件路径
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
     * 代码片段（检测出的问题代码）
     */
    private String codeSnippet;

    /**
     * 上下文代码
     */
    private String contextCode;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 修复建议（系统自动生成）
     */
    private String recommendation;

    /**
     * 置信度（0-100）
     */
    private BigDecimal confidence;

    /**
     * 状态（detected-已检测、false_positive-误报、fixed-已修复、ignored-已忽略）
     */
    private String status;

    /**
     * 是否误报（0-否 1-是）
     */
    private String isFalsePositive;

    /**
     * 是否已修复（0-否 1-是）
     */
    private String isFixed;

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
