package org.ruoyi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字段信息VO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段键
     */
    private String key;

    /**
     * 字段标签
     */
    private String label;

    /**
     * 字段类型：base/expanded/dictConverted/parsed
     */
    private String type;

    /**
     * 父字段（如果是展开字段）
     */
    private String parentField;
}
