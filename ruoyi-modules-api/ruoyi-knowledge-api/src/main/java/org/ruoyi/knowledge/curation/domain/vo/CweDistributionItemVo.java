package org.ruoyi.knowledge.curation.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 单个 CWE 漏洞类型分布项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CweDistributionItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * CWE 编号（如 CWE-89）
     */
    private String cweId;

    /**
     * CWE 英文名称
     */
    private String cweName;

    /**
     * CWE 中文名称
     */
    private String cweNameZh;

    /**
     * 条目数量
     */
    private Long count;

    /**
     * 占比（0-100）
     */
    private Double percentage;

    /**
     * 所属聚类名称（可选）
     */
    private String clusterName;
}
