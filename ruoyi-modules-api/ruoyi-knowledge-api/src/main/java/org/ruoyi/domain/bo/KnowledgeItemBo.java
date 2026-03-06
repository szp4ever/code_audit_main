package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.KnowledgeItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * 知识条目业务对象 knowledge_item
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KnowledgeItem.class, reverseConvertGenerate = false)
public class KnowledgeItemBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 知识条目UUID（单个）
     */
    private String itemUuid;

    /**
     * 知识条目UUID列表（多个，用于批量查询）
     */
    private List<String> itemUuids;

    /**
     * 知识库ID
     */
    @NotBlank(message = "知识库ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String kid;

    /**
     * 条目标题
     */
    @NotBlank(message = "条目标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 搜索关键词（用于多字段搜索）
     */
    private String searchKeyword;

    /**
     * 漏洞类型搜索关键词（用于Cluster过滤）
     */
    private String vulnerabilityTypeKeyword;

    /**
     * 漏洞类型（单个，保留用于兼容和快速查询）
     */
    private String vulnerabilityType;

    /**
     * 漏洞类型列表（多个CWE ID，如 ["CWE-89", "CWE-79"]）
     */
    private List<String> vulnerabilityTypes;

    /**
     * 适用语言（单个）
     */
    private String language;

    /**
     * 适用语言列表（多选筛选）
     */
    private List<String> languages;

    /**
     * 风险等级（单个）
     */
    private String severity;

    /**
     * 风险等级列表（多选筛选）
     */
    private List<String> severities;

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
     * CVSS 分数最小值（筛选用）
     */
    private BigDecimal cvssScoreMin;

    /**
     * CVSS 分数最大值（筛选用）
     */
    private BigDecimal cvssScoreMax;

    /**
     * CVSS 攻击方式筛选（AV: N/A/L/P）
     */
    private List<String> cvssAttackVector;

    /**
     * CVSS 利用复杂度筛选（AC: L/H）
     */
    private List<String> cvssAttackComplexity;

    /**
     * CVSS 权限需求筛选（PR: N/L/H）
     */
    private List<String> cvssPrivilegesRequired;

    /**
     * CVSS 用户交互筛选（UI: N/R）
     */
    private List<String> cvssUserInteraction;

    /**
     * CVSS 影响范围筛选（S: U/C）
     */
    private List<String> cvssScope;

    /**
     * CVSS 机密性影响筛选（C: N/L/H）
     */
    private List<String> cvssConfidentiality;

    /**
     * CVSS 完整性影响筛选（I: N/L/H）
     */
    private List<String> cvssIntegrity;

    /**
     * CVSS 可用性影响筛选（A: N/L/H）
     */
    private List<String> cvssAvailability;

    /**
     * 创建时间起始（筛选用）
     */
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTimeStart;

    /**
     * 创建时间结束（筛选用）
     */
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTimeEnd;

    /**
     * 问题描述
     */
    private String problemDescription;

    /**
     * 修复方案
     */
    private String fixSolution;

    /**
     * 示例代码
     */
    private String exampleCode;

    /**
     * 参考链接
     */
    private String referenceLink;

    /**
     * 状态（单个）
     */
    private String status;

    /**
     * 状态列表（多选筛选）
     */
    private List<String> statuses;

    /**
     * 标签列表（多选筛选）
     */
    private List<String> tags;

    /**
     * 发布时间
     */
    private java.util.Date publishTime;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源任务ID
     */
    private Long sourceTaskId;

    /**
     * 来源反馈ID
     */
    private Long sourceFeedbackId;
}
