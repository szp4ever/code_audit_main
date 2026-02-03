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
public class TaskMonthlyCountItem implements Serializable {

    /**
     * 年月，格式：YYYY-MM（如 2024-01）
     */
    private String month;

    /**
     * 该月的任务数量
     */
    private Integer count;

    /**
     * 该月的任务数量
     */
    private String type;
}



