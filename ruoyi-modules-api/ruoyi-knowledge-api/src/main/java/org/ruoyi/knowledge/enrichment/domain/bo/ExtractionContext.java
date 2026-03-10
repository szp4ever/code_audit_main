package org.ruoyi.knowledge.enrichment.domain.bo;

import lombok.Data;
import org.ruoyi.knowledge.cwe.domain.CweReference;

import java.util.List;

/**
 * 知识条目提取上下文
 */
@Data
public class ExtractionContext {
    private String kid;
    private String knowledgeBaseName;
    private String knowledgeBaseCategory;
    private String modelName;
    private List<String> availableTags;
    private List<CweReference> availableVulnerabilityTypes;
    private List<String> availableLanguages;
    private List<String> availableSeverities;
}
