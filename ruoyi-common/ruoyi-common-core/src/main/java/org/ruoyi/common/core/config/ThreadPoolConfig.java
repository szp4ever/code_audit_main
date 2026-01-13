package org.ruoyi.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.ruoyi.common.core.config.properties.ThreadPoolProperties;
import org.ruoyi.common.core.utils.Threads;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author Lion Li
 **/
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfig {

    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private final int core = Runtime.getRuntime().availableProcessors() + 1;

    @Bean(name = "threadPoolTaskExecutor")
    @ConditionalOnProperty(prefix = "thread-pool", name = "enabled", havingValue = "true")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolProperties threadPoolProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(core * 2);
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        log.info("====创建定时任务线程池====");
        return new ScheduledThreadPoolExecutor(core,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }

    /**
     * 任务处理专用线程池
     * 用于处理长时间运行的任务，确保任务在后台持续执行
     */
    @Bean(name = "taskProcessingExecutor")
    public ThreadPoolTaskExecutor taskProcessingExecutor() {
        log.info("====创建任务处理线程池====");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：CPU核心数 * 2
        executor.setCorePoolSize(core * 2);
        // 最大线程数：CPU核心数 * 4
        executor.setMaxPoolSize(core * 4);
        // 队列容量：1000
        executor.setQueueCapacity(1000);
        // 线程空闲时间：60秒
        executor.setKeepAliveSeconds(60);
        // 线程名前缀
        executor.setThreadNamePrefix("task-processing-");
        // 拒绝策略：调用者运行（确保任务不会丢失）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间：30秒
        executor.setAwaitTerminationSeconds(30);
        // 初始化
        executor.initialize();
        log.info("任务处理线程池创建完成，核心线程数: {}, 最大线程数: {}", core * 2, core * 4);
        return executor;
    }
}
