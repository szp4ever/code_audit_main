package org.ruoyi.knowledge.ingestion.loader;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.knowledge.ingestion.split.*;
import org.ruoyi.knowledge.shared.config.PdfProperties;
import org.ruoyi.knowledge.shared.constant.FileType;
import org.ruoyi.system.mapper.SysOssMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class ResourceLoaderFactory {
    private final CharacterTextSplitter characterTextSplitter;
    private final CodeTextSplitter codeTextSplitter;
    private final MarkdownTextSplitter markdownTextSplitter;
    private final TokenTextSplitter tokenTextSplitter;
    private final ExcelTextSplitter excelTextSplitter;
    private final PdfProperties pdfProperties;
    private final SysOssMapper sysOssMapper;


    public ResourceLoader getLoaderByFileType(String fileType) {
        if (FileType.isTextFile(fileType)) {
            return new TextFileLoader(characterTextSplitter);
        } else if (FileType.isWord(fileType)) {
            return new WordLoader(characterTextSplitter);
        } else if (FileType.isPdf(fileType) && pdfProperties.getTransition() != null && pdfProperties.getTransition().isEnableMinerU()) {
            return new PdfMinerUFileLoader(characterTextSplitter, pdfProperties, sysOssMapper);
        } else if (FileType.isPdf(fileType)) {
            // 检查是否未配置 MinerU，给用户提示
            if (pdfProperties.getTransition() == null) {
                log.info("PDF 处理使用标准解析器（MinerU 未配置）。如需启用 MinerU 增强解析，请在 application.yml 中添加配置：pdf.transition.enableMinerU=true");
            } else if (!pdfProperties.getTransition().isEnableMinerU()) {
                log.info("PDF 处理使用标准解析器（MinerU 已禁用）。如需启用，请设置 pdf.transition.enableMinerU=true");
            }
            return new PdfFileLoader(characterTextSplitter);
        } else if (FileType.isMdFile(fileType)) {
            return new MarkDownFileLoader(markdownTextSplitter);
        } else if (FileType.isCodeFile(fileType)) {
            return new CodeFileLoader(codeTextSplitter);
        } else if (FileType.isExcel(fileType)) {
            return new ExcelFileLoader(excelTextSplitter);
        } else {
            return new TextFileLoader(characterTextSplitter);
        }
    }
}
