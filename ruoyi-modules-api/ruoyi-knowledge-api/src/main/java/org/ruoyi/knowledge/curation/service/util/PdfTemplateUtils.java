package org.ruoyi.knowledge.curation.service.util;

import org.ruoyi.knowledge.cwe.domain.vo.CweReferenceVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeTagVo;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.ruoyi.knowledge.curation.service.impl.KnowledgeItemExportDataService;
import org.ruoyi.knowledge.shared.utils.CvssScoreCalculator;

import java.util.List;
import java.util.Map;

/**
 * PDF 模板工具类，在 Velocity 模板渲染 PDF 时作为上下文工具对象使用。
 * 通过 context.put("utils", utils) 注入，模板中可调用 $utils.xxx() 方法。
 */
public class PdfTemplateUtils {

    private IKnowledgeItemService serviceInstance;
    private KnowledgeItemExportDataService exportDataService;

    public void setServiceInstance(IKnowledgeItemService serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public IKnowledgeItemService getServiceInstance() {
        return serviceInstance;
    }

    public void setExportDataService(KnowledgeItemExportDataService exportDataService) {
        this.exportDataService = exportDataService;
    }

    // ── 字段取值（代理 KnowledgeItemExportDataService） ──────────────────────

    public String getFieldValue(KnowledgeItemVo item, String fieldKey,
                                Map<String, CweReferenceVo> cweMap,
                                Map<String, KnowledgeTagVo> tagMap,
                                Map<Long, String> userMap,
                                Map<String, String> knowledgeBaseMap,
                                Map<String, String> fieldFormats) {
        if (exportDataService == null) return "";
        return exportDataService.getFieldValue(item, fieldKey, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
    }

    // ── 严重程度 ─────────────────────────────────────────────────────────────

    public String mapSeverityByScore(Object cvssScore) {
        if (cvssScore == null) return "";
        try {
            java.math.BigDecimal score = new java.math.BigDecimal(cvssScore.toString());
            String result = CvssScoreCalculator.mapSeverityByScore(score);
            return result != null ? result : "";
        } catch (Exception e) {
            return "";
        }
    }

    public String getSeverityColor(String severity) {
        if (severity == null || severity.isEmpty()) return "#1890ff";
        switch (severity.toLowerCase()) {
            case "critical": return "#cf1322";
            case "high":     return "#ff4d4f";
            case "medium":   return "#faad14";
            case "low":      return "#52c41a";
            case "none":     return "#808080";
            default:         return "#1890ff";
        }
    }

    // ── 字符串工具 ───────────────────────────────────────────────────────────

    public String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    public String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength) + "...";
    }

    public boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public String nl2br(String text) {
        if (text == null) return "";
        return text.replace("\r\n", "<br/>").replace("\n", "<br/>").replace("\r", "<br/>");
    }

    public String join(List<?> list, String separator) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(separator);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public String formatDateTime(Object date) {
        if (date == null) return "";
        try {
            if (date instanceof java.time.LocalDateTime) {
                return ((java.time.LocalDateTime) date)
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            if (date instanceof java.util.Date) {
                return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((java.util.Date) date);
            }
            return date.toString();
        } catch (Exception e) {
            return date.toString();
        }
    }

    public String formatDate(Object date) {
        if (date == null) return "";
        try {
            if (date instanceof java.time.LocalDateTime) {
                return ((java.time.LocalDateTime) date)
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (date instanceof java.util.Date) {
                return new java.text.SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) date);
            }
            return date.toString();
        } catch (Exception e) {
            return date.toString();
        }
    }
}
