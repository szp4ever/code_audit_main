package org.ruoyi.chat.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 任务管理文件视图对象 TaskManagementFileVo
 *
 * @author ruoyi
 */
@Data
public class TaskManagementFileVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件URL
     */
    private String url;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
}








