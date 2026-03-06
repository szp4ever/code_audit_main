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
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeFeedbackBo;
import org.ruoyi.domain.vo.KnowledgeFeedbackVo;
import org.ruoyi.service.IKnowledgeFeedbackService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识反馈Controller
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/feedback")
@Tag(name = "知识反馈管理", description = "知识反馈相关接口")
public class KnowledgeFeedbackController extends BaseController {

    private final IKnowledgeFeedbackService knowledgeFeedbackService;

    /**
     * 查询知识反馈列表
     */
    @Operation(summary = "查询知识反馈列表")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeFeedbackVo> list(KnowledgeFeedbackBo bo, PageQuery pageQuery) {
        return knowledgeFeedbackService.queryPageList(bo, pageQuery);
    }

    /**
     * 根据status查询反馈列表
     */
    @Operation(summary = "根据status查询反馈列表")
    @GetMapping("/status/{status}")
    public R<List<KnowledgeFeedbackVo>> getByStatus(@NotEmpty(message = "status不能为空") @PathVariable("status") String status) {
        return R.ok(knowledgeFeedbackService.queryByStatus(status));
    }

    /**
     * 获取知识反馈详细信息
     */
    @Operation(summary = "获取知识反馈详细信息")
    @GetMapping("/{id}")
    public R<KnowledgeFeedbackVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(knowledgeFeedbackService.queryById(id));
    }

    /**
     * 根据feedbackUuid获取知识反馈详细信息
     */
    @Operation(summary = "根据feedbackUuid获取知识反馈详细信息")
    @GetMapping("/uuid/{feedbackUuid}")
    public R<KnowledgeFeedbackVo> getInfoByUuid(@NotEmpty(message = "feedbackUuid不能为空") @PathVariable("feedbackUuid") String feedbackUuid) {
        return R.ok(knowledgeFeedbackService.queryByFeedbackUuid(feedbackUuid));
    }

    /**
     * 新增知识反馈
     */
    @Operation(summary = "新增知识反馈")
    @Log(title = "知识反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody KnowledgeFeedbackBo bo) {
        return toAjax(knowledgeFeedbackService.insertByBo(bo));
    }

    /**
     * 修改知识反馈
     */
    @Operation(summary = "修改知识反馈")
    @Log(title = "知识反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody KnowledgeFeedbackBo bo) {
        return toAjax(knowledgeFeedbackService.updateByBo(bo));
    }

    /**
     * 审核通过反馈
     */
    @Operation(summary = "审核通过反馈")
    @Log(title = "知识反馈", businessType = BusinessType.UPDATE)
    @PostMapping("/approve/{feedbackUuid}")
    public R<Void> approve(@NotEmpty(message = "feedbackUuid不能为空") @PathVariable("feedbackUuid") String feedbackUuid,
                           @RequestParam(required = false) String targetKid,
                           @RequestParam(required = false, defaultValue = "false") Boolean autoPublish) {
        return toAjax(knowledgeFeedbackService.approveFeedback(feedbackUuid, targetKid, autoPublish));
    }

    /**
     * 驳回反馈
     */
    @Operation(summary = "驳回反馈")
    @Log(title = "知识反馈", businessType = BusinessType.UPDATE)
    @PostMapping("/reject/{feedbackUuid}")
    public R<Void> reject(@NotEmpty(message = "feedbackUuid不能为空") @PathVariable("feedbackUuid") String feedbackUuid,
                         @RequestParam String rejectReason) {
        return toAjax(knowledgeFeedbackService.rejectFeedback(feedbackUuid, rejectReason));
    }

    /**
     * 删除知识反馈
     */
    @Operation(summary = "删除知识反馈")
    @Log(title = "知识反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(knowledgeFeedbackService.deleteWithValidByIds(List.of(ids), true));
    }
}
