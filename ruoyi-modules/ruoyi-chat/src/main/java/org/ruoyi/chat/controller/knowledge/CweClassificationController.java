package org.ruoyi.chat.controller.knowledge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.CweStandardTypeBo;
import org.ruoyi.domain.vo.CweStandardTypeVo;
import org.ruoyi.domain.vo.CweStandardMappingVo;
import org.ruoyi.domain.vo.CweImpactMappingVo;
import org.ruoyi.domain.vo.CweHierarchyVo;
import org.ruoyi.service.ICweClassificationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CWE 分类Controller
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/cwe/classification")
@Tag(name = "CWE分类管理", description = "CWE分类相关接口")
public class CweClassificationController extends BaseController {

    private final ICweClassificationService cweClassificationService;

    @Operation(summary = "查询标准分类类型列表")
    @GetMapping("/standard-type/list")
    public TableDataInfo<CweStandardTypeVo> listStandardType(CweStandardTypeBo bo, PageQuery pageQuery) {
        return cweClassificationService.queryStandardTypePageList(bo, pageQuery);
    }

    @Operation(summary = "查询标准分类类型列表（不分页）")
    @GetMapping("/standard-type/list/all")
    public R<List<CweStandardTypeVo>> listAllStandardType(CweStandardTypeBo bo) {
        return R.ok(cweClassificationService.queryStandardTypeList(bo));
    }

    @Operation(summary = "获取标准分类类型详细信息")
    @GetMapping("/standard-type/{id}")
    public R<CweStandardTypeVo> getStandardTypeInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(cweClassificationService.queryStandardTypeById(id));
    }

    @Operation(summary = "根据typeCode获取标准分类类型详细信息")
    @GetMapping("/standard-type/code/{typeCode}")
    public R<CweStandardTypeVo> getStandardTypeInfoByCode(@NotEmpty(message = "typeCode不能为空") @PathVariable("typeCode") String typeCode) {
        return R.ok(cweClassificationService.queryStandardTypeByTypeCode(typeCode));
    }

    @Operation(summary = "根据cweId查询标准分类映射")
    @GetMapping("/standard-mapping/cwe/{cweId}")
    public R<List<CweStandardMappingVo>> getStandardMappingsByCweId(@NotEmpty(message = "cweId不能为空") @PathVariable("cweId") String cweId) {
        return R.ok(cweClassificationService.queryStandardMappingsByCweId(cweId));
    }

    @Operation(summary = "根据typeCode查询标准分类映射")
    @GetMapping("/standard-mapping/type/{typeCode}")
    public R<List<CweStandardMappingVo>> getStandardMappingsByTypeCode(@NotEmpty(message = "typeCode不能为空") @PathVariable("typeCode") String typeCode) {
        return R.ok(cweClassificationService.queryStandardMappingsByTypeCode(typeCode));
    }

    @Operation(summary = "根据cweId查询影响类型映射")
    @GetMapping("/impact-mapping/cwe/{cweId}")
    public R<List<CweImpactMappingVo>> getImpactMappingsByCweId(@NotEmpty(message = "cweId不能为空") @PathVariable("cweId") String cweId) {
        return R.ok(cweClassificationService.queryImpactMappingsByCweId(cweId));
    }

    @Operation(summary = "根据impactType查询影响类型映射")
    @GetMapping("/impact-mapping/type/{impactType}")
    public R<List<CweImpactMappingVo>> getImpactMappingsByImpactType(@NotEmpty(message = "impactType不能为空") @PathVariable("impactType") String impactType) {
        return R.ok(cweClassificationService.queryImpactMappingsByImpactType(impactType));
    }

    @Operation(summary = "根据cweId查询层级关系")
    @GetMapping("/hierarchy/cwe/{cweId}")
    public R<List<CweHierarchyVo>> getHierarchyByCweId(@NotEmpty(message = "cweId不能为空") @PathVariable("cweId") String cweId) {
        return R.ok(cweClassificationService.queryHierarchyByCweId(cweId));
    }

    @Operation(summary = "根据parentCweId查询层级关系")
    @GetMapping("/hierarchy/parent/{parentCweId}")
    public R<List<CweHierarchyVo>> getHierarchyByParentCweId(@NotEmpty(message = "parentCweId不能为空") @PathVariable("parentCweId") String parentCweId) {
        return R.ok(cweClassificationService.queryHierarchyByParentCweId(parentCweId));
    }
}
