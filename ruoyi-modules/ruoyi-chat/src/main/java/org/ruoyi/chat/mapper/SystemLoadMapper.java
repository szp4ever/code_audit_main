package org.ruoyi.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.chat.domain.vo.SystemLoadItem;

import java.util.List;

/**
 * 系统负载监控Mapper接口
 */
@Mapper
public interface SystemLoadMapper extends BaseMapper<org.ruoyi.chat.domain.SystemLoad> {

    /**
     * 查询系统负载数据
     * @param timeRange 时间范围：1h 或 24h
     * @return 系统负载数据列表
     */
    List<SystemLoadItem> selectSystemLoadByTimeRange(@Param("timeRange") String timeRange);

    /**
     * 插入系统负载数据
     * @param systemLoad 系统负载数据
     * @return 影响行数
     */
    int insertSystemLoad(org.ruoyi.chat.domain.SystemLoad systemLoad);
}