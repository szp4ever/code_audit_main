package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.DetectionResult;
import org.ruoyi.domain.vo.DetectionResultVo;

import java.util.List;

/**
 * 检测结果Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface DetectionResultMapper extends BaseMapperPlus<DetectionResult, DetectionResultVo> {

    /**
     * 根据resultUuid查询检测结果
     *
     * @param resultUuid 检测结果UUID
     * @return DetectionResult
     */
    DetectionResult selectByResultUuid(@Param("resultUuid") String resultUuid);

    /**
     * 根据taskId查询检测结果列表
     *
     * @param taskId 任务ID
     * @return 检测结果列表
     */
    List<DetectionResultVo> selectByTaskId(@Param("taskId") Long taskId);
}
