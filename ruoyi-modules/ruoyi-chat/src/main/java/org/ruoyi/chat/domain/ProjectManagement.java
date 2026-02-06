package org.ruoyi.chat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;
import java.util.List;

/**
 * 项目管理对象 project_management
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project_management")
public class ProjectManagement extends BaseEntity {
    
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目状态：active-进行中, completed-已完成, archived-已归档, cancelled-已取消
     */
    private String status;

    /**
     * 创建人ID
     */
    private Long createById;

    /**
     * 创建者（String类型，因为数据库中create_by是varchar类型）
     * 使用@TableField忽略父类的createBy字段（Long类型），使用自己的字段
     */
    @TableField(value = "create_by")
    private String createByName;

    /**
     * 前端传入的创建人（不映射到数据库，用于接收前端数据）
     */
    @TableField(exist = false)
    @JsonProperty("createdBy")
    private String createdBy;

    /**
     * 前端传入的创建人ID（不映射到数据库，用于接收前端数据）
     */
    @TableField(exist = false)
    @JsonProperty("createdById")
    private Long createdById;

    /**
     * 前端传入的创建部门ID（不映射到数据库，用于接收前端数据）
     * 如果前端传的是createDeptId，则映射到createDept
     */
    @TableField(exist = false)
    @JsonProperty("createDeptId")
    private Long createDeptId;

    /**
     * 前端传入的创建部门（不映射到数据库，用于接收前端数据）
     * 前端传入的字段名是createdByDept，可能是部门名称（String）或部门ID（Long）
     * 先定义为String类型，支持部门名称
     */
    @TableField(exist = false)
    @JsonProperty("createdByDept")
    private String createdByDept;

    /**
     * 创建部门（String类型，直接存储前端传来的部门名称字符串）
     * 注意：BaseEntity中的createDept是Long类型，但数据库中create_dept现在是varchar类型
     * 使用@TableField忽略父类的createDept字段（Long类型），使用自己的String类型字段
     */
    @TableField(value = "create_dept")
    @JsonProperty("createDept")
    private String createDeptName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 标签列表（不映射到数据库）
     */
    @TableField(exist = false)
    private List<String> tags;
}

