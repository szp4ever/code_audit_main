package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.CweStandardMapping;

import java.io.Serial;
import java.io.Serializable;

/**
 * CWE 标准分类映射视图对象 cwe_standard_mapping
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CweStandardMapping.class)
public class CweStandardMappingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "CWE编号")
    private String cweId;

    @ExcelProperty(value = "标准类型代码")
    private String typeCode;

    @ExcelProperty(value = "标准条目代码")
    private String entryCode;

    @ExcelProperty(value = "标准条目名称")
    private String entryName;
}
