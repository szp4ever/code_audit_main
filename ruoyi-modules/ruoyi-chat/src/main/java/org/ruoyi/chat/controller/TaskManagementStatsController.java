package org.ruoyi.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.vo.TaskDurationStatItem;
import org.ruoyi.chat.service.ITaskManagementService;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.web.core.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务管理统计Controller
 *
 * @author ruoyi
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/taskmanagement")
@Tag(name = "任务管理统计", description = "任务管理统计相关接口")
public class TaskManagementStatsController extends BaseController {

    private final ITaskManagementService taskManagementService;

    /**
     * 获取任务状态统计
     */
    @Operation(summary = "获取任务状态统计")
    @GetMapping("/stats/status")
    public R<Map<String, Integer>> getStatusStats() {
        Map<String, Integer> stats = taskManagementService.getStatusStats();
        return R.ok(stats);
    }

    /**
     * 获取任务类型统计
     */
    @Operation(summary = "获取任务类型统计")
    @GetMapping("/stats/type")
    public R<Map<String, Integer>> getTypeStats() {
        Map<String, Integer> stats = taskManagementService.getTypeStats();
        return R.ok(stats);
    }

    /**
     * 获取任务耗时统计
     */
    @Operation(summary = "获取任务耗时统计")
    @GetMapping("/stats/duration")
    public R<List<TaskDurationStatItem>> getDurationStats(
            @RequestParam(value = "timeRange", required = false, defaultValue = "day") String timeRange) {
        List<TaskDurationStatItem> stats = taskManagementService.getDurationStats(timeRange);
        return R.ok(stats);
    }
}



