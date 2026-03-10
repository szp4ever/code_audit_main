package org.ruoyi.knowledge.curation.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CWE × Severity 交叉矩阵单元格
 */
@Data
public class CweSeverityCellVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** CWE ID */
    private String cweId;

    /** CWE 名称（中文） */
    private String cweName;

    /** 严重级别 */
    private String severity;

    /** 数量 */
    private Integer count;
}
