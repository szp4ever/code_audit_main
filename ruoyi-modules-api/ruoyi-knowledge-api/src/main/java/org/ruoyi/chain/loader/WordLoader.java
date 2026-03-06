package org.ruoyi.chain.loader;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chain.split.TextSplitter;
import org.ruoyi.common.core.exception.UtilException;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class WordLoader implements ResourceLoader {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private final TextSplitter textSplitter;

    @Override
    public String getContent(InputStream inputStream) {
        try (InputStream bufferedStream = new BufferedInputStream(inputStream, DEFAULT_BUFFER_SIZE)) {
            ApacheTikaDocumentParser apacheTikaDocumentParser = new ApacheTikaDocumentParser();
            Document document = apacheTikaDocumentParser.parse(bufferedStream);
            return document.text();
        } catch (IOException e) {
            String errorMsg = "Word文件流读取失败";
            throw new UtilException(errorMsg, e);
        } catch (RuntimeException e) {
            String errorMsg = "Word内容解析异常";
            throw new UtilException(errorMsg, e);
        }
    }

    @Override
    public List<String> getChunkList(String content, String kid) {
        return textSplitter.split(content, kid);
    }

}
