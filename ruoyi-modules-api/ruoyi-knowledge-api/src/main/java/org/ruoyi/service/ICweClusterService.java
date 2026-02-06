package org.ruoyi.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.CweClusterBo;
import org.ruoyi.domain.vo.CweClusterVo;
import org.ruoyi.domain.vo.CweClusterMappingVo;

import java.util.List;

/**
 * CWE 聚类Service接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
public interface ICweClusterService {

    CweClusterVo queryById(Long id);

    CweClusterVo queryByClusterIdAndMethod(Integer clusterId, String clusterMethod);

    TableDataInfo<CweClusterVo> queryPageList(CweClusterBo bo, PageQuery pageQuery);

    List<CweClusterVo> queryList(CweClusterBo bo);

    List<CweClusterVo> queryByClusterMethod(String clusterMethod);

    List<CweClusterMappingVo> queryMappingsByCweId(String cweId);

    List<CweClusterMappingVo> queryMappingsByClusterIdAndMethod(Integer clusterId, String clusterMethod);
}
