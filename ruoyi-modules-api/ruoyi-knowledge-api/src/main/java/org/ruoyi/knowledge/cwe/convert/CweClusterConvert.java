package org.ruoyi.knowledge.cwe.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.cwe.domain.CweCluster;
import org.ruoyi.knowledge.cwe.domain.bo.CweClusterBo;
import org.ruoyi.knowledge.cwe.domain.vo.CweClusterVo;

@Mapper(componentModel = "spring")
public interface CweClusterConvert {
    CweCluster toEntity(CweClusterBo bo);
    CweClusterVo toVo(CweCluster entity);
    CweCluster voToEntity(CweClusterVo vo);
}
