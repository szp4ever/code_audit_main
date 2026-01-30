package org.ruoyi.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.vo.TaskDurationStatItem;
import org.ruoyi.chat.domain.vo.TaskMonthlyCountItem;
import org.ruoyi.chat.domain.vo.TaskQuarterlyStatsItem;
import org.ruoyi.chat.domain.vo.TaskRealTimeCountVO;
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

    /**
     * 月度任务数量统计
     */
    @Operation(summary = "获取月度任务数量统计")
    @GetMapping("/monthlycount")
    public R<List<TaskMonthlyCountItem>> getMonthlyCount(
            @RequestParam(value = "start",required = false) String startMonth,
            @RequestParam(value = "end",required = false) String endMonth) {
        List<TaskMonthlyCountItem> monthlyCount = taskManagementService.getTaskMonthlyCount(startMonth, endMonth);
//        System.out.println(monthlyCount);
        return R.ok(monthlyCount);
    }

    /**
     * 季度任务统计
     */
    @Operation(summary = "获取季度任务统计")
    @GetMapping("/stats/quarterly") // 接口路径语义清晰，和月度 /monthlycount 对应
    public R<List<TaskQuarterlyStatsItem>> getQuarterlyStats(
            @RequestParam(value = "year", required = false) String year) {
        // 调用 Service 层方法，获取季度统计数据
        List<TaskQuarterlyStatsItem> quarterlyStats = taskManagementService.getTaskQuarterlyStats(year);
        // 统一返回格式 R.ok()，和月度统计保持一致
        return R.ok(quarterlyStats);
    }

    // ========== 新增：任务实时数量统计接口 ==========
    @Operation(summary = "获取任务实时数量统计（执行中/排队中/已完成）")
    @GetMapping("/realtime_count")
    public R<TaskRealTimeCountVO> getTaskRealTimeCount() {
        // 调用 Service 层方法，获取统计结果
        TaskRealTimeCountVO realTimeCountVO = taskManagementService.getTaskRealTimeCount();
        // 返回前端（R 是项目统一返回结果封装，保持原有风格）
//        System.out.println( realTimeCountVO );
        return R.ok(realTimeCountVO);
    }
}



