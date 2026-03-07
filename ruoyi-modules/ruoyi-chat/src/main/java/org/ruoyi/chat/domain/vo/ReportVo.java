package org.ruoyi.chat.domain.vo;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ReportVo {
    private Long id;
    private String name;
    private String taskName;
    private String type;     // 映射任务类型作为报告类型
    private String status;   // 默认为 ready

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String fileUrl;
}