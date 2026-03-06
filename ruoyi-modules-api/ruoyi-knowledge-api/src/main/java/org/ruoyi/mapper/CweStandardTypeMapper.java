package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.CweStandardType;
import org.ruoyi.domain.vo.CweStandardTypeVo;

/**
 * CWE 标准分类类型Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Mapper
public interface CweStandardTypeMapper extends BaseMapperPlus<CweStandardType, CweStandardTypeVo> {

    CweStandardType selectByTypeCode(@Param("typeCode") String typeCode);
}
