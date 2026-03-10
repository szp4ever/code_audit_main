package org.ruoyi.knowledge.enrichment.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.chat.entity.chat.ChatCompletion;
import org.ruoyi.common.chat.entity.chat.ChatCompletionResponse;
import org.ruoyi.common.chat.entity.chat.Message;
import org.ruoyi.common.chat.openai.OpenAiStreamClient;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.exception.base.BaseException;
import org.ruoyi.common.chat.openai.exception.CommonError;
import org.ruoyi.knowledge.shared.utils.RateLimitHandler;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.knowledge.cwe.domain.CweReference;
import org.ruoyi.knowledge.enrichment.domain.bo.ExtractedItemData;
import org.ruoyi.knowledge.enrichment.domain.bo.ExtractionContext;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.service.IChatModelService;
import org.ruoyi.knowledge.cwe.service.ICweReferenceService;
import org.ruoyi.knowledge.curation.service.IKnowledgeTagService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * 知识条目提取服务实现
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KnowledgeItemExtractionServiceImpl implements IKnowledgeItemExtractionService {

    private final IChatModelService chatModelService;
    private final IKnowledgeTagService knowledgeTagService;
    private final ICweReferenceService cweReferenceService;

    @Override
    public ExtractedItemData extractFromChunk(String chunkContent, ExtractionContext context) {
        try {
            // 构建Prompt
            String prompt = buildExtractionPrompt(chunkContent, context);
            
            ChatModelVo chatModelVo = null;
            if (StringUtils.isNotBlank(context.getModelName())) {
                chatModelVo = chatModelService.selectModelByName(context.getModelName());
                if (chatModelVo != null && "chat".equals(chatModelVo.getCategory())) {
                    if (!isModelConfigValid(chatModelVo)) {
                        log.warn("指定的模型 {} 配置无效（api_key或api_host可能为占位符），降级到智能选择", 
                            context.getModelName());
                        chatModelVo = null;
                    }
                } else {
                    if (chatModelVo != null) {
                        log.warn("模型 {} 不是chat类型（category={}），降级到智能选择", 
                            context.getModelName(), chatModelVo.getCategory());
                    }
                    chatModelVo = null;
                }
            }
            if (chatModelVo == null) {
                chatModelVo = chatModelService.selectChatModelForKnowledgeExtraction();
                if (chatModelVo == null) {
                    throw new ServiceException("未找到可用的chat模型，请先在chat_model表中配置category='chat'且model_show='0'的有效模型");
                }
                log.info("使用智能选择的模型: {}", chatModelVo.getModelName());
            }
            
            // 验证模型配置
            validateModelConfig(chatModelVo, "知识条目提取");
            
            // 同步调用LLM
            String apiHost = chatModelVo.getApiHost();
            System.out.println("[知识提取] 模型: " + chatModelVo.getModelName() + ", apiHost原始值: " + apiHost);
            OpenAiStreamClient client = OpenAiStreamClient.builder()
                    .apiHost(apiHost)
                    .apiKey(Collections.singletonList(chatModelVo.getApiKey()))
                    .build();
            
            List<Message> messages = Collections.singletonList(
                    Message.builder()
                            .role(Message.Role.USER)
                            .content(prompt)
                            .build()
            );
            
            ChatCompletion completion = ChatCompletion.builder()
                    .messages(messages)
                    .model(chatModelVo.getModelName())
                    .stream(false)
                    .build();
            
            ChatCompletionResponse response = RateLimitHandler.executeWithRetry(() -> {
                RateLimitHandler.addCallInterval();
                return client.chatCompletion(completion);
            }, "LLM提取");
            
            Object content = response.getChoices().get(0).getMessage().getContent();
            String responseText = content != null ? content.toString() : "";
            System.out.println("[知识提取] LLM返回的完整响应内容长度: " + responseText.length());
            System.out.println("[知识提取] LLM返回的完整响应内容前500字符: " + (responseText.length() > 500 ? responseText.substring(0, 500) + "..." : responseText));
            
            // 解析响应
            return parseResponse(responseText, context);
            
        } catch (Exception e) {
            if (RateLimitHandler.isRateLimitError(e)) {
                log.error("LLM提取失败（速率限制，已重试）: {}", e.getMessage(), e);
                throw new ServiceException("LLM提取失败：速率限制，已重试但仍失败");
            } else {
                log.error("LLM提取失败: {}", e.getMessage(), e);
                throw new ServiceException("LLM提取失败: " + e.getMessage());
            }
        }
    }

    @Override
    public List<ExtractedItemData> extractBatch(List<String> chunkContents, ExtractionContext context) {
        List<ExtractedItemData> results = new ArrayList<>();
        for (int i = 0; i < chunkContents.size(); i++) {
            String chunkContent = chunkContents.get(i);
            try {
                ExtractedItemData data = extractFromChunk(chunkContent, context);
                results.add(data);
            } catch (Exception e) {
                if (RateLimitHandler.isRateLimitError(e)) {
                    log.error("批量提取片段{}遇到速率限制，已重试但仍失败，跳过: {}", i, e.getMessage());
                } else {
                    log.error("批量提取片段{}失败，跳过: {}", i, e.getMessage());
                }
                // 创建空数据，标记为失败
                ExtractedItemData failedData = new ExtractedItemData();
                results.add(failedData);
            }
        }
        return results;
    }

    @Override
    public ExtractionProgress getProgress(String taskId) {
        // 当前实现为同步批量处理，无需异步进度查询
        ExtractionProgress progress = new ExtractionProgress();
        progress.setTaskId(taskId);
        progress.setTotal(0);
        progress.setCompleted(0);
        progress.setFailed(0);
        return progress;
    }

    /**
     * 构建提取Prompt
     */
    private String buildExtractionPrompt(String chunkContent, ExtractionContext context) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("你是一位专注于电力行业代码审计的安全漏洞分析专家。");
        prompt.append("电力行业代码审计涉及电力监控系统、生产控制系统、管理信息系统等关键基础设施，");
        prompt.append("需要遵循《电力行业网络安全管理办法》和《电力监控系统安全防护规定》等法规要求。");
        prompt.append("请从以下文本片段中提取安全漏洞知识条目的结构化信息，所有文本内容必须使用中文。\n\n");
        
        prompt.append("【重要约束】\n");
        prompt.append("- 所有文本字段（title、summary、problemDescription、fixSolution）必须使用纯文本格式，严格禁止使用任何Markdown语法（包括但不限于：标题标记#、粗体**、代码块```、列表-或*、链接[]()等）。\n");
        prompt.append("- 输出必须是纯JSON格式，不要包含Markdown代码块标记（如```json或```）或其它任何Markdown标记！\n");
        prompt.append("- 长文本字段（problemDescription、fixSolution等）的内容本身可以使用换行符（\\n），系统会正确存储和显示换行。换行符用于提高文本的可读性，可以在适当的位置使用。\n");
        prompt.append("- exampleCode字段的值必须全部是代码，不能包含任何非代码的文字说明。如果需要说明，必须使用代码注释的形式（如//、/* */、#等），而不能直接写文字。换行必须规范，保持代码的可读性和正确性。代码中的换行符（\\n）会被正确保留和显示。\n\n");
        
        if (CollUtil.isNotEmpty(context.getAvailableTags())) {
            prompt.append("- 标签必须从以下列表中选择，不能生成新标签：\n");
            for (int i = 0; i < Math.min(context.getAvailableTags().size(), 100); i++) {
                prompt.append("  ").append(i + 1).append(". ").append(context.getAvailableTags().get(i)).append("\n");
            }
            if (context.getAvailableTags().size() > 100) {
                prompt.append("  ...（共").append(context.getAvailableTags().size()).append("个标签）\n");
            }
            prompt.append("\n");
        }
        
        if (CollUtil.isNotEmpty(context.getAvailableVulnerabilityTypes())) {
            prompt.append("- 漏洞类型必须从以下列表中选择，不能生成新类型。返回格式为完整CWE ID（如\"CWE-89\"），不要返回完整描述。可以多选，返回数组：\n");
            List<CweReference> stableCwes = context.getAvailableVulnerabilityTypes().stream()
                .filter(cwe -> "Stable".equals(cwe.getStatus()))
                .limit(50)
                .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(stableCwes)) {
                prompt.append("  【稳定状态】\n");
                for (int i = 0; i < Math.min(stableCwes.size(), 20); i++) {
                    CweReference cwe = stableCwes.get(i);
                    String name = StringUtils.isNotBlank(cwe.getNameZh()) ? cwe.getNameZh() : cwe.getNameEn();
                    prompt.append("    - ").append(cwe.getCweId()).append(": ").append(name).append("\n");
                }
            }
            prompt.append("\n");
        }
        
        if (CollUtil.isNotEmpty(context.getAvailableLanguages())) {
            prompt.append("- 适用语言必须从以下列表中选择：").append(String.join("、", context.getAvailableLanguages())).append("\n\n");
        }
        
        if (CollUtil.isNotEmpty(context.getAvailableSeverities())) {
            prompt.append("- 风险等级必须从以下列表中选择：").append(String.join("、", context.getAvailableSeverities())).append("\n\n");
        }
        
        prompt.append("【输出格式】\n");
        prompt.append("请以JSON格式返回，所有文本字段必须使用中文。格式如下：\n");
        prompt.append("{\n");
        prompt.append("  \"title\": \"string (必填，中文标题，最大255字符，简洁描述漏洞)\",\n");
        prompt.append("  \"summary\": \"string (可选，中文摘要，最大500字符，概述漏洞影响)\",\n");
        prompt.append("  \"problemDescription\": \"string (必填，中文问题描述，详细说明漏洞原理、触发条件、影响范围)\",\n");
        prompt.append("  \"fixSolution\": \"string (必填，中文修复方案，提供具体的修复步骤\",\n");
        prompt.append("  \"exampleCode\": \"string (可选，代码示例，必须全部是代码，不能包含非代码文字说明，如需说明请使用代码注释形式)\",\n");
        prompt.append("  \"vulnerabilityType\": \"string (可选，CWE ID，如\"CWE-89\"，必须从上述CWE列表中选择，单个值，保留用于兼容)\",\n");
        prompt.append("  \"vulnerabilityTypes\": [\"string (必填，CWE ID数组，如[\"CWE-89\", \"CWE-79\"]，必须从上述CWE列表中选择，至少选择一个，可多选)\"],\n");
        prompt.append("  \"language\": \"string (可选，必须从上述语言列表中选择，返回字典值如\"java\"、\"cpp\"等)\",\n");
        prompt.append("  \"severity\": \"string (可选，必须从上述风险等级列表中选择，返回字典值如\"none\"、\"low\"、\"medium\"、\"high\"、\"critical\")\",\n");
        prompt.append("  \"tags\": [\"string (可选，每个标签必须从上述标签列表中选择，可多选)\"],\n");
        prompt.append("  \"cvssAttackVector\": \"string (必填，CVSS攻击方式：N=远程、A=网络相邻、L=本地、P=物理)\",\n");
        prompt.append("  \"cvssAttackComplexity\": \"string (必填，CVSS利用复杂度：L=低、H=高)\",\n");
        prompt.append("  \"cvssPrivilegesRequired\": \"string (必填，CVSS权限需求：N=无需权限、L=需要权限、H=高级权限)\",\n");
        prompt.append("  \"cvssUserInteraction\": \"string (必填，CVSS用户交互：N=无需交互、R=需要交互)\",\n");
        prompt.append("  \"cvssImpact\": [\"string (必填，CVSS影响范围数组：C=机密性、I=完整性、A=可用性，至少选择一个，可多选如[\"C\", \"I\", \"A\"])\"],\n");
        prompt.append("  \"confidence\": {\n");
        prompt.append("    \"vulnerabilityType\": 0.85,\n");
        prompt.append("    \"language\": 0.92,\n");
        prompt.append("    \"tags\": [0.78, 0.82]\n");
        prompt.append("  }\n");
        prompt.append("}\n\n");
        
        prompt.append("【CVSS评分说明】\n");
        prompt.append("必须提供以下CVSS维度字段（与前端表单要求一致）：\n");
        prompt.append("- cvssAttackVector（攻击方式）：N=远程、A=网络相邻、L=本地、P=物理\n");
        prompt.append("- cvssAttackComplexity（利用复杂度）：L=低、H=高（注意：CVSS标准中只有L和H两个值）\n");
        prompt.append("- cvssPrivilegesRequired（权限需求）：N=无需权限、L=需要权限、H=高级权限\n");
        prompt.append("- cvssUserInteraction（用户交互）：N=无需交互、R=需要交互\n");
        prompt.append("- cvssImpact（影响范围）：数组，C=机密性、I=完整性、A=可用性，至少选择一个，可多选如[\"C\", \"I\", \"A\"]\n\n");
        
        prompt.append("【电力行业特殊要求】\n");
        prompt.append("- 重点关注可能影响电力监控系统、生产控制系统安全性的漏洞\n");
        prompt.append("- 考虑漏洞对电力系统可用性、完整性的影响\n");
        prompt.append("- 评估漏洞是否可能被利用进行横向移动或纵向渗透\n");
        prompt.append("- 如果涉及网络通信，考虑是否符合\"安全分区、网络专用、横向隔离、纵向认证\"原则\n\n");
        
        prompt.append("【文本内容】\n");
        prompt.append(chunkContent);
        
        return prompt.toString();
    }

    /**
     * 解析LLM响应
     */
    private ExtractedItemData parseResponse(String responseText, ExtractionContext context) {
        ExtractedItemData data = new ExtractedItemData();
        
        try {
            System.out.println("[parseResponse] ========== LLM完整原始响应 ==========");
            System.out.println("[parseResponse] 响应长度: " + responseText.length());
            System.out.println("[parseResponse] 完整响应内容:\n" + responseText);
            System.out.println("[parseResponse] ======================================");
            
            String jsonText = extractJsonFromResponse(responseText);
            System.out.println("[parseResponse] ========== 提取后的JSON文本 ==========");
            System.out.println("[parseResponse] JSON文本长度: " + jsonText.length());
            System.out.println("[parseResponse] 完整JSON文本:\n" + jsonText);
            System.out.println("[parseResponse] ======================================");
            jsonText = jsonText.trim();
            if (jsonText.startsWith("```json")) {
                jsonText = jsonText.substring(7).trim();
            }
            if (jsonText.startsWith("```")) {
                jsonText = jsonText.substring(3).trim();
            }
            if (jsonText.endsWith("```")) {
                jsonText = jsonText.substring(0, jsonText.length() - 3).trim();
            }
            System.out.println("[parseResponse] 最终清理后的JSON文本长度: " + jsonText.length());
            System.out.println("[parseResponse] 最终清理后的JSON文本前100字符: " + (jsonText.length() > 100 ? jsonText.substring(0, 100) + "..." : jsonText));
            JSONObject json = JSON.parseObject(jsonText);
            
            String title = json.getString("title");
            if (StringUtils.isNotBlank(title)) {
                if (title.length() > 255) {
                    title = title.substring(0, 255);
                }
                data.setTitle(title);
            }
            String summary = json.getString("summary");
            if (StringUtils.isNotBlank(summary)) {
                if (summary.length() > 500) {
                    summary = summary.substring(0, 500);
                }
                data.setSummary(summary);
            }
            data.setProblemDescription(json.getString("problemDescription"));
            data.setFixSolution(json.getString("fixSolution"));
            data.setExampleCode(json.getString("exampleCode"));
            
            data.setTags(validateAndFixTags(json.getJSONArray("tags"), context));
            data.setVulnerabilityType(validateAndFixVulnerabilityType(json.getString("vulnerabilityType"), context));
            data.setVulnerabilityTypes(validateAndFixVulnerabilityTypes(json.getJSONArray("vulnerabilityTypes"), context));
            if (CollUtil.isEmpty(data.getVulnerabilityTypes()) && StringUtils.isNotBlank(data.getVulnerabilityType())) {
                data.setVulnerabilityTypes(Collections.singletonList(data.getVulnerabilityType()));
            }
            if (StringUtils.isBlank(data.getVulnerabilityType()) && CollUtil.isNotEmpty(data.getVulnerabilityTypes())) {
                data.setVulnerabilityType(data.getVulnerabilityTypes().get(0));
            }
            data.setLanguage(validateAndFixLanguage(json.getString("language"), context));
            data.setSeverity(validateAndFixSeverity(json.getString("severity"), context));
            
            String cvssAttackVector = json.getString("cvssAttackVector");
            if (StringUtils.isNotBlank(cvssAttackVector)) {
                cvssAttackVector = cvssAttackVector.trim().toUpperCase();
                if (cvssAttackVector.equals("N") || cvssAttackVector.equals("A") || cvssAttackVector.equals("L") || cvssAttackVector.equals("P")) {
                    data.setCvssAttackVector(cvssAttackVector);
                }
            }
            String cvssAttackComplexity = json.getString("cvssAttackComplexity");
            if (StringUtils.isNotBlank(cvssAttackComplexity)) {
                cvssAttackComplexity = cvssAttackComplexity.trim().toUpperCase();
                if (cvssAttackComplexity.equals("L") || cvssAttackComplexity.equals("H")) {
                    data.setCvssAttackComplexity(cvssAttackComplexity);
                }
            }
            String cvssPrivilegesRequired = json.getString("cvssPrivilegesRequired");
            if (StringUtils.isNotBlank(cvssPrivilegesRequired)) {
                cvssPrivilegesRequired = cvssPrivilegesRequired.trim().toUpperCase();
                if (cvssPrivilegesRequired.equals("N") || cvssPrivilegesRequired.equals("L") || cvssPrivilegesRequired.equals("H")) {
                    data.setCvssPrivilegesRequired(cvssPrivilegesRequired);
                }
            }
            String cvssUserInteraction = json.getString("cvssUserInteraction");
            if (StringUtils.isNotBlank(cvssUserInteraction)) {
                cvssUserInteraction = cvssUserInteraction.trim().toUpperCase();
                if (cvssUserInteraction.equals("N") || cvssUserInteraction.equals("R")) {
                    data.setCvssUserInteraction(cvssUserInteraction);
                }
            }
            if (json.containsKey("cvssImpact")) {
                com.alibaba.fastjson2.JSONArray impactArray = json.getJSONArray("cvssImpact");
                if (impactArray != null && !impactArray.isEmpty()) {
                    List<String> impacts = new ArrayList<>();
                    for (Object obj : impactArray) {
                        if (obj != null) {
                            String impact = obj.toString().trim().toUpperCase();
                            if (impact.equals("C") || impact.equals("I") || impact.equals("A")) {
                                impacts.add(impact);
                            }
                        }
                    }
                    data.setCvssImpact(impacts);
                }
            }
            
            if (json.containsKey("confidence")) {
                JSONObject confidence = json.getJSONObject("confidence");
                Map<String, Double> confidenceMap = new HashMap<>();
                confidence.forEach((key, value) -> {
                    if (value instanceof Number) {
                        confidenceMap.put(key, ((Number) value).doubleValue());
                    }
                });
                data.setConfidence(confidenceMap);
            }
            
        } catch (Exception e) {
            log.error("解析LLM响应失败: {}", e.getMessage(), e);
            throw new ServiceException("解析LLM响应失败: " + e.getMessage());
        }
        
        return data;
    }

    private String extractJsonFromResponse(String responseText) {
        System.out.println("[extractJsonFromResponse] 输入文本长度: " + responseText.length());
        System.out.println("[extractJsonFromResponse] 输入文本前200字符: " + (responseText.length() > 200 ? responseText.substring(0, 200) + "..." : responseText));
        
        String cleanedText = responseText;
        if (cleanedText.contains("<think>")) {
            try {
                cleanedText = cleanedText.replaceAll("(?s)<think>.*?</think>", "").trim();
                System.out.println("[extractJsonFromResponse] 检测到<think>标签，已移除，清理后长度: " + cleanedText.length());
            } catch (Exception e) {
                System.out.println("[extractJsonFromResponse] 移除<think>标签时出错，使用原始文本: " + e.getMessage());
                cleanedText = responseText;
            }
        } else {
            System.out.println("[extractJsonFromResponse] 未检测到<think>标签，使用原始文本");
        }
        
        if (cleanedText.contains("```json")) {
            int start = cleanedText.indexOf("```json") + 7;
            int end = cleanedText.indexOf("```", start);
            if (end > start) {
                String extracted = cleanedText.substring(start, end).trim();
                System.out.println("[extractJsonFromResponse] 从```json代码块提取，长度: " + extracted.length());
                extracted = extracted.trim();
                if (extracted.startsWith("json")) {
                    extracted = extracted.substring(4).trim();
                }
                if (extracted.startsWith("{")) {
                    return extracted;
                }
            }
        }
        
        if (cleanedText.contains("```")) {
            int start = cleanedText.indexOf("```") + 3;
            int end = cleanedText.indexOf("```", start);
            if (end > start) {
                String extracted = cleanedText.substring(start, end).trim();
                extracted = extracted.trim();
                if (extracted.contains("\n")) {
                    String firstLine = extracted.substring(0, extracted.indexOf("\n")).trim();
                    if (firstLine.matches("^[a-zA-Z]+$")) {
                        extracted = extracted.substring(extracted.indexOf("\n") + 1).trim();
                    }
                }
                if (extracted.startsWith("{")) {
                    return extracted;
                }
            }
        }
        
        int start = cleanedText.indexOf("{");
        if (start >= 0) {
            int braceCount = 0;
            int end = -1;
            for (int i = start; i < cleanedText.length(); i++) {
                char c = cleanedText.charAt(i);
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        end = i;
                        break;
                    }
                }
            }
            if (end > start) {
                String extracted = cleanedText.substring(start, end + 1);
                return extracted;
            }
        }
        
        return cleanedText.trim();
    }

    private List<String> validateAndFixTags(com.alibaba.fastjson2.JSONArray tags, ExtractionContext context) {
        List<String> validTags = new ArrayList<>();
        if (tags == null || tags.isEmpty() || CollUtil.isEmpty(context.getAvailableTags())) {
            return validTags;
        }
        for (Object tagObj : tags) {
            String tagName = tagObj != null ? tagObj.toString() : null;
            if (StringUtils.isBlank(tagName)) {
                continue;
            }
            if (context.getAvailableTags().contains(tagName)) {
                validTags.add(tagName);
            } else {
                String matched = fuzzyMatch(tagName, context.getAvailableTags());
                if (matched != null) {
                    validTags.add(matched);
                }
            }
        }
        return validTags;
    }

    private String validateAndFixVulnerabilityType(String vulnerabilityType, ExtractionContext context) {
        if (StringUtils.isBlank(vulnerabilityType)) {
            return null;
        }
        String normalized = vulnerabilityType.trim().toUpperCase();
        for (CweReference cwe : context.getAvailableVulnerabilityTypes()) {
            String cweId = cwe.getCweId();
            if (normalized.equals(cweId) || normalized.equals(cweId.toUpperCase())) {
                return cweId;
            }
        }
        String numericPart = normalized.replaceAll("[^0-9]", "");
        if (StringUtils.isNotBlank(numericPart)) {
            for (CweReference cwe : context.getAvailableVulnerabilityTypes()) {
                String cweId = cwe.getCweId();
                String cweNumericPart = cweId.replace("CWE-", "").replaceAll("[^0-9]", "");
                if (numericPart.equals(cweNumericPart)) {
                    return cweId;
                }
            }
        }
        for (CweReference cwe : context.getAvailableVulnerabilityTypes()) {
            String nameEn = cwe.getNameEn();
            String nameZh = cwe.getNameZh();
            if ((nameEn != null && normalized.equals(nameEn.toUpperCase()))
                || (nameZh != null && normalized.equals(nameZh))) {
                return cwe.getCweId();
            }
        }
        for (CweReference cwe : context.getAvailableVulnerabilityTypes()) {
            String nameEn = cwe.getNameEn();
            String nameZh = cwe.getNameZh();
            if ((nameEn != null && (nameEn.toUpperCase().contains(normalized) || normalized.contains(nameEn.toUpperCase())))
                || (nameZh != null && (nameZh.contains(vulnerabilityType) || vulnerabilityType.contains(nameZh)))) {
                return cwe.getCweId();
            }
        }
        return vulnerabilityType;
    }

    private List<String> validateAndFixVulnerabilityTypes(com.alibaba.fastjson2.JSONArray vulnerabilityTypes, ExtractionContext context) {
        List<String> validTypes = new ArrayList<>();
        if (vulnerabilityTypes == null || vulnerabilityTypes.isEmpty() || CollUtil.isEmpty(context.getAvailableVulnerabilityTypes())) {
            return validTypes;
        }
        for (Object typeObj : vulnerabilityTypes) {
            String typeStr = typeObj != null ? typeObj.toString() : null;
            if (StringUtils.isBlank(typeStr)) {
                continue;
            }
            String validatedType = validateAndFixVulnerabilityType(typeStr, context);
            if (validatedType != null && !validatedType.isEmpty()) {
                if (!validatedType.startsWith("CWE-")) {
                    boolean found = false;
                    String numericPart = validatedType.replaceAll("[^0-9]", "");
                    for (CweReference cwe : context.getAvailableVulnerabilityTypes()) {
                        String cweId = cwe.getCweId();
                        String cweNumericPart = cweId.replace("CWE-", "");
                        if (validatedType.equals(cweId) || validatedType.equals(cweNumericPart) 
                            || (StringUtils.isNotBlank(numericPart) && numericPart.equals(cweNumericPart))) {
                            validatedType = cweId;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        log.warn("[validateAndFixVulnerabilityTypes] 无法匹配CWE ID: {}, 已跳过", typeStr);
                        continue;
                    }
                } else {
                    boolean isValidCweId = false;
                    String validatedNormalized = validatedType.toUpperCase();
                    String validatedNumericPart = validatedNormalized.replace("CWE-", "").replaceAll("[^0-9]", "");
                    for (CweReference cwe : context.getAvailableVulnerabilityTypes()) {
                        String cweId = cwe.getCweId();
                        if (validatedType.equals(cweId) || validatedNormalized.equals(cweId.toUpperCase())) {
                            validatedType = cweId;
                            isValidCweId = true;
                            break;
                        }
                        if (StringUtils.isNotBlank(validatedNumericPart)) {
                            String cweNumericPart = cweId.replace("CWE-", "").replaceAll("[^0-9]", "");
                            if (validatedNumericPart.equals(cweNumericPart)) {
                                validatedType = cweId;
                                isValidCweId = true;
                                break;
                            }
                        }
                    }
                    if (!isValidCweId) {
                        log.warn("[validateAndFixVulnerabilityTypes] CWE ID不在可用列表中: {}, 已跳过", validatedType);
                        continue;
                    }
                }
                if (!validTypes.contains(validatedType)) {
                    validTypes.add(validatedType);
                }
            }
        }
        return validTypes;
    }

    private String validateAndFixLanguage(String language, ExtractionContext context) {
        if (StringUtils.isBlank(language)) {
            return null;
        }
        if (context.getAvailableLanguages().contains(language)) {
            return language;
        }
        String matched = fuzzyMatch(language, context.getAvailableLanguages());
        return matched != null ? matched : language;
    }

    private String validateAndFixSeverity(String severity, ExtractionContext context) {
        if (StringUtils.isBlank(severity)) {
            return null;
        }
        if (context.getAvailableSeverities().contains(severity)) {
            return severity;
        }
        String matched = fuzzyMatch(severity, context.getAvailableSeverities());
        return matched != null ? matched : severity;
    }

    private String fuzzyMatch(String input, List<String> candidates) {
        if (StringUtils.isBlank(input) || CollUtil.isEmpty(candidates)) {
            return null;
        }
        double maxSimilarity = 0.8;
        String bestMatch = null;
        for (String candidate : candidates) {
            double similarity = calculateSimilarity(input.toLowerCase(), candidate.toLowerCase());
            if (similarity >= maxSimilarity && similarity > (bestMatch != null ? calculateSimilarity(input.toLowerCase(), bestMatch.toLowerCase()) : 0)) {
                maxSimilarity = similarity;
                bestMatch = candidate;
            }
        }
        return bestMatch;
    }

    private double calculateSimilarity(String s1, String s2) {
        if (s1.equals(s2)) {
            return 1.0;
        }
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) {
            return 1.0;
        }
        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLen;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private void validateModelConfig(ChatModelVo chatModelVo, String operationName) {
        if (chatModelVo == null) {
            throw new ServiceException(operationName + "失败：模型配置不存在");
        }
        if (StringUtils.isBlank(chatModelVo.getApiHost())) {
            throw new ServiceException(String.format(
                "%s失败：模型 %s 的API地址(api_host)未配置。请在chat_model表中为该模型设置正确的api_host。",
                operationName, chatModelVo.getModelName()
            ));
        }
        if (StringUtils.isBlank(chatModelVo.getApiKey())) {
            throw new ServiceException(String.format(
                "%s失败：模型 %s 的API密钥(api_key)未配置。请在chat_model表中为该模型设置正确的api_key。",
                operationName, chatModelVo.getModelName()
            ));
        }
        String apiKey = chatModelVo.getApiKey().trim();
        if (isPlaceholderApiKey(apiKey)) {
            throw new ServiceException(String.format(
                "%s失败：模型 %s 的API密钥(api_key)为占位符，需要配置真实的API密钥。\n" +
                "请在chat_model表中将模型 %s 的api_key字段更新为有效的API密钥。\n" +
                "当前值: %s",
                operationName, chatModelVo.getModelName(), chatModelVo.getModelName(), apiKey
            ));
        }
        if (StringUtils.isBlank(chatModelVo.getModelName())) {
            throw new ServiceException(String.format(
                "%s失败：模型配置的model_name为空。请在chat_model表中检查模型配置。",
                operationName
            ));
        }
    }

    private boolean isModelConfigValid(ChatModelVo modelVo) {
        if (modelVo == null) {
            return false;
        }
        if (StringUtils.isBlank(modelVo.getApiHost()) || StringUtils.isBlank(modelVo.getApiKey())) {
            return false;
        }
        String apiKey = modelVo.getApiKey().trim().toLowerCase();
        return !apiKey.equals("sk-xx")
            && !apiKey.equals("sk-xxx")
            && !apiKey.equals("your-api-key")
            && !apiKey.equals("api-key")
            && !apiKey.startsWith("sk-placeholder")
            && !(apiKey.length() < 10 && apiKey.startsWith("sk-"));
    }

    private boolean isPlaceholderApiKey(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            return true;
        }
        String lowerKey = apiKey.toLowerCase();
        return lowerKey.equals("sk-xx") 
            || lowerKey.equals("sk-xxx")
            || lowerKey.equals("your-api-key")
            || lowerKey.equals("api-key")
            || lowerKey.startsWith("sk-placeholder")
            || lowerKey.matches("sk-[a-z]{1,3}")
            || (lowerKey.length() < 10 && lowerKey.startsWith("sk-"));
    }
}
