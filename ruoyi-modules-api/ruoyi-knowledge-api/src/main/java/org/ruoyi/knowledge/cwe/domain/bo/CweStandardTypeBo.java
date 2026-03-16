package org.ruoyi.knowledge.cwe.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.knowledge.cwe.domain.CweStandardType;

/**
 * CWE 标准分类类型业务对象 cwe_standard_type
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CweStandardTypeBo extends BaseEntity {

    private Long id;

    private String typeCode;

    private String typeName;

    private String version;
}
