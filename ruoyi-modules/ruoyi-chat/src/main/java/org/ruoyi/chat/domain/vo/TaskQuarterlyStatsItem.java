package org.ruoyi.chat.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务耗时统计项
 *
 * @author ruoyi
 */
@Data
public class TaskQuarterlyStatsItem {
    /**
     * 季度标识，如 Q1、Q2
     */
    private String quarter;

    /**
     * 该季度总任务量
     */
    private Integer total;

    /**
     * 该季度成功任务数
     */
    private Integer success;

    /**
     * 该季度失败任务数
     */
    private Integer fail;

    /**
     * 该季度成功率（小数形式，如 0.8 对应 80%）
     */
    private Double successRate;

    /**
     * 该季度失败率（小数形式，如 0.2 对应 20%）
     */
    private Double failRate;
}


