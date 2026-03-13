package org.ruoyi.chat.domain.vo;

/**
 * 峰值时段分析项
 */
public class PeakTimeAnalysisItem {
    /**
     * 时间段起始小时，如"0"
     */
    private String hour;

    /**
     * 操作次数
     */
    private Integer count;

    // getter and setter
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}