package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.CweReference;
import org.ruoyi.domain.vo.CweReferenceVo;

/**
 * CWE 标准漏洞类型参考Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface CweReferenceMapper extends BaseMapperPlus<CweReference, CweReferenceVo> {

    /**
     * 根据cweId查询CWE参考
     *
     * @param cweId CWE编号
     * @return CweReference
     */
    CweReference selectByCweId(@Param("cweId") String cweId);
}
