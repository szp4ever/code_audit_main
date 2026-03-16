package org.ruoyi.knowledge.curation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.curation.domain.vo.ItemFragmentDetailVo;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemFragmentMapper;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemFragmentService;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeAttach;
import org.ruoyi.knowledge.ingestion.domain.KnowledgeFragment;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeAttachMapper;
import org.ruoyi.knowledge.ingestion.mapper.KnowledgeFragmentMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识条目-片段关联 Service 实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeItemFragmentServiceImpl implements IKnowledgeItemFragmentService {

    private final KnowledgeItemFragmentMapper itemFragmentMapper;
    private final KnowledgeFragmentMapper fragmentMapper;
    private final KnowledgeAttachMapper attachMapper;

    /**
     * 关联条目与片段（幂等设计）
     * <p>
     * 最佳实践：
     * 1. 使用数据库唯一索引保证并发安全（item_uuid + fragment_id）
     * 2. 捕获 DuplicateKeyException 实现幂等 - 重复请求返回已存在的关联记录
     * 3. 先查后插改为直接插，利用数据库原子性避免竞态条件
     *
     * @param itemUuid   条目UUID
     * @param fragmentId 片段ID
     * @return 关联记录（新创建或已存在）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeItemFragment associate(String itemUuid, Long fragmentId) {
        // 先尝试查询是否已存在（用于返回已有记录）
        KnowledgeItemFragment existing = itemFragmentMapper.selectOne(
            new LambdaQueryWrapper<KnowledgeItemFragment>()
                .eq(KnowledgeItemFragment::getItemUuid, itemUuid)
                .eq(KnowledgeItemFragment::getFragmentId, fragmentId)
                .last("LIMIT 1")
        );
        if (existing != null) {
            log.info("条目 {} 与片段 {} 已存在关联，幂等返回", itemUuid, fragmentId);
            return existing;
        }

        // 尝试插入，捕获唯一键冲突异常
        KnowledgeItemFragment entity = new KnowledgeItemFragment();
        entity.setItemUuid(itemUuid);
        entity.setFragmentId(fragmentId);
        entity.setCreatedBy("manual");
        entity.setCreateTime(new Date());

        try {
            itemFragmentMapper.insert(entity);
            log.info("条目 {} 关联片段 {}（人工）", itemUuid, fragmentId);
            return entity;
        } catch (DuplicateKeyException e) {
            // 并发场景下，其他事务已插入，查询并返回
            log.info("并发插入检测到重复关联，条目 {} 与片段 {}，幂等处理", itemUuid, fragmentId);
            return itemFragmentMapper.selectOne(
                new LambdaQueryWrapper<KnowledgeItemFragment>()
                    .eq(KnowledgeItemFragment::getItemUuid, itemUuid)
                    .eq(KnowledgeItemFragment::getFragmentId, fragmentId)
                    .last("LIMIT 1")
            );
        }
    }

    /**
     * 批量关联条目与片段（幂等设计）
     * <p>
     * 最佳实践：
     * 1. 每条记录独立 try-catch，单条失败不影响整体
     * 2. 使用 DuplicateKeyException 捕获实现幂等插入
     * 3. 批量操作结束后返回实际成功创建的记录
     *
     * @param itemUuid       条目UUID
     * @param fragmentIds    片段ID列表
     * @param relevanceScore 相关度评分（AI匹配时使用）
     * @return 成功创建的关联记录列表（已存在的不会重复添加）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<KnowledgeItemFragment> batchAssociate(String itemUuid, List<Long> fragmentIds, Double relevanceScore) {
        List<KnowledgeItemFragment> results = new ArrayList<>();
        Date now = new Date();
        int skippedCount = 0;
        int errorCount = 0;

        for (Long fragmentId : fragmentIds) {
            // 先查询是否已存在
            KnowledgeItemFragment existing = itemFragmentMapper.selectOne(
                new LambdaQueryWrapper<KnowledgeItemFragment>()
                    .eq(KnowledgeItemFragment::getItemUuid, itemUuid)
                    .eq(KnowledgeItemFragment::getFragmentId, fragmentId)
                    .last("LIMIT 1")
            );
            if (existing != null) {
                skippedCount++;
                log.debug("跳过已存在的关联: itemUuid={}, fragmentId={}", itemUuid, fragmentId);
                continue;
            }

            // 尝试插入
            KnowledgeItemFragment entity = new KnowledgeItemFragment();
            entity.setItemUuid(itemUuid);
            entity.setFragmentId(fragmentId);
            entity.setRelevanceScore(relevanceScore);
            entity.setCreatedBy("ai");
            entity.setCreateTime(now);

            try {
                itemFragmentMapper.insert(entity);
                results.add(entity);
            } catch (DuplicateKeyException e) {
                // 并发场景下其他事务已插入，幂等处理
                skippedCount++;
                log.debug("并发检测到重复关联，幂等跳过: itemUuid={}, fragmentId={}", itemUuid, fragmentId);
            } catch (Exception e) {
                // 其他异常记录但不中断批量操作
                errorCount++;
                log.warn("关联片段 {} 到条目 {} 失败: {}", fragmentId, itemUuid, e.getMessage());
            }
        }

        log.info("条目 {} 批量关联完成：成功创建 {} 个，跳过已存在 {} 个，失败 {} 个（AI，相关度={}）",
            itemUuid, results.size(), skippedCount, errorCount, relevanceScore);
        return results;
    }

    /**
     * 取消条目与片段的关联（幂等设计）
     * <p>
     * 最佳实践：
     * 1. 删除操作天然幂等 - 无论记录是否存在，最终状态都是"不存在"
     * 2. 不应因记录不存在而抛异常，符合幂等语义
     * 3. 通过返回值记录实际删除数量，用于日志审计
     *
     * @param itemUuid   条目UUID
     * @param fragmentId 片段ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disassociate(String itemUuid, Long fragmentId) {
        LambdaQueryWrapper<KnowledgeItemFragment> wrapper = new LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getItemUuid, itemUuid)
            .eq(KnowledgeItemFragment::getFragmentId, fragmentId);
        int deleted = itemFragmentMapper.delete(wrapper);

        // 幂等设计：删除0条也是成功的（记录本来就不存在）
        if (deleted > 0) {
            log.info("条目 {} 取消关联片段 {}，删除 {} 条记录", itemUuid, fragmentId, deleted);
        } else {
            log.debug("条目 {} 与片段 {} 关联记录不存在，幂等处理", itemUuid, fragmentId);
        }
    }

    /**
     * 取消条目的所有片段关联（幂等设计）
     * <p>
     * 最佳实践：
     * 1. 批量删除也是幂等操作
     * 2. 删除0条表示本来就没有关联，也是成功状态
     *
     * @param itemUuid 条目UUID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disassociateAll(String itemUuid) {
        int deleted = itemFragmentMapper.deleteByItemUuid(itemUuid);
        log.info("条目 {} 取消所有片段关联，共删除 {} 条", itemUuid, deleted);
    }

    @Override
    public List<KnowledgeItemFragment> listByItemUuid(String itemUuid) {
        return itemFragmentMapper.selectByItemUuid(itemUuid);
    }

    @Override
    public List<ItemFragmentDetailVo> listByItemUuidWithDetails(String itemUuid) {
        List<KnowledgeItemFragment> associations = itemFragmentMapper.selectByItemUuid(itemUuid);
        if (associations.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> fragmentIds = associations.stream()
            .map(KnowledgeItemFragment::getFragmentId)
            .distinct()
            .collect(Collectors.toList());

        List<KnowledgeFragment> fragments = fragmentMapper.selectBatchIds(fragmentIds);
        if (fragments.isEmpty()) {
            return new ArrayList<>();
        }

        // 批量查询 docId -> docName
        List<String> docIds = fragments.stream()
            .map(KnowledgeFragment::getDocId)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .collect(Collectors.toList());

        Map<String, String> docIdToName = docIds.isEmpty() ? Map.of() : attachMapper.selectList(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .in(KnowledgeAttach::getDocId, docIds)
                .select(KnowledgeAttach::getDocId, KnowledgeAttach::getDocName)
        ).stream().collect(Collectors.toMap(KnowledgeAttach::getDocId, a -> a.getDocName() != null ? a.getDocName() : "未知文档", (a, b) -> a));

        Map<Long, KnowledgeFragment> fragmentMap = fragments.stream().collect(Collectors.toMap(KnowledgeFragment::getId, f -> f, (a, b) -> a));

        List<ItemFragmentDetailVo> result = new ArrayList<>();
        for (KnowledgeItemFragment assoc : associations) {
            KnowledgeFragment frag = fragmentMap.get(assoc.getFragmentId());
            if (frag == null) continue;

            ItemFragmentDetailVo vo = new ItemFragmentDetailVo();
            vo.setId(frag.getId());
            vo.setFragmentId(frag.getId());
            vo.setContent(frag.getContent());
            String docName = StringUtils.isNotBlank(frag.getDocId()) ? docIdToName.getOrDefault(frag.getDocId(), "未知来源") : "未知来源";
            vo.setDocumentName(docName);
            vo.setSourceName(docName);
            vo.setRelevanceScore(assoc.getRelevanceScore());
            vo.setAssociationType(assoc.getCreatedBy() != null ? assoc.getCreatedBy() : "manual");
            vo.setCreateTime(assoc.getCreateTime());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<KnowledgeItemFragment> listByFragmentId(Long fragmentId) {
        return itemFragmentMapper.selectByFragmentId(fragmentId);
    }

    @Override
    public boolean exists(String itemUuid, Long fragmentId) {
        return itemFragmentMapper.existsAssociation(itemUuid, fragmentId);
    }

    @Override
    public long countByItemUuid(String itemUuid) {
        return itemFragmentMapper.selectCount(new LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getItemUuid, itemUuid));
    }

    @Override
    public long countByFragmentId(Long fragmentId) {
        return itemFragmentMapper.selectCount(new LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getFragmentId, fragmentId));
    }
}
