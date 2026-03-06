package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.CweHierarchy;

import java.io.Serial;
import java.io.Serializable;

/**
 * CWE 层级关系视图对象 cwe_hierarchy
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CweHierarchy.class)
public class CweHierarchyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "CWE编号")
    private String cweId;

    @ExcelProperty(value = "父CWE编号")
    private String parentCweId;

    @ExcelProperty(value = "关系类型")
    private String relationshipType;
}
