package org.ruoyi.chat.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目管理视图对象 ProjectManagementVo
 *
 * @author ruoyi
 */
@Data
public class ProjectManagementVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
     * 项目状态
     */
    private String status;

    /**
     * 创建人（用户名或昵称）
     */
    private String createdBy;

    /**
     * 创建人ID
     */
    private Long createdById;

    /**
     * 创建部门（部门名称）
     */
    private String createdByDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private Date updatedAt;

    /**
     * 任务数量（统计字段）
     */
    private Integer taskCount;

    /**
     * 已完成任务数量（统计字段）
     */
    private Integer completedTaskCount;

    /**
     * 标签列表
     */
    private List<String> tags;
}


