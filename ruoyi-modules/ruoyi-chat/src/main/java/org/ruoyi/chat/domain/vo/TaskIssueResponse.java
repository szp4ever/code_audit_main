package org.ruoyi.chat.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Flask接口返回的Issue响应对象
 *
 * @author ruoyi
 */
@Data
public class TaskIssueResponse implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private TaskIssueData data;

    @Data
    public static class TaskIssueData implements Serializable {
        
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 文件名
         */
        @JsonProperty("file_name")
        private String fileName;

        /**
         * 任务类型
         */
        @JsonProperty("task_type")
        private String taskType;

        /**
         * 总Issue数量
         */
        @JsonProperty("total_issues")
        private Integer totalIssues;

        /**
         * Issue列表
         */
        private List<TaskIssueItem> issues;
    }

    @Data
    public static class TaskIssueItem implements Serializable {
        
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 问题名称
         */
        @JsonProperty("issue_name")
        private String issueName;

        /**
         * 严重程度
         */
        private String severity;

        /**
         * 行号
         */
        @JsonProperty("line_number")
        private String lineNumber;

        /**
         * 问题描述
         */
        private String description;

        /**
         * 漏洞所属文件
         */
        @JsonProperty("file_name")
        private String fileName;

        /**
         * 修复建议
         */
        @JsonProperty("fix_suggestion")
        private String fixSuggestion;
    }
}

