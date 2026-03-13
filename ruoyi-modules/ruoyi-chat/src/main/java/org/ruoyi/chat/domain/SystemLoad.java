package org.ruoyi.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统负载监控实体
 */
@Data
@TableName("system_load")
public class SystemLoad {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 时间戳
     */
    private Date timestamp;

    /**
     * CPU使用率
     */
    private Double cpuUsage;

    /**
     * 内存使用率
     */
    private Double memoryUsage;

    /**
     * GPU使用率
     */
    private Double gpuUsage;

    /**
     * 创建时间
     */
    private Date createTime;
}