package org.ruoyi.knowledge.curation.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 知识条目-片段关联表（N:M）
 * <p>
 * 一个条目可以引用多个片段作为"证据"，一个片段也可以被多个条目引用。
 * 替代原来 KnowledgeFragment.itemUuid 的 1:1 直接绑定。
 *
 * @author refactored
 */
@Data
@TableName("knowledge_item_fragment")
public class KnowledgeItemFragment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 知识条目 UUID */
    private String itemUuid;

    /** 片段 ID */
    private Long fragmentId;

    /** 关联相关度评分（0-1，AI 匹配时填写，人工关联时为 null） */
    private Double relevanceScore;

    /** 关联来源：manual（人工）/ ai（AI 建议） */
    private String createdBy;

    /** 创建时间 */
    private Date createTime;
}
