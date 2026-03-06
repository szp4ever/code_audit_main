package org.ruoyi.chat.controller.knowledge;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.domain.enums.ProcessingStatus;
import org.ruoyi.domain.vo.KnowledgeAttachProcessVo;
import org.ruoyi.service.IAttachProcessService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 附件处理状态管理控制器
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/attach/process")
public class AttachProcessController extends BaseController {

    private final IAttachProcessService attachProcessService;

    /**
     * 获取处理状态
     * 轮询时调用：不尝试加锁，只返回状态和进度
     * 进入审阅界面时调用：尝试加锁（仅在USER_REVIEW_MATCHING或USER_REVIEW_ITEMS状态）
     */
    @GetMapping("/{processId}")
    public R<KnowledgeAttachProcessVo> getStatus(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestParam(required = false, defaultValue = "false") Boolean skipLock) {
        KnowledgeAttachProcessVo vo = attachProcessService.getCurrentStatus(processId);
        
        // 如果skipLock=true（轮询调用），不尝试加锁
        if (skipLock) {
            vo.setLocked(false);
            return R.ok(vo);
        }
        
        // 只有在需要用户交互的状态下才尝试加锁
        String currentStatus = vo.getCurrentStatus();
        if (ProcessingStatus.USER_REVIEW_MATCHING.getCode().equals(currentStatus) ||
            ProcessingStatus.USER_REVIEW_ITEMS.getCode().equals(currentStatus)) {
            // 尝试加锁（如果锁被占用，返回警告信息，但不阻止访问）
            boolean lockAcquired = attachProcessService.tryLock(processId);
            if (!lockAcquired) {
                vo.setLocked(true);
                vo.setLockMessage("处理任务正被其他用户编辑，请稍后重试");
            } else {
                vo.setLocked(false);
            }
        } else {
            // 非用户交互状态，不需要加锁
            vo.setLocked(false);
        }
        return R.ok(vo);
    }

    /**
     * 批量获取处理状态
     */
    @PostMapping("/batch")
    public R<List<KnowledgeAttachProcessVo>> getStatusBatch(
            @RequestBody List<String> processIds) {
        return R.ok(attachProcessService.getAttachProcessStatusBatch(processIds));
    }

    /**
     * 确认匹配结果
     */
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/{processId}/confirm-matching")
    public R<Void> confirmMatching(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestBody List<IAttachProcessService.MatchingDecision> decisions) {
        attachProcessService.confirmMatching(processId, decisions);
        return R.ok();
    }

    /**
     * 确认新条目
     */
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/{processId}/confirm-items")
    public R<Void> confirmItems(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestBody List<IAttachProcessService.ItemModification> modifications) {
        attachProcessService.confirmItems(processId, modifications);
        return R.ok();
    }

    /**
     * 保存草稿
     */
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/{processId}/save-draft")
    public R<Void> saveDraft(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestBody Map<String, Object> partialData) {
        attachProcessService.saveDraft(processId, partialData);
        return R.ok();
    }

    /**
     * 回退状态
     */
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/{processId}/rollback")
    public R<Void> rollback(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestParam @NotEmpty(message = "目标状态不能为空") String targetStatus) {
        ProcessingStatus status = ProcessingStatus.fromCode(targetStatus);
        attachProcessService.rollback(processId, status);
        return R.ok();
    }

    /**
     * 取消处理任务
     */
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/{processId}/cancel")
    public R<Void> cancelProcess(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId) {
        attachProcessService.cancelProcess(processId);
        return R.ok();
    }
}
