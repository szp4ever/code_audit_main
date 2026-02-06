package org.ruoyi.common.core.utils.file;

/**
 * 媒体类型工具类
 *
 * @author ruoyi
 */
public class MimeTypeUtils {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};

    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};
    /**
     * 音频扩展名
     */
    public static final String[] AUDIO__EXTENSION = {"mp3", "mp4", "mpeg", "mpga", "m4a", "wav", "webm"};

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // 音频格式
            "mp3", "mp4", "mpeg", "mpga", "m4a", "wav", "webm",
            // pdf
            "pdf"};

    /**
     * 代码文件扩展名（用于代码审计、代码规范检查等任务）
     */
    public static final String[] CODE_FILE_EXTENSION = {
            // Java
            "java", "class", "jar", "war", "ear",
            // JavaScript/TypeScript
            "js", "jsx", "ts", "tsx", "mjs", "cjs",
            // Python
            "py", "pyc", "pyo", "pyw", "pyd",
            // C/C++
            "c", "cpp", "cc", "cxx", "h", "hpp", "hxx", "hh",
            // C#
            "cs", "csx",
            // Go
            "go",
            // Rust
            "rs",
            // PHP
            "php", "php3", "php4", "php5", "phtml",
            // Ruby
            "rb", "rbw",
            // Swift
            "swift",
            // Kotlin
            "kt", "kts",
            // Scala
            "scala", "sc",
            // HTML/CSS
            "html", "htm", "xhtml", "css", "scss", "sass", "less", "styl",
            // 配置文件
            "json", "xml", "yml", "yaml", "properties", "ini", "conf", "config", "toml",
            // 脚本文件
            "sh", "bash", "zsh", "fish", "bat", "cmd", "ps1", "psm1",
            // SQL
            "sql", "ddl", "dml",
            // 构建和依赖管理文件
            "gradle", "maven", "pom", "build", "makefile", "cmake",
            // 其他代码相关
            "md", "markdown", "txt", "log", "gitignore", "dockerfile", "env",
            // Web相关
            "vue", "jsx", "tsx",
            // 数据文件
            "csv", "tsv"
    };

}
