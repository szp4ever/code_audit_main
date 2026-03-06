package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * CWE 标准漏洞类型参考对象 cwe_reference
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cwe_reference")
public class CweReference extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 创建部门（cwe_reference表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createDept;

    /**
     * 创建者（cwe_reference表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createBy;

    /**
     * 更新者（cwe_reference表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long updateBy;

    /**
     * CWE 编号（如 CWE-89）
     */
    private String cweId;

    /**
     * 英文名称
     */
    private String nameEn;

    /**
     * 中文名称（通过翻译生成，可人工校正）
     */
    private String nameZh;

    /**
     * 弱点抽象层级（Base/Variant/Class/Compound 等）
     */
    private String weaknessAbstraction;

    /**
     * 状态（Draft/Incomplete/Stable/Deprecated 等）
     */
    private String status;

    /**
     * 英文描述
     */
    private String descriptionEn;

    /**
     * 中文描述（通过翻译生成，可人工校正）
     */
    private String descriptionZh;

    /**
     * 原始 CSV 行的结构化 JSON（便于保留完整信息）
     */
    private String rawJson;

    /**
     * 原始 CSV 行的中文翻译 JSON（所有字段的中文版本）
     */
    private String rawJsonZh;

    /**
     * 租户ID（预留，通常为 0）
     */
    private Long tenantId;
}
