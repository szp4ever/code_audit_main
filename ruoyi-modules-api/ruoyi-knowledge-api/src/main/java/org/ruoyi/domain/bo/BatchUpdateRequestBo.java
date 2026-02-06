package org.ruoyi.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 批量更新请求对象
 *
 * @author ruoyi
 * @date 2026-02-05
 */
@Data
public class BatchUpdateRequestBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 要更新的条目UUID列表
     */
    @NotEmpty(message = "itemUuids不能为空")
    private List<String> itemUuids;

    /**
     * 要更新的字段名
     */
    @NotNull(message = "field不能为空")
    private String field;

    /**
     * 要更新的字段值
     */
    @NotNull(message = "value不能为空")
    private Object value;
}
