package org.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.system.domain.SysOperLog;
import org.ruoyi.system.domain.vo.SysOperLogVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作日志 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysOperLogMapper extends BaseMapperPlus<SysOperLog, SysOperLogVo> {

    /**
     * 查询用户操作热力图数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志原始数据（包含 oper_url 和 oper_time）
     */
    List<Map<String, Object>> selectUserOperationHeatmapData(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );
}
