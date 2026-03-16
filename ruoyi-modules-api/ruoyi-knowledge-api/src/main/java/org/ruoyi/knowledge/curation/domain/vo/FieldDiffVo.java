package org.ruoyi.knowledge.curation.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 单个字段的 diff 结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDiffVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段名（英文，如 title / description / fixSolution）
     */
    private String fieldName;

    /**
     * 字段标签（中文，如 标题 / 问题描述 / 修复方案）
     */
    private String fieldLabel;

    /**
     * 旧值
     */
    private String oldValue;

    /**
     * 新值
     */
    private String newValue;

    /**
     * 变更类型：added / removed / modified / unchanged
     */
    private String diffType;
}
