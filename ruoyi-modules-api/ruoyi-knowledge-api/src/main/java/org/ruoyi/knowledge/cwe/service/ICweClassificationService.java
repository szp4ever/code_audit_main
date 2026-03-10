package org.ruoyi.knowledge.cwe.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.cwe.domain.bo.CweStandardTypeBo;
import org.ruoyi.knowledge.cwe.domain.vo.CweStandardTypeVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweStandardMappingVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweImpactMappingVo;
import org.ruoyi.knowledge.cwe.domain.vo.CweHierarchyVo;

import java.util.List;

/**
 * CWE 分类Service接口
 *
 * @author ruoyi
 * @date 2026-01-15
 */
public interface ICweClassificationService {

    CweStandardTypeVo queryStandardTypeById(Long id);

    CweStandardTypeVo queryStandardTypeByTypeCode(String typeCode);

    TableDataInfo<CweStandardTypeVo> queryStandardTypePageList(CweStandardTypeBo bo, PageQuery pageQuery);

    List<CweStandardTypeVo> queryStandardTypeList(CweStandardTypeBo bo);

    List<CweStandardMappingVo> queryStandardMappingsByCweId(String cweId);

    List<CweStandardMappingVo> queryStandardMappingsByTypeCode(String typeCode);

    List<CweImpactMappingVo> queryImpactMappingsByCweId(String cweId);

    List<CweImpactMappingVo> queryImpactMappingsByImpactType(String impactType);

    List<CweHierarchyVo> queryHierarchyByCweId(String cweId);

    List<CweHierarchyVo> queryHierarchyByParentCweId(String parentCweId);
}
