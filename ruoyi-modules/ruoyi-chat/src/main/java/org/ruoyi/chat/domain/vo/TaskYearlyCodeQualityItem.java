package org.ruoyi.chat.domain.vo;

import lombok.Data;

/**
 * 年度代码质量统计项（和前端YearlyCodeQuality完全对齐）
 */
@Data
public class TaskYearlyCodeQualityItem {
    /**
     * 年份（格式：YYYY）
     */
    private String year;

    /**
     * 代码质量综合评分（范围：0-100）
     */
    private Double overallScore;
}