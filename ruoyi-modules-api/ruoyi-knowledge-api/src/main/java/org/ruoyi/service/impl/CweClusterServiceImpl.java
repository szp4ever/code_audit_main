package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.CweCluster;
import org.ruoyi.domain.bo.CweClusterBo;
import org.ruoyi.domain.vo.CweClusterVo;
import org.ruoyi.domain.vo.CweClusterMappingVo;
import org.ruoyi.mapper.CweClusterMapper;
import org.ruoyi.mapper.CweClusterMappingMapper;
import org.ruoyi.service.ICweClusterService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CWE 聚类Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class CweClusterServiceImpl implements ICweClusterService {

    private final CweClusterMapper baseMapper;
    private final CweClusterMappingMapper clusterMappingMapper;

    @Override
    public CweClusterVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public CweClusterVo queryByClusterIdAndMethod(Integer clusterId, String clusterMethod) {
        CweCluster entity = baseMapper.selectByClusterIdAndMethod(clusterId, clusterMethod);
        if (entity == null) {
            return null;
        }
        return baseMapper.selectVoById(entity.getId());
    }

    @Override
    public TableDataInfo<CweClusterVo> queryPageList(CweClusterBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CweCluster> lqw = buildQueryWrapper(bo);
        Page<CweClusterVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<CweClusterVo> queryList(CweClusterBo bo) {
        LambdaQueryWrapper<CweCluster> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public List<CweClusterVo> queryByClusterMethod(String clusterMethod) {
        return baseMapper.selectByClusterMethod(clusterMethod);
    }

    @Override
    public List<CweClusterMappingVo> queryMappingsByCweId(String cweId) {
        return clusterMappingMapper.selectByCweId(cweId);
    }

    @Override
    public List<CweClusterMappingVo> queryMappingsByClusterIdAndMethod(Integer clusterId, String clusterMethod) {
        return clusterMappingMapper.selectByClusterIdAndMethod(clusterId, clusterMethod);
    }

    private LambdaQueryWrapper<CweCluster> buildQueryWrapper(CweClusterBo bo) {
        LambdaQueryWrapper<CweCluster> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getClusterId() != null, CweCluster::getClusterId, bo.getClusterId());
        lqw.eq(StringUtils.isNotBlank(bo.getClusterMethod()), CweCluster::getClusterMethod, bo.getClusterMethod());
        lqw.like(StringUtils.isNotBlank(bo.getClusterNameZh()), CweCluster::getClusterNameZh, bo.getClusterNameZh());
        lqw.like(StringUtils.isNotBlank(bo.getClusterNameEn()), CweCluster::getClusterNameEn, bo.getClusterNameEn());
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryCode()), CweCluster::getCategoryCode, bo.getCategoryCode());
        return lqw;
    }
}
