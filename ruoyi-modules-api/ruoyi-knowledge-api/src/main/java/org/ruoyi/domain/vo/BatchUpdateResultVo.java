package org.ruoyi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 批量更新结果VO
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchUpdateResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成功更新数量
     */
    private Integer successCount;

    /**
     * 失败数量
     */
    private Integer failedCount;

    /**
     * 失败详情列表
     */
    private List<UpdateFailureVo> failures;
}
