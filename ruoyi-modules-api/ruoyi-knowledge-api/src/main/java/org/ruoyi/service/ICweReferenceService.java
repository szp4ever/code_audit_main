package org.ruoyi.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.CweReferenceBo;
import org.ruoyi.domain.vo.CweReferenceVo;

import java.util.List;

/**
 * CWE 标准漏洞类型参考Service接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
public interface ICweReferenceService {

    /**
     * 查询CWE参考
     */
    CweReferenceVo queryById(Long id);

    /**
     * 根据cweId查询CWE参考
     */
    CweReferenceVo queryByCweId(String cweId);

    /**
     * 查询CWE参考列表
     */
    TableDataInfo<CweReferenceVo> queryPageList(CweReferenceBo bo, PageQuery pageQuery);

    /**
     * 查询CWE参考列表
     */
    List<CweReferenceVo> queryList(CweReferenceBo bo);
}
