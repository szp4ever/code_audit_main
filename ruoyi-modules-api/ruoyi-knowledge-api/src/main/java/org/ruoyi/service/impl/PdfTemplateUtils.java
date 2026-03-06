package org.ruoyi.service.impl;

import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.domain.vo.CweReferenceVo;
import org.ruoyi.domain.vo.KnowledgeItemVo;
import org.ruoyi.domain.vo.KnowledgeTagVo;
import org.ruoyi.common.core.utils.SpringUtils;
import org.ruoyi.utils.CvssScoreCalculator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * PDF模板工具类，用于Velocity模板中调用
 */
public class PdfTemplateUtils {
    public String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
    public String formatDateTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public String getSeverityColor(String severity) {
        if (StringUtils.isBlank(severity)) {
            return "#1890ff";
        }
        String lowerSeverity = severity.toLowerCase();
        switch (lowerSeverity) {
            case "critical":
                return "#cf1322";
            case "high":
                return "#ff4d4f";
            case "medium":
                return "#faad14";
            case "low":
                return "#52c41a";
            case "none":
                return "#808080";
            default:
                return "#1890ff";
        }
    }
    public String mapSeverityByScore(Double cvssScore) {
        if (cvssScore == null) {
            return "";
        }
        return CvssScoreCalculator.mapSeverityByScore(java.math.BigDecimal.valueOf(cvssScore));
    }
    private KnowledgeItemServiceImpl serviceInstance;
    public void setServiceInstance(KnowledgeItemServiceImpl serviceInstance) {
        this.serviceInstance = serviceInstance;
    }
    public String getFieldValue(KnowledgeItemVo item, String fieldKey, Map<String, CweReferenceVo> cweMap, 
                               Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap, 
                               Map<String, String> knowledgeBaseMap, Map<String, String> fieldFormats) {
        if (serviceInstance == null) {
            serviceInstance = SpringUtils.getBean(KnowledgeItemServiceImpl.class);
        }
        return serviceInstance.getFieldValueForTemplate(item, fieldKey, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
    }
    public String join(List<String> list, String delimiter) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(delimiter, list);
    }
}
