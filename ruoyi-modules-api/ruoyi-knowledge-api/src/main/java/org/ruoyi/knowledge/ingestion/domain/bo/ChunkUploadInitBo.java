package org.ruoyi.knowledge.ingestion.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分片上传初始化请求
 */
@Data
public class ChunkUploadInitBo {

    /**
     * 知识库ID
     */
    @NotBlank(message = "知识库ID不能为空")
    private String kid;

    /**
     * 文件MD5
     */
    @NotBlank(message = "文件MD5不能为空")
    private String fileMd5;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    /**
     * 文件大小
     */
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    /**
     * MIME类型
     */
    private String mimeType;

    /**
     * 分片大小（默认2MB）
     */
    private Integer chunkSize = 2 * 1024 * 1024;
}
