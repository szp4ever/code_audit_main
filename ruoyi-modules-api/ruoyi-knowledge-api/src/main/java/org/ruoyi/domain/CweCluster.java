package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * CWE 聚类对象 cwe_cluster
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cwe_cluster")
public class CweCluster extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 创建部门（cwe_cluster表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createDept;

    /**
     * 创建者（cwe_cluster表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long createBy;

    /**
     * 更新者（cwe_cluster表不存在此字段，标记为不存在）
     */
    @TableField(exist = false)
    private Long updateBy;

    private Integer clusterId;

    private String clusterMethod;

    private String clusterNameZh;

    private String clusterNameEn;

    private String categoryCode;

    private String description;

    private String keywords;

    private Integer cweCount;

    private BigDecimal silhouetteScore;

    private BigDecimal calinskiHarabaszScore;

    private String llmInterpretation;
}
