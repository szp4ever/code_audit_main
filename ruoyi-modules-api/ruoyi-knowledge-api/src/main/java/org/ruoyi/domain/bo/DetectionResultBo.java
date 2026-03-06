package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.DetectionResult;

import java.math.BigDecimal;

/**
 * 检测结果业务对象 detection_result
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DetectionResult.class, reverseConvertGenerate = false)
public class DetectionResultBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 检测结果UUID
     */
    private String resultUuid;

    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空", groups = {AddGroup.class})
    private Long taskId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 规则ID
     */
    @NotBlank(message = "规则ID不能为空", groups = {AddGroup.class})
    private String ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 漏洞类型
     */
    private String vulnerabilityType;

    /**
     * 风险等级
     */
    private String severity;

    /**
     * CVSS 向量字符串
     */
    private String cvssVector;

    /**
     * CVSS 数值分数
     */
    private BigDecimal cvssScore;

    /**
     * CVSS 版本号
     */
    private String cvssVersion;

    /**
     * 代码语言
     */
    private String language;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 状态
     */
    private String status;
}
