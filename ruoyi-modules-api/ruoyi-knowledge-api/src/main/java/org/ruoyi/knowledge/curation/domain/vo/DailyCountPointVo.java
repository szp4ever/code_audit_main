package org.ruoyi.knowledge.curation.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 按日计数点（时间序列）
 */
@Data
public class DailyCountPointVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日期（yyyy-MM-dd）
     */
    private String date;

    /**
     * 数量
     */
    private Long count;
}
