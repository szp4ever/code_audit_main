package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.KnowledgeAttachProcess;

/**
 * 附件处理状态业务对象 knowledge_attach_process
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KnowledgeAttachProcess.class, reverseConvertGenerate = false)
public class KnowledgeAttachProcessBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 附件ID
     */
    private Long attachId;

    /**
     * 文档ID
     */
    private String docId;

    /**
     * 当前状态
     */
    private String currentStatus;

    /**
     * 状态数据（JSON格式）
     */
    private String statusData;

    /**
     * 总进度（0-100）
     */
    private Integer progress;

    /**
     * 错误信息
     */
    private String errorMessage;
}
