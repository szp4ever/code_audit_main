package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.CweReference;

/**
 * CWE 标准漏洞类型参考业务对象 cwe_reference
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CweReference.class, reverseConvertGenerate = false)
public class CweReferenceBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * CWE 编号
     */
    private String cweId;

    /**
     * 英文名称
     */
    private String nameEn;

    /**
     * 中文名称
     */
    private String nameZh;
}
