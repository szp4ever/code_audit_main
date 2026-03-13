package org.ruoyi.chat.domain.vo;

/**
 * 活跃用户分布项
 */
public class ActiveUserDistributionItem {
    /**
     * 时间段，如"0-2点"
     */
    private String timeSlot;

    /**
     * 用户数量
     */
    private Integer count;

    // getter and setter
    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}