package org.ruoyi.knowledge.cwe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.knowledge.cwe.domain.CweCluster;
import org.ruoyi.knowledge.cwe.domain.vo.CweClusterVo;

import java.util.List;

/**
 * CWE 聚类Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Mapper
public interface CweClusterMapper extends BaseMapperPlus<CweCluster, CweClusterVo> {

    CweCluster selectByClusterIdAndMethod(@Param("clusterId") Integer clusterId, @Param("clusterMethod") String clusterMethod);

    List<CweClusterVo> selectByClusterMethod(@Param("clusterMethod") String clusterMethod);
}
