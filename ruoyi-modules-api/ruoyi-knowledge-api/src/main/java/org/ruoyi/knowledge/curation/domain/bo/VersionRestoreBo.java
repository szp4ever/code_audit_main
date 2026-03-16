package org.ruoyi.knowledge.curation.domain.bo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 版本恢复请求
 */
@Data
public class VersionRestoreBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 恢复原因（审计用）
     */
    @NotBlank(message = "恢复原因不能为空")
    private String reason;
}
