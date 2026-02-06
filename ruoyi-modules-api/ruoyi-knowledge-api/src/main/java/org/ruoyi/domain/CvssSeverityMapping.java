package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * CVSS 严重性等级映射对象 cvss_severity_mapping
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cvss_severity_mapping")
public class CvssSeverityMapping extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 定性等级（none/low/medium/high/critical）
     */
    private String severityLevel;

    /**
     * 最小分数（包含）
     */
    private BigDecimal scoreMin;

    /**
     * 最大分数（包含）
     */
    private BigDecimal scoreMax;

    /**
     * CVSS 版本号
     */
    private String cvssVersion;

    /**
     * 等级说明
     */
    private String description;
}
