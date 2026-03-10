package org.ruoyi.knowledge.curation.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 存储占用 Top 对象（文档）
 */
@Data
public class StorageTopItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文档ID */
    private String objectId;

    /** 文档名称 */
    private String title;

    /** 文档类型（docType） */
    private String type;

    /** OSS ID（用于查询真实文件大小） */
    private Long ossId;

    /** 占用字节数（从 MinIO metadata 动态获取） */
    private Long sizeBytes;

    /** 最后更新时间 */
    private Date updateTime;
}
