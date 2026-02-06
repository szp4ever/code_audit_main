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
import org.ruoyi.domain.bo.CweClusterBo;
import org.ruoyi.domain.vo.CweClusterVo;
import org.ruoyi.domain.vo.CweClusterMappingVo;
import org.ruoyi.service.ICweClusterService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CWE 聚类Controller
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/cwe/cluster")
@Tag(name = "CWE聚类管理", description = "CWE聚类相关接口")
public class CweClusterController extends BaseController {

    private final ICweClusterService cweClusterService;

    @Operation(summary = "查询CWE聚类列表")
    @GetMapping("/list")
    public TableDataInfo<CweClusterVo> list(CweClusterBo bo, PageQuery pageQuery) {
        return cweClusterService.queryPageList(bo, pageQuery);
    }

    @Operation(summary = "查询CWE聚类列表（不分页）")
    @GetMapping("/list/all")
    public R<List<CweClusterVo>> listAll(CweClusterBo bo) {
        return R.ok(cweClusterService.queryList(bo));
    }

    @Operation(summary = "根据聚类方法查询CWE聚类列表")
    @GetMapping("/method/{clusterMethod}")
    public R<List<CweClusterVo>> listByMethod(@NotEmpty(message = "clusterMethod不能为空") @PathVariable("clusterMethod") String clusterMethod) {
        return R.ok(cweClusterService.queryByClusterMethod(clusterMethod));
    }

    @Operation(summary = "获取CWE聚类详细信息")
    @GetMapping("/{id}")
    public R<CweClusterVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(cweClusterService.queryById(id));
    }

    @Operation(summary = "根据clusterId和clusterMethod获取CWE聚类详细信息")
    @GetMapping("/{clusterId}/{clusterMethod}")
    public R<CweClusterVo> getInfoByClusterIdAndMethod(@NotNull(message = "clusterId不能为空") @PathVariable("clusterId") Integer clusterId,
                                                        @NotEmpty(message = "clusterMethod不能为空") @PathVariable("clusterMethod") String clusterMethod) {
        return R.ok(cweClusterService.queryByClusterIdAndMethod(clusterId, clusterMethod));
    }

    @Operation(summary = "根据cweId查询聚类映射")
    @GetMapping("/mapping/cwe/{cweId}")
    public R<List<CweClusterMappingVo>> getMappingsByCweId(@NotEmpty(message = "cweId不能为空") @PathVariable("cweId") String cweId) {
        return R.ok(cweClusterService.queryMappingsByCweId(cweId));
    }

    @Operation(summary = "根据clusterId和clusterMethod查询聚类映射")
    @GetMapping("/mapping/cluster/{clusterId}/{clusterMethod}")
    public R<List<CweClusterMappingVo>> getMappingsByClusterIdAndMethod(@NotNull(message = "clusterId不能为空") @PathVariable("clusterId") Integer clusterId,
                                                                         @NotEmpty(message = "clusterMethod不能为空") @PathVariable("clusterMethod") String clusterMethod) {
        return R.ok(cweClusterService.queryMappingsByClusterIdAndMethod(clusterId, clusterMethod));
    }
}
