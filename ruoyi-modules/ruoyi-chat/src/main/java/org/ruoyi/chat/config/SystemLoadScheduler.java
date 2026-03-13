package org.ruoyi.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.service.ISystemLoadService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 系统负载监控定时任务
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemLoadScheduler {

    private final ISystemLoadService systemLoadService;

    /**
     * 每5分钟收集一次系统负载数据
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void collectSystemLoadData() {
        log.info("开始收集系统负载数据");
        systemLoadService.collectSystemLoadData();
        log.info("系统负载数据收集完成");
    }
}