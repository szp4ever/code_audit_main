package org.ruoyi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用GraalVM Polyglot运行highlight.js进行代码高亮
 * 替代自定义tokenize，解决大代码块内存溢出问题
 */
@Slf4j
@Service
public class HighlightJsService {
    private Context jsContext;
    private Value hljs;
    private static final String HIGHLIGHT_JS_PATH = "js/highlight.min.js";
    private final Map<String, String> languageMapping = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            jsContext = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .option("js.ecmascript-version", "2022")
                .build();
            ClassPathResource resource = new ClassPathResource(HIGHLIGHT_JS_PATH);
            if (!resource.exists()) {
                log.error("highlight.js文件不存在: {}", HIGHLIGHT_JS_PATH);
                return;
            }
            try (InputStream is = resource.getInputStream()) {
                String highlightJsCode = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                jsContext.eval("js", highlightJsCode);
                hljs = jsContext.getBindings("js").getMember("hljs");
                if (hljs == null || !hljs.hasMember("highlight")) {
                    log.error("highlight.js加载失败，hljs对象不存在或highlight方法不可用");
                    return;
                }
                log.info("highlight.js加载成功，版本: {}", getVersion());
            }
            initLanguageMapping();
        } catch (Exception e) {
            log.error("初始化highlight.js失败", e);
        }
    }

    private void initLanguageMapping() {
        languageMapping.put("java", "java");
        languageMapping.put("python", "python");
        languageMapping.put("javascript", "javascript");
        languageMapping.put("js", "javascript");
        languageMapping.put("xml", "xml");
        languageMapping.put("html", "xml");
        languageMapping.put("sql", "sql");
        languageMapping.put("cpp", "cpp");
        languageMapping.put("c", "cpp");
        languageMapping.put("c++", "cpp");
        languageMapping.put("text", "plaintext");
    }

    private String getVersion() {
        try {
            Value version = hljs.getMember("versionString");
            return version != null && version.isString() ? version.asString() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 使用highlight.js高亮代码
     * @param code 代码内容
     * @param language 语言（如java、python等）
     * @return 高亮后的HTML字符串
     */
    public String highlight(String code, String language) {
        if (hljs == null || code == null || code.isEmpty()) {
            return escapeHtml(code);
        }
        try {
            String mappedLang = languageMapping.getOrDefault(language.toLowerCase(), language.toLowerCase());
            Value options = jsContext.eval("js", "({language: '" + mappedLang + "'})");
            Value result = hljs.invokeMember("highlight", code, options);
            if (result != null && result.hasMember("value")) {
                return result.getMember("value").asString();
            }
            return escapeHtml(code);
        } catch (Exception e) {
            log.warn("highlight.js高亮失败，语言: {}, 错误: {}", language, e.getMessage());
            return escapeHtml(code);
        }
    }

    /**
     * 自动检测语言并高亮
     */
    public String highlightAuto(String code) {
        if (hljs == null || code == null || code.isEmpty()) {
            return escapeHtml(code);
        }
        try {
            Value result = hljs.invokeMember("highlightAuto", code);
            if (result != null && result.hasMember("value")) {
                return result.getMember("value").asString();
            }
            return escapeHtml(code);
        } catch (Exception e) {
            log.warn("highlight.js自动高亮失败，错误: {}", e.getMessage());
            return escapeHtml(code);
        }
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    @PreDestroy
    public void destroy() {
        if (jsContext != null) {
            try {
                jsContext.close();
                log.info("highlight.js上下文已关闭");
            } catch (Exception e) {
                log.warn("关闭highlight.js上下文时出错", e);
            }
        }
    }
}
