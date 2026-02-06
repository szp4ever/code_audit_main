package org.ruoyi.chain.split;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.service.IKnowledgeInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MarkdownTextSplitter implements TextSplitter {
    
    @Lazy
    @Resource
    private IKnowledgeInfoService knowledgeInfoService;
    
    @Override
    public List<String> split(String content, String kid) {
        log.info("[MarkdownTextSplitter] 开始分块: kid={}, contentLength={}, contentIsNull={}", 
                kid, content != null ? content.length() : 0, content == null);
        
        if (content == null) {
            log.warn("[MarkdownTextSplitter] content为null，kid={}", kid);
            return new ArrayList<>();
        }
        
        String trimmedContent = content.trim();
        if (trimmedContent.isEmpty()) {
            log.warn("[MarkdownTextSplitter] content为空或只包含空白字符，kid={}, originalLength={}", kid, content.length());
            return new ArrayList<>();
        }
        
        KnowledgeInfoVo knowledgeInfoVo = knowledgeInfoService.queryByKid(kid);
        if (knowledgeInfoVo == null) {
            log.error("[MarkdownTextSplitter] 知识库不存在: kid={}", kid);
            return new ArrayList<>();
        }
        
        String knowledgeSeparator = knowledgeInfoVo.getKnowledgeSeparator();
        int textBlockSize = knowledgeInfoVo.getTextBlockSize();
        int overlapChar = knowledgeInfoVo.getOverlapChar();
        
        log.info("[MarkdownTextSplitter] 分块配置: kid={}, knowledgeSeparator={}, textBlockSize={}, overlapChar={}", 
                kid, knowledgeSeparator, textBlockSize, overlapChar);
        
        if (textBlockSize <= 0) {
            log.error("[MarkdownTextSplitter] textBlockSize配置无效: kid={}, textBlockSize={}", kid, textBlockSize);
            return new ArrayList<>();
        }
        if (overlapChar < 0) {
            overlapChar = 0;
        }
        
        List<String> chunkList = new ArrayList<>();
        // Markdown文件优先按标题切分（## 或 ###）
        if (content.contains("\n##") || content.contains("\n###")) {
            String[] chunks = content.split("\n(?=#{2,})");
            for (String chunk : chunks) {
                if (StringUtils.isNotBlank(chunk)) {
                    chunkList.add(chunk.trim());
                }
            }
            log.info("[MarkdownTextSplitter] 按Markdown标题切分: kid={}, chunks={}", kid, chunkList.size());
        } else if (StringUtils.isNotBlank(knowledgeSeparator) && content.contains(knowledgeSeparator)) {
            String[] chunks = content.split(knowledgeSeparator);
            for (String chunk : chunks) {
                if (StringUtils.isNotBlank(chunk)) {
                    chunkList.add(chunk.trim());
                }
            }
        } else {
            int len = content.length();
            int i = 0;
            int right = 0;
            while (len > right) {
                int begin = Math.max(0, i * textBlockSize - overlapChar);
                int end = Math.min(len, textBlockSize * (i + 1) + overlapChar);
                if (begin >= end) {
                    break;
                }
                String chunk = content.substring(begin, end);
                if (StringUtils.isNotBlank(chunk)) {
                    chunkList.add(chunk.trim());
                }
                i++;
                right = right + textBlockSize;
            }
        }
        
        log.info("[MarkdownTextSplitter] 分块完成: kid={}, 最终chunks数量={}", kid, chunkList.size());
        if (chunkList.isEmpty()) {
            log.warn("[MarkdownTextSplitter] 分块结果为空: kid={}, contentLength={}, knowledgeSeparator={}, textBlockSize={}", 
                    kid, content.length(), knowledgeSeparator, textBlockSize);
        }
        
        return chunkList;
    }
}
