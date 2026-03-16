package org.ruoyi.knowledge.ingestion.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 知识库附件视图对象 knowledge_attach
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Data
@ExcelIgnoreUnannotated
public class KnowledgeAttachVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 知识库ID
     */
    @ExcelProperty(value = "知识库ID")
    private String kid;

    /**
     * 文档ID
     */
    @ExcelProperty(value = "文档ID")
    private String docId;

    /**
     * 文档名称
     */
    @ExcelProperty(value = "文档名称")
    private String docName;

    /**
     * 文档类型
     */
    @ExcelProperty(value = "文档类型")
    private String docType;

    /**
     * 文档内容
     */
    @ExcelProperty(value = "文档内容")
    private String content;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 对象存储主键
     */
    @ExcelProperty(value = "对象存储主键")
    private Long ossId;

    /**
     * 文件大小（字节），从 MinIO 获取
     */
    private Long fileSize;

    /**
     * 片段数量，通过 knowledge_fragment 表统计
     */
    private Integer fragmentCount;

    /**
     * 关联知识条目UUID，通过 knowledge_fragment.item_uuid 查询
     */
    private String itemUuid;

    /**
     * 关联知识条目标题，通过 knowledge_item 表查询
     */
    private String itemTitle;

    /**
     * 关联的知识条目UUID列表（支持多个条目）
     */
    private List<String> itemUuids;

    /**
     * 关联的知识条目标题列表（支持多个条目）
     */
    private List<String> itemTitles;

    /**
     * 相似度匹配结果（如果启用了相似度匹配）
     */
    private MatchResultVo matchResult;

    /**
     * 处理任务ID（关联knowledge_attach_process.id）
     * 基于LLM与状态改革设计文档 v1.0
     */
    private String processId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
