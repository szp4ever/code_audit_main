package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.CweCluster;

/**
 * CWE 聚类业务对象 cwe_cluster
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CweCluster.class, reverseConvertGenerate = false)
public class CweClusterBo extends BaseEntity {

    private Long id;

    private Integer clusterId;

    private String clusterMethod;

    private String clusterNameZh;

    private String clusterNameEn;

    private String categoryCode;
}
