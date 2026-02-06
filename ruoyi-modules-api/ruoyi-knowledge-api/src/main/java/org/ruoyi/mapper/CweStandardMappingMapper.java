package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.CweStandardMapping;
import org.ruoyi.domain.vo.CweStandardMappingVo;

import java.util.List;

/**
 * CWE 标准分类映射Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Mapper
public interface CweStandardMappingMapper extends BaseMapperPlus<CweStandardMapping, CweStandardMappingVo> {

    List<CweStandardMappingVo> selectByCweId(@Param("cweId") String cweId);

    List<CweStandardMappingVo> selectByTypeCode(@Param("typeCode") String typeCode);
}
