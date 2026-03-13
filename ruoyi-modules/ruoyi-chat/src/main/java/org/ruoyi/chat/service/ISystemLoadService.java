package org.ruoyi.chat.service;

import org.ruoyi.chat.domain.SystemLoad;
import org.ruoyi.chat.domain.vo.SystemLoadItem;

import java.util.List;

/**
 * 系统负载监控Service接口
 */
public interface ISystemLoadService {

    /**
     * 获取系统负载数据
     * @param timeRange 时间范围：1h 或 24h
     * @return 系统负载数据列表
     */
    List<SystemLoadItem> getSystemLoadByTimeRange(String timeRange);

    /**
     * 收集系统负载数据
     */
    void collectSystemLoadData();

    /**
     * 保存系统负载数据
     * @param systemLoad 系统负载数据
     * @return 影响行数
     */
    int saveSystemLoad(SystemLoad systemLoad);
}