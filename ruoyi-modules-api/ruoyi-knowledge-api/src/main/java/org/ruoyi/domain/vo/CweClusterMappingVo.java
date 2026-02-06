package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.CweClusterMapping;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CWE 聚类映射视图对象 cwe_cluster_mapping
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CweClusterMapping.class)
public class CweClusterMappingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "CWE编号")
    private String cweId;

    @ExcelProperty(value = "聚类ID")
    private Integer clusterId;

    @ExcelProperty(value = "聚类方法")
    private String clusterMethod;

    private BigDecimal distanceToCenter;
}
