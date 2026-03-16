package org.ruoyi.knowledge.curation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.knowledge.curation.domain.bo.ExcelOptionsBo;
import org.ruoyi.knowledge.curation.domain.bo.ExportRequestBo;
import org.ruoyi.knowledge.curation.domain.vo.FieldInfoVo;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识条目Excel导出服务
 * 从KnowledgeItemServiceImpl中提取的Excel导出相关方法
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeItemExcelExportService {

    /**
     * 导出Excel
     * 注意：exportData和fieldInfos由调用方预先计算（原convertToMapList和buildFieldInfos方法）
     */
    public void exportToExcel(List<Map<String, Object>> exportData, List<FieldInfoVo> fieldInfos, ExportRequestBo request, String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        ExcelOptionsBo excelOptions = request.getExcelOptions();
        boolean freezeHeader = excelOptions != null && Boolean.TRUE.equals(excelOptions.getFreezeHeader());
        boolean includeFilter = excelOptions != null && Boolean.TRUE.equals(excelOptions.getIncludeFilter());
        boolean conditionalFormatting = excelOptions != null && Boolean.TRUE.equals(excelOptions.getConditionalFormatting());
        Map<String, Integer> columnWidths = request.getColumnWidths() != null ? request.getColumnWidths() : new HashMap<>();
        OutputStream outputStream = response.getOutputStream();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("知识条目");
            int rowIndex = 0;
            int columnCount = fieldInfos.size();
            XSSFCellStyle headerStyle = createHeaderStyle(workbook);
            XSSFCellStyle oddRowStyle = createOddRowStyle(workbook);
            XSSFCellStyle evenRowStyle = createEvenRowStyle(workbook);
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < fieldInfos.size(); i++) {
                FieldInfoVo fieldInfo = fieldInfos.get(i);
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(fieldInfo.getLabel());
                cell.setCellStyle(headerStyle);
                int excelColumnWidth = convertPixelToExcelWidth(columnWidths.getOrDefault(fieldInfo.getKey(), calculateDefaultColumnWidth(fieldInfo, exportData)));
                sheet.setColumnWidth(i, excelColumnWidth);
            }
            for (int dataIndex = 0; dataIndex < exportData.size(); dataIndex++) {
                Map<String, Object> item = exportData.get(dataIndex);
                Row row = sheet.createRow(rowIndex++);
                boolean isOddRow = (dataIndex + 1) % 2 == 1;
                XSSFCellStyle baseStyle = isOddRow ? oddRowStyle : evenRowStyle;
                for (int i = 0; i < fieldInfos.size(); i++) {
                    FieldInfoVo fieldInfo = fieldInfos.get(i);
                    org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);
                    Object value = item.getOrDefault(fieldInfo.getKey(), "");
                    setCellValue(cell, value);
                    XSSFCellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.cloneStyleFrom(baseStyle);
                    if (conditionalFormatting && ("severity".equals(fieldInfo.getKey()) || "severityLabel".equals(fieldInfo.getKey()))) {
                        applySeverityConditionalFormatting(cell, cellStyle, workbook, String.valueOf(value));
                    }
                    cell.setCellStyle(cellStyle);
                }
            }
            if (freezeHeader) {
                sheet.createFreezePane(0, 1);
            }
            if (includeFilter && columnCount > 0) {
                CellRangeAddress filterRange = new CellRangeAddress(0, 0, 0, columnCount - 1);
                sheet.setAutoFilter(filterRange);
            }
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new RuntimeException("导出Excel异常: " + e.getMessage(), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.warn("关闭Workbook异常", e);
                }
            }
        }
    }

    //创建表头样式：深色背景+白色文字
    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new Color(0, 51, 102), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(new XSSFColor(Color.BLACK, null));
        style.setBottomBorderColor(new XSSFColor(Color.BLACK, null));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK, null));
        style.setRightBorderColor(new XSSFColor(Color.BLACK, null));
        XSSFFont font = workbook.createFont();
        font.setColor(new XSSFColor(Color.WHITE, null));
        font.setBold(true);
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        return style;
    }
    //创建偶数行样式：白色背景
    private XSSFCellStyle createEvenRowStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(Color.WHITE, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(new XSSFColor(Color.BLACK, null));
        style.setBottomBorderColor(new XSSFColor(Color.BLACK, null));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK, null));
        style.setRightBorderColor(new XSSFColor(Color.BLACK, null));
        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }
    //创建奇数行样式：浅灰色背景
    private XSSFCellStyle createOddRowStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new Color(242, 242, 242), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(new XSSFColor(Color.BLACK, null));
        style.setBottomBorderColor(new XSSFColor(Color.BLACK, null));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK, null));
        style.setRightBorderColor(new XSSFColor(Color.BLACK, null));
        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }
    //设置单元格值（处理不同类型）
    private void setCellValue(org.apache.poi.ss.usermodel.Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof Number) {
            if (value instanceof Double || value instanceof Float) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof Long || value instanceof Integer || value instanceof Short || value instanceof Byte) {
                cell.setCellValue(((Number) value).longValue());
            } else {
                cell.setCellValue(value.toString());
            }
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
    //应用风险等级条件格式
    private void applySeverityConditionalFormatting(org.apache.poi.ss.usermodel.Cell cell, XSSFCellStyle cellStyle, XSSFWorkbook workbook, String cellValue) {
        if (StringUtils.isBlank(cellValue)) {
            return;
        }
        XSSFFont font = workbook.createFont();
        XSSFFont baseFont = (XSSFFont) cellStyle.getFont();
        if (baseFont != null) {
            font.setFontName(baseFont.getFontName());
            font.setFontHeightInPoints(baseFont.getFontHeightInPoints());
        } else {
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 11);
        }
        String lowerValue = cellValue.toLowerCase();
        if (lowerValue.contains("高") || lowerValue.contains("high")) {
            cellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 230, 230), null));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setColor(new XSSFColor(new Color(204, 0, 0), null));
            cellStyle.setFont(font);
        } else if (lowerValue.contains("中") || lowerValue.contains("medium")) {
            cellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 244, 230), null));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setColor(new XSSFColor(new Color(230, 115, 0), null));
            cellStyle.setFont(font);
        } else if (lowerValue.contains("低") || lowerValue.contains("low")) {
            cellStyle.setFillForegroundColor(new XSSFColor(new Color(230, 247, 230), null));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setColor(new XSSFColor(new Color(0, 102, 0), null));
            cellStyle.setFont(font);
        }
    }
    //将像素宽度转换为Excel列宽单位
    //Excel列宽单位：1个单位 = 1/256字符宽度，默认字符宽度约7像素
    //转换公式：excelWidth = (pixelWidth / 7) * 256
    private int convertPixelToExcelWidth(int pixelWidth) {
        if (pixelWidth <= 0) {
            return 2560;
        }
        double excelWidth = (pixelWidth / 7.0) * 256;
        int result = (int) Math.round(excelWidth);
        return Math.max(256, Math.min(result, 65535));
    }
    //计算默认列宽（与前端逻辑保持一致）
    private int calculateDefaultColumnWidth(FieldInfoVo fieldInfo, List<Map<String, Object>> exportData) {
        String fieldKey = fieldInfo.getKey();
        String fieldLabel = fieldInfo.getLabel();
        int headerWidth = estimateTextWidth(fieldLabel, 13);
        int headerPadding = 60;
        int baseWidth = Math.max(headerWidth + headerPadding, 100);
        if (exportData != null && !exportData.isEmpty()) {
            int maxContentWidth = headerWidth;
            for (Map<String, Object> row : exportData) {
                Object value = row.get(fieldKey);
                if (value != null) {
                    String contentText = String.valueOf(value);
                    int contentWidth = estimateTextWidth(contentText, 12);
                    if (contentWidth > maxContentWidth) {
                        maxContentWidth = contentWidth;
                    }
                }
            }
            if (maxContentWidth > headerWidth) {
                int contentPadding = 60;
                int contentBasedWidth = maxContentWidth + contentPadding;
                baseWidth = Math.max(baseWidth, contentBasedWidth);
            }
        }
        Map<String, Integer> specialMinWidths = new HashMap<>();
        specialMinWidths.put("title", 180);
        specialMinWidths.put("summary", 300);
        specialMinWidths.put("itemUuid", 280);
        specialMinWidths.put("problemDescription", 400);
        specialMinWidths.put("fixSolution", 400);
        specialMinWidths.put("exampleCode", 400);
        specialMinWidths.put("referenceLink", 300);
        if (specialMinWidths.containsKey(fieldKey)) {
            baseWidth = Math.max(baseWidth, specialMinWidths.get(fieldKey));
        }
        Map<String, Integer> maxWidths = new HashMap<>();
        maxWidths.put("title", 400);
        maxWidths.put("summary", 600);
        maxWidths.put("problemDescription", 800);
        maxWidths.put("fixSolution", 800);
        maxWidths.put("exampleCode", 800);
        maxWidths.put("referenceLink", 500);
        int maxWidth = maxWidths.getOrDefault(fieldKey, 600);
        return Math.max(100, Math.min(baseWidth, maxWidth));
    }
    //估算文本宽度（像素）
    private int estimateTextWidth(String text, int fontSize) {
        if (StringUtils.isBlank(text)) {
            return 0;
        }
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 0x4e00 && ch <= 0x9fa5 || (ch >= 0x3000 && ch <= 0x303f) || (ch >= 0xff00 && ch <= 0xffef)) {
                width += fontSize * 1.2;
            } else {
                width += fontSize * 0.6;
            }
        }
        return width;
    }
}
