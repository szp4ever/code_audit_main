package org.ruoyi.generator.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据模型分组视图对象 SchemaGroupVo
 *
 * @author ruoyi
 * @date 2024-01-01
 */
@Data
public class SchemaGroupVo implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组编码
     */
    private String code;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

}