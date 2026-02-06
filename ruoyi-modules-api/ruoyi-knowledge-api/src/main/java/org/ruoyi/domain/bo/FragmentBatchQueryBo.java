package org.ruoyi.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 批量查询片段请求对象
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Data
public class FragmentBatchQueryBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文档ID
     */
    private String docId;

    /**
     * 片段索引
     */
    private Integer idx;
}
