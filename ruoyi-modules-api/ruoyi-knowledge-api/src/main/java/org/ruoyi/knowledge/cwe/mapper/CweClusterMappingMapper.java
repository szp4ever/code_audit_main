package org.ruoyi.knowledge.cwe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.knowledge.cwe.domain.CweClusterMapping;
import org.ruoyi.knowledge.cwe.domain.vo.CweClusterMappingVo;

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
