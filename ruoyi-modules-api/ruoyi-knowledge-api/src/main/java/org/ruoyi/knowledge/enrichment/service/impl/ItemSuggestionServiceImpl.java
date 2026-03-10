package org.ruoyi.knowledge.enrichment.service.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeItemBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeTagVo;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemFragmentService;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.ruoyi.knowledge.curation.service.IKnowledgeTagService;
import org.ruoyi.knowledge.cwe.domain.CweReference;
import org.ruoyi.knowledge.cwe.domain.vo.CweReferenceVo;
import org.ruoyi.knowledge.cwe.service.ICweReferenceService;
import org.ruoyi.knowledge.enrichment.domain.bo.ExtractedItemData;
import org.ruoyi.knowledge.enrichment.domain.bo.ExtractionContext;
import org.ruoyi.knowledge.enrichment.service.IItemSuggestionService;
import org.ruoyi.knowledge.enrichment.service.IKnowledgeItemExtractionService;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeFragment;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeFragmentMapper;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.service.IChatModelService;
import org.ruoyi.system.domain.vo.SysDictDataVo;
import org.ruoyi.system.service.ISysDictTypeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 条目建议服务实现 — 从未关联的片段中生成知识条目草稿建议。
 * <p>
 * 替代旧流水线的 MATCHING → USER_REVIEW_MATCHING → CREATING_ITEMS → USER_REVIEW_ITEMS 流程，
 * 现已从上传流水线中解耦。
 *
 * @author system
 * @date 2026-03-05
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ItemSuggestionServiceImpl implements IItemSuggestionService {

    private final IKnowledgeItemExtractionService extractionService;
    private final IKnowledgeItemService knowledgeItemService;
    private final IKnowledgeItemFragmentService itemFragmentService;
    private final IKnowledgeTagService knowledgeTagService;
    private final ICweReferenceService cweReferenceService;
    private final IChatModelService chatModelService;
    private final ISysDictTypeService dictTypeService;
    private final KnowledgeFragmentMapper fragmentMapper;

    /** 内存存储建议（后续可持久化到数据库） */
    private final ConcurrentHashMap<String, ItemSuggestion> suggestionStore = new ConcurrentHashMap<>();

    // ======================== generateSuggestions ========================

    @Override
    public List<ItemSuggestion> generateSuggestions(List<Long> fragmentIds, String kid) {
        if (CollUtil.isEmpty(fragmentIds)) {
            throw new ServiceException("片段ID列表不能为空");
        }
        if (StringUtils.isBlank(kid)) {
            throw new ServiceException("知识库ID不能为空");
        }

        // 1. 加载片段
        List<KnowledgeFragment> fragments = fragmentMapper.selectBatchIds(fragmentIds);
        if (CollUtil.isEmpty(fragments)) {
            log.warn("[条目建议] 未找到任何片段, fragmentIds={}", fragmentIds);
            return Collections.emptyList();
        }
        log.info("[条目建议] 加载到 {} 个片段，准备生成建议", fragments.size());

        // 2. 构建 ExtractionContext（与 LlmTestController 逻辑一致）
        ExtractionContext context = buildExtractionContext(kid);

        // 3. 逐片段调用 LLM 提取，包装为建议
        List<ItemSuggestion> suggestions = new ArrayList<>();
        for (KnowledgeFragment fragment : fragments) {
            if (StringUtils.isBlank(fragment.getContent())) {
                log.warn("[条目建议] 片段 {} 内容为空，跳过", fragment.getId());
                continue;
            }
            try {
                ExtractedItemData extracted = extractionService.extractFromChunk(fragment.getContent(), context);
                ItemSuggestion suggestion = toSuggestion(fragment, kid, extracted);
                suggestionStore.put(suggestion.suggestionId(), suggestion);
                suggestions.add(suggestion);
                log.info("[条目建议] 片段 {} 提取成功 → 建议 {}", fragment.getId(), suggestion.suggestionId());
            } catch (Exception e) {
                log.error("[条目建议] 片段 {} 提取失败: {}", fragment.getId(), e.getMessage(), e);
                // 继续处理下一个片段，不中断整个批次
            }
        }

        log.info("[条目建议] 生成完成，共 {} 条建议（{} 个片段中）", suggestions.size(), fragments.size());
        return suggestions;
    }

    // ======================== acceptSuggestion ========================

    @Override
    public String acceptSuggestion(String suggestionId) {
        if (StringUtils.isBlank(suggestionId)) {
            throw new ServiceException("建议ID不能为空");
        }

        ItemSuggestion suggestion = suggestionStore.get(suggestionId);
        if (suggestion == null) {
            throw new ServiceException("建议不存在: " + suggestionId);
        }
        if (!"pending".equals(suggestion.status())) {
            throw new ServiceException("建议状态不是待审核，当前状态: " + suggestion.status());
        }

        // 1. 构建 KnowledgeItemBo
        KnowledgeItemBo bo = new KnowledgeItemBo();
        String itemUuid = UUID.randomUUID().toString().replace("-", "");
        bo.setItemUuid(itemUuid);
        bo.setKid(suggestion.kid());
        bo.setTitle(suggestion.suggestedTitle());
        bo.setSummary(suggestion.suggestedSummary());
        bo.setVulnerabilityType(suggestion.suggestedVulnerabilityType());
        bo.setSeverity(suggestion.suggestedSeverity());
        bo.setProblemDescription(suggestion.suggestedProblemDescription());
        bo.setFixSolution(suggestion.suggestedFixSuggestion());
        bo.setExampleCode(suggestion.suggestedExampleCode());
        bo.setTags(suggestion.suggestedTags());
        bo.setStatus("draft");
        bo.setSourceType("enrichment");

        // 2. 创建正式条目
        Boolean created = knowledgeItemService.insertByBo(bo);
        if (!Boolean.TRUE.equals(created)) {
            throw new ServiceException("创建知识条目失败");
        }

        // 获取实际生成的 itemUuid（insertByBo 可能会自动生成）
        String actualItemUuid = bo.getItemUuid();
        if (StringUtils.isBlank(actualItemUuid)) {
            // 回退：通过 id 查询
            KnowledgeItemVo itemVo = knowledgeItemService.queryById(bo.getId());
            if (itemVo != null) {
                actualItemUuid = itemVo.getItemUuid();
            }
        }
        if (StringUtils.isBlank(actualItemUuid)) {
            throw new ServiceException("创建条目成功但无法获取 itemUuid");
        }

        // 3. 关联片段
        if (suggestion.fragmentId() != null) {
            try {
                itemFragmentService.associate(actualItemUuid, suggestion.fragmentId());
                log.info("[条目建议] 已关联片段 {} → 条目 {}", suggestion.fragmentId(), actualItemUuid);
            } catch (Exception e) {
                log.error("[条目建议] 关联片段失败（条目已创建）: {}", e.getMessage(), e);
            }
        }

        // 4. 更新建议状态
        ItemSuggestion accepted = new ItemSuggestion(
            suggestion.suggestionId(),
            suggestion.fragmentId(),
            suggestion.fragmentContent(),
            suggestion.kid(),
            suggestion.suggestedTitle(),
            suggestion.suggestedSummary(),
            suggestion.suggestedVulnerabilityType(),
            suggestion.suggestedSeverity(),
            suggestion.suggestedProblemDescription(),
            suggestion.suggestedFixSuggestion(),
            suggestion.suggestedExampleCode(),
            suggestion.suggestedTags(),
            suggestion.confidence(),
            "accepted"
        );
        suggestionStore.put(suggestionId, accepted);

        log.info("[条目建议] 建议 {} 已接受，创建条目 {}", suggestionId, actualItemUuid);
        return actualItemUuid;
    }

    // ======================== rejectSuggestion ========================

    @Override
    public void rejectSuggestion(String suggestionId, String reason) {
        if (StringUtils.isBlank(suggestionId)) {
            throw new ServiceException("建议ID不能为空");
        }

        ItemSuggestion suggestion = suggestionStore.get(suggestionId);
        if (suggestion == null) {
            throw new ServiceException("建议不存在: " + suggestionId);
        }
        if (!"pending".equals(suggestion.status())) {
            throw new ServiceException("建议状态不是待审核，当前状态: " + suggestion.status());
        }

        ItemSuggestion rejected = new ItemSuggestion(
            suggestion.suggestionId(),
            suggestion.fragmentId(),
            suggestion.fragmentContent(),
            suggestion.kid(),
            suggestion.suggestedTitle(),
            suggestion.suggestedSummary(),
            suggestion.suggestedVulnerabilityType(),
            suggestion.suggestedSeverity(),
            suggestion.suggestedProblemDescription(),
            suggestion.suggestedFixSuggestion(),
            suggestion.suggestedExampleCode(),
            suggestion.suggestedTags(),
            suggestion.confidence(),
            "rejected"
        );
        suggestionStore.put(suggestionId, rejected);

        log.info("[条目建议] 建议 {} 已拒绝, 原因: {}", suggestionId,
            StringUtils.isNotBlank(reason) ? reason : "未提供");
    }

    // ======================== listPendingSuggestions ========================

    @Override
    public List<ItemSuggestion> listPendingSuggestions(String kid) {
        if (StringUtils.isBlank(kid)) {
            throw new ServiceException("知识库ID不能为空");
        }

        return suggestionStore.values().stream()
            .filter(s -> kid.equals(s.kid()) && "pending".equals(s.status()))
            .sorted(Comparator.comparing(ItemSuggestion::suggestionId))
            .collect(Collectors.toList());
    }

    // ======================== 私有辅助方法 ========================

    /**
     * 构建 ExtractionContext — 与 LlmTestController 逻辑保持一致
     */
    private ExtractionContext buildExtractionContext(String kid) {
        ExtractionContext context = new ExtractionContext();
        context.setKid(kid);

        // 选择模型
        ChatModelVo chatModelVo = chatModelService.selectChatModelForKnowledgeExtraction();
        if (chatModelVo == null) {
            throw new ServiceException("未找到可用的chat模型，请先在chat_model表中配置category='chat'且model_show='0'的有效模型");
        }
        context.setModelName(chatModelVo.getModelName());
        log.info("[条目建议] 使用模型: {}", chatModelVo.getModelName());

        // 加载标签
        List<KnowledgeTagVo> allTags = knowledgeTagService.queryList(
            new org.ruoyi.knowledge.curation.domain.bo.KnowledgeTagBo());
        context.setAvailableTags(allTags.stream()
            .map(KnowledgeTagVo::getTagName)
            .collect(Collectors.toList()));

        // 加载 CWE 引用
        List<CweReferenceVo> allCwes = cweReferenceService.queryList(
            new org.ruoyi.knowledge.cwe.domain.bo.CweReferenceBo());
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

        // 加载字典值：语言
        List<SysDictDataVo> languageDicts = dictTypeService.selectDictDataByType("knowledge_language");
        context.setAvailableLanguages(languageDicts.stream()
            .map(SysDictDataVo::getDictValue)
            .collect(Collectors.toList()));

        // 加载字典值：风险等级
        List<SysDictDataVo> severityDicts = dictTypeService.selectDictDataByType("knowledge_severity");
        context.setAvailableSeverities(severityDicts.stream()
            .map(SysDictDataVo::getDictValue)
            .collect(Collectors.toList()));

        return context;
    }

    /**
     * 将 LLM 提取结果转换为 ItemSuggestion 记录
     */
    private ItemSuggestion toSuggestion(KnowledgeFragment fragment, String kid, ExtractedItemData extracted) {
        // 计算综合置信度
        double confidence = 0.0;
        if (extracted.getConfidence() != null && !extracted.getConfidence().isEmpty()) {
            confidence = extracted.getConfidence().values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        }

        return new ItemSuggestion(
            UUID.randomUUID().toString().replace("-", ""),
            fragment.getId(),
            truncateContent(fragment.getContent(), 500),
            kid,
            extracted.getTitle(),
            extracted.getSummary(),
            extracted.getVulnerabilityType(),
            extracted.getSeverity(),
            extracted.getProblemDescription(),
            extracted.getFixSolution(),
            extracted.getExampleCode(),
            extracted.getTags() != null ? extracted.getTags() : Collections.emptyList(),
            confidence,
            "pending"
        );
    }

    /**
     * 截断内容用于预览
     */
    private String truncateContent(String content, int maxLength) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
