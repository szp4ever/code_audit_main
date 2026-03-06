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
import org.ruoyi.domain.bo.KnowledgeTagBo;
import org.ruoyi.domain.vo.KnowledgeTagVo;
import org.ruoyi.service.IKnowledgeTagService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识标签Controller
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/tag")
@Tag(name = "知识标签管理", description = "知识标签相关接口")
public class KnowledgeTagController extends BaseController {

    private final IKnowledgeTagService knowledgeTagService;

    /**
     * 查询知识标签列表
     */
    @Operation(summary = "查询知识标签列表")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeTagVo> list(KnowledgeTagBo bo, PageQuery pageQuery) {
        return knowledgeTagService.queryPageList(bo, pageQuery);
    }

    /**
     * 根据itemUuid查询标签列表
     */
    @Operation(summary = "根据itemUuid查询标签列表")
    @GetMapping("/item/{itemUuid}")
    public R<List<KnowledgeTagVo>> getTagsByItemUuid(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        return R.ok(knowledgeTagService.queryByItemUuid(itemUuid));
    }

    /**
     * 获取知识标签详细信息
     */
    @Operation(summary = "获取知识标签详细信息")
    @GetMapping("/{id}")
    public R<KnowledgeTagVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(knowledgeTagService.queryById(id));
    }

    /**
     * 新增知识标签
     */
    @Operation(summary = "新增知识标签")
    @Log(title = "知识标签", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody KnowledgeTagBo bo) {
        return toAjax(knowledgeTagService.insertByBo(bo));
    }

    /**
     * 修改知识标签
     */
    @Operation(summary = "修改知识标签")
    @Log(title = "知识标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody KnowledgeTagBo bo) {
        return toAjax(knowledgeTagService.updateByBo(bo));
    }

    /**
     * 删除知识标签
     */
    @Operation(summary = "删除知识标签")
    @Log(title = "知识标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(knowledgeTagService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 为知识条目添加标签
     */
    @Operation(summary = "为知识条目添加标签")
    @Log(title = "知识标签", businessType = BusinessType.UPDATE)
    @PostMapping("/item/{itemUuid}/tag/{tagId}")
    public R<Void> addTagToItem(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                 @NotNull(message = "tagId不能为空") @PathVariable("tagId") Long tagId) {
        return toAjax(knowledgeTagService.addTagToItem(itemUuid, tagId));
    }

    /**
     * 移除知识条目标签
     */
    @Operation(summary = "移除知识条目标签")
    @Log(title = "知识标签", businessType = BusinessType.UPDATE)
    @DeleteMapping("/item/{itemUuid}/tag/{tagId}")
    public R<Void> removeTagFromItem(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                      @NotNull(message = "tagId不能为空") @PathVariable("tagId") Long tagId) {
        return toAjax(knowledgeTagService.removeTagFromItem(itemUuid, tagId));
    }
}
