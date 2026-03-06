package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.KnowledgeFavorite;

/**
 * 知识收藏业务对象 knowledge_favorite
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KnowledgeFavorite.class, reverseConvertGenerate = false)
public class KnowledgeFavoriteBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = {AddGroup.class})
    private Long userId;

    /**
     * 知识条目UUID
     */
    @NotBlank(message = "知识条目UUID不能为空", groups = {AddGroup.class})
    private String itemUuid;

    /**
     * 知识库ID
     */
    private String kid;
}
