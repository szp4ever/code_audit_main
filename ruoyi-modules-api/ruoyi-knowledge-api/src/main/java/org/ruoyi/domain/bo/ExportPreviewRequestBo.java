package org.ruoyi.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 导出预览请求BO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
public class ExportPreviewRequestBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 导出格式：pdf或excel
     */
    private String format;

    /**
     * 导出范围：selected/currentPage/all
     */
    private String exportRange;

    /**
     * 选中的条目UUID列表（当exportRange为selected时使用）
     */
    private List<String> itemUuids;

    /**
     * 当前页码（当exportRange为currentPage时使用）
     */
    private Integer pageNum;

    /**
     * 每页大小（当exportRange为currentPage时使用）
     */
    private Integer pageSize;

    /**
     * 选中的字段列表
     */
    private List<String> selectedFields;

    /**
     * 展开的关联字段
     */
    private Map<String, List<String>> expandedFields;

    /**
     * 字段格式配置（key为字段名，value为格式：id_only/id_name/full）
     * 用于多值字段（vulnerabilityTypes、tags）的格式选择
     */
    private Map<String, String> fieldFormats;

    /**
     * 字段顺序
     */
    private List<String> fieldOrder;

    /**
     * PDF选项
     */
    private PdfOptionsBo pdfOptions;

    /**
     * Excel选项
     */
    private ExcelOptionsBo excelOptions;

    /**
     * 筛选条件（当exportRange为all时使用）
     */
    private KnowledgeItemBo filters;
}
