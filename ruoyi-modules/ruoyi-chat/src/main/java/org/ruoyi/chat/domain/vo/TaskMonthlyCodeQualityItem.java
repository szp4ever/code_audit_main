package org.ruoyi.chat.domain.vo;

import lombok.Data;

/**
 * 月度代码质量统计项（和前端MonthlyCodeQuality完全对齐）
 */
@Data
public class TaskMonthlyCodeQualityItem {
    /**
     * 月份（格式：YYYY-MM）
     */
    private String month;

    /**
     * 代码质量综合评分（范围：0-100）
     */
    private Double overallScore;
}