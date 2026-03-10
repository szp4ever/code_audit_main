package org.ruoyi.knowledge.curation.domain.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.ruoyi.common.excel.annotation.ExcelDictFormat;
import org.ruoyi.common.excel.convert.ExcelDictConvert;
import org.ruoyi.knowledge.curation.domain.KnowledgeInfo;

import java.io.Serial;
import java.io.Serializable;


/**
 * 知识库视图对象 knowledge_info
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Data
@ExcelIgnoreUnannotated
public class KnowledgeInfoVo implements Serializable {

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
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long uid;

    /**
     * 知识库名称
     */
    @ExcelProperty(value = "知识库名称")
    private String kname;

    /**
     * 是否公开知识库（0 否 1是）
     */
    @ExcelProperty(value = "是否公开知识库", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=,否=,1=是")
    private Integer share;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 知识库分类（用途类型）
     */
    @ExcelProperty(value = "分类", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "knowledge_category")
    private String category;

    /**
     * 占用字节数（所有知识条目内容的总大小）
     */
    @ExcelProperty(value = "存储大小")
    @TableField(exist = false)
    private Long dataSize;

    /**
     * 知识条目数量
     */
    @ExcelProperty(value = "条目数")
    @TableField(exist = false)
    private Integer itemCount;

    /**
     * 知识片段数量
     */
    @ExcelProperty(value = "片段数")
    @TableField(exist = false)
    private Integer fragmentCount;

    /**
     * 文档数量（附件数）
     */
    @ExcelProperty(value = "文档数")
    @TableField(exist = false)
    private Integer attachCount;

    /**
     * 知识分隔符
     */
    @ExcelProperty(value = "知识分隔符")
    private String knowledgeSeparator;

    /**
     * 提问分隔符
     */
    @ExcelProperty(value = "提问分隔符")
    private String questionSeparator;

    /**
     * 重叠字符数
     */
    @ExcelProperty(value = "重叠字符数")
    private Integer overlapChar;

    /**
     * 知识库中检索的条数
     */
    @ExcelProperty(value = "知识库中检索的条数")
    private Integer retrieveLimit;

    /**
     * 文本块大小
     */
    @ExcelProperty(value = "文本块大小")
    private Integer textBlockSize;

    /**
     * 向量库模型名称
     */
    private String vectorModelName;

    /**
     * 向量化模型id
     */
    private Long embeddingModelId;

    /**
     * 向量化模型名称
     */
    private String embeddingModelName;


    /**
     * 系统提示词
     */
    private String systemPrompt;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private java.util.Date updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 当前用户是否有编辑权限（管理员或创建者）
     */
    private Boolean canEdit;

}
