package org.ruoyi.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * LLM提取的结构化数据
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Data
public class ExtractedItemData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 生成型字段
     */
    private String title; // 标题
    private String summary; // 摘要
    private String problemDescription; // 问题描述
    private String fixSolution; // 修复方案
    private String exampleCode; // 示例代码
    private String referenceLink; // 参考链接

    /**
     * 选择型字段
     */
    private List<String> tags; // 标签列表
    private String vulnerabilityType; // 漏洞类型（CWE ID，单个，保留用于兼容）
    private List<String> vulnerabilityTypes; // 漏洞类型列表（CWE ID数组，如["CWE-89", "CWE-79"]）
    private String language; // 适用语言
    private String severity; // 风险等级

    /**
     * CVSS维度（用户输入的字段，用于生成cvssVector）
     */
    private String cvssAttackVector; // AV（攻击方式）：N/A/L/P
    private String cvssAttackComplexity; // AC（利用复杂度）：L/H
    private String cvssPrivilegesRequired; // PR（权限需求）：N/L/H
    private String cvssUserInteraction; // UI（用户交互）：N/R
    private List<String> cvssImpact; // VC/VI/VA（影响范围）：数组，如["C", "I", "A"]表示机密性、完整性、可用性都高

    /**
     * 置信度信息
     */
    private Map<String, Double> confidence; // 各字段的置信度

    /**
     * 无效字段（需要用户确认）
     */
    private List<String> invalidTags; // 无效标签列表
    private String invalidVulnerabilityType; // 无效漏洞类型
}
