package org.ruoyi.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.common.excel.utils.ExcelUtil;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.system.domain.bo.SysTemplateBo; // 使用 Bo
import org.ruoyi.system.domain.vo.SysTemplateVo; // 使用 Vo
import org.ruoyi.system.service.ISysTemplateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/operator/template")
public class SysTemplateController extends BaseController {

    private final ISysTemplateService sysTemplateService;

    @SaCheckPermission("operator:template:list")
    @GetMapping("/list")
    public TableDataInfo<SysTemplateVo> list(SysTemplateBo bo, PageQuery pageQuery) {
        return sysTemplateService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("operator:template:export")
    @Log(title = "模板配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysTemplateBo bo, HttpServletResponse response) {
        List<SysTemplateVo> list = sysTemplateService.queryList(bo);
        ExcelUtil.exportExcel(list, "模板配置", SysTemplateVo.class, response);
    }

    @SaCheckPermission("operator:template:query")
    @GetMapping("/info/{templateId}")
    public R<SysTemplateVo> getInfo(@PathVariable Long templateId) {
        return R.ok(sysTemplateService.queryById(templateId));
    }

    @SaCheckPermission("operator:template:add")
    @Log(title = "模板配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysTemplateBo bo) {
        return toAjax(sysTemplateService.insertByBo(bo));
    }

    @SaCheckPermission("operator:template:edit")
    @Log(title = "模板配置", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<Void> update(@Validated(EditGroup.class) @RequestBody SysTemplateBo bo) {
        return toAjax(sysTemplateService.updateByBo(bo));
    }

    @SaCheckPermission("operator:template:remove")
    @Log(title = "模板配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{templateIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] templateIds) {
        return toAjax(sysTemplateService.deleteWithValidByIds(Arrays.asList(templateIds), true));
    }
}