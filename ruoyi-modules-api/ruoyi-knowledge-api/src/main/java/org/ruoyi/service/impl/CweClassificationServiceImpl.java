package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.CweStandardType;
import org.ruoyi.domain.bo.CweStandardTypeBo;
import org.ruoyi.domain.vo.CweStandardTypeVo;
import org.ruoyi.domain.vo.CweStandardMappingVo;
import org.ruoyi.domain.vo.CweImpactMappingVo;
import org.ruoyi.domain.vo.CweHierarchyVo;
import org.ruoyi.mapper.CweStandardTypeMapper;
import org.ruoyi.mapper.CweStandardMappingMapper;
import org.ruoyi.mapper.CweImpactMappingMapper;
import org.ruoyi.mapper.CweHierarchyMapper;
import org.ruoyi.service.ICweClassificationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CWE 分类Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class CweClassificationServiceImpl implements ICweClassificationService {

    private final CweStandardTypeMapper standardTypeMapper;
    private final CweStandardMappingMapper standardMappingMapper;
    private final CweImpactMappingMapper impactMappingMapper;
    private final CweHierarchyMapper hierarchyMapper;

    @Override
    public CweStandardTypeVo queryStandardTypeById(Long id) {
        return standardTypeMapper.selectVoById(id);
    }

    @Override
    public CweStandardTypeVo queryStandardTypeByTypeCode(String typeCode) {
        CweStandardType entity = standardTypeMapper.selectByTypeCode(typeCode);
        if (entity == null) {
            return null;
        }
        return standardTypeMapper.selectVoById(entity.getId());
    }

    @Override
    public TableDataInfo<CweStandardTypeVo> queryStandardTypePageList(CweStandardTypeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CweStandardType> lqw = buildQueryWrapper(bo);
        Page<CweStandardTypeVo> result = standardTypeMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<CweStandardTypeVo> queryStandardTypeList(CweStandardTypeBo bo) {
        LambdaQueryWrapper<CweStandardType> lqw = buildQueryWrapper(bo);
        return standardTypeMapper.selectVoList(lqw);
    }

    @Override
    public List<CweStandardMappingVo> queryStandardMappingsByCweId(String cweId) {
        return standardMappingMapper.selectByCweId(cweId);
    }

    @Override
    public List<CweStandardMappingVo> queryStandardMappingsByTypeCode(String typeCode) {
        return standardMappingMapper.selectByTypeCode(typeCode);
    }

    @Override
    public List<CweImpactMappingVo> queryImpactMappingsByCweId(String cweId) {
        return impactMappingMapper.selectByCweId(cweId);
    }

    @Override
    public List<CweImpactMappingVo> queryImpactMappingsByImpactType(String impactType) {
        return impactMappingMapper.selectByImpactType(impactType);
    }

    @Override
    public List<CweHierarchyVo> queryHierarchyByCweId(String cweId) {
        return hierarchyMapper.selectByCweId(cweId);
    }

    @Override
    public List<CweHierarchyVo> queryHierarchyByParentCweId(String parentCweId) {
        return hierarchyMapper.selectByParentCweId(parentCweId);
    }

    private LambdaQueryWrapper<CweStandardType> buildQueryWrapper(CweStandardTypeBo bo) {
        LambdaQueryWrapper<CweStandardType> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTypeCode()), CweStandardType::getTypeCode, bo.getTypeCode());
        lqw.like(StringUtils.isNotBlank(bo.getTypeName()), CweStandardType::getTypeName, bo.getTypeName());
        lqw.eq(StringUtils.isNotBlank(bo.getVersion()), CweStandardType::getVersion, bo.getVersion());
        return lqw;
    }
}
