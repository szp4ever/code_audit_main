package org.ruoyi.chat.controller.knowledge.v2;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemFragmentService;
import org.ruoyi.knowledge.ingestion.domain.bo.FragmentBatchQueryBo;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeFragmentBo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeFragmentPageVo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeFragmentVo;
import org.ruoyi.knowledge.ingestion.service.IKnowledgeFragmentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 片段管理
 * 整合知识库片段查询、批量查询、条目-片段关联管理
 *
 * @author system
 * @date 2026-03-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/fragment")
@Tag(name = "片段管理")
@Slf4j
public class FragmentController extends BaseController {

    private final IKnowledgeFragmentService fragmentService;

    private final IKnowledgeItemFragmentService knowledgeItemFragmentService;

    // ==================== 片段查询 ====================

    /**
     * 查询知识库片段列表
     */
    @Operation(summary = "查询片段列表")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeFragmentVo> getFragmentList(KnowledgeFragmentBo bo, PageQuery pageQuery) {
        return fragmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询知识条目下的片段列表（带分面统计）
     */
    @Operation(summary = "按条目查询片段列表（带分面统计）")
    @PostMapping("/list-by-item")
    public R<KnowledgeFragmentPageVo> getFragmentListByItem(@RequestBody KnowledgeFragmentBo bo, PageQuery pageQuery) {
        KnowledgeFragmentPageVo result = fragmentService.queryPageListWithFacetStats(bo, pageQuery);
        return R.ok(result);
    }

    /**
     * 批量查询片段内容
     * 基于LLM与状态改革设计文档 v1.0
     * 用于审阅页面，需要包含未完成处理的片段
     */
    @Operation(summary = "批量查询片段")
    @PostMapping("/batch")
    public R<List<KnowledgeFragmentVo>> getFragmentBatch(
            @RequestBody List<FragmentBatchQueryBo> queries) {
        List<KnowledgeFragmentVo> fragments = new ArrayList<>();
        for (FragmentBatchQueryBo query : queries) {
            KnowledgeFragmentBo bo = new KnowledgeFragmentBo();
            bo.setDocId(query.getDocId());
            if (query.getIdx() != null) {
                bo.setIdx(query.getIdx().longValue());
            }
            //审阅页面需要包含未完成处理的片段
            bo.setIncludeIncomplete(true);
            List<KnowledgeFragmentVo> list = fragmentService.queryList(bo);
            if (CollUtil.isNotEmpty(list)) {
                fragments.addAll(list);
            }
        }
        return R.ok(fragments);
    }

    // ==================== 片段-条目关联管理 ====================

    /**
     * 关联片段与条目
     */
    @Operation(summary = "关联片段与条目")
    @PostMapping("/{fragmentId}/associate/{itemUuid}")
    public R<KnowledgeItemFragment> associate(
            @NotNull(message = "片段ID不能为空") @PathVariable Long fragmentId,
            @NotNull(message = "条目UUID不能为空") @PathVariable String itemUuid) {
        KnowledgeItemFragment result = knowledgeItemFragmentService.associate(itemUuid, fragmentId);
        return R.ok(result);
    }

    /**
     * 取消片段与条目的关联
     */
    @Operation(summary = "取消片段与条目的关联")
    @DeleteMapping("/{fragmentId}/associate/{itemUuid}")
    public R<Void> disassociate(
            @NotNull(message = "片段ID不能为空") @PathVariable Long fragmentId,
            @NotNull(message = "条目UUID不能为空") @PathVariable String itemUuid) {
        knowledgeItemFragmentService.disassociate(itemUuid, fragmentId);
        return R.ok();
    }

    /**
     * 查询片段关联的所有条目
     */
    @Operation(summary = "查询片段关联的条目列表")
    @GetMapping("/{fragmentId}/items")
    public R<List<KnowledgeItemFragment>> listItemsByFragment(
            @NotNull(message = "片段ID不能为空") @PathVariable Long fragmentId) {
        List<KnowledgeItemFragment> items = knowledgeItemFragmentService.listByFragmentId(fragmentId);
        return R.ok(items);
    }
}
