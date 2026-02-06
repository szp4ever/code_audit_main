package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.CweReference;
import org.ruoyi.domain.bo.CweReferenceBo;
import org.ruoyi.domain.vo.CweReferenceVo;
import org.ruoyi.mapper.CweReferenceMapper;
import org.ruoyi.service.ICweReferenceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CWE 标准漏洞类型参考Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class CweReferenceServiceImpl implements ICweReferenceService {

    private final CweReferenceMapper baseMapper;

    @Override
    public CweReferenceVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public CweReferenceVo queryByCweId(String cweId) {
        CweReference entity = baseMapper.selectByCweId(cweId);
        if (entity == null) {
            return null;
        }
        return baseMapper.selectVoById(entity.getId());
    }

    @Override
    public TableDataInfo<CweReferenceVo> queryPageList(CweReferenceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CweReference> lqw = buildQueryWrapper(bo);
        Page<CweReferenceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<CweReferenceVo> queryList(CweReferenceBo bo) {
        LambdaQueryWrapper<CweReference> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CweReference> buildQueryWrapper(CweReferenceBo bo) {
        LambdaQueryWrapper<CweReference> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCweId()), CweReference::getCweId, bo.getCweId());
        lqw.like(StringUtils.isNotBlank(bo.getNameEn()), CweReference::getNameEn, bo.getNameEn());
        lqw.like(StringUtils.isNotBlank(bo.getNameZh()), CweReference::getNameZh, bo.getNameZh());
        return lqw;
    }
}
