package org.ruoyi.chat.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Flask接口返回的Issue响应对象
 */
@Data
public class TaskIssueResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private TaskIssueData data;

    @Data
    public static class TaskIssueData implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @JsonProperty("file_name")
        private String fileName;

        @JsonProperty("task_type")
        private String taskType;

        @JsonProperty("total_issues")
        private Integer totalIssues;

        /**
         * [关键修复] 增加此字段接收 AI 生成的 5000 字大报告
         */
        @JsonProperty("report_markdown")
        private String reportMarkdown;

        private List<TaskIssueItem> issues;
    }

    @Data
    public static class TaskIssueItem implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @JsonProperty("issue_name")
        private String issueName;

        private String severity;

        @JsonProperty("line_number")
        private String lineNumber;

        private String description;

        @JsonProperty("file_name")
        private String fileName;

        @JsonProperty("fix_suggestion")
        private String fixSuggestion;
    }
}