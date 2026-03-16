package org.ruoyi.knowledge.ingestion.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;

/**
 * 知识库附件业务对象 knowledge_attach
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeAttachBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 知识库ID
     */
    @NotBlank(message = "知识库ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String kid;

    /**
     * 文档ID
     */
    @NotBlank(message = "文档ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String docId;

    /**
     * 文档名称
     */
    @NotBlank(message = "文档名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String docName;

    /**
     * 文档类型
     */
    @NotBlank(message = "文档类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String docType;

    /**
     * 文档内容
     */
    @NotBlank(message = "文档内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String remark;

    /**
     * 对象存储主键
     */
    @NotNull(message = "对象存储主键不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long ossId;


    /**
     * 创建时间范围筛选：开始时间
     */
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTimeStart;

    /**
     * 创建时间范围筛选：结束时间
     */
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTimeEnd;

    /**
     * 创建人筛选（支持多选）
     */
    private java.util.List<String> createByList;

    /**
     * 关联条目数量最小值（筛选用）
     */
    private Integer itemCountMin;

    /**
     * 关联条目数量最大值（筛选用）
     */
    private Integer itemCountMax;

    /**
     * 是否包含未完成的附件（true-包含，false/null-不包含，默认不包含）
     */
    private Boolean includeIncomplete;

}
