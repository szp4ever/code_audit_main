package org.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 密码规则视图对象
 *
 * @author system
 */
@Data
public class PasswordRuleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 最小密码长度
     */
    private Integer minLength;

    /**
     * 是否需要特殊字符（0表示需要，1表示不需要）
     */
    private Integer requireSpecialChar;

}
