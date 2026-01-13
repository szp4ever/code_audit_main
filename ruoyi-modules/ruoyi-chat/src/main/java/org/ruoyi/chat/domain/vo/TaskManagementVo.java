package org.ruoyi.chat.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务管理视图对象 TaskManagementVo
 *
 * @author ruoyi
 */
@Data
public class TaskManagementVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务优先级
     */
    private String priority;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 输入文件列表
     */
    private List<TaskManagementFileVo> inputFiles;

    /**
     * 输出文件列表
     */
    private List<TaskManagementFileVo> outputFiles;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}







