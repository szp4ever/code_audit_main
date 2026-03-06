package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.CvssSeverityMapping;
import org.ruoyi.domain.CvssSeverityMapping;

import java.math.BigDecimal;

/**
 * CVSS 严重性等级映射Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface CvssSeverityMappingMapper extends BaseMapperPlus<CvssSeverityMapping, CvssSeverityMapping> {

    /**
     * 根据CVSS分数查询严重性等级
     *
     * @param score CVSS分数
     * @param cvssVersion CVSS版本号
     * @return CvssSeverityMapping
     */
    CvssSeverityMapping selectByScore(@Param("score") BigDecimal score, @Param("cvssVersion") String cvssVersion);
}
