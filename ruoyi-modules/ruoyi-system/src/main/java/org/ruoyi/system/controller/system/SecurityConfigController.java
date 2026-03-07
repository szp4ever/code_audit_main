package org.ruoyi.system.controller.system;

import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.system.domain.bo.SecurityConfigBo;
import org.ruoyi.system.service.ISysConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 安全配置 信息操作处理
 *
 * @author system
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/security/config")
public class SecurityConfigController extends BaseController {

    private final ISysConfigService configService;

    /**
     * 获取安全配置
     *
     * @return 安全配置信息
     */
    @GetMapping
    public R<SecurityConfigBo> getConfig() {
        SecurityConfigBo config = configService.getSecurityConfig();
        return R.ok(config);
    }

    /**
     * 更新安全配置
     *
     * @param bo 安全配置信息
     * @return 结果
     */
    @Log(title = "安全配置", businessType = BusinessType.UPDATE)
    @PostMapping
    public R<Void> updateConfig(@RequestBody SecurityConfigBo bo) {
        configService.updateSecurityConfig(bo);
        return R.ok();
    }

}
