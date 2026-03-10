package org.ruoyi.generator.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;

import java.io.Serializable;

/**
 * 数据模型业务对象 SchemaBo
 *
 * @author ruoyi
 * @date 2024-01-01
 */
@Data
public class SchemaBo implements Serializable {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 分组ID
     */
    private Long schemaGroupId;

    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 表名
     */
    @NotBlank(message = "表名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String tableName;

    /**
     * 备注
     */
    private String remark;

}