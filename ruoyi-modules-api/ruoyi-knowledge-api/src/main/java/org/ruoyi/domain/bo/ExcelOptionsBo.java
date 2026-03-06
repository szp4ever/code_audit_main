package org.ruoyi.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Excel导出选项BO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
public class ExcelOptionsBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 包含筛选器
     */
    private Boolean includeFilter;

    /**
     * 冻结表头
     */
    private Boolean freezeHeader;

    /**
     * 条件格式
     */
    private Boolean conditionalFormatting;
}
