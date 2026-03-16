package org.ruoyi.knowledge.curation.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.SpringUtils;
import org.ruoyi.common.core.service.DictService;
import org.ruoyi.knowledge.enrichment.service.HighlightJsService;
import org.ruoyi.knowledge.curation.domain.KnowledgeItem;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeItemBo;
import org.ruoyi.knowledge.curation.domain.bo.ExportRequestBo;
import org.ruoyi.knowledge.curation.domain.bo.PdfOptionsBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeTagVo;
import org.ruoyi.knowledge.curation.domain.vo.FieldInfoVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweReferenceVo;
import org.ruoyi.knowledge.shared.utils.CvssScoreCalculator;
import org.ruoyi.knowledge.curation.service.util.PdfTemplateUtils;
import org.ruoyi.knowledge.curation.service.util.PageNumberCollectorRenderer;
import org.ruoyi.knowledge.curation.service.util.PreserveWhitespaceTextRenderer;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.ruoyi.knowledge.curation.mapper.KnowledgeInfoMapper;
import org.ruoyi.system.service.ISysUserService;
import org.ruoyi.system.service.ISysDeptService;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.OverflowPropertyValue;
import com.itextpdf.layout.properties.ParagraphOrphansControl;
import com.itextpdf.layout.properties.ParagraphWidowsControl;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

/**
 * 知识条目PDF导出服务
 * 从KnowledgeItemServiceImpl中提取的PDF导出相关方法
 */
@Slf4j
@Service
public class KnowledgeItemPdfExportService {

    private final IKnowledgeItemService knowledgeItemService;
    private final KnowledgeItemExportDataService exportDataService;
    private final HighlightJsService highlightJsService;
    private final ISysUserService sysUserService;
    private final ISysDeptService sysDeptService;
    private final KnowledgeInfoMapper knowledgeInfoMapper;

    public KnowledgeItemPdfExportService(
            IKnowledgeItemService knowledgeItemService,
            @Lazy KnowledgeItemExportDataService exportDataService,
            HighlightJsService highlightJsService,
            ISysUserService sysUserService,
            ISysDeptService sysDeptService,
            KnowledgeInfoMapper knowledgeInfoMapper) {
        this.knowledgeItemService = knowledgeItemService;
        this.exportDataService = exportDataService;
        this.highlightJsService = highlightJsService;
        this.sysUserService = sysUserService;
        this.sysDeptService = sysDeptService;
        this.knowledgeInfoMapper = knowledgeInfoMapper;
    }

