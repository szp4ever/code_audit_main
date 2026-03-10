package org.ruoyi.knowledge.curation.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 知识库存储监控统计
 *
 * 设计原则：只展示真实可计算的数据，不依赖任何外部容量配置或硬编码上限。
 * 移除字段：totalCapacityBytes / remainingBytes / usagePercent / remainingPercent /
 *           alertLevel / runwayDays / estimatedExhaustDate
 */
@Data
public class KnowledgeStorageStatsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 知识库ID */
    private String kid;

    // ── 存储用量 ──────────────────────────────────────────────
    /** 已用空间（字节，来自 knowledge_info.data_size） */
    private Long usedBytes;

    // ── 资产数量 ──────────────────────────────────────────────
    /** 条目数 */
    private Integer itemCount;

    /** 片段数 */
    private Integer fragmentCount;

    /** 文档数 */
    private Integer attachCount;

    // ── 增长趋势 ──────────────────────────────────────────────
    /** 近7日净增长（字节，估算） */
    private Long growth7dBytes;

    /** 日均增长（字节，估算） */
    private Long avgDailyGrowthBytes;

    // ── 更新活跃度 ────────────────────────────────────────────
    /** 今日更新条目数 */
    private Integer todayUpdates;

    /** 近7天更新条目数 */
    private Integer weekUpdates;

    /** 近30天更新条目数 */
    private Integer monthUpdates;

    /** 日均更新条目数 */
    private Double avgDailyUpdates;

    // ── 时间序列 ──────────────────────────────────────────────
    /** 近30天按日存储增长（文档新增数） */
    private List<DailyCountPointVo> storageGrowth = new ArrayList<>();

    /** 近30天按日更新频率（条目更新数） */
    private List<DailyCountPointVo> updateFrequency = new ArrayList<>();

    /** 近90天按日更新频率（用于热力图） */
    private List<DailyCountPointVo> updateFrequency90d = new ArrayList<>();
}
