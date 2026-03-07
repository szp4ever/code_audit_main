package org.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户操作热力图数据项
 *
 * @author ruoyi
 */
@Data
public class UserOperationHeatmapItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 时间段（格式：YYYY-MM-DD）
     */
    private String timeSlot;

    /**
     * 小时（0-23）
     */
    private Integer hour;

    /**
     * 操作次数
     */
    private Integer count;

    /**
     * 模块标识（如：chat, task, knowledge等）
     */
    private String module;
}
