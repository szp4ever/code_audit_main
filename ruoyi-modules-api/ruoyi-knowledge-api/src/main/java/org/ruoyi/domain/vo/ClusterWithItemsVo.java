package org.ruoyi.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CWE Cluster包含条目信息的视图对象
 */
@Data
public class ClusterWithItemsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer clusterId;

    private String clusterMethod;

    private String clusterName;

    private String clusterNameZh;

    private String clusterDescription;

    private String clusterDescriptionZh;

    private List<String> cwes = new ArrayList<>();

    private Integer count;

    private Long itemCount;
}
