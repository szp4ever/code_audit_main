package org.ruoyi.chat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;
import java.util.List;

/**
 * 任务管理对象 task_management
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("task_management")
public class TaskManagement extends BaseEntity {
    
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
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务优先级：low-低, medium-中, high-高, urgent-紧急
     */
    private String priority;

    /**
     * 任务类型：code_standard_check-编码规范检查, data_security_audit-数据安全审计, 
     * dependency_analysis-依赖关系分析, compliance_audit-合规审计, other-其他
     */
    private String taskType;

    /**
     * 任务状态：pending-待处理, in_progress-进行中, completed-已完成, cancelled-已取消
     */
    private String status;

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
     * 输入文件列表（不映射到数据库）
     */
    @TableField(exist = false)
    private List<TaskManagementFile> inputFiles;

    /**
     * 输出文件列表（不映射到数据库）
     */
    @TableField(exist = false)
    private List<TaskManagementFile> outputFiles;

    /**
     * 标签列表（不映射到数据库）
     */
    @TableField(exist = false)
    private List<String> tags;
}







