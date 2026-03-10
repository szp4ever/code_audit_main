package org.ruoyi.knowledge.ingestion.enums;

import lombok.Getter;

/**
 * 文档处理流水线状态枚举。
 * <p>
 * 简化后的核心流程：UPLOADING → PARSING → CHUNKING → VECTORIZING → COMPLETED
 * 失败状态：FAILED（可从任何状态转入）
 * 取消状态：CANCELLED（可从任何非终态转入）
 * <p>
 * 原有的 MATCHING / USER_REVIEW_MATCHING / CREATING_ITEMS / USER_REVIEW_ITEMS
 * 已从上传流水线中移除，改为独立的"AI 增强"操作（在 enrichment 层处理）。
 *
 * @author system
 * @date 2026-01-24
 */
@Getter
public enum ProcessingStatus {

    // === 核心流水线状态 ===

    /** 上传中 */
    UPLOADING("UPLOADING", "文件上传中", 0),
    /** 解析文档内容 */
    PARSING("PARSING", "文档解析中", 20),
    /** 切片/分块 */
    CHUNKING("CHUNKING", "文本分块中", 50),
    /** 向量化入库 */
    VECTORIZING("VECTORIZING", "向量化存储中", 80),
    /** 处理完成 */
    COMPLETED("COMPLETED", "处理完成", 100),
    /** 处理失败 */
    FAILED("FAILED", "处理失败", -1),
    /** 用户取消 */
    CANCELLED("CANCELLED", "用户取消", -1),

    // === 以下为兼容旧数据保留的状态，新流程不再使用 ===

    /** @deprecated 已移至 AI 增强层 — see ItemSuggestionService */
    @Deprecated
    MATCHING("MATCHING", "相似度匹配中", -1),
    /** @deprecated 已移至 AI 增强层 — see ItemSuggestionService */
    @Deprecated
    USER_REVIEW_MATCHING("USER_REVIEW_MATCHING", "用户审阅匹配结果", -1),
    /** @deprecated 已移至 AI 增强层 — see ItemSuggestionService */
    @Deprecated
    CREATING_ITEMS("CREATING_ITEMS", "创建条目中", -1),
    /** @deprecated 已移至 AI 增强层 — see ItemSuggestionService */
    @Deprecated
    USER_REVIEW_ITEMS("USER_REVIEW_ITEMS", "用户审阅新创建的条目", -1);

    private final String code;
    private final String description;
    private final int defaultProgress;

    ProcessingStatus(String code, String description, int defaultProgress) {
        this.code = code;
        this.description = description;
        this.defaultProgress = defaultProgress;
    }

    public static ProcessingStatus fromCode(String code) {
        for (ProcessingStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown processing status code: " + code);
    }

    /**
     * 检查是否可以从当前状态转换到目标状态。
     * 简化后的合法转换：
     * UPLOADING → PARSING → CHUNKING → VECTORIZING → COMPLETED
     * 任何非终态 → FAILED
     * 任何非终态 → CANCELLED
     */
    public boolean canTransitionTo(ProcessingStatus target) {
        if (target == FAILED || target == CANCELLED) {
            // 终态不能再转换
            return this != COMPLETED && this != FAILED && this != CANCELLED;
        }
        return switch (this) {
            case UPLOADING -> target == PARSING;
            case PARSING -> target == CHUNKING;
            case CHUNKING -> target == VECTORIZING;
            case VECTORIZING -> target == COMPLETED;
            case FAILED -> target == PARSING; // 允许重试
            default -> false;
        };
    }
}
