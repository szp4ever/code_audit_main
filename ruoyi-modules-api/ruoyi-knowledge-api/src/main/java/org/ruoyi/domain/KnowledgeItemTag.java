package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 知识条目标签关联对象 knowledge_item_tag
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_item_tag")
public class KnowledgeItemTag extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 知识条目UUID（关联 knowledge_item.item_uuid）
     */
    private String itemUuid;

    /**
     * 标签ID（关联 knowledge_tag.id）
     */
    private Long tagId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建部门（knowledge_item_tag表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createDept;

    /**
     * 创建者（knowledge_item_tag表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createBy;

    /**
     * 更新者（knowledge_item_tag表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long updateBy;

    /**
     * 更新时间（knowledge_item_tag表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private java.util.Date updateTime;
}
