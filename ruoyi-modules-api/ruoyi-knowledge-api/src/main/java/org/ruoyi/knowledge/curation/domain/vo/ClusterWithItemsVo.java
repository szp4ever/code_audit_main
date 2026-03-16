package org.ruoyi.knowledge.curation.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * CWE聚类与条目关联视图对象
 */
@Data
public class ClusterWithItemsVo {
    private Long id;
    private Integer clusterId;
    private String clusterMethod;
    private String clusterName;
    private String clusterNameZh;
    private String clusterDescription;
    private String clusterDescriptionZh;
    private List<String> cwes;
    private int count;
    private long itemCount;
}
