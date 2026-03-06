package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.CweCluster;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CWE 聚类视图对象 cwe_cluster
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CweCluster.class)
public class CweClusterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "聚类ID")
    private Integer clusterId;

    @ExcelProperty(value = "聚类方法")
    private String clusterMethod;

    @ExcelProperty(value = "聚类中文名称")
    private String clusterNameZh;

    @ExcelProperty(value = "聚类英文名称")
    private String clusterNameEn;

    @ExcelProperty(value = "分类代码")
    private String categoryCode;

    private String description;

    private String keywords;

    @ExcelProperty(value = "CWE数量")
    private Integer cweCount;

    private BigDecimal silhouetteScore;

    private BigDecimal calinskiHarabaszScore;

    private String llmInterpretation;
}
