package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * CWE 标准分类类型对象 cwe_standard_type
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cwe_standard_type")
public class CweStandardType extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 创建部门（cwe_standard_type表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createDept;

    /**
     * 创建者（cwe_standard_type表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createBy;

    /**
     * 更新者（cwe_standard_type表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long updateBy;

    private String typeCode;

    private String typeName;

    private String description;

    private String version;

    private Integer sortOrder;
}
