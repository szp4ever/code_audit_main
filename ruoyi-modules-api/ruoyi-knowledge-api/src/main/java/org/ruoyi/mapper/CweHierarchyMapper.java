package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.CweHierarchy;
import org.ruoyi.domain.vo.CweHierarchyVo;

import java.util.List;

/**
 * CWE 层级关系Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Mapper
public interface CweHierarchyMapper extends BaseMapperPlus<CweHierarchy, CweHierarchyVo> {

    List<CweHierarchyVo> selectByCweId(@Param("cweId") String cweId);

    List<CweHierarchyVo> selectByParentCweId(@Param("parentCweId") String parentCweId);
}
