package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.CweImpactMapping;

import java.io.Serial;
import java.io.Serializable;

/**
 * CWE 影响类型映射视图对象 cwe_impact_mapping
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CweImpactMapping.class)
public class CweImpactMappingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "CWE编号")
    private String cweId;

    @ExcelProperty(value = "影响类型")
    private String impactType;
}
