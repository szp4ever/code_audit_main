package org.ruoyi.domain.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeFragment;

import java.io.Serial;
import java.io.Serializable;


/**
 * 知识片段视图对象 knowledge_fragment
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KnowledgeFragment.class)
public class KnowledgeFragmentVo implements Serializable {

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
     * 知识条目UUID（关联 knowledge_item.item_uuid）
     */
    @ExcelProperty(value = "条目UUID")
    private String itemUuid;

    /**
     * 文档ID
     */
    @ExcelProperty(value = "文档ID")
    private String docId;

    /**
     * 知识片段ID
     */
    @ExcelProperty(value = "知识片段ID")
    private String fid;

    /**
     * 片段索引下标
     */
    @ExcelProperty(value = "片段索引下标")
    private Long idx;

    /**
     * 文档内容
     */
    @ExcelProperty(value = "文档内容")
    private String content;

    /**
     * 向量库UUID（Weaviate返回的向量ID，删除时必须使用此ID）
     */
    private String vectorId;

    /**
     * 漏洞类型（冗余字段，用于快速过滤）
     */
    private String vulnerabilityType;

    /**
     * 适用语言（冗余字段）
     */
    private String language;

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
     * 附件名称（关联查询）
     */
    private String docName;

    /**
     * 附件ID（关联查询，用于跳转）
     */
    private Long attachId;

    /**
     * 附件类型（关联查询）
     */
    private String docType;

    /**
     * 关联知识条目标题（关联查询，如果条目不存在则为null）
     */
    private String itemTitle;

}
