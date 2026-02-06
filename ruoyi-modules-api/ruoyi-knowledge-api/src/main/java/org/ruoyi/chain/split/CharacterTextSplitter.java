package org.ruoyi.chain.split;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.service.IKnowledgeInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Primary
public class CharacterTextSplitter implements TextSplitter {

    @Lazy
    @Resource
    private IKnowledgeInfoService knowledgeInfoService;

    @Override
    public List<String> split(String content, String kid) {
        log.info("[CharacterTextSplitter] 开始分块: kid={}, contentLength={}, contentIsNull={}", 
                kid, content != null ? content.length() : 0, content == null);
        
        // 检查content是否为null
        if (content == null) {
            log.warn("[CharacterTextSplitter] content为null，kid={}", kid);
            return new ArrayList<>();
        }
        
        // 检查content是否为空或只包含空白字符
        String trimmedContent = content.trim();
        if (trimmedContent.isEmpty()) {
            log.warn("[CharacterTextSplitter] content为空或只包含空白字符，kid={}, originalLength={}", kid, content.length());
            return new ArrayList<>();
        }
        
        // 从知识库表中获取配置
        KnowledgeInfoVo knowledgeInfoVo = knowledgeInfoService.queryByKid(kid);
        if (knowledgeInfoVo == null) {
            log.error("[CharacterTextSplitter] 知识库不存在: kid={}", kid);
            return new ArrayList<>();
        }
        
        String knowledgeSeparator = knowledgeInfoVo.getKnowledgeSeparator();
        int textBlockSize = knowledgeInfoVo.getTextBlockSize();
        int overlapChar = knowledgeInfoVo.getOverlapChar();
        
        log.info("[CharacterTextSplitter] 分块配置: kid={}, knowledgeSeparator={}, textBlockSize={}, overlapChar={}", 
                kid, knowledgeSeparator, textBlockSize, overlapChar);
        
        // 验证配置参数
        if (textBlockSize <= 0) {
            log.error("[CharacterTextSplitter] textBlockSize配置无效: kid={}, textBlockSize={}", kid, textBlockSize);
            return new ArrayList<>();
        }
        if (overlapChar < 0) {
            log.warn("[CharacterTextSplitter] overlapChar为负数，重置为0: kid={}, overlapChar={}", kid, overlapChar);
            overlapChar = 0;
        }
        
        List<String> chunkList = new ArrayList<>();
        if (StringUtils.isNotBlank(knowledgeSeparator) && content.contains(knowledgeSeparator)) {
            // 按自定义分隔符切分
            log.debug("[CharacterTextSplitter] 使用分隔符切分: kid={}, separator={}", kid, knowledgeSeparator);
            String[] chunks = content.split(knowledgeSeparator);
            log.debug("[CharacterTextSplitter] 分隔符切分结果: kid={}, chunksCount={}", kid, chunks.length);
            for (String chunk : chunks) {
                // 过滤空字符串和只包含空白字符的chunk
                if (StringUtils.isNotBlank(chunk)) {
                    chunkList.add(chunk.trim());
                }
            }
            log.info("[CharacterTextSplitter] 分隔符切分完成: kid={}, 原始chunks={}, 过滤后chunks={}", 
                    kid, chunks.length, chunkList.size());
        } else {
            // 按固定大小切分
            log.debug("[CharacterTextSplitter] 使用固定大小切分: kid={}, textBlockSize={}", kid, textBlockSize);
            int indexMin = 0;
            int len = content.length();
            int i = 0;
            int right = 0;
            while (true) {
                if (len > right) {
                    int begin = i * textBlockSize - overlapChar;
                    if (begin < indexMin) {
                        begin = indexMin;
                    }
                    int end = textBlockSize * (i + 1) + overlapChar;
                    if (end > len) {
                        end = len;
                    }
                    // 确保begin < end
                    if (begin >= end) {
                        break;
                    }
                    String chunk = content.substring(begin, end);
                    // 过滤空字符串和只包含空白字符的chunk
                    if (StringUtils.isNotBlank(chunk)) {
                        chunkList.add(chunk.trim());
                    }
                    i++;
                    right = right + textBlockSize;
                } else {
                    break;
                }
            }
            log.info("[CharacterTextSplitter] 固定大小切分完成: kid={}, contentLength={}, chunks={}", 
                    kid, len, chunkList.size());
        }
        
        log.info("[CharacterTextSplitter] 分块完成: kid={}, 最终chunks数量={}", kid, chunkList.size());
        if (chunkList.isEmpty()) {
            log.warn("[CharacterTextSplitter] 分块结果为空: kid={}, contentLength={}, knowledgeSeparator={}, textBlockSize={}", 
                    kid, content.length(), knowledgeSeparator, textBlockSize);
        }
        
        return chunkList;
    }
}
