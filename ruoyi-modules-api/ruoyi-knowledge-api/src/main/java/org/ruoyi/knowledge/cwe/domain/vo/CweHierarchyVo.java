package org.ruoyi.knowledge.cwe.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.ruoyi.knowledge.cwe.domain.CweHierarchy;

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
