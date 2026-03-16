package org.ruoyi.knowledge.enrichment.domain.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * LLM提取的知识条目结构化数据
 */
@Data
public class ExtractedItemData {
    private String title;
    private String summary;
    private String problemDescription;
    private String fixSolution;
    private String exampleCode;
    private String vulnerabilityType;
    private List<String> vulnerabilityTypes;
    private String language;
    private String severity;
    private List<String> tags;
    private String cvssAttackVector;
    private String cvssAttackComplexity;
    private String cvssPrivilegesRequired;
    private String cvssUserInteraction;
    private List<String> cvssImpact;
    private Map<String, Double> confidence;
}
