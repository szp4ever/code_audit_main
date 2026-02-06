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
import org.ruoyi.domain.bo.DetectionResultBo;
import org.ruoyi.domain.vo.DetectionResultVo;
import org.ruoyi.service.IDetectionResultService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检测结果Controller
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/detection")
@Tag(name = "检测结果管理", description = "检测结果相关接口")
public class DetectionResultController extends BaseController {

    private final IDetectionResultService detectionResultService;

    @Operation(summary = "查询检测结果列表")
    @GetMapping("/list")
    public TableDataInfo<DetectionResultVo> list(DetectionResultBo bo, PageQuery pageQuery) {
        return detectionResultService.queryPageList(bo, pageQuery);
    }

    @Operation(summary = "导出检测结果列表")
    @Log(title = "检测结果", businessType = BusinessType.EXPORT)
    @SaCheckPermission("knowledge:detection:export")
    @PostMapping("/export")
    public void export(DetectionResultBo bo, HttpServletResponse response) {
        List<DetectionResultVo> list = detectionResultService.queryByTaskId(bo.getTaskId());
        //TODO 实现导出逻辑
    }

    @Operation(summary = "获取检测结果详细信息")
    @GetMapping("/{id}")
    public R<DetectionResultVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(detectionResultService.queryById(id));
    }

    @Operation(summary = "根据resultUuid获取检测结果详细信息")
    @GetMapping("/uuid/{resultUuid}")
    public R<DetectionResultVo> getInfoByUuid(@NotEmpty(message = "resultUuid不能为空") @PathVariable("resultUuid") String resultUuid) {
        return R.ok(detectionResultService.queryByResultUuid(resultUuid));
    }

    @Operation(summary = "根据taskId查询检测结果列表")
    @GetMapping("/task/{taskId}")
    public R<List<DetectionResultVo>> getByTaskId(@NotNull(message = "taskId不能为空") @PathVariable("taskId") Long taskId) {
        return R.ok(detectionResultService.queryByTaskId(taskId));
    }

    @Operation(summary = "新增检测结果")
    @Log(title = "检测结果", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody DetectionResultBo bo) {
        return toAjax(detectionResultService.insertByBo(bo));
    }

    @Operation(summary = "修改检测结果")
    @Log(title = "检测结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody DetectionResultBo bo) {
        return toAjax(detectionResultService.updateByBo(bo));
    }

    @Operation(summary = "删除检测结果")
    @Log(title = "检测结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(detectionResultService.deleteWithValidByIds(List.of(ids), true));
    }
}
