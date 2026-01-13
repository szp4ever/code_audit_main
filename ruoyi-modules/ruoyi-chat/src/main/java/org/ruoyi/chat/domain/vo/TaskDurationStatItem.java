package org.ruoyi.chat.domain.vo;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 任务耗时统计项
 *
 * @author ruoyi
 */
@Data
public class TaskDurationStatItem implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日期（格式根据timeRange不同：day-YYYY-MM-DD, hour-YYYY-MM-DD HH:00:00, week-YYYY-WW）
     */
    private String date;

    /**
     * 平均耗时（秒）
     */
    private Double avgDuration;

    /**
     * 任务数量
     */
    private Integer count;
}


