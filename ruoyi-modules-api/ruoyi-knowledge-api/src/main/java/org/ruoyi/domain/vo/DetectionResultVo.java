package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.DetectionResult;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 检测结果视图对象 detection_result
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DetectionResult.class)
public class DetectionResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 检测结果UUID
     */
    @ExcelProperty(value = "结果UUID")
    private String resultUuid;

    /**
     * 任务ID
     */
    @ExcelProperty(value = "任务ID")
    private Long taskId;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 规则ID
     */
    @ExcelProperty(value = "规则ID")
    private String ruleId;

    /**
     * 规则名称
     */
    @ExcelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 漏洞类型
     */
    @ExcelProperty(value = "漏洞类型")
    private String vulnerabilityType;

    /**
     * 风险等级
     */
    @ExcelProperty(value = "风险等级")
    private String severity;

    /**
     * CVSS 向量字符串
     */
    private String cvssVector;

    /**
     * CVSS 数值分数
     */
    private BigDecimal cvssScore;

    /**
     * CVSS 版本号
     */
    private String cvssVersion;

    /**
     * 代码语言
     */
    @ExcelProperty(value = "代码语言")
    private String language;

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
     * 问题描述
     */
    @ExcelProperty(value = "问题描述")
    private String description;

    /**
     * 修复建议
     */
    @ExcelProperty(value = "修复建议")
    private String recommendation;

    /**
     * 置信度
     */
    @ExcelProperty(value = "置信度")
    private BigDecimal confidence;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 是否误报
     */
    @ExcelProperty(value = "是否误报")
    private String isFalsePositive;

    /**
     * 是否已修复
     */
    @ExcelProperty(value = "是否已修复")
    private String isFixed;

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
