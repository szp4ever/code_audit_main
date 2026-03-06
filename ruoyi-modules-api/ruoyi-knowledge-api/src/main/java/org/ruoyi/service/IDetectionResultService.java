package org.ruoyi.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.DetectionResultBo;
import org.ruoyi.domain.vo.DetectionResultVo;

import java.util.Collection;
import java.util.List;

/**
 * 检测结果Service接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
public interface IDetectionResultService {

    /**
     * 查询检测结果
     */
    DetectionResultVo queryById(Long id);

    /**
     * 根据resultUuid查询检测结果
     */
    DetectionResultVo queryByResultUuid(String resultUuid);

    /**
     * 查询检测结果列表
     */
    TableDataInfo<DetectionResultVo> queryPageList(DetectionResultBo bo, PageQuery pageQuery);

    /**
     * 根据taskId查询检测结果列表
     */
    List<DetectionResultVo> queryByTaskId(Long taskId);

    /**
     * 新增检测结果
     */
    Boolean insertByBo(DetectionResultBo bo);

    /**
     * 修改检测结果
     */
    Boolean updateByBo(DetectionResultBo bo);

    /**
     * 校验并批量删除检测结果信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
