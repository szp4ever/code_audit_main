package org.ruoyi.knowledge.ingestion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分片上传状态查询响应
 */
@Data
public class ChunkUploadStatusVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上传任务ID
     */
    private String uploadId;

    /**
     * 文件MD5
     */
    private String fileMd5;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 总分片数
     */
    private Integer totalChunks;

    /**
     * 已上传的分片索引列表
     */
    private List<Integer> uploadedChunks;

    /**
     * 上传进度（百分比）
     */
    private Integer progress;

    /**
     * 是否已完成
     */
    private Boolean isCompleted;

    /**
     * 附件ID（已完成时返回）
     */
    private Long attachId;
}
