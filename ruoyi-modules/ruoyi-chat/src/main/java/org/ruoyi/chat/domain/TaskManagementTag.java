package org.ruoyi.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 任务管理标签对象 task_management_tag
 *
 * @author ruoyi
 */
@Data
@TableName("task_management_tag")
public class TaskManagementTag implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}







