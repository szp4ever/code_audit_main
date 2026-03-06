package org.ruoyi.chat.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务耗时统计项
 *
 * @author ruoyi
 */
@Data
public class TaskYearlyCountItem implements Serializable {

    // 年份（如 "2023"），替换原有的 month 字段
    private String year;
    // 任务类型（与原有一致：code_standard_check 等）
    private String type;
    // 该年度该类型的任务数量
    private Integer count;
}



