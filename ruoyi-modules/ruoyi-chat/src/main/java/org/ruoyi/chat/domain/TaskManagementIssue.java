package org.ruoyi.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 任务管理Issue对象 task_management_issue
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("task_management_issue")
public class TaskManagementIssue extends BaseEntity {
    
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
     * 文件名
     */
    private String fileName;

    /**
     * 问题名称
     */
    private String issueName;

    /**
     * 严重程度：Low-低, Medium-中, High-高, Critical-严重
     */
    private String severity;

    /**
     * 行号（可能是单个行号或范围，如"45"或"45-48"）
     */
    private String lineNumber;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 修复建议
     */
    private String fixSuggestion;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;
}

