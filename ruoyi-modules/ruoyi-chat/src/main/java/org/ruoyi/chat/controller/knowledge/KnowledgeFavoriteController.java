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
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeFavoriteBo;
import org.ruoyi.domain.vo.KnowledgeFavoriteVo;
import org.ruoyi.service.IKnowledgeFavoriteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识收藏Controller
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/favorite")
@Tag(name = "知识收藏管理", description = "知识收藏相关接口")
public class KnowledgeFavoriteController extends BaseController {

    private final IKnowledgeFavoriteService knowledgeFavoriteService;

    /**
     * 查询知识收藏列表
     */
    @Operation(summary = "查询知识收藏列表")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeFavoriteVo> list(KnowledgeFavoriteBo bo, PageQuery pageQuery) {
        return knowledgeFavoriteService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询我的收藏列表
     */
    @Operation(summary = "查询我的收藏列表")
    @GetMapping("/my")
    public R<List<KnowledgeFavoriteVo>> getMyFavorites() {
        Long userId = LoginHelper.getUserId();
        return R.ok(knowledgeFavoriteService.queryMyFavorites(userId));
    }

    /**
     * 检查是否已收藏
     */
    @Operation(summary = "检查是否已收藏")
    @GetMapping("/check/{itemUuid}")
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
    @PostMapping("/export")
    public void export(KnowledgeFavoriteBo bo, HttpServletResponse response) {
        List<KnowledgeFavoriteVo> list = knowledgeFavoriteService.queryMyFavorites(LoginHelper.getUserId());
        // TODO 实现导出逻辑
    }

    /**
     * 新增知识收藏
     */
    @Operation(summary = "新增知识收藏")
    @Log(title = "知识收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody KnowledgeFavoriteBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        return toAjax(knowledgeFavoriteService.insertByBo(bo));
    }

    /**
     * 取消收藏
     */
    @Operation(summary = "取消收藏")
    @Log(title = "知识收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemUuid}")
    public R<Void> remove(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        Long userId = LoginHelper.getUserId();
        return toAjax(knowledgeFavoriteService.deleteByItemUuid(userId, itemUuid));
    }
}
