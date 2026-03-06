package org.ruoyi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 导出预览VO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportPreviewVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 样本数据（前5-10条）
     */
    private List<Map<String, Object>> sampleData;

    /**
     * 总数据量
     */
    private Long totalCount;

    /**
     * 选中的字段信息
     */
    private List<FieldInfoVo> selectedFields;

    /**
     * 预计文件大小（字节）
     */
    private Long estimatedFileSize;

    /**
     * 预计耗时（秒）
     */
    private Integer estimatedTime;

    /**
     * PDF预览HTML（可选）
     */
    private String previewHtml;

    /**
     * PDF格式类型：table（表格格式）或 report（报告格式）
     */
    private String pdfFormatType;
}
