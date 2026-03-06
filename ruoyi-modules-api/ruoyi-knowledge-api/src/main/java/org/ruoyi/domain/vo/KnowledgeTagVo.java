package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeTag;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识标签视图对象 knowledge_tag
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KnowledgeTag.class)
public class KnowledgeTagVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 标签名称
     */
    @ExcelProperty(value = "标签名称")
    private String tagName;

    /**
     * 标签类型
     */
    @ExcelProperty(value = "标签类型")
    private String tagType;

    /**
     * 标签分类
     */
    @ExcelProperty(value = "标签分类")
    private String tagCategory;

    /**
     * 标签描述
     */
    @ExcelProperty(value = "标签描述")
    private String description;

    /**
     * 使用次数
     */
    @ExcelProperty(value = "使用次数")
    private Integer usageCount;

    /**
     * 删除标志
     */
    private String delFlag;

    /**
     * 租户ID
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
