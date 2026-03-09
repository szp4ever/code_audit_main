package org.ruoyi.system.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.system.domain.vo.SysDbBackupScheduleConfigVo;
import org.ruoyi.system.service.ISysDbBackupService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

/**
 * 数据库备份定时任务
 *
 * 功能：
 * 1. 动态读取备份配置（backup.enabled, backup.cron, backup.keepcount）
 * 2. 根据 Cron 表达式定时执行备份
 * 3. 备份后自动清理超出保留数量的旧备份
 *
 * 配置说明：
 * - backup.enabled: 0=启用，1=不启用
 * - backup.cron: Cron 表达式（如 "0 0 2 * * ?" 表示每天凌晨2点）
 * - backup.keepcount: 保留备份数量
 *
 * @author GPT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DbBackupScheduleTask implements CommandLineRunner {

    private final ISysDbBackupService dbBackupService;
    private final TaskScheduler taskScheduler;

    /**
     * 当前调度的任务
     */
    private ScheduledFuture<?> scheduledTask;

    /**
     * 当前使用的 Cron 表达式（用于检测配置变更）
     */
    private String currentCron;

    /**
     * 当前是否启用（用于检测配置变更）
     */
    private Boolean currentEnabled;

    /**
     * 配置检查间隔，默认 60 秒检查一次配置
     */
    private static final Duration CONFIG_CHECK_INTERVAL = Duration.ofSeconds(60);

    /**
     * 应用启动后初始化定时任务
     */
    @Override
    public void run(String... args) {
        log.info("数据库备份定时任务初始化...");
        scheduleTask();
        // 启动配置检查任务
        startConfigCheckTask();
    }

    /**
     * 启动配置检查任务
     * 定期检查配置是否变更，如果变更则重新调度任务
     */
    private void startConfigCheckTask() {
        taskScheduler.scheduleWithFixedDelay(() -> {
            try {
                checkAndReschedule();
            } catch (Exception e) {
                log.error("检查备份配置时发生异常", e);
            }
        }, Instant.now().plusSeconds(30), CONFIG_CHECK_INTERVAL);
    }

    /**
     * 检查配置并重新调度任务（如果需要）
     */
    private void checkAndReschedule() {
        SysDbBackupScheduleConfigVo config = dbBackupService.getScheduleConfig();
        boolean enabled = config.getEnabled() != null && config.getEnabled();
        String cron = StringUtils.isNotBlank(config.getCron()) ? config.getCron() : null;

        // 检查配置是否变更
        boolean enabledChanged = currentEnabled == null || !currentEnabled.equals(enabled);
        boolean cronChanged = (cron == null && currentCron != null) ||
                (cron != null && !cron.equals(currentCron));
        boolean configChanged = enabledChanged || cronChanged;

        if (configChanged) {
            log.info("检测到备份配置变更，enabled: {} -> {}, cron: {} -> {}",
                    currentEnabled, enabled, currentCron, cron);
            cancelCurrentTask();
            currentEnabled = enabled;
            currentCron = cron;
            if (enabled && cron != null) {
                scheduleTask();
            } else {
                log.info("定时备份已禁用或 Cron 表达式为空，取消调度");
            }
        }
    }

    /**
     * 调度备份任务
     */
    private void scheduleTask() {
        SysDbBackupScheduleConfigVo config = dbBackupService.getScheduleConfig();
        boolean enabled = config.getEnabled() != null && config.getEnabled();
        String cron = StringUtils.isNotBlank(config.getCron()) ? config.getCron() : null;

        currentEnabled = enabled;
        currentCron = cron;

        if (!enabled) {
            log.info("定时备份未启用（backup.enabled != 0），跳过调度");
            return;
        }

        if (cron == null) {
            log.warn("定时备份已启用，但 Cron 表达式为空，跳过调度");
            return;
        }

        try {
            // 验证 Cron 表达式
            new CronTrigger(cron);

            // 取消旧任务（如果存在）
            cancelCurrentTask();

            // 创建新任务
            scheduledTask = taskScheduler.schedule(this::executeBackup, new CronTrigger(cron));

            log.info("数据库备份定时任务已调度，Cron 表达式: {}", cron);
        } catch (Exception e) {
            log.error("调度数据库备份任务失败，Cron 表达式: {}", cron, e);
        }
    }

    /**
     * 执行备份
     */
    private void executeBackup() {
        log.info("开始执行定时数据库备份...");
        try {
            // 执行备份
            dbBackupService.createBackup("定时备份");
            log.info("定时数据库备份完成");

            // 备份后清理旧备份
            SysDbBackupScheduleConfigVo config = dbBackupService.getScheduleConfig();
            Integer keepCount = config.getKeepCount();
            if (keepCount != null && keepCount > 0) {
                log.info("开始清理旧备份，保留数量: {}", keepCount);
                int deletedCount = dbBackupService.cleanupOldBackups(keepCount);
                if (deletedCount > 0) {
                    log.info("清理旧备份完成，删除了 {} 个备份", deletedCount);
                }
            }
        } catch (Exception e) {
            log.error("执行定时数据库备份时发生异常", e);
        }
    }

    /**
     * 取消当前任务
     */
    private void cancelCurrentTask() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            log.info("已取消旧的数据库备份定时任务");
        }
    }

    /**
     * 应用关闭时清理资源
     */
    @PreDestroy
    public void destroy() {
        cancelCurrentTask();
        log.info("数据库备份定时任务已停止");
    }
}
