package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.CweReference;

import java.io.Serial;
import java.io.Serializable;

/**
 * CWE 标准漏洞类型参考视图对象 cwe_reference
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CweReference.class)
public class CweReferenceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * CWE 编号
     */
    @ExcelProperty(value = "CWE编号")
    private String cweId;

    /**
     * 英文名称
     */
    @ExcelProperty(value = "英文名称")
    private String nameEn;

    /**
     * 中文名称
     */
    @ExcelProperty(value = "中文名称")
    private String nameZh;

    /**
     * 弱点抽象层级
     */
    @ExcelProperty(value = "抽象层级")
    private String weaknessAbstraction;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 英文描述
     */
    private String descriptionEn;

    /**
     * 中文描述
     */
    private String descriptionZh;

    /**
     * 原始 CSV 行的结构化 JSON（便于保留完整信息）
     */
    private String rawJson;

    /**
     * 原始 CSV 行的中文翻译 JSON（所有字段的中文版本）
     */
    private String rawJsonZh;

    /**
     * 租户ID（预留，通常为 0）
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private java.util.Date updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 更新人
     */
    private Long updateBy;
}
