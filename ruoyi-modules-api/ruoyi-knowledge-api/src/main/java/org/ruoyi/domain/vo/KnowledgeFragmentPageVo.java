package org.ruoyi.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.page.TableDataInfo;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * 知识片段分页视图对象（包含分面统计）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeFragmentPageVo extends TableDataInfo<KnowledgeFragmentVo> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分面统计（附件分布统计）
     * key: 附件名称, value: 片段数量
     */
    private Map<String, Long> facetStats;

    public KnowledgeFragmentPageVo() {
        this.facetStats = new HashMap<>();
    }
}
