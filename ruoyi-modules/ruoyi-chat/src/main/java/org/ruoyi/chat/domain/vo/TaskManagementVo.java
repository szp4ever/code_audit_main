package org.ruoyi.chat.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.chat.domain.TaskManagement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务管理视图对象 task_management
 *
 * @author ruoyi
 * @date 2024-06-25
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TaskManagement.class)
public class TaskManagementVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 关联项目ID
     */
    @ExcelProperty(value = "关联项目ID")
    private Long projectId;

    /**
     * 关联模板ID (新增字段)
     */
    @ExcelProperty(value = "关联模板ID")
    private Long templateId;

    /**
     * 任务标题
     */
    @ExcelProperty(value = "任务标题")
    private String title;

    /**
     * 任务描述/要求
     */
    @ExcelProperty(value = "任务描述/要求")
    private String description;

    /**
     * 优先级
     */
    @ExcelProperty(value = "优先级")
    private String priority;

    /**
     * 任务类型
     */
    @ExcelProperty(value = "任务类型")
    private String taskType;

    /**
     * 任务状态
     */
    @ExcelProperty(value = "任务状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createdAt;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updatedAt;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 输入文件列表
     */
    private List<TaskManagementFileVo> inputFiles;

    /**
     * 输出文件列表
     */
    private List<TaskManagementFileVo> outputFiles;

    /**
     * [新增] 关联模板名称 (用于前端显示)
     */
    @ExcelProperty(value = "关联模板名称")
    private String templateName;
}