package org.ruoyi.knowledge.curation.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 两个版本之间的 diff 结果
 */
@Data
public class VersionDiffVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 条目 UUID
     */
    private String itemUuid;

    /**
     * 起始版本号
     */
    private Integer fromVersion;

    /**
     * 目标版本号
     */
    private Integer toVersion;

    /**
     * 起始版本变更人
     */
    private String fromChangedByName;

    /**
     * 目标版本变更人
     */
    private String toChangedByName;

    /**
     * 起始版本变更时间
     */
    private Date fromChangedAt;

    /**
     * 目标版本变更时间
     */
    private Date toChangedAt;

    /**
     * 变更字段列表
     */
    private List<FieldDiffVo> diffs = new ArrayList<>();

    /**
     * 变更字段数量
     */
    private Integer changedFieldCount;

    /**
     * 变更字段名称列表（便于前端快速展示标签）
     */
    private List<String> changedFieldLabels = new ArrayList<>();
}
