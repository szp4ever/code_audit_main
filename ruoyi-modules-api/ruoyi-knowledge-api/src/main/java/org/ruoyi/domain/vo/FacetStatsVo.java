package org.ruoyi.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 分面统计视图对象
 */
@Data
public class FacetStatsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Map<String, Long> severities = new HashMap<>();

    private Map<String, Long> languages = new HashMap<>();

    private Map<String, Long> statuses = new HashMap<>();

    private Map<String, Long> vulnerabilityTypes = new HashMap<>();

    private Map<String, Long> tags = new HashMap<>();

    private Map<String, Long> cvssAttackVector = new HashMap<>();

    private Map<String, Long> cvssAttackComplexity = new HashMap<>();

    private Map<String, Long> cvssPrivilegesRequired = new HashMap<>();

    private Map<String, Long> cvssUserInteraction = new HashMap<>();

    private Map<String, Long> cvssScope = new HashMap<>();

    private Map<String, Long> cvssConfidentiality = new HashMap<>();

    private Map<String, Long> cvssIntegrity = new HashMap<>();

    private Map<String, Long> cvssAvailability = new HashMap<>();
}
