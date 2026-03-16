package org.ruoyi.knowledge.ingestion.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 分片上传初始化响应
 */
@Data
public class ChunkUploadInitVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上传任务ID
     */
    private String uploadId;

    /**
     * 文件唯一标识（MD5）
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
     * 分片大小
     */
    private Integer chunkSize;

    /**
     * 总分片数
     */
    private Integer totalChunks;

    /**
     * 已上传的分片索引列表（用于断点续传）
     */
    private java.util.List<Integer> uploadedChunks;

    /**
     * 是否已存在相同文件（秒传）
     */
    private Boolean isExists;

    /**
     * 已存在的附件ID（秒传时使用）
     */
    private Long existingAttachId;
}
