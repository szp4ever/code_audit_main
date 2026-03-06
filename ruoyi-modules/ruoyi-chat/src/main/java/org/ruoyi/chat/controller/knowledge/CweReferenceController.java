package org.ruoyi.chat.controller.knowledge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.CweReferenceBo;
import org.ruoyi.domain.vo.CweReferenceVo;
import org.ruoyi.service.ICweReferenceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CWE 标准漏洞类型参考Controller
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/cwe")
@Tag(name = "CWE标准参考管理", description = "CWE标准参考相关接口")
public class CweReferenceController extends BaseController {

    private final ICweReferenceService cweReferenceService;

    /**
     * 查询CWE参考列表
     */
    @Operation(summary = "查询CWE参考列表")
    @GetMapping("/list")
    public TableDataInfo<CweReferenceVo> list(CweReferenceBo bo, PageQuery pageQuery) {
        return cweReferenceService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询CWE参考列表（不分页）
     */
    @Operation(summary = "查询CWE参考列表（不分页）")
    @GetMapping("/list/all")
    public R<List<CweReferenceVo>> listAll(CweReferenceBo bo) {
        return R.ok(cweReferenceService.queryList(bo));
    }

    /**
     * 获取CWE参考详细信息
     */
    @Operation(summary = "获取CWE参考详细信息")
    @GetMapping("/{id}")
    public R<CweReferenceVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(cweReferenceService.queryById(id));
    }

    /**
     * 根据cweId获取CWE参考详细信息
     */
    @Operation(summary = "根据cweId获取CWE参考详细信息")
    @GetMapping("/cweId/{cweId}")
    public R<CweReferenceVo> getInfoByCweId(@NotEmpty(message = "cweId不能为空") @PathVariable("cweId") String cweId) {
        return R.ok(cweReferenceService.queryByCweId(cweId));
    }
}
