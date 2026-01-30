package org.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板配置对象 sys_template
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_template")
// 取消 extends BaseEntity，防止类型冲突
public class SysTemplate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    @TableId(value = "template_id", type = IdType.AUTO)
    private Long templateId;

    /** 模板名称 */
    private String templateName;

    /** 模板编码 */
    private String templateCode;

    /** 模板内容 */
    private String templateContent;

    /** 模板类型 */
    private String templateType;

    /** 状态 */
    private String status;

    /** 备注 */
    private String remark;

    /** 文件路径 */
    private String filePath;

    /** 文件种类 (docx, markdown) */
    private String fileKind;

    /** 创建者 (Long类型) */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 (Long类型) */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 请求参数 (模拟 BaseEntity 的 params) */
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}