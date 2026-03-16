package org.ruoyi.chat.controller.knowledge.v2;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;
import org.ruoyi.knowledge.curation.domain.vo.ItemFragmentDetailVo;
import org.ruoyi.knowledge.curation.domain.bo.*;
import org.ruoyi.knowledge.curation.domain.vo.*;
import org.ruoyi.knowledge.curation.service.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 知识条目管理（v2 聚合控制器）
 * <p>
 * 将 KnowledgeItemController、KnowledgeItemHistoryController、KnowledgeTagController、
 * KnowledgeFavoriteController、KnowledgeFeedbackController、DetectionResultController
 * 以及 N:M 片段关联操作统一到一个控制器中。
 *
 * @author ruoyi
 * @date 2026-03-05
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/item")
@Tag(name = "知识条目管理")
public class ItemController extends BaseController {

    private final IKnowledgeItemService knowledgeItemService;
    private final IKnowledgeItemHistoryService knowledgeItemHistoryService;
    private final IKnowledgeTagService knowledgeTagService;
    private final IKnowledgeFavoriteService knowledgeFavoriteService;
    private final IKnowledgeFeedbackService knowledgeFeedbackService;
    private final IDetectionResultService detectionResultService;
    private final IKnowledgeItemFragmentService knowledgeItemFragmentService;

    // ==================== Item CRUD (from KnowledgeItemController) ====================

    /**
     * 查询知识条目列表（使用POST以支持复杂筛选条件，特别是空数组）
     * pageNum/pageSize通过URL参数传递，筛选条件通过body传递
     */
    @Operation(summary = "查询知识条目列表")
    @PostMapping("/list")
    public TableDataInfo<KnowledgeItemVo> list(@RequestBody KnowledgeItemBo bo, PageQuery pageQuery) {
        return knowledgeItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取分面统计（用于筛选面板，不返回条目列表，仅返回各维度的统计计数）
     */
    @Operation(summary = "获取分面统计")
    @PostMapping("/facetStats")
    public R<FacetStatsVo> getFacetStats(@RequestBody KnowledgeItemBo bo) {
        return R.ok(knowledgeItemService.calculateFacetStats(bo));
    }

    /**
     * 获取知识条目详细信息
     */
    @Operation(summary = "获取知识条目详细信息")
    @GetMapping("/{id}")
    public R<KnowledgeItemVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(knowledgeItemService.queryById(id));
    }

    /**
     * 根据itemUuid获取知识条目详细信息
     */
    @Operation(summary = "根据itemUuid获取知识条目详细信息")
    @GetMapping("/uuid/{itemUuid}")
    public R<KnowledgeItemVo> getInfoByUuid(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        return R.ok(knowledgeItemService.queryByItemUuid(itemUuid));
    }