    //流式PDF生成：使用Velocity模板+OpenHTML to PDF方案
    public void exportToPdfStreaming(ExportRequestBo request, String fileName, HttpServletResponse response, List<String> warnings) throws IOException {
        response.setContentType("application/pdf");
        String originalFileName = fileName;
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName);
        List<FieldInfoVo> fieldInfos = exportDataService.buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new ServiceException("请至少选择一个导出字段");
        }
        PdfOptionsBo pdfOptions = request.getPdfOptions();
        boolean useReportFormat;
        if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
            useReportFormat = "report".equals(pdfOptions.getFormatType());
        } else {
            useReportFormat = shouldUseReportFormat(fieldInfos);
        }
        boolean includeHeaderFooter = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getIncludeHeaderFooter());
        boolean includeTOC = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getIncludeTOC());
        boolean codeHighlight = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getCodeHighlight());
        log.info("PDF流式导出(Velocity+OpenHTML) - 初始化参数: useReportFormat={}, includeHeaderFooter={}, includeTOC={}, codeHighlight={}", useReportFormat, includeHeaderFooter, includeTOC, codeHighlight);
        OutputStream outputStream = response.getOutputStream();
        log.info("PDF流式导出(Velocity+OpenHTML) - 开始获取导出数据");
        List<KnowledgeItemVo> allData = exportDataService.getExportData(request, null);
        if (CollectionUtils.isEmpty(allData)) {
            throw new ServiceException("没有可导出的数据");
        }
        log.info("PDF流式导出(Velocity+OpenHTML) - 获取到{}条数据", allData.size());
        List<String> selectedFields = fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList());
        Map<String, List<String>> expandedFields = exportDataService.extractExpandedFields(fieldInfos);
        Map<String, CweReferenceVo> cweMap = exportDataService.buildCweMap(allData, expandedFields, request.getFieldFormats());
        Map<String, KnowledgeTagVo> tagMap = exportDataService.buildTagMap(allData, expandedFields, request.getFieldFormats());
        Map<Long, String> userMap = exportDataService.buildUserMap(allData, selectedFields);
        Map<String, String> knowledgeBaseMap = exportDataService.buildKnowledgeBaseMap(allData, selectedFields, expandedFields);
        DictService dictService = SpringUtils.getBean(DictService.class);
        HighlightJsService highlightJsService = SpringUtils.getBean(HighlightJsService.class);
        log.info("PDF流式导出(Velocity+OpenHTML) - 数据准备完成，开始初始化Velocity");
        initializeVelocity();
        PdfTemplateUtils utils = new PdfTemplateUtils();
        utils.setServiceInstance(knowledgeItemService);
        utils.setExportDataService(exportDataService);
        VelocityContext context = new VelocityContext();
        context.put("items", allData);
        context.put("fieldInfos", fieldInfos);
        context.put("cweMap", cweMap);
        context.put("tagMap", tagMap);
        context.put("userMap", userMap);
        context.put("knowledgeBaseMap", knowledgeBaseMap);
        context.put("fieldFormats", request.getFieldFormats());
        context.put("dictService", dictService);
        context.put("utils", utils);
        context.put("highlightJsService", highlightJsService);
        context.put("includeTOC", includeTOC);
        context.put("codeHighlight", codeHighlight);
        String headerText = includeHeaderFooter ? originalFileName : "";
        context.put("headerText", headerText);
        context.put("includeHeaderFooter", includeHeaderFooter);
        List<Map<String, Object>> tocItems = new ArrayList<>();
        if (includeTOC && useReportFormat) {
            for (int i = 0; i < allData.size(); i++) {
                KnowledgeItemVo item = allData.get(i);
                Map<String, Object> tocItem = new HashMap<>();
                tocItem.put("title", item.getTitle() != null ? item.getTitle() : ("条目 " + (i + 1)));
                tocItem.put("index", i + 1);
                tocItem.put("anchor", "item-" + (i + 1));
                tocItems.add(tocItem);
            }
        }
        context.put("tocItems", tocItems);
        String templatePath = useReportFormat ? "templates/pdf/report-format.vm" : "templates/pdf/table-format.vm";
        log.info("PDF流式导出(Velocity+OpenHTML) - 开始渲染Velocity模板: {}", templatePath);
        String html = renderVelocityTemplate(templatePath, context);
        log.info("PDF流式导出(Velocity+OpenHTML) - Velocity模板渲染完成，HTML长度: {}", html.length());
        log.info("PDF流式导出(Velocity+OpenHTML) - 开始使用OpenHTML to PDF转换");
        convertHtmlToPdfWithOpenHtml(html, outputStream, includeHeaderFooter, headerText);
        log.info("PDF流式导出(Velocity+OpenHTML) - PDF生成完成，共处理{}条数据", allData.size());
    }

    //旧版iText原生API方案（保留作为备用）
    @SuppressWarnings("unused")
    public void exportToPdfStreamingOld(ExportRequestBo request, String fileName, HttpServletResponse response, List<String> warnings) throws IOException {
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        List<FieldInfoVo> fieldInfos = exportDataService.buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new ServiceException("请至少选择一个导出字段");
        }
        PdfOptionsBo pdfOptions = request.getPdfOptions();
        boolean useReportFormat;
        if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
            useReportFormat = "report".equals(pdfOptions.getFormatType());
        } else {
            useReportFormat = shouldUseReportFormat(fieldInfos);
        }
        boolean includeTOC = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getIncludeTOC());
        boolean includeHeaderFooter = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getIncludeHeaderFooter());
        boolean codeHighlight = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getCodeHighlight());
        boolean needTOC = includeTOC && useReportFormat;
        log.info("PDF流式导出 - 初始化参数: needTOC={}, useReportFormat={}, codeHighlight={}", needTOC, useReportFormat, codeHighlight);
        OutputStream finalOutputStream = response.getOutputStream();
        ByteArrayOutputStream tempOutputStream = needTOC ? new ByteArrayOutputStream() : null;
        OutputStream outputStream = needTOC ? tempOutputStream : finalOutputStream;
        log.info("PDF流式导出 - 输出流初始化完成: needTOC={}, tempOutputStream={}", needTOC, tempOutputStream != null);
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        log.info("PDF流式导出 - PdfDocument创建完成");
        pdfDoc.setTagged();
        log.info("PDF流式导出 - setTagged()设置完成");
        if (needTOC) {
            pdfDoc.initializeOutlines();
            log.info("PDF流式导出 - initializeOutlines()调用完成");
        }
        Document document = new Document(pdfDoc, PageSize.A4, !needTOC);
        log.info("PDF流式导出 - Document创建完成: immediateFlush={}", !needTOC);
        document.setMargins(36, 36, 36, 36);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontChinese = null;
        PdfFont fontChineseBold = null;
        String[] fontPaths = {
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Bold.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Bold.ttc",
            "/usr/share/fonts/wenquanyi/wqy-zenhei/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/msttcore/SimSun.ttf",
            "/usr/share/fonts/truetype/msttcore/SimHei.ttf",
            "C:/Windows/Fonts/simsun.ttc",
            "C:/Windows/Fonts/simhei.ttf",
            "C:/Windows/Fonts/msyh.ttf",
            System.getProperty("java.home") + "/lib/fonts/SimSun.ttf"
        };
        boolean fontLoaded = false;
        String loadedFontPath = null;
        log.info("开始查找中文字体文件，共{}个路径", fontPaths.length);
        for (String fontPath : fontPaths) {
            try {
                java.io.File fontFile = new java.io.File(fontPath);
                boolean exists = fontFile.exists();
                boolean isFile = fontFile.isFile();
                log.debug("检查字体文件: {}, 存在: {}, 是文件: {}", fontPath, exists, isFile);
                if (exists && isFile) {
                    if (fontPath.toLowerCase().endsWith(".ttc")) {
                        FontProgram fontProgram = FontProgramFactory.createFont(fontPath, 0, false);
                        fontChinese = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTC文件加载中文字体: {}", fontPath);
                        break;
                    } else {
                        fontChinese = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTF文件加载中文字体: {}", fontPath);
                        break;
                    }
                } else {
                    log.debug("字体文件不存在或不是文件: {}", fontPath);
                }
            } catch (Exception e) {
                log.warn("尝试从文件加载字体失败: {}, 错误: {}", fontPath, e.getMessage());
            }
        }
        if (!fontLoaded) {
            log.warn("所有字体文件路径都未找到字体文件，将尝试其他方式加载");
        }
        if (fontLoaded && loadedFontPath != null) {
            String boldFontPath = loadedFontPath.replace("Regular", "Bold");
            try {
                java.io.File boldFontFile = new java.io.File(boldFontPath);
                if (boldFontFile.exists() && boldFontFile.isFile()) {
                    if (boldFontPath.toLowerCase().endsWith(".ttc")) {
                        FontProgram boldFontProgram = FontProgramFactory.createFont(boldFontPath, 0, false);
                        fontChineseBold = PdfFontFactory.createFont(boldFontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    } else {
                        fontChineseBold = PdfFontFactory.createFont(boldFontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    }
                    log.info("成功加载中文字体粗体: {}", boldFontPath);
                } else {
                    fontChineseBold = fontChinese;
                    log.info("未找到对应的粗体字体，使用常规字体作为粗体");
                }
            } catch (Exception e) {
                log.warn("加载粗体字体失败，使用常规字体: {}", e.getMessage());
                fontChineseBold = fontChinese;
            }
        }
        if (!fontLoaded) {
            try {
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/simsun.ttf");
                if (fontStream == null) {
                    fontStream = getClass().getClassLoader().getResourceAsStream("fonts/SimSun.ttf");
                }
                if (fontStream != null) {
                    byte[] fontBytes = fontStream.readAllBytes();
                    fontStream.close();
                    fontChinese = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = fontChinese;
                    fontLoaded = true;
                    log.info("从resources加载中文字体成功");
                }
            } catch (Exception e) {
                log.debug("从resources加载字体失败");
            }
        }
        if (!fontLoaded) {
            log.warn("尝试使用字体名称加载（可能无法正确渲染中文）");
            try {
                fontChinese = PdfFontFactory.createFont("STSong-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontChineseBold = PdfFontFactory.createFont("STSongStd-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontLoaded = true;
                log.warn("使用字体名称加载成功（但可能无法正确渲染中文）: STSong-Light");
            } catch (Exception e1) {
                try {
                    fontChinese = PdfFontFactory.createFont("SimSun", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontLoaded = true;
                } catch (Exception e2) {
                    try {
                        fontChinese = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        fontChineseBold = fontChinese;
                        fontLoaded = true;
                    } catch (Exception e3) {
                        try {
                            fontChinese = PdfFontFactory.createFont("Microsoft YaHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                            fontChineseBold = fontChinese;
                            fontLoaded = true;
                        } catch (Exception e4) {
                            log.error("无法加载中文字体，将使用Helvetica（中文可能无法正确显示）", e4);
                            fontChinese = font;
                            fontChineseBold = fontBold;
                        }
                    }
                }
            }
        }
        if (fontChinese == null || fontChineseBold == null) {
            log.error("中文字体加载失败，PDF中的中文内容可能无法正确显示");
            fontChinese = font;
            fontChineseBold = fontBold;
        } else {
            String fontName = fontChinese.getFontProgram().getFontNames().getFontName();
            String boldFontName = fontChineseBold.getFontProgram().getFontNames().getFontName();
            log.info("中文字体加载成功: regular={}, bold={}", fontName, boldFontName);
            try {
                String testText = "测试";
                float width = fontChinese.getWidth(testText, 12);
                if (width <= 0) {
                    log.warn("字体可能不支持中文字符，测试文本宽度为0");
                } else {
                    log.info("字体中文字符测试通过，测试文本宽度: {}", width);
                }
            } catch (Exception e) {
                log.warn("字体中文字符测试失败", e);
            }
        }
        if (includeHeaderFooter) {
            HeaderFooterHandler handler = new HeaderFooterHandler(font, fontChinese, fileName);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        }
        long totalCount = exportDataService.getExportDataCount(request);
        log.info("PDF流式导出开始，总数据量: {}", totalCount);
        if (needTOC && totalCount > 1000) {
            log.info("数据量过大({}条)，跳过目录生成", totalCount);
            warnings.add("数据量过大(" + totalCount + "条)，已跳过目录生成");
            needTOC = false;
        }
        Map<String, Integer> pageNumberMap = new HashMap<>();
        if (needTOC && totalCount > 0 && totalCount <= 1000) {
            log.info("将使用两遍渲染策略：先收集页码，再生成目录");
        }
        final int BATCH_SIZE = 100;
        int processedCount = 0;
        int itemIndex = 0;
        KnowledgeItemBo bo = buildExportQueryBo(request);
        Table sharedTable = null;
        if (!useReportFormat) {
            sharedTable = new Table(fieldInfos.size()).useAllAvailableWidth();
            sharedTable.setMarginBottom(20);
            sharedTable.setKeepTogether(false);
            for (FieldInfoVo fieldInfo : fieldInfos) {
                PdfFont headerFont = fontChinese != null && fontChinese != font ? fontChinese : fontBold;
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell().add(new Paragraph(fieldInfo.getLabel())
                    .setFont(headerFont)
                    .setFontSize(10));
                headerCell.setBackgroundColor(new DeviceRgb(245, 245, 245));
                headerCell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
                headerCell.setTextAlignment(TextAlignment.CENTER);
                headerCell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
                headerCell.setPadding(5);
                sharedTable.addHeaderCell(headerCell);
            }
            document.add(sharedTable);
        }
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            List<String> itemUuids = request.getItemUuids();
            int totalBatches = (itemUuids.size() + BATCH_SIZE - 1) / BATCH_SIZE;
            for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
                int start = batchIndex * BATCH_SIZE;
                int end = Math.min(start + BATCH_SIZE, itemUuids.size());
                List<String> batchUuids = itemUuids.subList(start, end);
                bo.setItemUuids(batchUuids);
                List<KnowledgeItemVo> batchData = knowledgeItemService.queryList(bo);
                if (CollectionUtils.isNotEmpty(batchData)) {
                    if (useReportFormat) {
                        generateReportFormatBatch(document, pdfDoc, batchData, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, itemIndex, pageNumberMap);
                    } else {
                        generateTableFormatBatch(sharedTable, batchData, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
                    }
                    itemIndex += batchData.size();
                    processedCount += batchData.size();
                    log.info("PDF流式导出进度: {}/{}", processedCount, totalCount);
                    batchData = null;
                    System.gc();
                }
            }
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
            PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
            TableDataInfo<KnowledgeItemVo> pageData = knowledgeItemService.queryPageList(bo, pageQuery);
            List<KnowledgeItemVo> batchData = pageData.getRows();
            if (CollectionUtils.isNotEmpty(batchData)) {
                if (useReportFormat) {
                    generateReportFormatBatch(document, pdfDoc, batchData, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, itemIndex, pageNumberMap);
                } else {
                    generateTableFormatBatch(sharedTable, batchData, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
                }
                processedCount += batchData.size();
                log.info("PDF流式导出进度: {}/{}", processedCount, totalCount);
            }
        } else {
            int pageNum = 1;
            int pageSize = BATCH_SIZE;
            while (true) {
                PageQuery pageQuery = new PageQuery(pageSize, pageNum);
                TableDataInfo<KnowledgeItemVo> pageData = knowledgeItemService.queryPageList(bo, pageQuery);
                List<KnowledgeItemVo> batchData = pageData.getRows();
                if (CollectionUtils.isEmpty(batchData)) {
                    break;
                }
                if (useReportFormat) {
                    generateReportFormatBatch(document, pdfDoc, batchData, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, itemIndex, pageNumberMap);
                } else {
                    generateTableFormatBatch(sharedTable, batchData, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
                }
                itemIndex += batchData.size();
                processedCount += batchData.size();
                log.info("PDF流式导出进度: {}/{}", processedCount, totalCount);
                if (batchData.size() < pageSize) {
                    break;
                }
                pageNum++;
                batchData = null;
                System.gc();
            }
        }
        log.info("PDF流式导出 - 数据渲染完成，needTOC={}, pageNumberMap大小={}", needTOC, pageNumberMap.size());
        if (needTOC) {
            log.info("PDF流式导出 - 需要生成目录，开始flush document以触发页码收集");
            document.flush();
            log.info("PDF流式导出 - document.flush()完成，触发页码收集，当前pageNumberMap大小={}", pageNumberMap.size());
            if (pageNumberMap != null && !pageNumberMap.isEmpty()) {
                log.debug("PDF流式导出 - pageNumberMap内容: {}", pageNumberMap);
            }
            if (pageNumberMap.isEmpty()) {
                log.warn("PDF流式导出 - flush后pageNumberMap仍为空，无法生成目录");
                warnings.add("页码收集失败，已跳过目录生成");
            } else {
                log.info("PDF流式导出 - 开始生成目录，pageNumberMap包含{}个条目", pageNumberMap.size());
                try {
                    List<KnowledgeItemVo> allData;
                    if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
                        PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
                        TableDataInfo<KnowledgeItemVo> pageData = knowledgeItemService.queryPageList(bo, pageQuery);
                        allData = pageData.getRows() != null ? pageData.getRows() : new ArrayList<>();
                        log.info("PDF流式导出 - 目录生成：从currentPage查询数据，共{}条", allData.size());
                    } else {
                        allData = exportDataService.getExportData(request, null);
                        log.info("PDF流式导出 - 目录生成：从getExportData查询数据，共{}条", allData != null ? allData.size() : 0);
                    }
                    log.info("PDF流式导出 - 开始调用addTableOfContentsWithPageNumbers，数据条数={}", allData.size());
                    addTableOfContentsWithPageNumbers(document, pdfDoc, allData, fontChineseBold, fontChinese, pageNumberMap, warnings);
                    log.info("PDF流式导出 - 目录生成成功，当前PDF页数: {}", pdfDoc.getNumberOfPages());
                    allData = null;
                    System.gc();
                } catch (Exception e) {
                    log.error("PDF流式导出 - 生成目录失败", e);
                    warnings.add("目录生成失败: " + e.getMessage());
                }
            }
        }
        try {
            log.info("PDF流式导出 - 开始关闭document和输出流");
            document.close();
            log.info("PDF流式导出 - document.close()完成");
            if (needTOC && tempOutputStream != null) {
                log.info("PDF流式导出 - 开始重新排序页面，将目录页移到第一页");
                try {
                    byte[] tempPdfBytes = tempOutputStream.toByteArray();
                    log.info("PDF流式导出 - 临时PDF字节数组大小: {} bytes", tempPdfBytes.length);
                    tempOutputStream.close();
                    log.info("PDF流式导出 - tempOutputStream关闭完成");
                    log.info("PDF流式导出 - 开始读取临时PDF");
                    PdfDocument tempPdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(tempPdfBytes), new ReaderProperties()));
                    log.info("PDF流式导出 - 临时PDF读取成功");
                    tempPdfDoc.initializeOutlines();
                    log.info("PDF流式导出 - 临时PDF initializeOutlines()完成");
                    int totalPages = tempPdfDoc.getNumberOfPages();
                    int tocPageNum = totalPages;
                    log.info("PDF流式导出 - 临时PDF总页数: {}, 目录页页码: {}", totalPages, tocPageNum);
                    if (totalPages <= 0) {
                        log.error("PDF流式导出 - 错误：临时PDF页数为0或负数");
                        throw new ServiceException("临时PDF页数异常: " + totalPages);
                    }
                    log.info("PDF流式导出 - 开始创建最终PDF");
                    PdfDocument finalPdfDoc = new PdfDocument(new PdfWriter(finalOutputStream));
                    log.info("PDF流式导出 - 最终PDF创建成功");
                    finalPdfDoc.initializeOutlines();
                    log.info("PDF流式导出 - 最终PDF initializeOutlines()完成");
                    PdfPageFormCopier formCopier = new PdfPageFormCopier();
                    log.info("PDF流式导出 - PdfPageFormCopier创建完成");
                    List<Integer> pageOrder = new ArrayList<>();
                    pageOrder.add(tocPageNum);
                    for (int i = 1; i < tocPageNum; i++) {
                        pageOrder.add(i);
                    }
                    log.info("PDF流式导出 - 页面顺序: {}, 共{}页", pageOrder, pageOrder.size());
                    log.info("PDF流式导出 - 开始执行copyPagesTo，从临时PDF复制{}页到最终PDF", pageOrder.size());
                    tempPdfDoc.copyPagesTo(pageOrder, finalPdfDoc, formCopier);
                    log.info("PDF流式导出 - copyPagesTo执行完成");
                    log.info("PDF流式导出 - 最终PDF当前页数: {}", finalPdfDoc.getNumberOfPages());
                    log.info("PDF流式导出 - 开始关闭临时PDF");
                    tempPdfDoc.close();
                    log.info("PDF流式导出 - 临时PDF关闭完成");
                    log.info("PDF流式导出 - 开始关闭最终PDF");
                    finalPdfDoc.close();
                    log.info("PDF流式导出 - 最终PDF关闭完成");
                    log.info("PDF流式导出 - 页面重新排序完成，最终PDF应包含{}页", pageOrder.size());
                } catch (Exception e) {
                    log.error("PDF流式导出 - 页面重新排序过程中发生异常", e);
                    throw e;
                }
            } else {
                log.info("PDF流式导出 - 无需重新排序页面，直接刷新输出流");
                outputStream.flush();
                log.info("PDF流式导出 - outputStream.flush()完成");
            }
            log.info("PDF流式导出完成，共处理 {} 条数据", processedCount);
        } catch (Exception e) {
            log.error("PDF流式导出关闭失败", e);
            log.error("PDF流式导出 - 异常详情: 类型={}, 消息={}, 堆栈=", e.getClass().getName(), e.getMessage(), e);
            if (!response.isCommitted()) {
                response.reset();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"PDF生成失败: " + e.getMessage().replace("\"", "\\\"").replace("\n", "\\n") + "\"}");
                response.getWriter().flush();
            }
            throw new IOException("PDF生成失败: " + e.getMessage(), e);
        }
    }

    private KnowledgeItemBo buildExportQueryBo(ExportRequestBo request) {
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if (request.getFilters() != null) {
            bo = request.getFilters();
        }
        return bo;
    }
    private void generateReportFormatBatch(Document document, PdfDocument pdfDoc, List<KnowledgeItemVo> batchData, List<FieldInfoVo> fieldInfos,
                                          Map<String, String> fieldFormats, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese, 
                                          boolean codeHighlight, int startIndex, Map<String, Integer> pageNumberMap) throws IOException {
        Map<String, CweReferenceVo> cweMap = exportDataService.buildCweMap(batchData, exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = exportDataService.buildTagMap(batchData, exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        Map<Long, String> userMap = exportDataService.buildUserMap(batchData, fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()));
        Map<String, String> knowledgeBaseMap = exportDataService.buildKnowledgeBaseMap(batchData, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            exportDataService.extractExpandedFields(fieldInfos));
        DictService dictService = SpringUtils.getBean(DictService.class);
        for (int i = 0; i < batchData.size(); i++) {
            KnowledgeItemVo item = batchData.get(i);
            int itemIndex = startIndex + i;
            Div card = createItemCard(pdfDoc, item, itemIndex, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap, 
                font, fontChineseBold, fontChinese, codeHighlight, dictService, pageNumberMap);
            document.add(card);
        }
    }
    private Div createItemCard(PdfDocument pdfDoc, KnowledgeItemVo item, int itemIndex, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                              Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap,
                              Map<String, String> knowledgeBaseMap, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese,
                              boolean codeHighlight, DictService dictService, Map<String, Integer> pageNumberMap) throws IOException {
        Div card = new Div();
        card.setBackgroundColor(new DeviceRgb(255, 255, 255));
        card.setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 1));
        card.setBorderLeft(new SolidBorder(new DeviceRgb(64, 64, 64), 4));
        card.setPadding(16);
        card.setPaddingLeft(20);
        card.setMarginBottom(16);
        String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (itemIndex + 1));
        if (StringUtils.isBlank(title)) {
            title = "条目 " + (itemIndex + 1);
        }
        String titleText = String.format("%d. %s", itemIndex + 1, title);
        String destinationId = "item_" + itemIndex;
        Paragraph titlePara = new Paragraph(titleText)
            .setFont(fontChineseBold != null ? fontChineseBold : font)
            .setFontSize(14)
            .setFontColor(new DeviceRgb(64, 64, 64))
            .setMarginBottom(12);
        titlePara.setDestination(destinationId);
        if (pdfDoc != null) {
            PdfOutline rootOutLine = pdfDoc.getOutlines(false);
            PdfOutline outline = rootOutLine.addOutline(destinationId);
            outline.addDestination(PdfDestination.makeDestination(new PdfString(destinationId)));
        }
        if (pageNumberMap != null) {
            titlePara.setNextRenderer(new PageNumberCollectorRenderer(titlePara, destinationId, pageNumberMap));
        }
        card.add(titlePara);
        if (pageNumberMap != null && itemIndex == 0) {
            log.debug("PDF流式导出 - 第一个条目已添加，destinationId={}", destinationId);
        }
        Div tagsContainer = createTagsRow(item, cweMap, dictService, fontChinese);
        if (tagsContainer != null) {
            card.add(tagsContainer);
        }
        Div metaInfoSection = createMetaInfoSection(item, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap, 
            font, fontChineseBold, fontChinese, dictService);
        if (metaInfoSection != null) {
            card.add(metaInfoSection);
        }
        Div contentSection = createContentSection(item, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap,
            font, fontChineseBold, fontChinese, codeHighlight);
        if (contentSection != null) {
            card.add(contentSection);
        }
        Div footerMeta = createFooterMeta(item, fieldInfos, fieldFormats, userMap, knowledgeBaseMap, fontChinese, font);
        if (footerMeta != null) {
            card.add(footerMeta);
        }
        return card;
    }
    private Div createTagsRow(KnowledgeItemVo item, Map<String, CweReferenceVo> cweMap, DictService dictService, PdfFont fontChinese) {
        List<Div> tags = new ArrayList<>();
        String severity = item.getSeverity();
        if (StringUtils.isBlank(severity) && item.getCvssScore() != null) {
            severity = CvssScoreCalculator.mapSeverityByScore(item.getCvssScore());
        }
        if (StringUtils.isNotBlank(severity)) {
            String severityLabel = dictService.getDictLabel("knowledge_severity", severity);
            String severityColorHex = getSeverityColor(severity);
            DeviceRgb severityColor = parseHexColor(severityColorHex);
            Div severityTag = createTag(severityLabel, severityColor, severityColor, fontChinese);
            if (!severityTag.getChildren().isEmpty() && severityTag.getChildren().get(0) instanceof Paragraph) {
                ((Paragraph) severityTag.getChildren().get(0)).setFontColor(new DeviceRgb(255, 255, 255));
            }
            tags.add(severityTag);
        }
        if (StringUtils.isNotBlank(item.getLanguage())) {
            String languageLabel = dictService.getDictLabel("knowledge_language", item.getLanguage());
            Div languageTag = createTag(languageLabel, new DeviceRgb(217, 217, 217), new DeviceRgb(245, 245, 245), fontChinese);
            if (!languageTag.getChildren().isEmpty() && languageTag.getChildren().get(0) instanceof Paragraph) {
                ((Paragraph) languageTag.getChildren().get(0)).setFontColor(new DeviceRgb(102, 102, 102));
            }
            tags.add(languageTag);
        }
        if (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty()) {
            for (String vulnType : item.getVulnerabilityTypes()) {
                if (cweMap != null && cweMap.containsKey(vulnType)) {
                    CweReferenceVo cwe = cweMap.get(vulnType);
                    String vulnName = StringUtils.isNotBlank(cwe.getNameZh()) ? cwe.getNameZh() : cwe.getNameEn();
                    if (vulnName.length() > 25) {
                        vulnName = vulnName.substring(0, 25) + "...";
                    }
                    Div vulnTag = createTag(vulnName, new DeviceRgb(217, 217, 217), new DeviceRgb(245, 245, 245), fontChinese);
                    if (!vulnTag.getChildren().isEmpty() && vulnTag.getChildren().get(0) instanceof Paragraph) {
                        ((Paragraph) vulnTag.getChildren().get(0)).setFontColor(new DeviceRgb(96, 96, 96));
                    }
                    tags.add(vulnTag);
                }
            }
        }
        if (tags.isEmpty()) {
            return null;
        }
        Div tagsContainer = new Div();
        tagsContainer.setMarginBottom(12);
        tagsContainer.setProperty(Property.OVERFLOW_X, OverflowPropertyValue.VISIBLE);
        for (Div tag : tags) {
            tagsContainer.add(tag);
        }
        return tagsContainer;
    }
    private Div createTag(String text, DeviceRgb borderColor, DeviceRgb bgColor, PdfFont font) {
        Div tag = new Div();
        tag.setBorder(new SolidBorder(borderColor, 1));
        tag.setBackgroundColor(bgColor);
        tag.setPadding(4);
        tag.setPaddingLeft(8);
        tag.setPaddingRight(8);
        tag.setMarginRight(6);
        tag.setMarginBottom(4);
        tag.setWidth(UnitValue.createPointValue(0));
        Paragraph tagText = new Paragraph(text)
            .setFont(font)
            .setFontSize(9)
            .setFontColor(borderColor)
            .setMargin(0);
        tag.add(tagText);
        return tag;
    }
    private DeviceRgb parseHexColor(String hex) {
        if (hex == null || !hex.startsWith("#")) {
            return new DeviceRgb(64, 64, 64);
        }
        try {
            int r = Integer.parseInt(hex.substring(1, 3), 16);
            int g = Integer.parseInt(hex.substring(3, 5), 16);
            int b = Integer.parseInt(hex.substring(5, 7), 16);
            return new DeviceRgb(r, g, b);
        } catch (Exception e) {
            return new DeviceRgb(64, 64, 64);
        }
    }
    private Div createMetaInfoSection(KnowledgeItemVo item, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                                     Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap,
                                     Map<String, String> knowledgeBaseMap, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese,
                                     DictService dictService) {
        Map<String, Object> basicMeta = new LinkedHashMap<>();
        Map<String, Object> cvssMeta = new LinkedHashMap<>();
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("title".equals(key) || "summary".equals(key) || "problemDescription".equals(key) || 
                "fixSolution".equals(key) || "exampleCode".equals(key)) {
                continue;
            }
            Object value = exportDataService.getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
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
        if (basicMeta.isEmpty() && cvssMeta.isEmpty()) {
            return null;
        }
        Div metaSection = new Div();
        metaSection.setMarginBottom(12);
        if (!basicMeta.isEmpty()) {
            Div basicMetaDiv = createMetaTable("基本信息", basicMeta, font, fontChineseBold, fontChinese);
            metaSection.add(basicMetaDiv);
        }
        if (!cvssMeta.isEmpty()) {
            Div cvssMetaDiv = createMetaTable("CVSS评分", cvssMeta, font, fontChineseBold, fontChinese);
            metaSection.add(cvssMetaDiv);
        }
        return metaSection;
    }
    private Div createMetaTable(String title, Map<String, Object> metaData, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese) {
        Div metaGroup = new Div();
        metaGroup.setMarginBottom(8);
        Paragraph groupTitle = new Paragraph(title)
            .setFont(fontChineseBold != null ? fontChineseBold : font)
            .setFontSize(10)
            .setFontColor(new DeviceRgb(102, 102, 102))
            .setMarginBottom(4);
        metaGroup.add(groupTitle);
        Table metaTable = new Table(UnitValue.createPercentArray(new float[]{0.4f, 0.6f})).useAllAvailableWidth();
        metaTable.setBorder(new SolidBorder(new DeviceRgb(245, 245, 245), 1));
        metaTable.setBackgroundColor(new DeviceRgb(245, 245, 245));
        metaTable.setKeepTogether(false);
        for (Map.Entry<String, Object> entry : metaData.entrySet()) {
            com.itextpdf.layout.element.Cell labelCell = new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(entry.getKey() + ": ")
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(102, 102, 102)))
                .setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 0.5f))
                .setPadding(6)
                .setPaddingLeft(8);
            com.itextpdf.layout.element.Cell valueCell = new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(String.valueOf(entry.getValue()))
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(51, 51, 51)))
                .setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 0.5f))
                .setPadding(6)
                .setPaddingRight(8);
            metaTable.addCell(labelCell);
            metaTable.addCell(valueCell);
        }
        metaGroup.add(metaTable);
        return metaGroup;
    }
    private Div createContentSection(KnowledgeItemVo item, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                                    Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap,
                                    Map<String, String> knowledgeBaseMap, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese,
                                    boolean codeHighlight) throws IOException {
        Div contentSection = new Div();
        boolean hasContent = false;
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("title".equals(key)) {
                continue;
            }
            if ("summary".equals(key) || "problemDescription".equals(key) || 
                "fixSolution".equals(key) || "exampleCode".equals(key)) {
                Object value = exportDataService.getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                    String text = String.valueOf(value);
                    String label = fieldInfo.getLabel();
                    Div contentItem = new Div();
                    contentItem.setMarginBottom(12);
                    Paragraph sectionTitle = new Paragraph(label)
                        .setFont(fontChineseBold != null ? fontChineseBold : font)
                        .setFontSize(11)
                        .setFontColor(new DeviceRgb(51, 51, 51))
                        .setMarginBottom(4);
                    Div divider = new Div();
                    divider.setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 1));
                    divider.setMarginBottom(8);
                    contentItem.add(sectionTitle);
                    contentItem.add(divider);
                    if ("exampleCode".equals(key) && codeHighlight) {
                        Div codeBlock = createCodeBlock(text, font, fontChinese);
                        contentItem.add(codeBlock);
                    } else {
                        Paragraph contentPara = new Paragraph(text)
                            .setFont(fontChinese != null ? fontChinese : font)
                            .setFontSize(10)
                            .setFontColor(new DeviceRgb(51, 51, 51))
                            .setMargin(0)
                            .setOrphansControl(new ParagraphOrphansControl(2))
                            .setWidowsControl(new ParagraphWidowsControl(2, 1, true));
                        contentItem.add(contentPara);
                    }
                    contentSection.add(contentItem);
                    hasContent = true;
                }
            }
        }
        if (item.getTags() != null && !item.getTags().isEmpty()) {
            Div tagsSection = new Div();
            tagsSection.setMarginBottom(12);
            Paragraph tagsTitle = new Paragraph("标签")
                .setFont(fontChineseBold != null ? fontChineseBold : font)
                .setFontSize(11)
                .setFontColor(new DeviceRgb(51, 51, 51))
                .setMarginBottom(4);
            Div divider = new Div();
            divider.setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 1));
            divider.setMarginBottom(8);
            tagsSection.add(tagsTitle);
            tagsSection.add(divider);
            Div tagsContainer = new Div();
            for (String tagName : item.getTags()) {
                Div tag = createTag(tagName, new DeviceRgb(82, 196, 26), new DeviceRgb(246, 255, 237), fontChinese);
                if (!tag.getChildren().isEmpty() && tag.getChildren().get(0) instanceof Paragraph) {
                    ((Paragraph) tag.getChildren().get(0)).setFontColor(new DeviceRgb(82, 196, 26));
                }
                tagsContainer.add(tag);
            }
            tagsSection.add(tagsContainer);
            contentSection.add(tagsSection);
            hasContent = true;
        }
        return hasContent ? contentSection : null;
    }

    private Div createFooterMeta(KnowledgeItemVo item, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                                Map<Long, String> userMap, Map<String, String> knowledgeBaseMap, PdfFont fontChinese, PdfFont font) {
        List<String> footerInfo = new ArrayList<>();
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("createTime".equals(key) && item.getCreateTime() != null) {
                footerInfo.add("创建时间：" + exportDataService.formatDateTime(item.getCreateTime()));
            } else if ("updateTime".equals(key) && item.getUpdateTime() != null) {
                footerInfo.add("更新时间：" + exportDataService.formatDateTime(item.getUpdateTime()));
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
        if (footerInfo.isEmpty()) {
            return null;
        }
        Div footerMeta = new Div();
        footerMeta.setMarginTop(8);
        footerMeta.setPaddingTop(8);
        footerMeta.setBorderTop(new SolidBorder(new DeviceRgb(245, 245, 245), 1));
        String footerText = String.join(" | ", footerInfo);
        PdfFont footerFont = fontChinese != null ? fontChinese : font;
        Paragraph footerPara = new Paragraph(footerText)
            .setFont(footerFont)
            .setFontSize(9)
            .setFontColor(new DeviceRgb(102, 102, 102))
            .setMargin(0);
        footerMeta.add(footerPara);
        return footerMeta;
    }
    private void generateTableFormatBatch(Table table, List<KnowledgeItemVo> batchData, List<FieldInfoVo> fieldInfos,
                                         Map<String, String> fieldFormats, PdfFont font, PdfFont fontBold, PdfFont fontChinese) throws IOException {
        List<Map<String, Object>> exportData = exportDataService.convertToMapList(batchData, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        for (Map<String, Object> rowData : exportData) {
            for (FieldInfoVo fieldInfo : fieldInfos) {
                Object value = rowData.get(fieldInfo.getKey());
                String text = value != null ? String.valueOf(value) : "";
                Paragraph textPara = new Paragraph(text)
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setMargin(0)
                    .setOrphansControl(new ParagraphOrphansControl(1))
                    .setWidowsControl(new ParagraphWidowsControl(1, 1, true));
                com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell().add(textPara);
                cell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
                cell.setPadding(5);
                cell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
                table.addCell(cell);
            }
        }
    }
    public void exportToPdf(List<KnowledgeItemVo> data, ExportRequestBo request, String fileName, HttpServletResponse response) throws IOException {
        if (CollectionUtils.isEmpty(data)) {
            throw new ServiceException("没有可导出的数据");
        }
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        List<FieldInfoVo> fieldInfos = exportDataService.buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new ServiceException("请至少选择一个导出字段");
        }
        PdfOptionsBo pdfOptions = request.getPdfOptions();
        boolean useReportFormat;
        if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
            useReportFormat = "report".equals(pdfOptions.getFormatType());
        } else {
            useReportFormat = shouldUseReportFormat(fieldInfos);
        }
        boolean includeTOC = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getIncludeTOC());
        boolean includeHeaderFooter = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getIncludeHeaderFooter());
        boolean useHtmlToPdf = false;
        if (useHtmlToPdf) {
            try {
                exportToPdfFromHtml(data, request, fileName, response, fieldInfos, useReportFormat, includeTOC, includeHeaderFooter);
                return;
            } catch (Exception e) {
                log.warn("HTML转PDF失败，回退到iText原生方案", e);
            }
        }
        OutputStream outputStream = response.getOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setTagged();
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(36, 36, 36, 36);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontChinese = null;
        PdfFont fontChineseBold = null;
        String[] fontPaths = {
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Bold.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Bold.ttc",
            "/usr/share/fonts/wenquanyi/wqy-zenhei/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/msttcore/SimSun.ttf",
            "/usr/share/fonts/truetype/msttcore/SimHei.ttf",
            "C:/Windows/Fonts/simsun.ttc",
            "C:/Windows/Fonts/simhei.ttf",
            "C:/Windows/Fonts/msyh.ttf",
            System.getProperty("java.home") + "/lib/fonts/SimSun.ttf"
        };
        boolean fontLoaded = false;
        String loadedFontPath = null;
        log.info("开始查找中文字体文件，共{}个路径", fontPaths.length);
        for (String fontPath : fontPaths) {
            try {
                java.io.File fontFile = new java.io.File(fontPath);
                boolean exists = fontFile.exists();
                boolean isFile = fontFile.isFile();
                log.debug("检查字体文件: {}, 存在: {}, 是文件: {}", fontPath, exists, isFile);
                if (exists && isFile) {
                    if (fontPath.toLowerCase().endsWith(".ttc")) {
                        //TTC文件需要使用FontProgramFactory加载，索引0表示第一个字体
                        //cached=false减少内存占用，避免OutOfMemoryError
                        FontProgram fontProgram = FontProgramFactory.createFont(fontPath, 0, false);
                        fontChinese = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTC文件加载中文字体: {}", fontPath);
                        break;
                    } else {
                        //TTF文件可以直接使用PdfFontFactory加载
                        fontChinese = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTF文件加载中文字体: {}", fontPath);
                        break;
                    }
                } else {
                    log.debug("字体文件不存在或不是文件: {}", fontPath);
                }
            } catch (Exception e) {
                log.warn("尝试从文件加载字体失败: {}, 错误: {}", fontPath, e.getMessage());
            }
        }
        if (!fontLoaded) {
            log.warn("所有字体文件路径都未找到字体文件，将尝试其他方式加载");
        }
        if (fontLoaded && loadedFontPath != null) {
            String boldFontPath = loadedFontPath.replace("Regular", "Bold");
            try {
                java.io.File boldFontFile = new java.io.File(boldFontPath);
                if (boldFontFile.exists() && boldFontFile.isFile()) {
                    if (boldFontPath.toLowerCase().endsWith(".ttc")) {
                        //cached=false减少内存占用
                        FontProgram boldFontProgram = FontProgramFactory.createFont(boldFontPath, 0, false);
                        fontChineseBold = PdfFontFactory.createFont(boldFontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    } else {
                        fontChineseBold = PdfFontFactory.createFont(boldFontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    }
                    log.info("成功加载中文字体粗体: {}", boldFontPath);
                } else {
                    fontChineseBold = fontChinese;
                    log.info("未找到对应的粗体字体，使用常规字体作为粗体");
                }
            } catch (Exception e) {
                log.warn("加载粗体字体失败，使用常规字体: {}", e.getMessage());
                fontChineseBold = fontChinese;
            }
        }
        if (!fontLoaded) {
            try {
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/simsun.ttf");
                if (fontStream == null) {
                    fontStream = getClass().getClassLoader().getResourceAsStream("fonts/SimSun.ttf");
                }
                if (fontStream != null) {
                    byte[] fontBytes = fontStream.readAllBytes();
                    fontStream.close();
                    fontChinese = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = fontChinese;
                    fontLoaded = true;
                    log.info("从resources加载中文字体成功");
                }
            } catch (Exception e) {
                log.debug("从resources加载字体失败");
            }
        }
        if (!fontLoaded) {
            log.warn("尝试使用字体名称加载（可能无法正确渲染中文）");
            try {
                fontChinese = PdfFontFactory.createFont("STSong-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontChineseBold = PdfFontFactory.createFont("STSongStd-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontLoaded = true;
                log.warn("使用字体名称加载成功（但可能无法正确渲染中文）: STSong-Light");
        } catch (Exception e1) {
            try {
                    fontChinese = PdfFontFactory.createFont("SimSun", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontLoaded = true;
            } catch (Exception e2) {
                try {
                        fontChinese = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = fontChinese;
                        fontLoaded = true;
                } catch (Exception e3) {
                    try {
                            fontChinese = PdfFontFactory.createFont("Microsoft YaHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                            fontChineseBold = fontChinese;
                            fontLoaded = true;
                        } catch (Exception e4) {
                            log.error("无法加载中文字体，将使用Helvetica（中文可能无法正确显示）", e4);
                    fontChinese = font;
                    fontChineseBold = fontBold;
                }
                    }
                }
            }
        }
        if (fontChinese == null || fontChineseBold == null) {
            log.error("中文字体加载失败，PDF中的中文内容可能无法正确显示");
            fontChinese = font;
            fontChineseBold = fontBold;
        } else {
            String fontName = fontChinese.getFontProgram().getFontNames().getFontName();
            String boldFontName = fontChineseBold.getFontProgram().getFontNames().getFontName();
            log.info("中文字体加载成功: regular={}, bold={}", fontName, boldFontName);
            try {
                String testText = "测试";
                float width = fontChinese.getWidth(testText, 12);
                if (width <= 0) {
                    log.warn("字体可能不支持中文字符，测试文本宽度为0");
                } else {
                    log.info("字体中文字符测试通过，测试文本宽度: {}", width);
                }
            } catch (Exception e) {
                log.warn("字体中文字符测试失败", e);
            }
        }
        if (includeHeaderFooter) {
            HeaderFooterHandler handler = new HeaderFooterHandler(font, fontChinese, fileName);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        }
        boolean codeHighlight = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getCodeHighlight());
        Map<String, Integer> pageNumberMap = new HashMap<>();
        if (useReportFormat) {
            generateReportFormat(document, pdfDoc, data, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, pageNumberMap);
        } else {
            generateTableFormat(document, data, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
        }
        if (includeTOC && useReportFormat && !pageNumberMap.isEmpty()) {
            List<String> warnings = new ArrayList<>();
            addTableOfContentsWithPageNumbers(document, pdfDoc, data, fontChineseBold, fontChinese, pageNumberMap, warnings);
        }
        document.close();
        outputStream.flush();
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

    private static class HeaderFooterHandler implements IEventHandler {
        private final PdfFont font;
        private final PdfFont fontChinese;
        private final String fileName;
        public HeaderFooterHandler(PdfFont font, PdfFont fontChinese, String fileName) {
            this.font = font;
            this.fontChinese = fontChinese;
            this.fileName = fileName;
        }
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            canvas.saveState();
            canvas.setFontAndSize(font, 9);
            canvas.setFillColor(ColorConstants.GRAY);
            String headerText = "知识条目导出报告";
            String footerText = String.format("第 %d 页", pageNumber);
            String dateText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            float headerY = pageSize.getTop() - 20;
            float footerY = 20;
            canvas.setFontAndSize(fontChinese, 9);
            canvas.beginText();
            canvas.moveText(pageSize.getWidth() / 2 - 50, headerY);
            canvas.showText(headerText);
            canvas.endText();
            canvas.beginText();
            canvas.moveText(pageSize.getWidth() / 2 - 30, footerY);
            canvas.showText(footerText);
            canvas.endText();
            canvas.setFontAndSize(font, 9);
            canvas.beginText();
            canvas.moveText(20, footerY);
            canvas.showText(dateText);
            canvas.endText();
            canvas.restoreState();
        }
    }
    private void addTableOfContentsWithPageNumbers(Document document, PdfDocument pdfDoc, List<KnowledgeItemVo> data, 
                                                   PdfFont fontChineseBold, PdfFont fontChinese, Map<String, Integer> pageNumberMap, List<String> warnings) throws IOException {
        log.info("PDF流式导出 - addTableOfContentsWithPageNumbers开始，数据条数={}, pageNumberMap大小={}", data.size(), pageNumberMap.size());
        PdfFont tocBoldFont = fontChineseBold != null ? fontChineseBold : PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont tocFont = fontChinese != null ? fontChinese : PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.add(new Paragraph("\n"));
        Paragraph tocTitle = new Paragraph("目录")
            .setFont(tocBoldFont)
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(24);
        document.add(tocTitle);
        document.add(new Paragraph("\n"));
        List<TabStop> tabStops = new ArrayList<>();
        tabStops.add(new TabStop(550, TabAlignment.RIGHT, new DottedLine(1, 2)));
        int tocItemCount = 0;
        for (int i = 0; i < Math.min(data.size(), 1000); i++) {
            KnowledgeItemVo item = data.get(i);
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
            if (title.length() > 50) {
                title = title.substring(0, 50) + "...";
            }
            String destinationId = "item_" + i;
            int pageNumber = pageNumberMap.getOrDefault(destinationId, 0);
            if (pageNumber == 0) {
                log.debug("PDF流式导出 - 目录项跳过：destinationId={} 未找到页码", destinationId);
                continue;
            }
            tocItemCount++;
            Paragraph tocItem = new Paragraph();
            tocItem.addTabStops(tabStops);
            Link titleLink = new Link(String.format("%d. %s", i + 1, title), PdfAction.createGoTo(destinationId));
            titleLink.setFont(tocFont);
            titleLink.setFontSize(11);
            titleLink.setFontColor(new DeviceRgb(51, 51, 51));
            tocItem.add(titleLink);
            tocItem.add(new Tab());
            Text pageText = new Text(String.valueOf(pageNumber));
            pageText.setFont(tocFont);
            pageText.setFontSize(11);
            pageText.setFontColor(new DeviceRgb(102, 102, 102));
            tocItem.add(pageText);
            tocItem.setMarginBottom(10);
            document.add(tocItem);
        }
        document.add(new Paragraph("\n"));
        log.info("PDF流式导出 - 目录项添加完成，共{}个有效目录项", tocItemCount);
        int currentPageCount = pdfDoc.getNumberOfPages();
        log.info("PDF流式导出 - 添加目录后当前PDF总页数: {}", currentPageCount);
        if (currentPageCount > 0) {
            int tocPageNum = pdfDoc.getPageNumber(pdfDoc.getLastPage());
            log.info("PDF流式导出 - 目录页当前页码: {}", tocPageNum);
            document.flush();
            int pageCountAfterFlush = pdfDoc.getNumberOfPages();
            log.info("PDF流式导出 - flush后PDF总页数: {}", pageCountAfterFlush);
            log.info("PDF流式导出 - addTableOfContentsWithPageNumbers完成，目录页页码: {}", tocPageNum);
        } else {
            log.warn("PDF流式导出 - 警告：PDF页数为0，无法确定目录页页码");
            document.flush();
            log.info("PDF流式导出 - addTableOfContentsWithPageNumbers完成（页数为0）");
        }
    }
    private void generateTableFormat(Document document, List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, 
                                     Map<String, String> fieldFormats, PdfFont font, PdfFont fontBold, PdfFont fontChinese) throws IOException {
        List<Map<String, Object>> exportData = exportDataService.convertToMapList(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        Table table = new Table(UnitValue.createPercentArray(fieldInfos.size())).useAllAvailableWidth();
        table.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
        table.setKeepTogether(false);
        PdfFont headerFont = fontChinese != null && fontChinese != font ? fontChinese : fontBold;
        for (FieldInfoVo fieldInfo : fieldInfos) {
            com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell().add(new Paragraph(fieldInfo.getLabel())
                .setFont(headerFont)
                .setFontSize(10));
            headerCell.setBackgroundColor(new DeviceRgb(245, 245, 245));
            headerCell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
            headerCell.setTextAlignment(TextAlignment.CENTER);
            headerCell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
            headerCell.setPadding(5);
            table.addHeaderCell(headerCell);
        }
        for (Map<String, Object> rowData : exportData) {
            for (FieldInfoVo fieldInfo : fieldInfos) {
                Object value = rowData.get(fieldInfo.getKey());
                String text = value != null ? String.valueOf(value) : "";
                Paragraph textPara = new Paragraph(text)
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setMargin(0)
                    .setOrphansControl(new ParagraphOrphansControl(1))
                    .setWidowsControl(new ParagraphWidowsControl(1, 1, true));
                com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell().add(textPara);
                cell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
                cell.setPadding(5);
                cell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
                table.addCell(cell);
            }
        }
        document.add(table);
    }
    private void generateReportFormat(Document document, PdfDocument pdfDoc, List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos,
                                     Map<String, String> fieldFormats, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese, 
                                     boolean codeHighlight, Map<String, Integer> pageNumberMap) throws IOException {
        log.info("PDF导出 - 开始生成报告格式PDF，数据条数: {}, 字段数: {}", data.size(), fieldInfos.size());
        Map<String, CweReferenceVo> cweMap = exportDataService.buildCweMap(data, exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = exportDataService.buildTagMap(data, exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        Map<Long, String> userMap = exportDataService.buildUserMap(data, fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()));
        Map<String, String> knowledgeBaseMap = exportDataService.buildKnowledgeBaseMap(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            exportDataService.extractExpandedFields(fieldInfos));
        DictService dictService = SpringUtils.getBean(DictService.class);
        for (int i = 0; i < data.size(); i++) {
            KnowledgeItemVo item = data.get(i);
            Div card = createItemCard(pdfDoc, item, i, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap,
                font, fontChineseBold, fontChinese, codeHighlight, dictService, pageNumberMap);
            document.add(card);
        }
        log.info("PDF导出 - 报告格式PDF生成完成，共生成 {} 个条目", data.size());
    }

    private Div createCodeBlock(String code, PdfFont defaultFont, PdfFont chineseFont) throws IOException {
        PdfFont codeFont = PdfFontFactory.createFont(StandardFonts.COURIER);
        Div codeContainer = new Div();
        codeContainer.setBackgroundColor(new DeviceRgb(248, 248, 248));
        codeContainer.setBorder(new SolidBorder(new DeviceRgb(220, 220, 220), 1));
        codeContainer.setPadding(8);
        codeContainer.setMarginBottom(5);
        Paragraph codePara = new Paragraph();
        codePara.setFont(codeFont);
        codePara.setFontSize(9);
        codePara.setMargin(0);
        String language = detectCodeLanguage(code);
        String highlightedHtml = highlightJsService.highlight(code, language);
        parseHighlightedHtml(highlightedHtml, codePara, codeFont);
        codeContainer.add(codePara);
        return codeContainer;
    }
    private void parseHighlightedHtml(String html, Paragraph para, PdfFont font) {
        if (html == null || html.isEmpty()) {
            return;
        }
        int i = 0;
        int len = html.length();
        while (i < len) {
            if (html.charAt(i) == '<') {
                int tagEnd = html.indexOf('>', i);
                if (tagEnd == -1) {
                    break;
                }
                String tag = html.substring(i + 1, tagEnd);
                if (tag.startsWith("span")) {
                    int closeTag = html.indexOf("</span>", tagEnd);
                    if (closeTag == -1) {
                        i = tagEnd + 1;
                        continue;
                    }
                    String text = html.substring(tagEnd + 1, closeTag);
                    text = decodeHtmlEntities(text);
                    DeviceRgb color = getColorFromSpanClass(tag);
                    Text textElement = new Text(text);
                    textElement.setFont(font);
                    textElement.setFontSize(9);
                    textElement.setFontColor(color);
                    textElement.setNextRenderer(new PreserveWhitespaceTextRenderer(textElement));
                    para.add(textElement);
                    i = closeTag + 7;
                } else if (tag.equals("br") || tag.startsWith("br ")) {
                    Text newlineText = new Text("\n");
                    newlineText.setNextRenderer(new PreserveWhitespaceTextRenderer(newlineText));
                    para.add(newlineText);
                    i = tagEnd + 1;
                } else {
                    i = tagEnd + 1;
                }
            } else {
                int nextTag = html.indexOf('<', i);
                if (nextTag == -1) {
                    String text = decodeHtmlEntities(html.substring(i));
                    Text textElement = new Text(text);
                    textElement.setFont(font);
                    textElement.setFontSize(9);
                    textElement.setFontColor(new DeviceRgb(0, 0, 0));
                    textElement.setNextRenderer(new PreserveWhitespaceTextRenderer(textElement));
                    para.add(textElement);
                    break;
                }
                if (nextTag > i) {
                    String text = decodeHtmlEntities(html.substring(i, nextTag));
                    Text textElement = new Text(text);
                    textElement.setFont(font);
                    textElement.setFontSize(9);
                    textElement.setFontColor(new DeviceRgb(0, 0, 0));
                    textElement.setNextRenderer(new PreserveWhitespaceTextRenderer(textElement));
                    para.add(textElement);
                }
                i = nextTag;
            }
        }
    }
    private String decodeHtmlEntities(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&nbsp;", " ");
    }
    private DeviceRgb getColorFromSpanClass(String spanTag) {
        String classAttr = extractClassFromTag(spanTag);
        if (classAttr == null) {
            return new DeviceRgb(0, 0, 0);
        }
        if (classAttr.contains("keyword") || classAttr.contains("built_in") || classAttr.contains("type")) {
            return new DeviceRgb(0, 0, 255);
        } else if (classAttr.contains("string") || classAttr.contains("attr")) {
            return new DeviceRgb(163, 21, 21);
        } else if (classAttr.contains("comment")) {
            return new DeviceRgb(0, 128, 0);
        } else if (classAttr.contains("number") || classAttr.contains("literal")) {
            return new DeviceRgb(0, 128, 128);
        } else if (classAttr.contains("function")) {
            return new DeviceRgb(121, 93, 163);
        } else if (classAttr.contains("variable")) {
            return new DeviceRgb(0, 0, 128);
        }
        return new DeviceRgb(0, 0, 0);
    }
    private String extractClassFromTag(String tag) {
        int classIdx = tag.indexOf("class=\"");
        if (classIdx == -1) {
            classIdx = tag.indexOf("class='");
            if (classIdx == -1) {
                return null;
            }
            int end = tag.indexOf("'", classIdx + 7);
            return end == -1 ? null : tag.substring(classIdx + 7, end);
        }
        int end = tag.indexOf("\"", classIdx + 7);
        return end == -1 ? null : tag.substring(classIdx + 7, end);
    }
    private String detectCodeLanguage(String code) {
        if (code == null || code.trim().isEmpty()) {
            return "text";
        }
        String trimmed = code.trim();
        if (trimmed.startsWith("<?xml") || trimmed.startsWith("<")) {
            return "xml";
        }
        if (trimmed.startsWith("package ") || trimmed.contains("public class") || trimmed.contains("import ")) {
            return "java";
        }
        if (trimmed.startsWith("def ") || trimmed.contains("import ") && trimmed.contains("print(")) {
            return "python";
        }
        if (trimmed.startsWith("function ") || trimmed.contains("const ") || trimmed.contains("let ")) {
            return "javascript";
        }
        if (trimmed.startsWith("SELECT ") || trimmed.startsWith("select ")) {
            return "sql";
        }
        if (trimmed.startsWith("#include") || trimmed.contains("int main")) {
            return "cpp";
        }
        return "text";
    }
    private List<CodeToken> tokenizeCode(String code, String language) {
        //预分配容量，减少ArrayList扩容次数，避免内存溢出
        int estimatedSize = code.length() / 10 + code.split("\n").length * 2;
        List<CodeToken> tokens = new ArrayList<>(Math.min(estimatedSize, 100000));
        if (code == null || code.isEmpty()) {
            return tokens;
        }
        String[] keywords = getKeywordsForLanguage(language);
        String[] lines = code.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().isEmpty()) {
                tokens.add(new CodeToken("\n", TokenType.DEFAULT));
                continue;
            }
            if (language.equals("java") || language.equals("cpp") || language.equals("javascript")) {
                tokenizeLine(line, keywords, tokens);
            } else if (language.equals("python")) {
                tokenizePythonLine(line, tokens);
            } else if (language.equals("xml")) {
                tokenizeXmlLine(line, tokens);
            } else {
                tokens.add(new CodeToken(line, TokenType.DEFAULT));
            }
            if (i < lines.length - 1) {
                tokens.add(new CodeToken("\n", TokenType.DEFAULT));
            }
        }
        return tokens;
    }
    private void tokenizeLine(String line, String[] keywords, List<CodeToken> tokens) {
        int i = 0;
        int lineLen = line.length();
        while (i < lineLen) {
            char c = line.charAt(i);
            if (c == '"' || c == '\'') {
                char quote = c;
                int end = line.indexOf(quote, i + 1);
                if (end == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.STRING));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, end + 1), TokenType.STRING));
                i = end + 1;
            } else if (i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                tokens.add(new CodeToken(line.substring(i), TokenType.COMMENT));
                break;
            } else if (i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                int end = line.indexOf("*/", i + 2);
                if (end == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.COMMENT));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, end + 2), TokenType.COMMENT));
                i = end + 2;
            } else {
                int next = findNextSpecialChar(line, i);
                String word = line.substring(i, next);
                if (isKeyword(word, keywords)) {
                    tokens.add(new CodeToken(word, TokenType.KEYWORD));
                } else if (isNumber(word)) {
                    tokens.add(new CodeToken(word, TokenType.NUMBER));
                } else {
                    tokens.add(new CodeToken(word, TokenType.DEFAULT));
                }
                i = next;
            }
        }
    }
    private void tokenizePythonLine(String line, List<CodeToken> tokens) {
        if (line.trim().startsWith("#")) {
            tokens.add(new CodeToken(line, TokenType.COMMENT));
            return;
        }
        if (line.trim().startsWith("\"\"\"") || line.trim().startsWith("'''")) {
            tokens.add(new CodeToken(line, TokenType.COMMENT));
            return;
        }
        tokenizeLine(line, getKeywordsForLanguage("python"), tokens);
    }
    private void tokenizeXmlLine(String line, List<CodeToken> tokens) {
        int i = 0;
        while (i < line.length()) {
            if (line.charAt(i) == '<') {
                int end = line.indexOf('>', i);
                if (end == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.KEYWORD));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, end + 1), TokenType.KEYWORD));
                i = end + 1;
            } else {
                int next = line.indexOf('<', i);
                if (next == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.DEFAULT));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, next), TokenType.DEFAULT));
                i = next;
            }
        }
    }
    private int findNextSpecialChar(String line, int start) {
        int len = line.length();
        for (int i = start; i < len; i++) {
            char c = line.charAt(i);
            if (c == '"' || c == '\'' || c == ' ' || c == '\t') {
                return i;
            }
            if (i < len - 1 && c == '/') {
                char next = line.charAt(i + 1);
                if (next == '/' || next == '*') {
                    return i;
                }
            }
        }
        return len;
    }
    private boolean isKeyword(String word, String[] keywords) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        String trimmed = word.trim();
        for (String keyword : keywords) {
            if (keyword.equals(trimmed)) {
                return true;
            }
        }
        return false;
    }
    private boolean isNumber(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(word.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private String[] getKeywordsForLanguage(String language) {
        switch (language) {
            case "java":
                return new String[]{"public", "private", "protected", "class", "interface", "extends", "implements",
                    "static", "final", "void", "return", "if", "else", "for", "while", "do", "switch", "case",
                    "break", "continue", "try", "catch", "finally", "throw", "throws", "new", "this", "super",
                    "import", "package", "boolean", "int", "long", "float", "double", "char", "String", "null"};
            case "python":
                return new String[]{"def", "class", "if", "elif", "else", "for", "while", "try", "except",
                    "finally", "with", "as", "import", "from", "return", "yield", "pass", "break", "continue",
                    "and", "or", "not", "in", "is", "None", "True", "False", "lambda"};
            case "javascript":
                return new String[]{"function", "var", "let", "const", "if", "else", "for", "while", "do",
                    "switch", "case", "break", "continue", "return", "try", "catch", "finally", "throw",
                    "new", "this", "class", "extends", "import", "export", "default", "async", "await"};
            case "cpp":
                return new String[]{"int", "char", "float", "double", "void", "bool", "if", "else", "for",
                    "while", "do", "switch", "case", "break", "continue", "return", "class", "struct",
                    "public", "private", "protected", "static", "const", "virtual", "new", "delete"};
            case "sql":
                return new String[]{"SELECT", "FROM", "WHERE", "INSERT", "UPDATE", "DELETE", "CREATE",
                    "ALTER", "DROP", "TABLE", "INDEX", "VIEW", "JOIN", "INNER", "OUTER", "LEFT", "RIGHT",
                    "ON", "GROUP", "BY", "ORDER", "HAVING", "UNION", "AND", "OR", "NOT", "NULL", "AS"};
            default:
                return new String[]{};
        }
    }
    private static class CodeToken {
        String text;
        TokenType type;
        CodeToken(String text, TokenType type) {
            this.text = text;
            this.type = type;
        }
    }
    private enum TokenType {
        KEYWORD, STRING, COMMENT, NUMBER, DEFAULT
    }

    private void exportToPdfFromHtml(List<KnowledgeItemVo> data, ExportRequestBo request, String fileName, HttpServletResponse response, 
                                     List<FieldInfoVo> fieldInfos, boolean useReportFormat, boolean includeTOC, boolean includeHeaderFooter) throws IOException {
        String html = generateFullHtmlForPdf(data, fieldInfos, request.getFieldFormats(), useReportFormat, includeTOC, includeHeaderFooter);
        log.info("PDF导出 - 生成的HTML长度: {}, 字段数量: {}", html.length(), fieldInfos.size());
        if (html.length() < 1000) {
            log.info("PDF导出 - 生成的HTML内容: {}", html);
        }
        OutputStream outputStream = response.getOutputStream();
        ConverterProperties properties = new ConverterProperties();
        DefaultFontProvider fontProvider = new DefaultFontProvider(true, true, true);
        String[] fontDirs = {
            "/usr/share/fonts/truetype/msttcore",
            "/usr/share/fonts/truetype",
            "C:/Windows/Fonts",
            System.getProperty("java.home") + "/lib/fonts"
        };
        String[] fontFiles = {
            "SimSun.ttf", "simsun.ttf", "simsun.ttc",
            "SimHei.ttf", "simhei.ttf",
            "msyh.ttf", "msyhbd.ttf"
        };
        boolean fontAdded = false;
        String loadedFontPath = null;
        for (String fontDir : fontDirs) {
            java.io.File dir = new java.io.File(fontDir);
            if (dir.exists() && dir.isDirectory()) {
                try {
                    fontProvider.addDirectory(fontDir);
                    fontAdded = true;
                    loadedFontPath = fontDir;
                    log.info("成功添加字体目录: {}", fontDir);
                    break;
                } catch (Exception e) {
                    log.debug("添加字体目录失败: {}", fontDir, e);
                }
            }
        }
        if (!fontAdded) {
            for (String fontDir : fontDirs) {
                for (String fontFile : fontFiles) {
                    String fontPath = fontDir + "/" + fontFile;
                    try {
                        java.io.File font = new java.io.File(fontPath);
                        if (font.exists() && font.isFile()) {
                            try {
                                FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
                                fontProvider.addFont(fontProgram);
                                fontAdded = true;
                                loadedFontPath = fontPath;
                                log.info("成功加载中文字体: {}", fontPath);
                                break;
                            } catch (Exception e1) {
                                try {
                                    fontProvider.addFont(fontPath);
                                    fontAdded = true;
                                    loadedFontPath = fontPath;
                                    log.info("成功加载中文字体(直接路径): {}", fontPath);
                                    break;
                                } catch (Exception e2) {
                                    log.debug("尝试加载字体失败: {}", fontPath, e2);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.debug("检查字体文件失败: {}", fontPath, e);
                    }
                }
                if (fontAdded) break;
            }
        }
        if (!fontAdded) {
            try {
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/simsun.ttf");
                if (fontStream == null) {
                    fontStream = getClass().getClassLoader().getResourceAsStream("fonts/SimSun.ttf");
                }
                if (fontStream != null) {
                    byte[] fontBytes = fontStream.readAllBytes();
                    fontStream.close();
                    FontProgram fontProgram = FontProgramFactory.createFont(fontBytes);
                    fontProvider.addFont(fontProgram);
                    fontAdded = true;
                    log.info("从resources加载中文字体成功");
                }
            } catch (Exception e) {
                log.warn("无法从resources加载中文字体", e);
            }
        }
        if (!fontAdded) {
            log.warn("未找到中文字体文件，html2pdf将尝试使用系统字体。如果中文无法显示，请将中文字体文件放在resources/fonts/目录下");
        } else {
            log.info("已加载中文字体支持，路径: {}", loadedFontPath);
        }
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), outputStream, properties);
        outputStream.flush();
    }
    private String generateFullHtmlForPdf(List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats, 
                                          boolean useReportFormat, boolean includeTOC, boolean includeHeaderFooter) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif; margin: 0; padding: 20px; background-color: #fff;'>");
        if (includeTOC && useReportFormat) {
            html.append("<div style='margin-bottom: 30px; page-break-after: always;'><div style='font-size: 16px; font-weight: bold; text-align: center; margin-bottom: 20px;'>目录</div>");
            for (int i = 0; i < Math.min(data.size(), 50); i++) {
                KnowledgeItemVo item = data.get(i);
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
                if (title.length() > 50) {
                    title = title.substring(0, 50) + "...";
                }
                html.append("<div style='font-size: 12px; margin-bottom: 5px;'>").append((i + 1)).append(". ").append(exportDataService.escapeHtml(title)).append("</div>");
            }
            html.append("</div>");
        }
        if (useReportFormat) {
            html.append(generateReportFormatHtmlForPdf(data, fieldInfos, fieldFormats));
        } else {
            html.append(generateTableFormatHtmlForPdf(data, fieldInfos, fieldFormats));
        }
        html.append("</body></html>");
        return html.toString();
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
    private String getSeverityBgColor(String severity) {
        if (StringUtils.isBlank(severity)) {
            return "#e6f7ff";
        }
        String lowerSeverity = severity.toLowerCase();
        switch (lowerSeverity) {
            case "critical":
                return "#fff1f0";
            case "high":
                return "#fff1f0";
            case "medium":
                return "#fffbe6";
            case "low":
                return "#f6ffed";
            case "none":
                return "#f5f5f5";
            default:
                return "#e6f7ff";
        }
    }
    private String generateReportFormatHtmlForPdf(List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        Map<String, CweReferenceVo> cweMap = exportDataService.buildCweMap(data, exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = exportDataService.buildTagMap(data, exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        Map<Long, String> userMap = exportDataService.buildUserMap(data, fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()));
        Map<String, String> knowledgeBaseMap = exportDataService.buildKnowledgeBaseMap(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            exportDataService.extractExpandedFields(fieldInfos));
        DictService dictService = SpringUtils.getBean(DictService.class);
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            KnowledgeItemVo item = data.get(i);
            String severity = item.getSeverity();
            if (StringUtils.isBlank(severity) && item.getCvssScore() != null) {
                severity = CvssScoreCalculator.mapSeverityByScore(item.getCvssScore());
            }
            String borderColor = getSeverityColor(severity);
            String bgColor = getSeverityBgColor(severity);
            html.append("<div style='border: 1px solid #e5e5e5; border-left: 4px solid ").append(borderColor).append("; padding: 15px; margin-bottom: 15px; page-break-inside: avoid; background-color: ").append(bgColor).append("; border-radius: 6px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>");
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
            if (StringUtils.isBlank(title)) {
                title = "条目 " + (i + 1);
            }
            html.append("<div style='font-size: 14px; font-weight: bold; color: #404040; margin-bottom: 8px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append((i + 1)).append(". ").append(exportDataService.escapeHtml(title)).append("</div>");
            List<String> metaInfo = new ArrayList<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key) || "summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    continue;
                }
                Object value = exportDataService.getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                String valueStr = value != null ? String.valueOf(value) : "";
                if (StringUtils.isNotBlank(valueStr)) {
                    String labelText = exportDataService.escapeHtml(fieldInfo.getLabel());
                    String valueText = exportDataService.escapeHtml(valueStr);
                    if ("severity".equals(key) || "severityLabel".equals(key)) {
                        String severityValue = "severity".equals(key) ? severity : valueStr;
                        String severityColor = getSeverityColor(severityValue);
                        metaInfo.add("<span style='color: #495057;'>" + labelText + "</span>: <span style='background-color: " + severityColor + "; color: #fff; padding: 2px 8px; border-radius: 4px; font-weight: 600; display: inline-block;'>" + valueText + "</span>");
                    } else {
                        metaInfo.add("<span style='color: #495057;'>" + labelText + "</span>: <span style='color: #6c757d; font-weight: 500;'>" + valueText + "</span>");
                    }
                }
            }
            if (!metaInfo.isEmpty()) {
                html.append("<div style='font-size: 10px; color: #808080; margin-bottom: 8px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(String.join(" <span style='color: #dee2e6; margin: 0 8px;'>|</span> ", metaInfo)).append("</div>");
            }
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key)) {
                    continue;
                }
                if ("summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    Object value = exportDataService.getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                    if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                        html.append("<div style='margin-bottom: 14px; padding: 12px; background-color: #f8f9fa; border-radius: 4px; border-left: 3px solid #52c41a; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>");
                        html.append("<div style='font-weight: 600; color: #212529; font-size: 13px; margin-bottom: 6px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(exportDataService.escapeHtml(fieldInfo.getLabel())).append("</div>");
                        html.append("<div style='color: #495057; font-size: 12px; line-height: 1.7; white-space: pre-wrap; word-wrap: break-word; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(exportDataService.escapeHtml(String.valueOf(value))).append("</div>");
                        html.append("</div>");
                    }
                }
            }
            html.append("</div>");
        }
        return html.toString();
    }
    private String generateTableFormatHtmlForPdf(List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        List<Map<String, Object>> exportData = exportDataService.convertToMapList(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            exportDataService.extractExpandedFields(fieldInfos), fieldFormats);
        StringBuilder html = new StringBuilder("<table style='border-collapse: collapse; width: 100%; font-size: 12px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>");
        html.append("<thead><tr style='background-color: #f5f5f5;'>");
        for (FieldInfoVo fieldInfo : fieldInfos) {
            html.append("<th style='padding: 10px 8px; border: 1px solid #c8c8c8; font-weight: 600; text-align: center; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>")
                .append(exportDataService.escapeHtml(fieldInfo.getLabel())).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (Map<String, Object> rowData : exportData) {
            html.append("<tr>");
            for (FieldInfoVo fieldInfo : fieldInfos) {
                Object value = rowData.get(fieldInfo.getKey());
                String text = value != null ? String.valueOf(value) : "";
                if (text.length() > 100) {
                    text = text.substring(0, 100) + "...";
                }
                html.append("<td style='padding: 5px; border: 1px solid #c8c8c8; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(exportDataService.escapeHtml(text)).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table>");
        return html.toString();
    }
    private VelocityEngine velocityEngine;

    private VelocityEngine getVelocityEngine() {
        if (velocityEngine == null) {
            synchronized (this) {
                if (velocityEngine == null) {
                    try {
                        Properties p = new Properties();
                        p.setProperty("resource.loaders", "classpath");
                        p.setProperty("resource.loader.classpath.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
                        p.setProperty("resource.default_encoding", StandardCharsets.UTF_8.name());
                        VelocityEngine ve = new VelocityEngine();
                        ve.init(p);
                        velocityEngine = ve;
                        log.info("Velocity引擎初始化完成");
                    } catch (Exception e) {
                        log.error("Velocity引擎初始化失败", e);
                        throw new RuntimeException("Velocity引擎初始化失败", e);
                    }
                }
            }
        }
        return velocityEngine;
    }

    private void initializeVelocity() {
        getVelocityEngine(); // 确保初始化
    }

    private String renderVelocityTemplate(String templatePath, VelocityContext context) throws IOException {
        try {
            Template template = getVelocityEngine().getTemplate(templatePath, StandardCharsets.UTF_8.name());
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("Velocity模板渲染失败: {}", templatePath, e);
            throw new IOException("Velocity模板渲染失败: " + templatePath, e);
        }
    }
    private void convertHtmlToPdfWithOpenHtml(String html, OutputStream outputStream, boolean includeHeaderFooter, String headerText) throws IOException {
        log.info("PDF流式导出 - 开始使用OpenHTML to PDF转换HTML到PDF");
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM);
            //优先从项目resources/fonts目录加载字体（跨平台兼容）
            //字体配置：[资源路径, 字体族名称]
            //支持多字重：Regular(400), Medium(500), Bold(700), Light(300), Thin(100), DemiLight(350), Black(900)
            String[][] classpathFontConfigs = {
                {"fonts/NotoSansCJKsc-Regular.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Medium.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Bold.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Light.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Thin.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-DemiLight.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Black.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSerifCJK-Regular.ttf", "Noto Serif CJK SC"},
                {"fonts/NotoSerifCJK-Bold.ttf", "Noto Serif CJK SC"},
                {"fonts/WenQuanYiZenHei.ttf", "WenQuanYi Zen Hei"},
                {"fonts/SimSun.ttf", "SimSun"},
                {"fonts/SimHei.ttf", "SimHei"},
                {"fonts/MicrosoftYaHei.ttf", "Microsoft YaHei"}
            };
            //系统字体路径作为回退（兼容性）
            String[][] systemFontConfigs = {
                {"/usr/share/fonts/truetype/msttcore/SimSun.ttf", "SimSun"},
                {"/usr/share/fonts/truetype/msttcore/SimHei.ttf", "SimHei"},
                {"/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc", "WenQuanYi Zen Hei"},
                {"/usr/share/fonts/wenquanyi/wqy-zenhei/wqy-zenhei.ttc", "WenQuanYi Zen Hei"},
                {"/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc", "Noto Sans CJK SC"},
                {"/usr/share/fonts/opentype/noto/NotoSerifCJK-Regular.ttc", "Noto Serif CJK SC"},
                {"C:/Windows/Fonts/simsun.ttc", "SimSun"},
                {"C:/Windows/Fonts/simhei.ttf", "SimHei"},
                {"C:/Windows/Fonts/msyh.ttf", "Microsoft YaHei"}
            };
            List<String> registeredFonts = new ArrayList<>();
            Set<String> registeredFontKeys = new HashSet<>();
            Set<String> registeredFamilyNamesFromClasspath = new HashSet<>();
            //优先从classpath加载字体（支持多字重）
            List<java.io.File> tempFontFiles = new ArrayList<>();
            for (String[] fontConfig : classpathFontConfigs) {
                String resourcePath = fontConfig[0];
                String fontFamilyName = fontConfig[1];
                String fontKey = resourcePath + "|" + fontFamilyName;
                if (registeredFontKeys.contains(fontKey)) {
                    continue;
                }
                try {
                    Resource resource = new ClassPathResource(resourcePath);
                    if (resource.exists()) {
                        java.io.File fontFile;
                        try {
                            fontFile = resource.getFile();
                        } catch (Exception e) {
                            //jar包内资源，需要复制到临时文件
                            java.io.File tempFile = java.io.File.createTempFile("font_", "_" + new java.io.File(resourcePath).getName());
                            tempFile.deleteOnExit();
                            tempFontFiles.add(tempFile);
                            try (InputStream is = resource.getInputStream();
                                 java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
                                is.transferTo(fos);
                            }
                            fontFile = tempFile;
                            log.debug("PDF流式导出 - 从jar包内复制字体到临时文件: {}", tempFile.getAbsolutePath());
                        }
                        builder.useFont(fontFile, fontFamilyName);
                        registeredFonts.add(fontFamilyName + " (" + resourcePath + ")");
                        registeredFontKeys.add(fontKey);
                        registeredFamilyNamesFromClasspath.add(fontFamilyName);
                        log.info("PDF流式导出 - OpenHTML to PDF从classpath加载字体: {} (字体族: {})", resourcePath, fontFamilyName);
                    }
                } catch (Exception e) {
                    log.debug("PDF流式导出 - OpenHTML to PDF从classpath加载字体失败: {}", resourcePath, e);
                }
            }
            //回退到系统字体路径（仅当classpath中未找到该字体族时）
            for (String[] fontConfig : systemFontConfigs) {
                String fontPath = fontConfig[0];
                String fontFamilyName = fontConfig[1];
                if (registeredFamilyNamesFromClasspath.contains(fontFamilyName)) {
                    log.debug("PDF流式导出 - 跳过系统路径字体 {}，因为classpath中已注册字体族: {}", fontPath, fontFamilyName);
                    continue;
                }
                String fontKey = fontPath + "|" + fontFamilyName;
                if (registeredFontKeys.contains(fontKey)) {
                    continue;
                }
                try {
                    java.io.File fontFile = new java.io.File(fontPath);
                    if (fontFile.exists() && fontFile.isFile()) {
                        builder.useFont(fontFile, fontFamilyName);
                        registeredFonts.add(fontFamilyName + " (" + fontPath + ")");
                        registeredFontKeys.add(fontKey);
                        log.info("PDF流式导出 - OpenHTML to PDF从系统路径加载字体: {} (字体族: {})", fontPath, fontFamilyName);
                    }
                } catch (Exception e) {
                    log.debug("PDF流式导出 - OpenHTML to PDF从系统路径加载字体失败: {}", fontPath, e);
                }
            }
            if (registeredFonts.isEmpty()) {
                log.warn("PDF流式导出 - OpenHTML to PDF未找到中文字体，将使用默认字体");
            } else {
                log.info("PDF流式导出 - OpenHTML to PDF已注册字体: {}", String.join(", ", registeredFonts));
            }
            builder.run();
            log.info("PDF流式导出 - OpenHTML to PDF转换完成");
            outputStream.flush();
            log.info("PDF流式导出 - 输出流已刷新");
        } catch (Exception e) {
            log.error("PDF流式导出 - OpenHTML to PDF转换失败", e);
            throw new IOException("OpenHTML to PDF转换失败", e);
        }
    }
}
