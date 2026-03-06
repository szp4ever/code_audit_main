package org.ruoyi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 删除失败详情VO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFailureVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 条目UUID
     */
    private String itemUuid;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 错误代码
     */
    private String errorCode;
}
