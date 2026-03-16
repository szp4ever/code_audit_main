package org.ruoyi.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.vo.TaskDurationStatItem;
import org.ruoyi.chat.domain.vo.TaskMonthlyCountItem;
import org.ruoyi.chat.domain.vo.TaskYearlyCountItem;
import org.ruoyi.chat.domain.vo.TaskQuarterlyStatsItem;
import org.ruoyi.chat.domain.vo.TaskRealTimeCountVO;
import org.ruoyi.system.domain.vo.UserOperationHeatmapItem;
import org.ruoyi.chat.domain.vo.*;
import org.ruoyi.chat.service.ITaskManagementService;
import org.ruoyi.chat.service.ISystemLoadService;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.system.service.ISysOperLogService;
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
    private final ISysOperLogService sysOperLogService;
    private final ISystemLoadService systemLoadService;

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
     * 年度任务数量统计
     */
    @Operation(summary = "获取年度任务数量统计")
    @GetMapping("/yearlycount")  // 接口路径从 monthlycount 改为 yearlycount
    public R<List<TaskYearlyCountItem>> getYearlyCount(

            // 参数名从 startMonth/endMonth 改为 startYear/endYear，语义更清晰
            @RequestParam(value = "start", required = false) String startYear,
            @RequestParam(value = "end", required = false) String endYear) {

        // 调用服务层的年度统计方法（需同步修改服务层）
        List<TaskYearlyCountItem> yearlyCount = taskManagementService.getTaskYearlyCount(startYear, endYear);
        return R.ok(yearlyCount);
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

    /**
     * 获取用户操作热力图数据
     */
    @Operation(summary = "获取用户操作热力图数据")
    @GetMapping("/stats/user-operation-heatmap")
    public R<List<UserOperationHeatmapItem>> getUserOperationHeatmap(
            @RequestParam(value = "timeRange", required = false, defaultValue = "week") String timeRange,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        try {
            log.info("接收到热力图数据请求 - timeRange: {}, startDate: {}, endDate: {}", timeRange, startDate, endDate);
            List<UserOperationHeatmapItem> result = sysOperLogService.getUserOperationHeatmap(timeRange, startDate, endDate);
            log.info("返回热力图数据，共 {} 条", result != null ? result.size() : 0);
            return R.ok(result);
        } catch (Exception e) {
            log.error("获取用户操作热力图数据失败", e);
            return R.fail("获取热力图数据失败: " + e.getMessage());
        }
    }
    /**
     * 按时间范围查询代码规范检查通过率（适配前端饼图）
     */
    @Operation(summary = "按时间范围查询通过率", description = "返回passed/failed数量，用于饼图展示")
    @GetMapping("/code_standard_pass_rate")
    public R<CodeStandardPassRate> getCodeStandardPassRate(
            @RequestParam String start,
            @RequestParam String end) {
        log.info("查询代码规范检查通过率，时间范围：{} 至 {}", start, end);
        CodeStandardPassRate passRate = taskManagementService.getPassRateByTimeRange(start, end);
        return R.ok(passRate);
    }

    /**
     * 月度代码质量统计
     */
    @Operation(summary = "获取月度代码质量统计")
    @GetMapping("/monthly_code_Quality")
    public R<List<TaskMonthlyCodeQualityItem>> getMonthlyCodeQuality(
            @RequestParam(value = "start", required = false) String startMonth,
            @RequestParam(value = "end", required = false) String endMonth) {
        List<TaskMonthlyCodeQualityItem> monthlyCodeQuality = taskManagementService.getTaskMonthlyCodeQuality(startMonth, endMonth);
        return R.ok(monthlyCodeQuality);
    }

    /**
     * 年度代码质量统计
     */
    @Operation(summary = "获取年度代码质量统计")
    @GetMapping("/yearly_code_Quality")
    public R<List<TaskYearlyCodeQualityItem>> getYearlyCodeQuality(
            @RequestParam(value = "start", required = false) String startYear,
            @RequestParam(value = "end", required = false) String endYear) {
        List<TaskYearlyCodeQualityItem> yearlyCodeQuality = taskManagementService.getTaskYearlyCodeQuality(startYear, endYear);
        return R.ok(yearlyCodeQuality);
    }

    /**
     * 活跃用户分布
     */
    @Operation(summary = "获取活跃用户分布")
    @GetMapping("/stats/active-user-distribution")
    public R<List<ActiveUserDistributionItem>> getActiveUserDistribution(
            @RequestParam(value = "date", required = false) String date) {
        List<ActiveUserDistributionItem> distribution = taskManagementService.getActiveUserDistribution(date);
        return R.ok(distribution);
    }

    /**
     * 峰值时段分析
     */
    @Operation(summary = "获取峰值时段分析")
    @GetMapping("/stats/peak-time-analysis")
    public R<List<PeakTimeAnalysisItem>> getPeakTimeAnalysis(
            @RequestParam(value = "date", required = true) String date) {
        List<PeakTimeAnalysisItem> analysis = taskManagementService.getPeakTimeAnalysis(date);
        return R.ok(analysis);
    }

    /**
     * 在线用户数量
     */
    @Operation(summary = "获取在线用户数量")
    @GetMapping("/stats/online-user-count")
    public R<OnlineUserCountVO> getOnlineUserCount() {
        OnlineUserCountVO count = taskManagementService.getOnlineUserCount();
        return R.ok(count);
    }

    /**
     * 系统负载监控
     */
    @Operation(summary = "获取系统负载数据")
    @GetMapping("/stats/system-load")
    public R<List<SystemLoadItem>> getSystemLoad(
            @RequestParam(value = "timeRange", required = true) String timeRange) {
        List<SystemLoadItem> systemLoad = systemLoadService.getSystemLoadByTimeRange(timeRange);
        return R.ok(systemLoad);
    }

    /**
     * 漏洞修复效率统计
     */
    @Operation(summary = "获取漏洞修复效率统计")
    @GetMapping("/stats/vulnerability-fix-efficiency")
    public R<List<VulnerabilityFixEfficiencyItem>> getVulnerabilityFixEfficiency(
            @RequestParam(value = "startDate", required = true) String startDate,
            @RequestParam(value = "endDate", required = true) String endDate) {
        List<VulnerabilityFixEfficiencyItem> efficiency = taskManagementService.getVulnerabilityFixEfficiency(startDate, endDate);
        return R.ok(efficiency);
    }
}



