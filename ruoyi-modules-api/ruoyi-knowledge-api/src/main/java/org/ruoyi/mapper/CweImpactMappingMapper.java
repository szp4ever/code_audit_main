package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.CweImpactMapping;
import org.ruoyi.domain.vo.CweImpactMappingVo;

import java.util.List;

/**
 * CWE 影响类型映射Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Mapper
public interface CweImpactMappingMapper extends BaseMapperPlus<CweImpactMapping, CweImpactMappingVo> {

    List<CweImpactMappingVo> selectByCweId(@Param("cweId") String cweId);

    List<CweImpactMappingVo> selectByImpactType(@Param("impactType") String impactType);
}
