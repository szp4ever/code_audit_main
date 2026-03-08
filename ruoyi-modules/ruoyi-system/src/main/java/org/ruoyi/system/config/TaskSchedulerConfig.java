package org.ruoyi.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务调度器配置
 * 提供 TaskScheduler bean 用于定时任务调度
 *
 * @author GPT
 */
@Slf4j
@Configuration
public class TaskSchedulerConfig {

    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private final int core = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 创建 TaskScheduler bean
     * 用于执行定时任务和周期性任务
     */
    @Bean
    public TaskScheduler taskScheduler() {
        log.info("====创建任务调度器 TaskScheduler====");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(core);
        scheduler.setThreadNamePrefix("task-scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(30);
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        scheduler.initialize();
        log.info("任务调度器创建完成，线程池大小: {}", core);
        return scheduler;
    }
}