    /**
     * 新增知识条目
     */
    @Operation(summary = "新增知识条目")
    @Log(title = "知识条目", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<java.util.Map<String, String>> add(@Validated @RequestBody KnowledgeItemBo bo) {
        boolean ok = knowledgeItemService.insertByBo(bo);
        if (ok && bo.getItemUuid() != null) {
            return R.ok(java.util.Map.of("itemUuid", bo.getItemUuid()));
        }
        return ok ? R.ok(Map.<String, String>of()) : R.fail();
    }

    /**
     * 修改知识条目
     */
    @Operation(summary = "修改知识条目")
    @Log(title = "知识条目", businessType = BusinessType.UPDATE)
    @PostMapping("/edit/{itemUuid}")
    public R<Void> edit(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid, @Validated @RequestBody KnowledgeItemBo bo) {
        bo.setItemUuid(itemUuid);
        return toAjax(knowledgeItemService.updateByBo(bo));
    }

    /**
     * 删除知识条目
     */
    @Operation(summary = "删除知识条目")
    @Log(title = "知识条目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(knowledgeItemService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 根据itemUuid删除知识条目
     */
    @Operation(summary = "根据itemUuid删除知识条目")
    @Log(title = "知识条目", businessType = BusinessType.DELETE)
    @DeleteMapping("/uuid/{itemUuid}")
    public R<Void> removeByUuid(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        return toAjax(knowledgeItemService.deleteByItemUuid(itemUuid));
    }

    /**
     * 批量删除知识条目（基于itemUuid）
     */
    @Operation(summary = "批量删除知识条目")
    @Log(title = "知识条目", businessType = BusinessType.DELETE)
    @SaCheckPermission("knowledge:item:remove")
    @PostMapping("/delete/batch")
    public R<BatchDeleteResultVo> batchDelete(@RequestBody @NotEmpty(message = "itemUuids不能为空") List<String> itemUuids) {
        return R.ok(knowledgeItemService.batchDeleteByItemUuids(itemUuids));
    }

    /**
     * 批量更新知识条目（基于itemUuid）
     */
    @Operation(summary = "批量更新知识条目")
    @Log(title = "知识条目", businessType = BusinessType.UPDATE)
    @SaCheckPermission("knowledge:item:edit")
    @PostMapping("/batch-update")
    public R<BatchUpdateResultVo> batchUpdate(@Validated @RequestBody BatchUpdateRequestBo request) {
        return R.ok(knowledgeItemService.batchUpdateByItemUuids(request));
    }

    /**
     * 导出预览
     */
    @Operation(summary = "导出预览")
    @PostMapping("/export-preview")
    public R<ExportPreviewVo> exportPreview(@RequestBody ExportPreviewRequestBo request) {
        return R.ok(knowledgeItemService.exportPreview(request));
    }

    /**
     * 导出知识条目
     */
    @Operation(summary = "导出知识条目")
    @Log(title = "知识条目", businessType = BusinessType.EXPORT)
    @SaCheckPermission("knowledge:item:export")
    @PostMapping("/export")
    public void export(@RequestBody ExportRequestBo request, HttpServletResponse response) throws IOException {
        knowledgeItemService.export(request, response);
    }

    /**
     * 获取漏洞类型分布统计
     */
    @Operation(summary = "获取漏洞类型分布统计")
    @GetMapping("/vulnerability-distribution")
    public R<VulnerabilityDistributionVo> getVulnerabilityDistribution(
            @RequestParam("kid") String kid,
            @RequestParam(value = "topN", required = false, defaultValue = "10") Integer topN) {
        return R.ok(knowledgeItemService.getVulnerabilityDistribution(kid, topN));
    }

    // ==================== History (from KnowledgeItemHistoryController) ====================

    /**
     * 根据itemUuid查询版本历史列表
     */
    @Operation(summary = "根据itemUuid查询版本历史列表")
    @GetMapping("/{itemUuid}/history")
    public R<List<KnowledgeItemHistoryVo>> getHistoryByItemUuid(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        return R.ok(knowledgeItemHistoryService.queryByItemUuid(itemUuid));
    }

    /**
     * 根据itemUuid和version查询版本历史
     */
    @Operation(summary = "根据itemUuid和version查询版本历史")
    @GetMapping("/{itemUuid}/history/{version}")
    public R<KnowledgeItemHistoryVo> getHistoryByItemUuidAndVersion(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                                                      @NotNull(message = "version不能为空") @PathVariable("version") Integer version) {
        return R.ok(knowledgeItemHistoryService.queryByItemUuidAndVersion(itemUuid, version));
    }

    /**
     * 创建版本快照
     */
    @Operation(summary = "创建版本快照")
    @Log(title = "知识条目版本历史", businessType = BusinessType.INSERT)
    @PostMapping("/{itemUuid}/history/snapshot")
    public R<Void> createSnapshot(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                   @RequestParam(required = false) String changeType,
                                   @RequestParam(required = false) String changeReason) {
        if (changeType == null) {
            changeType = "update";
        }
        return toAjax(knowledgeItemHistoryService.createVersionSnapshot(itemUuid, changeType, changeReason));
    }

    /**
     * 版本 diff 对比
     */
    @Operation(summary = "版本 diff 对比")
    @GetMapping("/{itemUuid}/history/diff")
    public R<VersionDiffVo> diffVersions(
            @NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
            @RequestParam("from") Integer fromVersion,
            @RequestParam("to") Integer toVersion) {
        return R.ok(knowledgeItemHistoryService.diffVersions(itemUuid, fromVersion, toVersion));
    }

    /**
     * 恢复到指定版本（非破坏性）
     */
    @Operation(summary = "恢复到指定版本")
    @Log(title = "知识条目版本恢复", businessType = BusinessType.UPDATE)
    @PostMapping("/{itemUuid}/history/{version}/restore")
    public R<Void> restoreVersion(
            @NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
            @NotNull(message = "version不能为空") @PathVariable("version") Integer version,
            @Validated @RequestBody VersionRestoreBo bo) {
        return toAjax(knowledgeItemHistoryService.restoreToVersion(itemUuid, version, bo.getReason()));
    }

    // ==================== Tag (from KnowledgeTagController) ====================

    /**
     * 查询知识标签列表
     */
    @Operation(summary = "查询知识标签列表")
    @GetMapping("/tag/list")
    public TableDataInfo<KnowledgeTagVo> tagList(KnowledgeTagBo bo, PageQuery pageQuery) {
        return knowledgeTagService.queryPageList(bo, pageQuery);
    }

    /**
     * 根据itemUuid查询标签列表
     */
    @Operation(summary = "根据itemUuid查询标签列表")
    @GetMapping("/tag/item/{itemUuid}")
    public R<List<KnowledgeTagVo>> getTagsByItemUuid(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        return R.ok(knowledgeTagService.queryByItemUuid(itemUuid));
    }

    /**
     * 获取知识标签详细信息
     */
    @Operation(summary = "获取知识标签详细信息")
    @GetMapping("/tag/{id}")
    public R<KnowledgeTagVo> getTagInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(knowledgeTagService.queryById(id));
    }

    /**
     * 新增知识标签
     */
    @Operation(summary = "新增知识标签")
    @Log(title = "知识标签", businessType = BusinessType.INSERT)
    @PostMapping("/tag")
    public R<Void> addTag(@Validated @RequestBody KnowledgeTagBo bo) {
        return toAjax(knowledgeTagService.insertByBo(bo));
    }

    /**
     * 修改知识标签
     */
    @Operation(summary = "修改知识标签")
    @Log(title = "知识标签", businessType = BusinessType.UPDATE)
    @PutMapping("/tag")
    public R<Void> editTag(@Validated @RequestBody KnowledgeTagBo bo) {
        return toAjax(knowledgeTagService.updateByBo(bo));
    }

    /**
     * 删除知识标签
     */
    @Operation(summary = "删除知识标签")
    @Log(title = "知识标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/tag/{ids}")
    public R<Void> removeTag(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(knowledgeTagService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 为知识条目添加标签
     */
    @Operation(summary = "为知识条目添加标签")
    @Log(title = "知识标签", businessType = BusinessType.UPDATE)
    @PostMapping("/tag/item/{itemUuid}/tag/{tagId}")
    public R<Void> addTagToItem(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                 @NotNull(message = "tagId不能为空") @PathVariable("tagId") Long tagId) {
        return toAjax(knowledgeTagService.addTagToItem(itemUuid, tagId));
    }

    /**
     * 移除知识条目标签
     */
    @Operation(summary = "移除知识条目标签")
    @Log(title = "知识标签", businessType = BusinessType.UPDATE)
    @DeleteMapping("/tag/item/{itemUuid}/tag/{tagId}")
    public R<Void> removeTagFromItem(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                      @NotNull(message = "tagId不能为空") @PathVariable("tagId") Long tagId) {
        return toAjax(knowledgeTagService.removeTagFromItem(itemUuid, tagId));
    }

    // ==================== Favorite (from KnowledgeFavoriteController) ====================

    /**
     * 查询知识收藏列表
     */
    @Operation(summary = "查询知识收藏列表")
    @GetMapping("/favorite/list")
    public TableDataInfo<KnowledgeFavoriteVo> favoriteList(KnowledgeFavoriteBo bo, PageQuery pageQuery) {
        return knowledgeFavoriteService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询我的收藏列表
     */
    @Operation(summary = "查询我的收藏列表")
    @GetMapping("/favorite/my")
    public R<List<KnowledgeFavoriteVo>> getMyFavorites() {
        Long userId = LoginHelper.getUserId();
        return R.ok(knowledgeFavoriteService.queryMyFavorites(userId));
    }

    /**
     * 检查是否已收藏
     */
    @Operation(summary = "检查是否已收藏")
    @GetMapping("/favorite/check/{itemUuid}")
    public R<Boolean> checkFavorite(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        Long userId = LoginHelper.getUserId();
        return R.ok(knowledgeFavoriteService.checkFavorite(userId, itemUuid));
    }

    /**
     * 导出知识收藏列表
     */
    @Operation(summary = "导出知识收藏列表")
    @Log(title = "知识收藏", businessType = BusinessType.EXPORT)
    @SaCheckPermission("knowledge:favorite:export")
    @PostMapping("/favorite/export")
    public void exportFavorite(KnowledgeFavoriteBo bo, HttpServletResponse response) {
        List<KnowledgeFavoriteVo> list = knowledgeFavoriteService.queryMyFavorites(LoginHelper.getUserId());
        // TODO 实现导出逻辑
    }

    /**
     * 新增知识收藏
     */
    @Operation(summary = "新增知识收藏")
    @Log(title = "知识收藏", businessType = BusinessType.INSERT)
    @PostMapping("/favorite")
    public R<Void> addFavorite(@Validated @RequestBody KnowledgeFavoriteBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        return toAjax(knowledgeFavoriteService.insertByBo(bo));
    }

    /**
     * 取消收藏
     */
    @Operation(summary = "取消收藏")
    @Log(title = "知识收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/favorite/{itemUuid}")
    public R<Void> removeFavorite(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        Long userId = LoginHelper.getUserId();
        return toAjax(knowledgeFavoriteService.deleteByItemUuid(userId, itemUuid));
    }

    // ==================== Feedback (from KnowledgeFeedbackController) ====================

    /**
     * 查询知识反馈列表
     */
    @Operation(summary = "查询知识反馈列表")
    @GetMapping("/feedback/list")
    public TableDataInfo<KnowledgeFeedbackVo> feedbackList(KnowledgeFeedbackBo bo, PageQuery pageQuery) {
        return knowledgeFeedbackService.queryPageList(bo, pageQuery);
    }

    /**
     * 根据status查询反馈列表
     */
    @Operation(summary = "根据status查询反馈列表")
    @GetMapping("/feedback/status/{status}")
    public R<List<KnowledgeFeedbackVo>> getFeedbackByStatus(@NotEmpty(message = "status不能为空") @PathVariable("status") String status) {
        return R.ok(knowledgeFeedbackService.queryByStatus(status));
    }

    /**
     * 获取知识反馈详细信息
     */
    @Operation(summary = "获取知识反馈详细信息")
    @GetMapping("/feedback/{id}")
    public R<KnowledgeFeedbackVo> getFeedbackInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(knowledgeFeedbackService.queryById(id));
    }

    /**
     * 根据feedbackUuid获取知识反馈详细信息
     */
    @Operation(summary = "根据feedbackUuid获取知识反馈详细信息")
    @GetMapping("/feedback/uuid/{feedbackUuid}")
    public R<KnowledgeFeedbackVo> getFeedbackInfoByUuid(@NotEmpty(message = "feedbackUuid不能为空") @PathVariable("feedbackUuid") String feedbackUuid) {
        return R.ok(knowledgeFeedbackService.queryByFeedbackUuid(feedbackUuid));
    }

    /**
     * 新增知识反馈
     */
    @Operation(summary = "新增知识反馈")
    @Log(title = "知识反馈", businessType = BusinessType.INSERT)
    @PostMapping("/feedback")
    public R<Void> addFeedback(@Validated @RequestBody KnowledgeFeedbackBo bo) {
        return toAjax(knowledgeFeedbackService.insertByBo(bo));
    }

    /**
     * 修改知识反馈
     */
    @Operation(summary = "修改知识反馈")
    @Log(title = "知识反馈", businessType = BusinessType.UPDATE)
    @PutMapping("/feedback")
    public R<Void> editFeedback(@Validated @RequestBody KnowledgeFeedbackBo bo) {
        return toAjax(knowledgeFeedbackService.updateByBo(bo));
    }

    /**
     * 审核通过反馈
     */
    @Operation(summary = "审核通过反馈")
    @Log(title = "知识反馈", businessType = BusinessType.UPDATE)
    @PostMapping("/feedback/approve/{feedbackUuid}")
    public R<Void> approveFeedback(@NotEmpty(message = "feedbackUuid不能为空") @PathVariable("feedbackUuid") String feedbackUuid,
                                    @RequestParam(required = false) String targetKid,
                                    @RequestParam(required = false, defaultValue = "false") Boolean autoPublish) {
        return toAjax(knowledgeFeedbackService.approveFeedback(feedbackUuid, targetKid, autoPublish));
    }

    /**
     * 驳回反馈
     */
    @Operation(summary = "驳回反馈")
    @Log(title = "知识反馈", businessType = BusinessType.UPDATE)
    @PostMapping("/feedback/reject/{feedbackUuid}")
    public R<Void> rejectFeedback(@NotEmpty(message = "feedbackUuid不能为空") @PathVariable("feedbackUuid") String feedbackUuid,
                                   @RequestParam String rejectReason) {
        return toAjax(knowledgeFeedbackService.rejectFeedback(feedbackUuid, rejectReason));
    }

    /**
     * 删除知识反馈
     */
    @Operation(summary = "删除知识反馈")
    @Log(title = "知识反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/feedback/{ids}")
    public R<Void> removeFeedback(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(knowledgeFeedbackService.deleteWithValidByIds(List.of(ids), true));
    }

    // ==================== Detection (from DetectionResultController) ====================

    /**
     * 查询检测结果列表
     */
    @Operation(summary = "查询检测结果列表")
    @GetMapping("/detection/list")
    public TableDataInfo<DetectionResultVo> detectionList(DetectionResultBo bo, PageQuery pageQuery) {
        return detectionResultService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出检测结果列表
     */
    @Operation(summary = "导出检测结果列表")
    @Log(title = "检测结果", businessType = BusinessType.EXPORT)
    @SaCheckPermission("knowledge:detection:export")
    @PostMapping("/detection/export")
    public void exportDetection(DetectionResultBo bo, HttpServletResponse response) {
        List<DetectionResultVo> list = detectionResultService.queryByTaskId(bo.getTaskId());
        // TODO 实现导出逻辑
    }

    /**
     * 获取检测结果详细信息
     */
    @Operation(summary = "获取检测结果详细信息")
    @GetMapping("/detection/{id}")
    public R<DetectionResultVo> getDetectionInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(detectionResultService.queryById(id));
    }

    /**
     * 根据resultUuid获取检测结果详细信息
     */
    @Operation(summary = "根据resultUuid获取检测结果详细信息")
    @GetMapping("/detection/uuid/{resultUuid}")
    public R<DetectionResultVo> getDetectionInfoByUuid(@NotEmpty(message = "resultUuid不能为空") @PathVariable("resultUuid") String resultUuid) {
        return R.ok(detectionResultService.queryByResultUuid(resultUuid));
    }

    /**
     * 根据taskId查询检测结果列表
     */
    @Operation(summary = "根据taskId查询检测结果列表")
    @GetMapping("/detection/task/{taskId}")
    public R<List<DetectionResultVo>> getDetectionByTaskId(@NotNull(message = "taskId不能为空") @PathVariable("taskId") Long taskId) {
        return R.ok(detectionResultService.queryByTaskId(taskId));
    }

    /**
     * 新增检测结果
     */
    @Operation(summary = "新增检测结果")
    @Log(title = "检测结果", businessType = BusinessType.INSERT)
    @PostMapping("/detection")
    public R<Void> addDetection(@Validated @RequestBody DetectionResultBo bo) {
        return toAjax(detectionResultService.insertByBo(bo));
    }

    /**
     * 修改检测结果
     */
    @Operation(summary = "修改检测结果")
    @Log(title = "检测结果", businessType = BusinessType.UPDATE)
    @PutMapping("/detection")
    public R<Void> editDetection(@Validated @RequestBody DetectionResultBo bo) {
        return toAjax(detectionResultService.updateByBo(bo));
    }

    /**
     * 删除检测结果
     */
    @Operation(summary = "删除检测结果")
    @Log(title = "检测结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/detection/{ids}")
    public R<Void> removeDetection(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(detectionResultService.deleteWithValidByIds(List.of(ids), true));
    }

    // ==================== Fragment Association (N:M) ====================

    /**
     * 查询条目关联的所有片段（含 content、documentName 等详情，用于前端展示）
     */
    @Operation(summary = "查询条目关联的所有片段")
    @GetMapping("/{itemUuid}/fragments")
    public R<List<ItemFragmentDetailVo>> listFragments(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        return R.ok(knowledgeItemFragmentService.listByItemUuidWithDetails(itemUuid));
    }

    /**
     * 为条目关联一个片段
     */
    @Operation(summary = "为条目关联一个片段")
    @Log(title = "知识条目片段关联", businessType = BusinessType.INSERT)
    @PostMapping("/{itemUuid}/fragments/{fragmentId}")
    public R<KnowledgeItemFragment> associateFragment(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                                       @NotNull(message = "fragmentId不能为空") @PathVariable("fragmentId") Long fragmentId) {
        return R.ok(knowledgeItemFragmentService.associate(itemUuid, fragmentId));
    }

    /**
     * 取消条目与片段的关联
     */
    @Operation(summary = "取消条目与片段的关联")
    @Log(title = "知识条目片段关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemUuid}/fragments/{fragmentId}")
    public R<Void> disassociateFragment(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                         @NotNull(message = "fragmentId不能为空") @PathVariable("fragmentId") Long fragmentId) {
        knowledgeItemFragmentService.disassociate(itemUuid, fragmentId);
        return R.ok();
    }
}
