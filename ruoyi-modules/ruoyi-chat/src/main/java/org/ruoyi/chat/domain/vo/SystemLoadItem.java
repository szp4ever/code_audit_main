package org.ruoyi.chat.domain.vo;

/**
 * 系统负载数据项
 */
public class SystemLoadItem {
    /**
     * 时间戳，ISO 8601格式
     */
    private String timestamp;

    /**
     * CPU使用率，百分比，保留一位小数
     */
    private Double cpuUsage;

    /**
     * 内存使用率，百分比，保留一位小数
     */
    private Double memoryUsage;

    /**
     * GPU使用率，百分比，保留一位小数（可选字段）
     */
    private Double gpuUsage;

    // getter and setter
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getGpuUsage() {
        return gpuUsage;
    }

    public void setGpuUsage(Double gpuUsage) {
        this.gpuUsage = gpuUsage;
    }
}