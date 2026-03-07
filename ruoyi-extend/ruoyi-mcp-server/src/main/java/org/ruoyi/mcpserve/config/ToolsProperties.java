//package org.ruoyi.mcpserve.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
///**
// * 工具配置属性类
// *
// * @author OpenX
// */
//@Data
//@Component
//@ConfigurationProperties(prefix = "tools")
//public class ToolsProperties {
//
//    /**
//     * Pexels图片搜索配置
//     */
//    private Pexels pexels = new Pexels();
//
//    /**
//     * Tavily搜索配置
//     */
//    private Tavily tavily = new Tavily();
//
//    /**
//     * 文件操作配置
//     */
//    private FileConfig file = new FileConfig();
//
//    @Data
//    public static class Pexels {
//        /**
//         * Pexels API密钥
//         */
//        private String apiKey;
//
//        /**
//         * API地址
//         */
//        private String apiUrl;
//    }
//
//    @Data
//    public static class Tavily {
//        /**
//         * Tavily API密钥
//         */
//        private String apiKey;
//
//        /**
//         * API地址
//         */
//        private String baseUrl;
//    }
//
//    @Data
//    public static class FileConfig {
//        /**
//         * 文件保存目录
//         */
//        private String saveDir;
//    }
//    public Pexels getPexels() {
//        return pexels;
//    }
//
//    public void setPexels(Pexels pexels) {
//        this.pexels = pexels;
//    }
//
//    public Tavily getTavily() {
//        return tavily;
//    }
//
//    public void setTavily(Tavily tavily) {
//        this.tavily = tavily;
//    }
//
//    public FileConfig getFile() {
//        return file;
//    }
//
//    public void setFile(FileConfig file) {
//        this.file = file;
//    }
//}
package org.ruoyi.mcpserve.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 工具配置类 (已手动补全 Get/Set 方法，不再依赖 Lombok)
 */
@Component
@ConfigurationProperties(prefix = "tools")
public class ToolsProperties {

    /**
     * Pexels图片搜索配置
     */
    private Pexels pexels = new Pexels();

    /**
     * Tavily搜索配置
     */
    private Tavily tavily = new Tavily();

    /**
     * 文件操作配置
     */
    private FileConfig file = new FileConfig();

    // ==========================================
    // 1. 外层类的 Getter / Setter
    // ==========================================

    public Pexels getPexels() {
        return pexels;
    }

    public void setPexels(Pexels pexels) {
        this.pexels = pexels;
    }

    public Tavily getTavily() {
        return tavily;
    }

    public void setTavily(Tavily tavily) {
        this.tavily = tavily;
    }

    public FileConfig getFile() {
        return file;
    }

    public void setFile(FileConfig file) {
        this.file = file;
    }

    // ==========================================
    // 2. 内部类 Pexels (及其 Getter/Setter)
    // ==========================================
    public static class Pexels {
        /**
         * Pexels API密钥
         */
        private String apiKey;

        /**
         * API地址
         */
        private String apiUrl;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }
    }

    // ==========================================
    // 3. 内部类 Tavily (及其 Getter/Setter)
    // ==========================================
    public static class Tavily {
        /**
         * Tavily API密钥
         */
        private String apiKey;

        /**
         * API地址
         */
        private String baseUrl;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    // ==========================================
    // 4. 内部类 FileConfig (及其 Getter/Setter)
    // ==========================================
    public static class FileConfig {
        /**
         * 文件保存目录
         */
        private String saveDir;

        public String getSaveDir() {
            return saveDir;
        }

        public void setSaveDir(String saveDir) {
            this.saveDir = saveDir;
        }
    }
}