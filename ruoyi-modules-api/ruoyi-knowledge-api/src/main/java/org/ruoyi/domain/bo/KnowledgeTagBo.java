package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.KnowledgeTag;

/**
 * 知识标签业务对象 knowledge_tag
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KnowledgeTag.class, reverseConvertGenerate = false)
public class KnowledgeTagBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String tagName;

    /**
     * 标签类型
     */
    private String tagType;

    /**
     * 标签分类
     */
    private String tagCategory;

    /**
     * 标签描述
     */
    private String description;
}
