package org.ruoyi.chat.controller.knowledge.v2;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.config.KnowledgeRoleConfig;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeInfoBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeInfoVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeStorageStatsVo;
import org.ruoyi.knowledge.curation.service.IKnowledgeInfoService;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 知识库管理
 *
 * @author ageerle
 * @date 2025-05-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/base")
@Tag(name = "知识库管理")
@Slf4j
public class KnowledgeBaseController extends BaseController {

    private final IKnowledgeInfoService knowledgeInfoService;

    private final KnowledgeRoleConfig knowledgeRoleConfig;

    /**
     * 根据用户信息查询本地知识库（分页）
     */
    @Operation(summary = "查询知识库列表")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeInfoVo> list(KnowledgeInfoBo bo, PageQuery pageQuery) {
        if (!StpUtil.isLogin()) {
            throw new SecurityException("请先去登录!");
        }
        if (!Objects.equals(LoginHelper.getUserId(), 1L)) {
            bo.setUid(LoginHelper.getUserId());
        }
        return knowledgeInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 根据用户信息及知识库角色查询本地知识库
     */
    @Operation(summary = "按角色查询知识库列表")
    @GetMapping("/listByRole")
    public TableDataInfo<KnowledgeInfoVo> listByRole(KnowledgeInfoBo bo, PageQuery pageQuery) {
        if (!StpUtil.isLogin()) {
            throw new SecurityException("请先去登录!");
        }

        bo.setUid(LoginHelper.getUserId());

        if (!knowledgeRoleConfig.getEnable()) {
            return knowledgeInfoService.queryPageList(bo, pageQuery);
        } else {
            return knowledgeInfoService.queryPageListByRole(bo, pageQuery);
        }
    }

    /**
     * 查询知识库详情
     */
    @Operation(summary = "获取知识库详情")
    @GetMapping("/{kid}")
    public R<KnowledgeInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String kid) {
        return R.ok(knowledgeInfoService.queryByKid(kid));
    }

    /**
     * 新增知识库
     */
    @Operation(summary = "新增知识库")
    @Log(title = "知识库", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> save(@Validated(AddGroup.class) @RequestBody KnowledgeInfoBo bo) {
        knowledgeInfoService.saveOne(bo);
        return R.ok();
    }

    /**
     * 修改知识库
     */
    @Operation(summary = "修改知识库")
    @Log(title = "知识库", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> update(@Validated(AddGroup.class) @RequestBody KnowledgeInfoBo bo) {
        knowledgeInfoService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 删除知识库
     */
    @Operation(summary = "删除知识库")
    @Log(title = "知识库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{kids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                         @PathVariable String kids) {
        knowledgeInfoService.removeKnowledge(kids);
        return R.ok();
    }

    /**
     * 刷新所有知识库的统计字段（条目数、片段数、存储大小）
     * 不更新update_time字段
     */
    @Operation(summary = "刷新知识库统计")
    @PostMapping("/refresh-statistics")
    public R<Void> refreshStatistics() {
        knowledgeInfoService.refreshAllKnowledgeStatistics();
        return R.ok();
    }

    /**
     * 获取知识库存储监控统计
     */
    @Operation(summary = "获取知识库存储监控统计")
    @GetMapping("/{kid}/storage-stats")
    public R<KnowledgeStorageStatsVo> getStorageStats(@NotBlank(message = "知识库ID不能为空") @PathVariable String kid) {
        return R.ok(knowledgeInfoService.getStorageStats(kid));
    }
}
