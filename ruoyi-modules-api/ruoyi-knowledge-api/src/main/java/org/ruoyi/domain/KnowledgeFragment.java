package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 知识片段对象 knowledge_fragment
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_fragment")
public class KnowledgeFragment extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 知识库ID
     */
    private String kid;

    /**
     * 知识条目UUID（关联 knowledge_item.item_uuid）
     */
    private String itemUuid;

    /**
     * 文档ID
     */
    private String docId;

    /**
     * 知识片段ID
     */
    private String fid;

    /**
     * 片段索引下标
     */
    private Integer idx;

    /**
     * 文档内容
     */
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
    private String remark;

    /**
     * 删除标志（0-存在 2-删除）
     */
    @TableField("del_flag")
    private String delFlag;

}
