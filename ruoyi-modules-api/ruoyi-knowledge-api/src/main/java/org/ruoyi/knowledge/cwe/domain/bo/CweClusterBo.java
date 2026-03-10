package org.ruoyi.knowledge.cwe.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.knowledge.cwe.domain.CweCluster;

/**
 * CWE 聚类业务对象 cwe_cluster
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CweClusterBo extends BaseEntity {

    private Long id;

    private Integer clusterId;

    private String clusterMethod;

    private String clusterNameZh;

    private String clusterNameEn;

    private String categoryCode;
}
