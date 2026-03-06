package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 知识标签对象 knowledge_tag
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_tag")
public class KnowledgeTag extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 标签名称（如：内存泄漏、IEC协议、TOD时间戳）
     */
    private String tagName;

    /**
     * 标签类型（system-系统标签、user-用户标签）
     */
    private String tagType;

    /**
     * 标签分类（用于分组）
     */
    private String tagCategory;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 使用次数
     */
    private Integer usageCount;

    /**
     * 删除标志
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 租户ID
     */
    private Long tenantId;
}
