package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeAttach;

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
@AutoMapper(target = KnowledgeAttach.class)
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
     * 拆解图片状态10未开始，20进行中，30已完成
     */
    @ExcelProperty(value = "拆解图片状态10未开始，20进行中，30已完成")
    private Integer picStatus;

    /**
     * 分析图片状态10未开始，20进行中，30已完成
     */
    @ExcelProperty(value = "分析图片状态10未开始，20进行中，30已完成")
    private Integer picAnysStatus;

    /**
     * 写入向量数据库状态10未开始，20进行中，30已完成
     */
    @ExcelProperty(value = "写入向量数据库状态10未开始，20进行中，30已完成")
    private Integer vectorStatus;

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
