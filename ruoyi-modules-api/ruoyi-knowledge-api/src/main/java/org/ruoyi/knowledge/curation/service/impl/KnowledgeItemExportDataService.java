package org.ruoyi.knowledge.curation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.SpringUtils;
import org.ruoyi.common.core.service.DictService;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.curation.domain.KnowledgeItem;
import org.ruoyi.knowledge.curation.domain.KnowledgeTag;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeItemBo;
import org.ruoyi.knowledge.curation.domain.bo.ExportPreviewRequestBo;
import org.ruoyi.knowledge.curation.domain.bo.ExportRequestBo;
import org.ruoyi.knowledge.curation.domain.bo.PdfOptionsBo;
import org.ruoyi.knowledge.curation.domain.vo.ExportPreviewVo;
import org.ruoyi.knowledge.curation.domain.vo.FieldInfoVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeInfoVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeTagVo;
import org.ruoyi.knowledge.cwe.domain.CweReference;
import org.ruoyi.knowledge.cwe.domain.vo.CweReferenceVo;
import org.ruoyi.knowledge.cwe.mapper.CweReferenceMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeTagMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeInfoMapper;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.ruoyi.knowledge.shared.utils.CvssScoreCalculator;
import org.ruoyi.system.service.ISysUserService;
import org.ruoyi.system.domain.vo.SysUserVo;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 知识条目导出数据准备服务
 * 提供导出预览、数据获取、字段构建等共享方法
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeItemExportDataService {

    private final IKnowledgeItemService knowledgeItemService;
    private final KnowledgeItemExcelExportService knowledgeItemExcelExportService;
    private final KnowledgeItemPdfExportService knowledgeItemPdfExportService;
    private final CweReferenceMapper cweReferenceMapper;
    private final KnowledgeTagMapper knowledgeTagMapper;
    private final ISysUserService sysUserService;
    private final KnowledgeInfoMapper knowledgeInfoMapper;
    private final KnowledgeItemMapper knowledgeItemMapper;

    public ExportPreviewVo exportPreview(ExportPreviewRequestBo request) {
        int previewLimit = Math.min(10, Math.max(5, (int) Math.min(getExportDataCount(request), 10)));
        List<KnowledgeItemVo> sampleData = getExportData(request, previewLimit);
        long totalCount = getExportDataCount(request);
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        long estimatedFileSize = estimateFileSize(sampleData, request.getFormat(), fieldInfos.size());
        int estimatedTime = estimateTime(totalCount, request.getFormat());
        String previewHtml = null;
        String pdfFormatType = null;
        if ("pdf".equals(request.getFormat())) {
            PdfOptionsBo pdfOptions = request.getPdfOptions();
            boolean useReportFormat;
            if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
                useReportFormat = "report".equals(pdfOptions.getFormatType());
            } else {
                useReportFormat = shouldUseReportFormat(fieldInfos);
            }
            pdfFormatType = useReportFormat ? "report" : "table";
            previewHtml = generatePreviewHtml(sampleData, fieldInfos, request.getFieldFormats(), useReportFormat);
        }
        return ExportPreviewVo.builder()
            .sampleData(convertToMapList(sampleData, request.getSelectedFields(), request.getExpandedFields(), request.getFieldFormats()))
            .totalCount(totalCount)
            .selectedFields(fieldInfos)
            .estimatedFileSize(estimatedFileSize)
            .estimatedTime(estimatedTime)
            .previewHtml(previewHtml)
            .pdfFormatType(pdfFormatType)
            .build();
    }

    public void export(ExportRequestBo request, HttpServletResponse response) {
        List<String> warnings = new ArrayList<>();
        try {
            String fileName = StringUtils.isNotBlank(request.getFileName()) 
                ? request.getFileName() 
                : generateDefaultFileName(request.getFormat());
            if (StringUtils.isBlank(request.getFormat())) {
                throw new ServiceException("导出格式不能为空");
            }
            if (CollectionUtils.isEmpty(request.getSelectedFields())) {
                throw new ServiceException("请至少选择一个导出字段");
            }
            if ("excel".equals(request.getFormat())) {
                long totalCount = getExportDataCount(request);
                if (totalCount == 0) {
                    throw new ServiceException("没有可导出的数据");
                }
                List<KnowledgeItemVo> data = getExportData(request, null);
                List<Map<String, Object>> exportData = convertToMapList(data, request.getSelectedFields(), request.getExpandedFields(), request.getFieldFormats());
                List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
                knowledgeItemExcelExportService.exportToExcel(exportData, fieldInfos, request, fileName, response);
            } else if ("pdf".equals(request.getFormat())) {
                long totalCount = getExportDataCount(request);
                if (totalCount == 0) {
                    throw new ServiceException("没有可导出的数据");
                }
                knowledgeItemPdfExportService.exportToPdfStreaming(request, fileName, response, warnings);
            } else {
                throw new ServiceException("不支持的导出格式: " + request.getFormat());
            }
            if (!warnings.isEmpty()) {
                String warningsJson = String.join(";", warnings);
                response.setHeader("X-Export-Warnings", URLEncoder.encode(warningsJson, StandardCharsets.UTF_8));
            }
        } catch (ServiceException e) {
            log.error("导出失败: {}", e.getMessage());
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write("{\"code\":500,\"msg\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
                    response.getWriter().flush();
                }
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
            throw e;
        } catch (Exception e) {
            log.error("导出失败", e);
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json;charset=utf-8");
                    String errorMsg = "导出失败: " + e.getMessage();
                    response.getWriter().write("{\"code\":500,\"msg\":\"" + errorMsg.replace("\"", "\\\"").replace("\n", "\\n") + "\"}");
                    response.getWriter().flush();
                }
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
            throw new ServiceException("导出失败: " + e.getMessage());
        }
    }

    public List<KnowledgeItemVo> getExportData(ExportPreviewRequestBo request, Integer limit) {
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            bo.setItemUuids(request.getItemUuids());
            List<KnowledgeItemVo> list = knowledgeItemService.queryList(bo);
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
            PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            TableDataInfo<KnowledgeItemVo> pageData = knowledgeItemService.queryPageList(bo, pageQuery);
            List<KnowledgeItemVo> list = pageData.getRows();
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        } else {
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            List<KnowledgeItemVo> list = knowledgeItemService.queryList(bo);
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        }
    }

    public long getExportDataCount(ExportPreviewRequestBo request) {
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            return request.getItemUuids().size();
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
            KnowledgeItemBo bo = new KnowledgeItemBo();
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
            TableDataInfo<KnowledgeItemVo> pageData = knowledgeItemService.queryPageList(bo, pageQuery);
            return pageData.getRows() != null ? pageData.getRows().size() : 0;
        }
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if (request.getFilters() != null) {
            bo = request.getFilters();
        }
        List<KnowledgeItemVo> list = knowledgeItemService.queryList(bo);
        return list != null ? (long) list.size() : 0L;
    }

    public List<FieldInfoVo> buildFieldInfos(List<String> selectedFields, Map<String, List<String>> expandedFields) {
        Map<String, String> fieldLabels = getFieldLabels();
        List<FieldInfoVo> fieldInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(selectedFields)) {
            for (String field : selectedFields) {
                //过滤掉已废弃的字段
                if ("vulnerabilityType".equals(field)) {
                    continue;
                }
                fieldInfos.add(FieldInfoVo.builder()
                    .key(field)
                    .label(fieldLabels.getOrDefault(field, field))
                    .type("base")
                    .build());
                if (expandedFields != null && expandedFields.containsKey(field)) {
                    for (String expandedField : expandedFields.get(field)) {
                        fieldInfos.add(FieldInfoVo.builder()
                            .key(expandedField)
                            .label(fieldLabels.getOrDefault(expandedField, expandedField))
                            .type("expanded")
                            .parentField(field)
                            .build());
                    }
                }
            }
        }
        return fieldInfos;
    }

    public Map<String, String> getFieldLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("title", "标题");
        labels.put("summary", "摘要");
        labels.put("problemDescription", "问题描述");
        labels.put("fixSolution", "修复方案");
        labels.put("exampleCode", "示例代码");
        labels.put("referenceLink", "参考链接");
        labels.put("severity", "风险等级");
        labels.put("vulnerabilityTypes", "漏洞类型");
        labels.put("vulnerabilityTypeName", "漏洞类型名称（中文）");
        labels.put("vulnerabilityTypeNameEn", "漏洞类型名称（英文）");
        labels.put("vulnerabilityTypeDescription", "漏洞类型描述（中文）");
        labels.put("vulnerabilityTypeDescriptionEn", "漏洞类型描述（英文）");
        labels.put("language", "编程语言");
        labels.put("cvssScore", "CVSS评分");
        labels.put("cvssAttackVector", "CVSS攻击方式");
        labels.put("cvssAttackComplexity", "CVSS利用复杂度");
        labels.put("cvssPrivilegesRequired", "CVSS权限需求");
        labels.put("cvssUserInteraction", "CVSS用户交互");
        labels.put("cvssConfidentialityImpact", "CVSS机密性影响");
        labels.put("cvssIntegrityImpact", "CVSS完整性影响");
        labels.put("cvssAvailabilityImpact", "CVSS可用性影响");
        labels.put("status", "状态");
        labels.put("tags", "标签");
        labels.put("fragmentCount", "片段数量");
        labels.put("createTime", "创建时间");
        labels.put("updateTime", "更新时间");
        labels.put("createBy", "创建人");
        labels.put("updateBy", "更新人");
        labels.put("kid", "知识库");
        return labels;
    }

    public long estimateFileSize(List<KnowledgeItemVo> sampleData, String format, int fieldCount) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return 0;
        }
        long avgSize = sampleData.stream()
            .mapToLong(item -> estimateItemSize(item))
            .sum() / sampleData.size();
        return avgSize * fieldCount * 2;
    }

    public long estimateItemSize(KnowledgeItemVo item) {
        long size = 0;
        if (StringUtils.isNotBlank(item.getTitle())) size += item.getTitle().length();
        if (StringUtils.isNotBlank(item.getSummary())) size += item.getSummary().length();
        if (StringUtils.isNotBlank(item.getProblemDescription())) size += item.getProblemDescription().length();
        if (StringUtils.isNotBlank(item.getFixSolution())) size += item.getFixSolution().length();
        if (StringUtils.isNotBlank(item.getExampleCode())) size += item.getExampleCode().length();
        return size;
    }

    public int estimateTime(long totalCount, String format) {
        int baseTime = "excel".equals(format) ? 1 : 2;
        return (int) Math.max(1, baseTime + totalCount / 1000);
    }

    public String generatePreviewHtml(List<KnowledgeItemVo> sampleData, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats, boolean useReportFormat) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return "<p>暂无数据</p>";
        }
        if (useReportFormat) {
            return generateReportFormatPreviewHtml(sampleData, fieldInfos, fieldFormats);
        } else {
            return generateTableFormatPreviewHtml(sampleData, fieldInfos, fieldFormats);
        }
    }

    public String generateTableFormatPreviewHtml(List<KnowledgeItemVo> sampleData, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return "<p>暂无数据</p>";
        }
        List<String> selectedFields = fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList());
        Map<String, List<String>> expandedFields = extractExpandedFields(fieldInfos);
        Map<String, CweReferenceVo> cweMap = buildCweMap(sampleData, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(sampleData, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(sampleData, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(sampleData, selectedFields, expandedFields);
        StringBuilder html = new StringBuilder("<div style='font-family: \"Microsoft YaHei\", \"SimSun\", \"Helvetica Neue\", Arial, sans-serif;'>");
        html.append("<table style='border-collapse: collapse; width: 100%; font-size: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); background-color: #fff;'>");
        html.append("<thead><tr style='background-color: #f5f5f5;'>");
        for (FieldInfoVo fieldInfo : fieldInfos) {
            html.append("<th style='padding: 10px 8px; border: 1px solid #c8c8c8; font-weight: 600; text-align: center; color: #333; white-space: nowrap;'>")
                .append(escapeHtml(fieldInfo.getLabel())).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (int i = 0; i < sampleData.size(); i++) {
            KnowledgeItemVo item = sampleData.get(i);
            String rowStyle = i % 2 == 0 
                ? "background-color: #ffffff; transition: background-color 0.2s;" 
                : "background-color: #f9f9f9; transition: background-color 0.2s;";
            html.append("<tr style='").append(rowStyle).append("' onmouseover=\"this.style.backgroundColor='#f0f7ff'\" onmouseout=\"this.style.backgroundColor='").append(i % 2 == 0 ? "#ffffff" : "#f9f9f9").append("'\">");
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String value = getFieldValue(item, fieldInfo.getKey(), cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                if (value.length() > 100) {
                    value = value.substring(0, 100) + "...";
                }
                html.append("<td style='padding: 10px 8px; border: 1px solid #c8c8c8; color: #666; word-wrap: break-word;'>").append(escapeHtml(value)).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table></div>");
        return html.toString();
    }

    public String generateReportFormatPreviewHtml(List<KnowledgeItemVo> sampleData, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return "<p>暂无数据</p>";
        }
        List<String> selectedFields = fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList());
        Map<String, List<String>> expandedFields = extractExpandedFields(fieldInfos);
        Map<String, CweReferenceVo> cweMap = buildCweMap(sampleData, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(sampleData, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(sampleData, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(sampleData, selectedFields, expandedFields);
        DictService dictService = SpringUtils.getBean(DictService.class);
        StringBuilder html = new StringBuilder("<div style='font-family: \"Microsoft YaHei\", \"SimSun\", \"Helvetica Neue\", Arial, sans-serif; background-color: #fafafa; padding: 16px;'>");
        for (int i = 0; i < sampleData.size(); i++) {
            KnowledgeItemVo item = sampleData.get(i);
            String severity = item.getSeverity();
            if (StringUtils.isBlank(severity) && item.getCvssScore() != null) {
                severity = CvssScoreCalculator.mapSeverityByScore(item.getCvssScore());
            }
            String borderColor = getSeverityColor(severity);
            html.append("<div style='background-color: #ffffff; border: 1px solid #e5e5e5; border-left: 4px solid #404040; border-radius: 4px; padding: 16px; margin-bottom: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1);'>");
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
            html.append("<h3 style='color: #404040; font-size: 14px; font-weight: 600; margin: 0 0 12px 0; line-height: 1.4;'>")
                .append((i + 1)).append(". ").append(escapeHtml(title)).append("</h3>");
            html.append("<div style='margin-bottom: 12px; display: flex; flex-wrap: wrap; gap: 6px;'>");
            if (StringUtils.isNotBlank(severity)) {
                String severityLabel = dictService.getDictLabel("knowledge_severity", severity.toLowerCase());
                if (StringUtils.isBlank(severityLabel)) severityLabel = severity;
                String severityColorHex = getSeverityColor(severity);
                html.append("<span style='background-color: ").append(severityColorHex).append("; color: #fff; padding: 4px 8px; border-radius: 4px; font-size: 9px; font-weight: 600; display: inline-block;'>")
                    .append(escapeHtml(severityLabel)).append("</span>");
            }
            if (StringUtils.isNotBlank(item.getLanguage())) {
                String languageLabel = dictService.getDictLabel("knowledge_language", item.getLanguage());
                html.append("<span style='background-color: #f5f5f5; color: #666; padding: 4px 8px; border-radius: 4px; font-size: 9px; border: 1px solid #d9d9d9; display: inline-block;'>")
                    .append(escapeHtml(languageLabel)).append("</span>");
            }
            if (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty()) {
                for (String vulnType : item.getVulnerabilityTypes()) {
                    if (cweMap != null && cweMap.containsKey(vulnType)) {
                        CweReferenceVo cwe = cweMap.get(vulnType);
                        String vulnName = StringUtils.isNotBlank(cwe.getNameZh()) ? cwe.getNameZh() : cwe.getNameEn();
                        if (vulnName.length() > 25) {
                            vulnName = vulnName.substring(0, 25) + "...";
                        }
                        html.append("<span style='background-color: #f5f5f5; color: #666; padding: 4px 8px; border-radius: 4px; font-size: 9px; border: 1px solid #d9d9d9; display: inline-block;'>")
                            .append(escapeHtml(vulnName)).append("</span>");
                    }
                }
            }
            html.append("</div>");
            Map<String, String> basicMeta = new LinkedHashMap<>();
            Map<String, String> cvssMeta = new LinkedHashMap<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key) || "summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    continue;
                }
                String value = String.valueOf(getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats));
                if (StringUtils.isNotBlank(value) && !"null".equals(value)) {
                    if (key.startsWith("cvss") || "cvssScore".equals(key)) {
                        cvssMeta.put(fieldInfo.getLabel(), value);
                    } else if (!"createTime".equals(key) && !"updateTime".equals(key) && 
                              !"createBy".equals(key) && !"updateBy".equals(key) &&
                              !"createByName".equals(key) && !"updateByName".equals(key) &&
                              !"kid".equals(key) && !"tags".equals(key)) {
                        basicMeta.put(fieldInfo.getLabel(), value);
                    }
                }
            }
            if (!basicMeta.isEmpty() || !cvssMeta.isEmpty()) {
                html.append("<div style='margin-bottom: 12px;'>");
                if (!basicMeta.isEmpty()) {
                    html.append("<div style='margin-bottom: 8px;'>");
                    html.append("<div style='font-size: 10px; color: #666; margin-bottom: 4px; font-weight: 600;'>基本信息</div>");
                    html.append("<table style='width: 100%; border-collapse: collapse; background-color: #f5f5f5; border: 1px solid #f5f5f5; font-size: 9px;'>");
                    for (Map.Entry<String, String> entry : basicMeta.entrySet()) {
                        html.append("<tr>");
                        html.append("<td style='width: 40%; padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #666;'>").append(escapeHtml(entry.getKey())).append(":</td>");
                        html.append("<td style='width: 60%; padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #333;'>").append(escapeHtml(entry.getValue())).append("</td>");
                        html.append("</tr>");
                    }
                    html.append("</table>");
                    html.append("</div>");
                }
                if (!cvssMeta.isEmpty()) {
                    html.append("<div style='margin-bottom: 8px;'>");
                    html.append("<div style='font-size: 10px; color: #666; margin-bottom: 4px; font-weight: 600;'>CVSS评分</div>");
                    html.append("<table style='width: 100%; border-collapse: collapse; background-color: #f5f5f5; border: 1px solid #e5e5e5; font-size: 9px;'>");
                    int colIndex = 0;
                    for (Map.Entry<String, String> entry : cvssMeta.entrySet()) {
                        if (colIndex % 6 == 0) {
                            if (colIndex > 0) html.append("</tr>");
                            html.append("<tr>");
                        }
                        html.append("<td style='padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #666;'>").append(escapeHtml(entry.getKey())).append(":</td>");
                        html.append("<td style='padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #333;'>").append(escapeHtml(entry.getValue())).append("</td>");
                        colIndex += 2;
                    }
                    if (colIndex > 0) html.append("</tr>");
                    html.append("</table>");
                    html.append("</div>");
                }
                html.append("</div>");
            }
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key)) {
                    continue;
                }
                if ("summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    String value = String.valueOf(getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats));
                    if (StringUtils.isNotBlank(value) && !"null".equals(value)) {
                        html.append("<div style='margin-bottom: 12px;'>");
                        html.append("<div style='font-weight: 600; color: #333; font-size: 11px; margin-bottom: 4px;'>")
                            .append(escapeHtml(fieldInfo.getLabel())).append("</div>");
                        html.append("<div style='border-top: 1px solid #e5e5e5; margin-bottom: 8px;'></div>");
                        if ("exampleCode".equals(key)) {
                            html.append("<pre style='background-color: #f8f8f8; border: 1px solid #dcdfe6; border-radius: 4px; padding: 8px; margin: 0; font-size: 10px; line-height: 1.5; overflow-x: auto; white-space: pre; font-family: \"Courier New\", monospace;'>")
                                .append(escapeHtml(value)).append("</pre>");
                        } else {
                            html.append("<div style='color: #333; font-size: 10px; line-height: 1.5; white-space: pre-wrap; word-wrap: break-word;'>")
                            .append(escapeHtml(value)).append("</div>");
                        }
                        html.append("</div>");
                    }
                }
            }
            if (item.getTags() != null && !item.getTags().isEmpty()) {
                html.append("<div style='margin-bottom: 12px;'>");
                html.append("<div style='font-weight: 600; color: #333; font-size: 11px; margin-bottom: 4px;'>标签</div>");
                html.append("<div style='border-top: 1px solid #e5e5e5; margin-bottom: 8px;'></div>");
                html.append("<div style='display: flex; flex-wrap: wrap; gap: 6px;'>");
                for (String tagName : item.getTags()) {
                    html.append("<span style='background-color: #f6ffed; color: #52c41a; padding: 4px 8px; border-radius: 4px; font-size: 9px; border: 1px solid #52c41a; display: inline-block;'>")
                        .append(escapeHtml(tagName)).append("</span>");
                }
                html.append("</div>");
                html.append("</div>");
            }
            List<String> footerInfo = new ArrayList<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("createTime".equals(key) && item.getCreateTime() != null) {
                    footerInfo.add("创建时间：" + formatDateTime(item.getCreateTime()));
                } else if ("updateTime".equals(key) && item.getUpdateTime() != null) {
                    footerInfo.add("更新时间：" + formatDateTime(item.getUpdateTime()));
                } else if (("createBy".equals(key) || "createByName".equals(key)) && item.getCreateBy() != null && userMap != null) {
                    String creator = userMap.get(item.getCreateBy());
                    if (StringUtils.isNotBlank(creator)) {
                        footerInfo.add("创建人：" + creator);
                    }
                } else if (("updateBy".equals(key) || "updateByName".equals(key)) && item.getUpdateBy() != null && userMap != null) {
                    String updater = userMap.get(item.getUpdateBy());
                    if (StringUtils.isNotBlank(updater)) {
                        footerInfo.add("更新人：" + updater);
                    }
                } else if ("kid".equals(key) && StringUtils.isNotBlank(item.getKid()) && knowledgeBaseMap != null) {
                    String kbName = knowledgeBaseMap.get(item.getKid());
                    if (StringUtils.isNotBlank(kbName)) {
                        footerInfo.add("知识库：" + kbName);
                    }
                }
            }
            if (!footerInfo.isEmpty()) {
                html.append("<div style='margin-top: 8px; padding-top: 8px; border-top: 1px solid #f5f5f5;'>");
                html.append("<div style='color: #999; font-size: 9px;'>").append(String.join(" | ", footerInfo)).append("</div>");
                html.append("</div>");
            }
            html.append("</div>");
        }
        html.append("</div>");
        return html.toString();
    }

    public Map<String, List<String>> extractExpandedFields(List<FieldInfoVo> fieldInfos) {
        Map<String, List<String>> expandedFields = new HashMap<>();
        if (CollectionUtils.isEmpty(fieldInfos)) {
            return expandedFields;
        }
        for (FieldInfoVo fieldInfo : fieldInfos) {
            if ("expanded".equals(fieldInfo.getType()) && StringUtils.isNotBlank(fieldInfo.getParentField())) {
                expandedFields.computeIfAbsent(fieldInfo.getParentField(), k -> new ArrayList<>())
                    .add(fieldInfo.getKey());
            }
        }
        return expandedFields;
    }

    public String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    public String getFieldValueForTemplate(KnowledgeItemVo item, String fieldKey, Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap, Map<String, String> knowledgeBaseMap, Map<String, String> fieldFormats) {
        return getFieldValue(item, fieldKey, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
    }

    public String getFieldValue(KnowledgeItemVo item, String fieldKey, Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap, Map<String, String> knowledgeBaseMap, Map<String, String> fieldFormats) {
        DictService dictService = SpringUtils.getBean(DictService.class);
        switch (fieldKey) {
            case "title": return StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : "";
            case "summary": return StringUtils.isNotBlank(item.getSummary()) ? item.getSummary() : "";
            case "problemDescription": return StringUtils.isNotBlank(item.getProblemDescription()) ? item.getProblemDescription() : "";
            case "fixSolution": return StringUtils.isNotBlank(item.getFixSolution()) ? item.getFixSolution() : "";
            case "exampleCode": return StringUtils.isNotBlank(item.getExampleCode()) ? item.getExampleCode() : "";
            case "referenceLink": return StringUtils.isNotBlank(item.getReferenceLink()) ? item.getReferenceLink() : "";
            case "severity": 
                if (StringUtils.isBlank(item.getSeverity())) return "";
                String severityLabel = dictService.getDictLabel("knowledge_severity", item.getSeverity().toLowerCase());
                return StringUtils.isNotBlank(severityLabel) ? severityLabel : item.getSeverity();
            case "vulnerabilityTypes": 
                String vulnFormat = fieldFormats != null ? fieldFormats.get("vulnerabilityTypes") : null;
                return formatVulnerabilityTypes(item.getVulnerabilityTypes(), cweMap, vulnFormat);
            case "vulnerabilityTypeName":
                String vulnType = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnType != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnType);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                        return cwe.getNameZh();
                    }
                }
                return "";
            case "vulnerabilityTypeNameEn":
                String vulnTypeEn = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeEn != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeEn);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getNameEn())) {
                        return cwe.getNameEn();
                    }
                }
                return "";
            case "vulnerabilityTypeDescription":
                String vulnTypeDesc = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeDesc != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeDesc);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getDescriptionZh())) {
                        return cwe.getDescriptionZh();
                    }
                }
                return "";
            case "vulnerabilityTypeDescriptionEn":
                String vulnTypeDescEn = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeDescEn != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeDescEn);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getDescriptionEn())) {
                        return cwe.getDescriptionEn();
                    }
                }
                return "";
            case "language": 
                if (StringUtils.isBlank(item.getLanguage())) return "";
                return dictService.getDictLabel("knowledge_language", item.getLanguage());
            case "cvssScore": return item.getCvssScore() != null ? item.getCvssScore().toString() : "";
            case "cvssAttackVector": return getCvssComponentLabel(item.getCvssVector(), "AV");
            case "cvssAttackComplexity": return getCvssComponentLabel(item.getCvssVector(), "AC");
            case "cvssPrivilegesRequired": return getCvssComponentLabel(item.getCvssVector(), "PR");
            case "cvssUserInteraction": return getCvssComponentLabel(item.getCvssVector(), "UI");
            case "cvssConfidentialityImpact": return getCvssComponentLabel(item.getCvssVector(), "VC");
            case "cvssIntegrityImpact": return getCvssComponentLabel(item.getCvssVector(), "VI");
            case "cvssAvailabilityImpact": return getCvssComponentLabel(item.getCvssVector(), "VA");
            case "status": 
                if (StringUtils.isBlank(item.getStatus())) return "";
                return dictService.getDictLabel("knowledge_item_status", item.getStatus());
            case "tags": 
                String tagFormat = fieldFormats != null ? fieldFormats.get("tags") : null;
                return formatTags(item.getTags(), tagMap, tagFormat);
            case "fragmentCount": return item.getFragmentCount() != null ? item.getFragmentCount().toString() : "";
            case "createTime": return item.getCreateTime() != null ? formatDateTime(item.getCreateTime()) : "";
            case "updateTime": return item.getUpdateTime() != null ? formatDateTime(item.getUpdateTime()) : "";
            case "createBy":
                if (item.getCreateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getCreateBy(), "");
                }
                return "";
            case "kid":
                if (StringUtils.isNotBlank(item.getKid()) && knowledgeBaseMap != null) {
                    return knowledgeBaseMap.getOrDefault(item.getKid(), "");
                }
                return "";
            case "createByName":
                if (item.getCreateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getCreateBy(), "");
                }
                return "";
            case "updateBy":
                if (item.getUpdateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getUpdateBy(), "");
                }
                return "";
            case "updateByName":
                if (item.getUpdateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getUpdateBy(), "");
                }
                return "";
            default: return "";
        }
    }

    public String formatDateTime(java.util.Date date) {
        if (date == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public String formatDataSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    public String parseCvssVector(String cvssVector) {
        if (StringUtils.isBlank(cvssVector)) {
            return "";
        }
        Map<String, String> metricLabels = new HashMap<>();
        metricLabels.put("AV:N", "网络");
        metricLabels.put("AV:A", "网络相邻");
        metricLabels.put("AV:L", "本地");
        metricLabels.put("AV:P", "物理");
        metricLabels.put("AC:L", "低");
        metricLabels.put("AC:H", "高");
        metricLabels.put("PR:N", "无");
        metricLabels.put("PR:L", "低");
        metricLabels.put("PR:H", "高");
        metricLabels.put("UI:N", "无");
        metricLabels.put("UI:R", "必需");
        metricLabels.put("UI:A", "活跃");
        metricLabels.put("AT:N", "无");
        metricLabels.put("AT:P", "存在");
        metricLabels.put("VC:H", "高");
        metricLabels.put("VC:L", "低");
        metricLabels.put("VC:N", "无");
        metricLabels.put("VI:H", "高");
        metricLabels.put("VI:L", "低");
        metricLabels.put("VI:N", "无");
        metricLabels.put("VA:H", "高");
        metricLabels.put("VA:L", "低");
        metricLabels.put("VA:N", "无");
        metricLabels.put("SC:H", "高");
        metricLabels.put("SC:L", "低");
        metricLabels.put("SC:N", "无");
        metricLabels.put("SI:H", "高");
        metricLabels.put("SI:L", "低");
        metricLabels.put("SI:N", "无");
        metricLabels.put("SA:H", "高");
        metricLabels.put("SA:L", "低");
        metricLabels.put("SA:N", "无");
        Map<String, String> metricNames = new HashMap<>();
        metricNames.put("AV", "攻击方式");
        metricNames.put("AC", "利用复杂度");
        metricNames.put("PR", "权限需求");
        metricNames.put("UI", "用户交互");
        metricNames.put("AT", "攻击要求");
        metricNames.put("VC", "机密性影响");
        metricNames.put("VI", "完整性影响");
        metricNames.put("VA", "可用性影响");
        metricNames.put("SC", "后续机密性影响");
        metricNames.put("SI", "后续完整性影响");
        metricNames.put("SA", "后续可用性影响");
        List<String> components = new ArrayList<>();
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    String metric = kv[0].trim();
                    String value = kv[1].trim();
                    String metricName = metricNames.getOrDefault(metric, metric);
                    String label = metricLabels.getOrDefault(metric + ":" + value, value);
                    components.add(metricName + "：" + label);
                }
            }
        }
        return String.join("；", components);
    }

    public List<Map<String, Object>> convertToMapList(List<KnowledgeItemVo> items, List<String> selectedFields, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        List<FieldInfoVo> fieldInfos = buildFieldInfos(selectedFields, expandedFields);
        Map<String, CweReferenceVo> cweMap = buildCweMap(items, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(items, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(items, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(items, selectedFields, expandedFields);
        List<Map<String, Object>> result = new ArrayList<>();
        for (KnowledgeItemVo item : items) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String fieldKey = fieldInfo.getKey();
                Object fieldValue = getFieldValue(item, fieldKey, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                map.put(fieldKey, fieldValue);
            }
            result.add(map);
        }
        return result;
    }

    public Map<String, CweReferenceVo> buildCweMap(List<KnowledgeItemVo> items, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        Map<String, CweReferenceVo> cweMap = new HashMap<>();
        boolean needCwe = false;
        if (expandedFields != null && (expandedFields.containsKey("vulnerabilityType") || expandedFields.containsKey("vulnerabilityTypes"))) {
            needCwe = true;
        }
        if (fieldFormats != null && (fieldFormats.containsKey("vulnerabilityTypes") || fieldFormats.containsKey("vulnerabilityType"))) {
            String format = fieldFormats.get("vulnerabilityTypes");
            if (format == null) format = fieldFormats.get("vulnerabilityType");
            if (format != null) {
                needCwe = true;
            }
        }
        if (!needCwe) {
            return cweMap;
        }
        Set<String> cweIds = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getVulnerabilityTypes() != null) {
                cweIds.addAll(item.getVulnerabilityTypes());
            }
            if (StringUtils.isNotBlank(item.getVulnerabilityType())) {
                cweIds.add(item.getVulnerabilityType());
            }
        }
        if (!cweIds.isEmpty()) {
            List<CweReferenceVo> cweList = cweReferenceMapper.selectVoList(
                Wrappers.<CweReference>lambdaQuery().in(CweReference::getCweId, cweIds)
            );
            for (CweReferenceVo cwe : cweList) {
                cweMap.put(cwe.getCweId(), cwe);
            }
        }
        return cweMap;
    }

    public Map<String, KnowledgeTagVo> buildTagMap(List<KnowledgeItemVo> items, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        Map<String, KnowledgeTagVo> tagMap = new HashMap<>();
        boolean needTag = false;
        if (expandedFields != null && expandedFields.containsKey("tags")) {
            needTag = true;
        }
        if (fieldFormats != null && fieldFormats.containsKey("tags")) {
            String format = fieldFormats.get("tags");
            if (format != null && "full".equals(format)) {
                needTag = true;
            }
        }
        if (!needTag) {
            return tagMap;
        }
        Set<String> tagNames = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getTags() != null) {
                tagNames.addAll(item.getTags());
            }
        }
        if (!tagNames.isEmpty()) {
            List<KnowledgeTagVo> tagList = knowledgeTagMapper.selectVoList(
                Wrappers.<KnowledgeTag>lambdaQuery().in(KnowledgeTag::getTagName, tagNames)
            );
            for (KnowledgeTagVo tag : tagList) {
                tagMap.put(tag.getTagName(), tag);
            }
        }
        return tagMap;
    }

    public Map<Long, String> buildUserMap(List<KnowledgeItemVo> items, List<String> selectedFields) {
        Map<Long, String> userMap = new HashMap<>();
        if (selectedFields == null) {
            return userMap;
        }
        boolean needUser = selectedFields.contains("createBy") || selectedFields.contains("updateBy");
        if (!needUser) {
            return userMap;
        }
        Set<Long> userIds = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getCreateBy() != null) {
                userIds.add(item.getCreateBy());
            }
            if (item.getUpdateBy() != null) {
                userIds.add(item.getUpdateBy());
            }
        }
        if (!userIds.isEmpty()) {
            for (Long userId : userIds) {
                SysUserVo user = sysUserService.selectUserById(userId);
                if (user != null && StringUtils.isNotBlank(user.getUserName())) {
                    userMap.put(userId, user.getUserName());
                }
            }
        }
        return userMap;
    }

    public Map<String, String> buildKnowledgeBaseMap(List<KnowledgeItemVo> items, List<String> selectedFields, Map<String, List<String>> expandedFields) {
        Map<String, String> knowledgeBaseMap = new HashMap<>();
        //如果选择了kid字段，需要构建映射
        boolean needKnowledgeBase = (selectedFields != null && selectedFields.contains("kid")) 
            || (expandedFields != null && expandedFields.containsKey("kid"));
        if (!needKnowledgeBase) {
            return knowledgeBaseMap;
        }
        Set<String> kids = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (StringUtils.isNotBlank(item.getKid())) {
                kids.add(item.getKid());
            }
        }
        if (!kids.isEmpty()) {
            for (String kid : kids) {
                KnowledgeInfoVo info = knowledgeInfoMapper.selectVoByKid(kid);
                if (info != null && StringUtils.isNotBlank(info.getKname())) {
                    knowledgeBaseMap.put(kid, info.getKname());
                }
            }
        }
        return knowledgeBaseMap;
    }

    public String formatVulnerabilityTypes(List<String> vulnerabilityTypes, Map<String, CweReferenceVo> cweMap, String format) {
        if (CollectionUtils.isEmpty(vulnerabilityTypes)) {
            return "";
        }
        List<String> formatted = new ArrayList<>();
        for (String cweId : vulnerabilityTypes) {
            CweReferenceVo cwe = cweMap != null ? cweMap.get(cweId) : null;
            if (format == null || "name_only".equals(format)) {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            } else if ("id_name".equals(format)) {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cweId + ": " + cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            } else if ("full".equals(format)) {
                StringBuilder sb = new StringBuilder();
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    sb.append(cwe.getNameZh());
                    if (StringUtils.isNotBlank(cwe.getDescriptionZh())) {
                        String desc = cwe.getDescriptionZh();
                        if (desc.length() > 50) {
                            desc = desc.substring(0, 50) + "...";
                        }
                        sb.append("（").append(desc).append("）");
                    }
                } else {
                    sb.append(cweId);
                }
                formatted.add(sb.toString());
            } else {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            }
        }
        return String.join(", ", formatted);
    }

    public String formatTags(List<String> tags, Map<String, KnowledgeTagVo> tagMap, String format) {
        if (CollectionUtils.isEmpty(tags)) {
            return "";
        }
        List<String> formatted = new ArrayList<>();
        for (String tagName : tags) {
            KnowledgeTagVo tag = tagMap != null ? tagMap.get(tagName) : null;
            if (format == null || "name_only".equals(format)) {
                formatted.add(tagName);
            } else if ("full".equals(format)) {
                StringBuilder sb = new StringBuilder(tagName);
                if (tag != null) {
                    if (StringUtils.isNotBlank(tag.getTagCategory())) {
                        sb.append(" [").append(tag.getTagCategory()).append("]");
                    }
                    if (StringUtils.isNotBlank(tag.getDescription())) {
                        String desc = tag.getDescription();
                        if (desc.length() > 30) {
                            desc = desc.substring(0, 30) + "...";
                        }
                        sb.append("（").append(desc).append("）");
                    }
                }
                formatted.add(sb.toString());
            } else {
                formatted.add(tagName);
            }
        }
        return String.join(", ", formatted);
    }

    public String generateDefaultFileName(String format) {
        String extension = "excel".equals(format) ? "xlsx" : "pdf";
        return "知识条目导出_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "." + extension;
    }

    // --- Helper methods used internally ---

    private String parseCvssField(String cvssVector, String metric) {
        if (StringUtils.isBlank(cvssVector)) {
            return null;
        }
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.startsWith(metric + ":")) {
                return part.substring(metric.length() + 1);
            }
        }
        return null;
    }

    private String getCvssComponentLabel(String cvssVector, String metric) {
        if (StringUtils.isBlank(cvssVector)) {
            return "";
        }
        String value = parseCvssField(cvssVector, metric);
        if (StringUtils.isBlank(value)) {
            return "";
        }
        Map<String, String> metricLabels = new HashMap<>();
        metricLabels.put("AV:N", "网络");
        metricLabels.put("AV:A", "网络相邻");
        metricLabels.put("AV:L", "本地");
        metricLabels.put("AV:P", "物理");
        metricLabels.put("AC:L", "低");
        metricLabels.put("AC:H", "高");
        metricLabels.put("PR:N", "无");
        metricLabels.put("PR:L", "低");
        metricLabels.put("PR:H", "高");
        metricLabels.put("UI:N", "无");
        metricLabels.put("UI:R", "必需");
        metricLabels.put("UI:A", "活跃");
        metricLabels.put("VC:H", "高");
        metricLabels.put("VC:L", "低");
        metricLabels.put("VC:N", "无");
        metricLabels.put("VI:H", "高");
        metricLabels.put("VI:L", "低");
        metricLabels.put("VI:N", "无");
        metricLabels.put("VA:H", "高");
        metricLabels.put("VA:L", "低");
        metricLabels.put("VA:N", "无");
        String key = metric + ":" + value;
        return metricLabels.getOrDefault(key, value);
    }

    private boolean shouldUseReportFormat(List<FieldInfoVo> fieldInfos) {
        if (CollectionUtils.isEmpty(fieldInfos)) {
            return false;
        }
        int fieldCount = fieldInfos.size();
        boolean hasLongTextFields = false;
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("summary".equals(key) || "problemDescription".equals(key) || 
                "fixSolution".equals(key) || "exampleCode".equals(key)) {
                hasLongTextFields = true;
                break;
            }
        }
        return hasLongTextFields || fieldCount <= 6;
    }

    private String getSeverityColor(String severity) {
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
}
