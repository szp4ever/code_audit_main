package org.ruoyi.chat.controller.knowledge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.domain.vo.KnowledgeItemHistoryVo;
import org.ruoyi.service.IKnowledgeItemHistoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识条目版本历史Controller
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/item/history")
@Tag(name = "知识条目版本历史管理", description = "知识条目版本历史相关接口")
public class KnowledgeItemHistoryController extends BaseController {

    private final IKnowledgeItemHistoryService knowledgeItemHistoryService;

    /**
     * 根据itemUuid查询版本历史列表
     */
    @Operation(summary = "根据itemUuid查询版本历史列表")
    @GetMapping("/{itemUuid}")
    public R<List<KnowledgeItemHistoryVo>> getHistoryByItemUuid(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid) {
        return R.ok(knowledgeItemHistoryService.queryByItemUuid(itemUuid));
    }

    /**
     * 根据itemUuid和version查询版本历史
     */
    @Operation(summary = "根据itemUuid和version查询版本历史")
    @GetMapping("/{itemUuid}/{version}")
    public R<KnowledgeItemHistoryVo> getHistoryByItemUuidAndVersion(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                                                      @NotNull(message = "version不能为空") @PathVariable("version") Integer version) {
        return R.ok(knowledgeItemHistoryService.queryByItemUuidAndVersion(itemUuid, version));
    }

    /**
     * 创建版本快照
     */
    @Operation(summary = "创建版本快照")
    @Log(title = "知识条目版本历史", businessType = BusinessType.INSERT)
    @PostMapping("/snapshot/{itemUuid}")
    public R<Void> createSnapshot(@NotEmpty(message = "itemUuid不能为空") @PathVariable("itemUuid") String itemUuid,
                                   @RequestParam(required = false) String changeType,
                                   @RequestParam(required = false) String changeReason) {
        if (changeType == null) {
            changeType = "update";
        }
        return toAjax(knowledgeItemHistoryService.createVersionSnapshot(itemUuid, changeType, changeReason));
    }
}
