package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.CweClusterMapping;
import org.ruoyi.domain.vo.CweClusterMappingVo;

import java.util.List;

/**
 * CWE 聚类映射Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Mapper
public interface CweClusterMappingMapper extends BaseMapperPlus<CweClusterMapping, CweClusterMappingVo> {

    List<CweClusterMappingVo> selectByCweId(@Param("cweId") String cweId);

    List<CweClusterMappingVo> selectByClusterIdAndMethod(@Param("clusterId") Integer clusterId, @Param("clusterMethod") String clusterMethod);
}
