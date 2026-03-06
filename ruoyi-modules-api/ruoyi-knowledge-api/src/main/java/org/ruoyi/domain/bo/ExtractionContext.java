package org.ruoyi.domain.bo;

import lombok.Data;
import org.ruoyi.domain.CweReference;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 提取上下文：包含所有约束信息
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Data
public class ExtractionContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 预定义选项
     */
    private List<String> availableTags; // 所有可用标签
    private List<CweReference> availableVulnerabilityTypes; // 所有漏洞类型
    private List<String> availableLanguages; // 所有语言
    private List<String> availableSeverities; // 所有风险等级

    /**
     * 知识库上下文
     */
    private String kid; // 知识库ID
    private String knowledgeBaseName; // 知识库名称
    private String knowledgeBaseCategory; // 知识库分类

    /**
     * 模型配置
     */
    private String modelName; // LLM模型名称
    private Double temperature; // 温度参数
    private Integer maxTokens; // 最大token数
}
