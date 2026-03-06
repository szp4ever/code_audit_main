package org.ruoyi.utils;

import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.constant.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 文件上传验证工具类
 * 基于文件上传约束条件调查结果和OWASP最佳实践
 *
 * @author system
 * @date 2026-01-24
 */
public class FileUploadValidator {

    // 文件大小限制（与application.yml保持一致）
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    // 文件名长度限制（与数据库字段保持一致）
    private static final int MAX_FILENAME_LENGTH = 500;

    // 单知识库总附件量限制
    private static final int MAX_ATTACHMENTS_PER_KNOWLEDGE_BASE = 2000;

    // 文件名危险字符正则
    private static final Pattern DANGEROUS_CHARS_PATTERN = Pattern.compile("[<>:\"|?*\\x00-\\x1f\\x7f]");
    // Windows保留名正则
    private static final Pattern RESERVED_NAMES_PATTERN = Pattern.compile("^(CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(\\.|$)", Pattern.CASE_INSENSITIVE);

    // 支持的文件扩展名白名单（基于FileType.java）
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
        // 文本文件
        "txt", "csv", "properties", "ini", "yaml", "yml", "log", "xml",
        // Word文档
        "doc", "docx",
        // PDF文档
        "pdf",
        // Excel表格
        "xls", "xlsx",
        // Markdown
        "md",
        // 代码文件
        "java", "html", "htm", "js", "py", "cpp", "sql", "php", "ruby", "c", "h", "hpp", "swift", "ts", "rs", "perl", "shell", "bat", "cmd", "css"
    ));

    /**
     * 验证文件
     *
     * @param file 文件
     * @throws ServiceException 验证失败时抛出
     */
    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new ServiceException("文件名不能为空");
        }

        // 验证文件大小
        validateFileSize(file.getSize());

        // 验证文件扩展名
        validateFileExtension(fileName);

        // 验证文件名
        validateFileName(fileName);
    }

    /**
     * 验证文件大小
     */
    public static void validateFileSize(long fileSize) {
        if (fileSize > MAX_FILE_SIZE) {
            throw new ServiceException(String.format("文件大小不能超过 %s", formatFileSize(MAX_FILE_SIZE)));
        }
    }

    /**
     * 验证文件扩展名
     */
    public static void validateFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new ServiceException("文件名必须包含扩展名");
        }

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new ServiceException(String.format("不支持的文件格式：%s。支持格式：%s等", ext.toUpperCase(), String.join(", ", ALLOWED_EXTENSIONS.stream().limit(10).toList())));
        }
    }

    /**
     * 验证文件名
     */
    public static void validateFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new ServiceException("文件名不能为空");
        }

        if (fileName.length() > MAX_FILENAME_LENGTH) {
            throw new ServiceException(String.format("文件名长度不能超过 %d 个字符", MAX_FILENAME_LENGTH));
        }

        // 检查危险字符
        if (DANGEROUS_CHARS_PATTERN.matcher(fileName).find()) {
            throw new ServiceException("文件名包含不允许的特殊字符（/ \\ : * ? \" < > | 等）");
        }

        // 检查Windows保留名
        if (RESERVED_NAMES_PATTERN.matcher(fileName).find()) {
            throw new ServiceException("文件名不能使用系统保留名称（CON、PRN、AUX、NUL、COM1-9、LPT1-9）");
        }
    }

    /**
     * 清理文件名（移除危险字符）
     *
     * @param fileName 原始文件名
     * @return 清理后的文件名
     */
    public static String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return fileName;
        }

        // 移除危险字符
        String sanitized = DANGEROUS_CHARS_PATTERN.matcher(fileName).replaceAll("_");

        // 处理Windows保留名
        if (RESERVED_NAMES_PATTERN.matcher(sanitized).find()) {
            sanitized = "_" + sanitized;
        }

        // 去除首尾空格和点
        sanitized = sanitized.trim().replaceAll("^\\.+|\\.+$", "");

        return sanitized;
    }

    /**
     * 格式化文件大小
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }

    /**
     * 获取单知识库最大附件数限制
     */
    public static int getMaxAttachmentsPerKnowledgeBase() {
        return MAX_ATTACHMENTS_PER_KNOWLEDGE_BASE;
    }

    /**
     * 检查文件类型是否支持
     */
    public static boolean isSupportedFileType(String fileType) {
        if (fileType == null) {
            return false;
        }
        String ext = fileType.toLowerCase();
        return FileType.isTextFile(ext) || FileType.isWord(ext) || FileType.isPdf(ext)
            || FileType.isExcel(ext) || FileType.isMdFile(ext) || FileType.isCodeFile(ext);
    }
}
