package org.ruoyi.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 项目管理标签关联对象 project_management_tag
 *
 * @author ruoyi
 */
@Data
@TableName("project_management_tag")
public class ProjectManagementTag implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 创建时间
     */
    private Date createTime;
}

