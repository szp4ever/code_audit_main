package org.ruoyi.chat.controller.knowledge.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.knowledge.cwe.domain.CweReference;
import org.ruoyi.knowledge.enrichment.domain.bo.ExtractionContext;
import org.ruoyi.knowledge.enrichment.domain.bo.ExtractedItemData;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweReferenceVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeTagVo;
import org.ruoyi.service.IChatModelService;
import org.ruoyi.knowledge.cwe.service.ICweReferenceService;
import org.ruoyi.knowledge.enrichment.service.IKnowledgeItemExtractionService;
import org.ruoyi.knowledge.curation.service.IKnowledgeTagService;
import org.ruoyi.system.service.ISysDictTypeService;
import org.ruoyi.system.domain.vo.SysDictDataVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AI 增强控制器
 * <p>
 * 提供 LLM 提取、条目建议、片段匹配、重复检测等 AI 增强操作。
 *
 * @author ruoyi
 * @date 2026-03-05
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/ai")
@Tag(name = "AI 增强")
public class EnrichmentController {

    private final IKnowledgeItemExtractionService extractionService;
    private final IChatModelService chatModelService;
    private final IKnowledgeTagService knowledgeTagService;
    private final ICweReferenceService cweReferenceService;
    private final ISysDictTypeService dictTypeService;

    // ==================== Request DTO ====================

    public static class LlmExtractReq {
        @NotBlank
        private String content;
        private String kid;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getKid() {
            return kid;
        }

        public void setKid(String kid) {
            this.kid = kid;
        }
    }

    // ==================== Endpoints ====================

    /**
     * LLM 提取测试（从 LlmTestController 迁移）
     */
    @Operation(summary = "LLM 提取测试")
    @PostMapping("/extract")
    public R<ExtractedItemData> extract(@RequestBody LlmExtractReq req) {
        ExtractionContext context = new ExtractionContext();
        context.setKid(req.getKid());
        ChatModelVo chatModelVo = chatModelService.selectChatModelForKnowledgeExtraction();
        if (chatModelVo == null) {
            return R.fail("未找到可用的chat模型，请先在chat_model表中配置category='chat'且model_show='0'的有效模型");
        }
        System.out.println("[测试LLM提取] 使用模型: " + chatModelVo.getModelName() + ", apiHost: " + chatModelVo.getApiHost());
        context.setModelName(chatModelVo.getModelName());
        List<KnowledgeTagVo> allTags = knowledgeTagService.queryList(new org.ruoyi.knowledge.curation.domain.bo.KnowledgeTagBo());
        context.setAvailableTags(allTags.stream()
            .map(KnowledgeTagVo::getTagName)
            .collect(Collectors.toList()));
        List<CweReferenceVo> allCwes = cweReferenceService.queryList(new org.ruoyi.knowledge.cwe.domain.bo.CweReferenceBo());
        context.setAvailableVulnerabilityTypes(allCwes.stream()
            .map(vo -> {
                CweReference cwe = new CweReference();
                cwe.setCweId(vo.getCweId());
                cwe.setNameEn(vo.getNameEn());
                cwe.setNameZh(vo.getNameZh());
                cwe.setStatus(vo.getStatus() != null ? vo.getStatus() : "Stable");
                return cwe;
            })
            .collect(Collectors.toList()));
        // 从字典服务获取语言和风险等级选项（使用dictValue，与前端一致）
        List<SysDictDataVo> languageDicts = dictTypeService.selectDictDataByType("knowledge_language");
        List<String> languages = languageDicts.stream()
            .map(SysDictDataVo::getDictValue)
            .collect(Collectors.toList());
        context.setAvailableLanguages(languages);

        List<SysDictDataVo> severityDicts = dictTypeService.selectDictDataByType("knowledge_severity");
        List<String> severities = severityDicts.stream()
            .map(SysDictDataVo::getDictValue)
            .collect(Collectors.toList());
        context.setAvailableSeverities(severities);
        ExtractedItemData data = extractionService.extractFromChunk(req.getContent(), context);
        return R.ok(data);
    }

    /**
     * 从片段建议知识条目（占位）
     */
    @Operation(summary = "从片段建议知识条目")
    @PostMapping("/suggest-items")
    public R<String> suggestItems(@RequestBody Object request) {
        return R.ok("功能开发中");
    }

    /**
     * 为条目匹配相关片段（占位）
     */
    @Operation(summary = "为条目匹配相关片段")
    @PostMapping("/match-fragments")
    public R<String> matchFragments(@RequestBody Object request) {
        return R.ok("功能开发中");
    }

    /**
     * 检测重复条目（占位）
     */
    @Operation(summary = "检测重复条目")
    @PostMapping("/detect-duplicates")
    public R<String> detectDuplicates(@RequestBody Object request) {
        return R.ok("功能开发中");
    }
}
