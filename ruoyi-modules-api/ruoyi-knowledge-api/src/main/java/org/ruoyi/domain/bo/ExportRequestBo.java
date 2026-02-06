package org.ruoyi.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 导出请求BO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
public class ExportRequestBo extends ExportPreviewRequestBo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文件名（可选）
     */
    private String fileName;
}
