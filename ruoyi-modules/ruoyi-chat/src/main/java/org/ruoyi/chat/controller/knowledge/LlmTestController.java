package org.ruoyi.chat.controller.knowledge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.domain.CweReference;
import org.ruoyi.domain.bo.ExtractionContext;
import org.ruoyi.domain.bo.ExtractedItemData;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.domain.vo.CweReferenceVo;
import org.ruoyi.domain.vo.KnowledgeTagVo;
import org.ruoyi.service.IChatModelService;
import org.ruoyi.service.ICweReferenceService;
import org.ruoyi.service.IKnowledgeItemExtractionService;
import org.ruoyi.service.IKnowledgeTagService;
import org.ruoyi.system.service.ISysDictTypeService;
import org.ruoyi.system.domain.vo.SysDictDataVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/llm")
@Tag(name = "LLM测试", description = "LLM提取测试接口")
public class LlmTestController {

	private final IKnowledgeItemExtractionService extractionService;
	private final IChatModelService chatModelService;
	private final IKnowledgeTagService knowledgeTagService;
	private final ICweReferenceService cweReferenceService;
	private final ISysDictTypeService dictTypeService;

	public static class LlmTestReq {
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

	@Operation(summary = "测试LLM提取")
	@PostMapping("/test-extract")
	public R<ExtractedItemData> testExtract(@RequestBody LlmTestReq req) {
		ExtractionContext context = new ExtractionContext();
		context.setKid(req.getKid());
		ChatModelVo chatModelVo = chatModelService.selectChatModelForKnowledgeExtraction();
		if (chatModelVo == null) {
			return R.fail("未找到可用的chat模型，请先在chat_model表中配置category='chat'且model_show='0'的有效模型");
		}
		System.out.println("[测试LLM提取] 使用模型: " + chatModelVo.getModelName() + ", apiHost: " + chatModelVo.getApiHost());
		context.setModelName(chatModelVo.getModelName());
		List<KnowledgeTagVo> allTags = knowledgeTagService.queryList(new org.ruoyi.domain.bo.KnowledgeTagBo());
		context.setAvailableTags(allTags.stream()
			.map(KnowledgeTagVo::getTagName)
			.collect(Collectors.toList()));
		List<CweReferenceVo> allCwes = cweReferenceService.queryList(new org.ruoyi.domain.bo.CweReferenceBo());
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
}
