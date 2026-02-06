package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 知识收藏对象 knowledge_favorite
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_favorite")
public class KnowledgeFavorite extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 知识条目UUID（关联 knowledge_item.item_uuid）
     */
    private String itemUuid;

    /**
     * 知识库ID（冗余字段，用于快速查询）
     */
    private String kid;

    /**
     * 租户ID
     */
    private Long tenantId;
}
