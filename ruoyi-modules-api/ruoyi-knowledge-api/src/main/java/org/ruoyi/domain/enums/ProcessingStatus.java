package org.ruoyi.domain.enums;

import lombok.Getter;

/**
 * 附件处理状态枚举
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Getter
public enum ProcessingStatus {
    UPLOADING("UPLOADING", "文件上传中"),
    PARSING("PARSING", "文档解析中"),
    CHUNKING("CHUNKING", "文本分块中"),
    MATCHING("MATCHING", "相似度匹配中"),
    USER_REVIEW_MATCHING("USER_REVIEW_MATCHING", "用户审阅匹配结果"),
    CREATING_ITEMS("CREATING_ITEMS", "创建条目中"),
    USER_REVIEW_ITEMS("USER_REVIEW_ITEMS", "用户审阅新创建的条目"),
    VECTORIZING("VECTORIZING", "向量化存储中"),
    COMPLETED("COMPLETED", "处理完成"),
    FAILED("FAILED", "处理失败"),
    CANCELLED("CANCELLED", "用户取消");

    private final String code;
    private final String description;

    ProcessingStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProcessingStatus fromCode(String code) {
        for (ProcessingStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown processing status code: " + code);
    }
}
