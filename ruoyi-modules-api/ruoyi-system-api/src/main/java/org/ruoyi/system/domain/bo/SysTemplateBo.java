package org.ruoyi.system.domain.bo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.system.domain.SysTemplate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "模板配置业务对象")
@AutoMapper(target = SysTemplate.class, reverseConvertGenerate = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SysTemplateBo extends BaseEntity {

    @Schema(description = "模板ID")
    @NotNull(message = "模板ID不能为空", groups = { EditGroup.class })
    private Long templateId;

    @Schema(description = "模板名称")
    @NotBlank(message = "模板名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String templateName;

    @Schema(description = "模板编码")
    @NotBlank(message = "模板编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String templateCode;

    @Schema(description = "模板内容")
    private String templateContent;

    @Schema(description = "模板类型")
    @NotBlank(message = "模板类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String templateType;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件种类")
    private String fileKind;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;

    // --- [核心修改] 专门用于搜索的字段 ---

    /**
     * 创建人名称 (前端传来的搜索字符，String类型)
     * Service层会拿这个名字去 sys_user 表查 user_id
     */
    @Schema(description = "创建人名称搜索")
    private String createByName;

    /**
     * 更新人名称 (前端传来的搜索字符，String类型)
     */
    @Schema(description = "更新人名称搜索")
    private String updateByName;
}