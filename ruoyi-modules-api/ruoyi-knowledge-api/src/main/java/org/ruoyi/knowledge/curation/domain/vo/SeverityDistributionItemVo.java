package org.ruoyi.knowledge.curation.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 按严重级别分布项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeverityDistributionItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 严重级别（Critical / High / Medium / Low / None）
     */
    private String severity;

    /**
     * 条目数量
     */
    private Long count;

    /**
     * 占比（0-100）
     */
    private Double percentage;

    /**
     * 该级别下 Top5 CWE 类型
     */
    private Map<String, Long> topCwes;
}
