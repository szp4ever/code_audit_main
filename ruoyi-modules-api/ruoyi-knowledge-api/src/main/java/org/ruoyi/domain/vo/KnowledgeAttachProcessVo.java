package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeAttachProcess;

import java.io.Serial;
import java.io.Serializable;

/**
 * 附件处理状态视图对象 knowledge_attach_process
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KnowledgeAttachProcess.class)
public class KnowledgeAttachProcessVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 附件ID
     */
    @ExcelProperty(value = "附件ID")
    private Long attachId;

    /**
     * 文档ID
     */
    @ExcelProperty(value = "文档ID")
    private String docId;

    /**
     * 当前状态
     */
    @ExcelProperty(value = "当前状态")
    private String currentStatus;

    /**
     * 状态数据（JSON格式）
     */
    @ExcelProperty(value = "状态数据")
    private String statusData;

    /**
     * 总进度（0-100）
     */
    @ExcelProperty(value = "总进度")
    private Integer progress;

    /**
     * 错误信息
     */
    @ExcelProperty(value = "错误信息")
    private String errorMessage;

    /**
     * 是否被锁定（Redis分布式锁）
     */
    private Boolean locked;

    /**
     * 锁定提示信息
     */
    private String lockMessage;

    /**
     * 处理速率（实际处理的数据量/秒，如片段/秒、条目/秒等）
     */
    private Double processingSpeed;

    /**
     * 速率单位（如"片段/秒"、"条目/秒"等）
     */
    private String speedUnit;

    /**
     * 预计剩余时间（秒）
     */
    private Long eta;
}
