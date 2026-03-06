package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.DetectionResult;
import org.ruoyi.domain.bo.DetectionResultBo;
import org.ruoyi.domain.vo.DetectionResultVo;
import org.ruoyi.mapper.DetectionResultMapper;
import org.ruoyi.service.IDetectionResultService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 检测结果Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class DetectionResultServiceImpl implements IDetectionResultService {

    private final DetectionResultMapper baseMapper;

    @Override
    public DetectionResultVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public DetectionResultVo queryByResultUuid(String resultUuid) {
        DetectionResult entity = baseMapper.selectByResultUuid(resultUuid);
        if (entity == null) {
            return null;
        }
        return baseMapper.selectVoById(entity.getId());
    }

    @Override
    public TableDataInfo<DetectionResultVo> queryPageList(DetectionResultBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DetectionResult> lqw = buildQueryWrapper(bo);
        Page<DetectionResultVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<DetectionResultVo> queryByTaskId(Long taskId) {
        return baseMapper.selectByTaskId(taskId);
    }

    private LambdaQueryWrapper<DetectionResult> buildQueryWrapper(DetectionResultBo bo) {
        LambdaQueryWrapper<DetectionResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getTaskId() != null, DetectionResult::getTaskId, bo.getTaskId());
        lqw.eq(bo.getProjectId() != null, DetectionResult::getProjectId, bo.getProjectId());
        lqw.eq(StringUtils.isNotBlank(bo.getRuleId()), DetectionResult::getRuleId, bo.getRuleId());
        lqw.eq(StringUtils.isNotBlank(bo.getVulnerabilityType()), DetectionResult::getVulnerabilityType, bo.getVulnerabilityType());
        lqw.eq(StringUtils.isNotBlank(bo.getSeverity()), DetectionResult::getSeverity, bo.getSeverity());
        lqw.eq(StringUtils.isNotBlank(bo.getLanguage()), DetectionResult::getLanguage, bo.getLanguage());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), DetectionResult::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getFilePath()), DetectionResult::getFilePath, bo.getFilePath());
        return lqw;
    }

    @Override
    public Boolean insertByBo(DetectionResultBo bo) {
        DetectionResult add = MapstructUtils.convert(bo, DetectionResult.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(DetectionResultBo bo) {
        DetectionResult update = MapstructUtils.convert(bo, DetectionResult.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void validEntityBeforeSave(DetectionResult entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
