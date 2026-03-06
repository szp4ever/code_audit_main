package org.ruoyi.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.page.TableDataInfo;

import java.io.Serial;
import java.util.List;

/**
 * 知识条目分页视图对象（包含分面统计）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeItemPageVo extends TableDataInfo<KnowledgeItemVo> {

    @Serial
    private static final long serialVersionUID = 1L;

    private FacetStatsVo facetStats;

    private List<ClusterWithItemsVo> groupedByClusters;
}
