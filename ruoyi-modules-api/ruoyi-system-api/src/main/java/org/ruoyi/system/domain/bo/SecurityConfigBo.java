package org.ruoyi.system.domain.bo;

import lombok.Data;

/**
 * 安全配置业务对象
 *
 * @author system
 */
@Data
public class SecurityConfigBo {

    /**
     * 白名单IP，多个用逗号分隔
     */
    private String whitelistIps;

    /**
     * 空闲超时分钟数
     */
    private Integer idleTimeoutMinutes;

    /**
     * 密码最小长度
     */
    private Integer passwordMinLength;

    /**
     * 密码是否需要特殊字符（0表示需要，1表示不需要）
     */
    private Integer passwordRequireSpecial;

}
