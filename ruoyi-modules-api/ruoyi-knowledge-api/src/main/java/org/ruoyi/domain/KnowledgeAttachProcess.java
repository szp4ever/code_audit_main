package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 附件处理状态对象 knowledge_attach_process
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_attach_process")
public class KnowledgeAttachProcess extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 附件ID（关联knowledge_attach.id）
     */
    private Long attachId;

    /**
     * 文档ID（关联knowledge_attach.doc_id）
     */
    private String docId;

    /**
     * 当前状态
     */
    private String currentStatus;

    /**
     * 状态数据（JSON格式，存储匹配结果、LLM生成结果等）
     * 注意：MySQL JSON类型在MyBatis-Plus中直接使用String存储，由Jackson自动序列化/反序列化
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
