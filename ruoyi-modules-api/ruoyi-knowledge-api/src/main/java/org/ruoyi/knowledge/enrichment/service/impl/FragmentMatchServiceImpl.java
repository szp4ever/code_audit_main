package org.ruoyi.knowledge.enrichment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeItemBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeInfoVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemVo;
import org.ruoyi.knowledge.curation.service.IKnowledgeInfoService;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemFragmentService;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemService;
import org.ruoyi.knowledge.enrichment.service.IFragmentMatchService;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeFragment;
import org.ruoyi.knowledge.ingestion.domain.bo.QueryVectorBo;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeFragmentMapper;
import org.ruoyi.knowledge.ingestion.service.VectorStoreService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 片段智能匹配服务实现
 * <p>
 * 当前实现仅使用向量相似度搜索，LLM 二次确认为 TODO。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FragmentMatchServiceImpl implements IFragmentMatchService {

    private final IKnowledgeItemService knowledgeItemService;
    private final VectorStoreService vectorStoreService;
    private final IKnowledgeItemFragmentService knowledgeItemFragmentService;
    private final KnowledgeFragmentMapper knowledgeFragmentMapper;
    private final IKnowledgeInfoService knowledgeInfoService;

    @Override
    public List<MatchResult> matchFragments(String itemUuid, String kid, int limit) {
        // 1. 加载条目
        KnowledgeItemVo item = knowledgeItemService.queryByItemUuid(itemUuid);
        if (item == null) {
            log.warn("matchFragments: 条目不存在, itemUuid={}", itemUuid);
            return Collections.emptyList();
        }

        String title = StrUtil.blankToDefault(item.getTitle(), "");
        String summary = StrUtil.blankToDefault(item.getSummary(), "");
        String queryText = (title + " " + summary).trim();
        if (StrUtil.isBlank(queryText)) {
            log.warn("matchFragments: 条目标题和摘要均为空, itemUuid={}", itemUuid);
            return Collections.emptyList();
        }

        // 2. 获取知识库的 embedding 模型名称
        KnowledgeInfoVo knowledgeInfo = knowledgeInfoService.queryByKid(kid);
        if (knowledgeInfo == null) {
            log.warn("matchFragments: 知识库不存在, kid={}", kid);
            return Collections.emptyList();
        }

        // 3. 构建向量查询
        QueryVectorBo queryBo = new QueryVectorBo();
        queryBo.setKid(kid);
        queryBo.setQuery(queryText);
        // 请求更多结果以便过滤后仍有足够数量
        queryBo.setMaxResults(limit * 3);
        queryBo.setEmbeddingModelName(knowledgeInfo.getEmbeddingModelName());

        List<String> similarTexts = vectorStoreService.getQueryVector(queryBo);
        if (CollUtil.isEmpty(similarTexts)) {
            log.info("matchFragments: 向量搜索无结果, itemUuid={}, kid={}", itemUuid, kid);
            return Collections.emptyList();
        }

        // 4. 获取该条目已关联的片段 ID 集合
        List<KnowledgeItemFragment> existingAssociations = knowledgeItemFragmentService.listByItemUuid(itemUuid);
        Set<Long> existingFragmentIds = existingAssociations.stream()
            .map(KnowledgeItemFragment::getFragmentId)
            .collect(Collectors.toSet());

        // 5. 根据向量搜索返回的文本内容，反查片段记录
        List<MatchResult> results = new ArrayList<>();
        for (String similarText : similarTexts) {
            if (results.size() >= limit) {
                break;
            }

            // 通过内容匹配查找片段
            List<KnowledgeFragment> fragments = knowledgeFragmentMapper.selectList(
                Wrappers.<KnowledgeFragment>lambdaQuery()
                    .eq(KnowledgeFragment::getKid, kid)
                    .like(KnowledgeFragment::getContent, truncateForLike(similarText))
                    .last("LIMIT 1")
            );

            if (CollUtil.isEmpty(fragments)) {
                continue;
            }

            KnowledgeFragment fragment = fragments.get(0);

            // 过滤已关联的片段
            if (existingFragmentIds.contains(fragment.getId())) {
                continue;
            }

            // 计算简单的文本相似度作为向量相似度的近似值
            double similarity = calculateTextSimilarity(queryText, fragment.getContent());

            String preview = StrUtil.maxLength(fragment.getContent(), 200);

            results.add(new MatchResult(
                itemUuid,
                title,
                fragment.getId(),
                preview,
                similarity,
                0.0,  // TODO: LLM 确认步骤，当前置为 0
                "向量相似度匹配"
            ));
        }

        // 按相似度降序排序
        results.sort(Comparator.comparingDouble(MatchResult::vectorSimilarity).reversed());

        log.info("matchFragments: 为条目 [{}] 找到 {} 个候选片段, itemUuid={}, kid={}",
            title, results.size(), itemUuid, kid);

        return results;
    }

    @Override
    public List<MatchResult> batchMatch(String kid, int limit) {
        log.info("batchMatch: 开始批量匹配, kid={}, limit={}", kid, limit);

        // 查询知识库中所有条目
        KnowledgeItemBo queryBo = new KnowledgeItemBo();
        queryBo.setKid(kid);
        List<KnowledgeItemVo> items = knowledgeItemService.queryList(queryBo);

        if (CollUtil.isEmpty(items)) {
            log.info("batchMatch: 知识库中无条目, kid={}", kid);
            return Collections.emptyList();
        }

        List<MatchResult> allResults = new ArrayList<>();

        for (KnowledgeItemVo item : items) {
            // 仅处理片段关联较少的条目
            long associationCount = knowledgeItemFragmentService.countByItemUuid(item.getItemUuid());
            if (associationCount >= 5) {
                // 已有足够关联，跳过
                continue;
            }

            try {
                List<MatchResult> itemResults = matchFragments(item.getItemUuid(), kid, limit);
                allResults.addAll(itemResults);
            } catch (Exception e) {
                log.error("batchMatch: 条目匹配失败, itemUuid={}, error={}",
                    item.getItemUuid(), e.getMessage(), e);
            }
        }

        log.info("batchMatch: 批量匹配完成, kid={}, 总匹配数={}", kid, allResults.size());
        return allResults;
    }

    // ======================== 私有方法 ========================

    /**
     * 截取文本用于 LIKE 查询（取前 80 个字符，避免过长导致查询低效）
     */
    private String truncateForLike(String text) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        String cleaned = text.replace("%", "").replace("_", "");
        return cleaned.length() > 80 ? cleaned.substring(0, 80) : cleaned;
    }

    /**
     * 简单文本相似度计算（基于 Jaccard 系数）
     * 用于在向量搜索结果中提供一个近似的相似度分数
     */
    private double calculateTextSimilarity(String a, String b) {
        if (StrUtil.isBlank(a) || StrUtil.isBlank(b)) {
            return 0.0;
        }
        Set<String> setA = new HashSet<>(Arrays.asList(a.split("\\s+")));
        Set<String> setB = new HashSet<>(Arrays.asList(b.split("\\s+")));

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        if (union.isEmpty()) {
            return 0.0;
        }
        return (double) intersection.size() / union.size();
    }
}
