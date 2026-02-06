package org.ruoyi.chat.controller.knowledge;

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
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.BatchUpdateRequestBo;
import org.ruoyi.domain.bo.KnowledgeItemBo;
import org.ruoyi.domain.bo.ExportPreviewRequestBo;
import org.ruoyi.domain.bo.ExportRequestBo;
import org.ruoyi.domain.vo.KnowledgeItemVo;
import org.ruoyi.domain.vo.BatchDeleteResultVo;
import org.ruoyi.domain.vo.BatchUpdateResultVo;
import org.ruoyi.domain.vo.ExportPreviewVo;
import org.ruoyi.service.IKnowledgeItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识条目Controller
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/item")
@Tag(name = "知识条目管理", description = "知识条目相关接口")
public class KnowledgeItemController extends BaseController {

    private final IKnowledgeItemService knowledgeItemService;

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
    public R<Void> add(@Validated @RequestBody KnowledgeItemBo bo) {
        return toAjax(knowledgeItemService.insertByBo(bo));
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
    public void export(@RequestBody ExportRequestBo request, HttpServletResponse response) {
        knowledgeItemService.export(request, response);
    }
}
