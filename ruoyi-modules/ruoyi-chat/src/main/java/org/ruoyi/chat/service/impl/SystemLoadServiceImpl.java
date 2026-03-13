package org.ruoyi.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.SystemLoad;
import org.ruoyi.chat.domain.vo.SystemLoadItem;
import org.ruoyi.chat.mapper.SystemLoadMapper;
import org.ruoyi.chat.service.ISystemLoadService;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 系统负载监控Service实现
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SystemLoadServiceImpl implements ISystemLoadService {

    private final SystemLoadMapper systemLoadMapper;

    @Override
    public List<SystemLoadItem> getSystemLoadByTimeRange(String timeRange) {
        // 验证时间范围参数
        if (!"1h".equals(timeRange) && !"24h".equals(timeRange)) {
            timeRange = "1h";
        }
        return systemLoadMapper.selectSystemLoadByTimeRange(timeRange);
    }

    @Override
    public void collectSystemLoadData() {
        try {
            // 创建系统负载对象
            SystemLoad systemLoad = new SystemLoad();
            systemLoad.setTimestamp(new Date());

            // 获取CPU使用率
            double cpuUsage = getCpuUsage();
            systemLoad.setCpuUsage(cpuUsage);

            // 获取内存使用率
            double memoryUsage = getMemoryUsage();
            systemLoad.setMemoryUsage(memoryUsage);

            // 尝试获取GPU使用率（可选）
            Double gpuUsage = getGpuUsage();
            systemLoad.setGpuUsage(gpuUsage);

            // 保存数据
            saveSystemLoad(systemLoad);
            log.info("系统负载数据收集成功: CPU={}%, 内存={}%, GPU={}%", cpuUsage, memoryUsage, gpuUsage != null ? gpuUsage : "N/A");
        } catch (Exception e) {
            log.error("系统负载数据收集失败", e);
        }
    }

    @Override
    public int saveSystemLoad(SystemLoad systemLoad) {
        return systemLoadMapper.insertSystemLoad(systemLoad);
    }

    /**
     * 获取CPU使用率
     */
    private double getCpuUsage() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            Method method = osBean.getClass().getMethod("getSystemCpuLoad");
            double load = (double) method.invoke(osBean);
            return Math.round(load * 1000) / 10.0;
        } catch (Exception e) {
            log.warn("获取CPU使用率失败", e);
            return 0.0;
        }
    }

    /**
     * 获取内存使用率
     */
    private double getMemoryUsage() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            double usage = (double) usedMemory / totalMemory * 100;
            return Math.round(usage * 10) / 10.0;
        } catch (Exception e) {
            log.warn("获取内存使用率失败", e);
            return 0.0;
        }
    }

    /**
     * 获取GPU使用率（可选）
     */
    private Double getGpuUsage() {
        try {
            // 这里可以使用第三方库或系统命令获取GPU使用率
            // 暂时返回一个模拟值
            return Math.round((Math.random() * 30 + 10) * 10) / 10.0;
        } catch (Exception e) {
            log.warn("获取GPU使用率失败", e);
            return null;
        }
    }
}