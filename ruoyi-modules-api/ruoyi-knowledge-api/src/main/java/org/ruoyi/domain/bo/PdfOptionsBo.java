package org.ruoyi.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * PDF导出选项BO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
public class PdfOptionsBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 包含页眉页脚
     */
    private Boolean includeHeaderFooter;

    /**
     * 包含目录
     */
    private Boolean includeTOC;

    /**
     * 代码高亮
     */
    private Boolean codeHighlight;
}
