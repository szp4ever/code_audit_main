package org.ruoyi.knowledge.cwe.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.ruoyi.knowledge.cwe.domain.CweStandardType;

import java.io.Serial;
import java.io.Serializable;

/**
 * CWE 标准分类类型视图对象 cwe_standard_type
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@ExcelIgnoreUnannotated
public class CweStandardTypeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "标准类型代码")
    private String typeCode;

    @ExcelProperty(value = "标准类型名称")
    private String typeName;

    private String description;

    @ExcelProperty(value = "版本")
    private String version;

    private Integer sortOrder;
}
