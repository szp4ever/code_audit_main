package org.ruoyi.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.common.excel.annotation.ExcelDictFormat;
import org.ruoyi.common.excel.convert.ExcelDictConvert;
import org.ruoyi.system.domain.SysTemplate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysTemplate.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SysTemplateVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "模板ID")
    private Long templateId;

    // --- 以下是补全的字段 ---

    @ExcelProperty(value = "模板名称")
    private String templateName;

    @ExcelProperty(value = "模板编码")
    private String templateCode;

    @ExcelProperty(value = "模板内容")
    private String templateContent;

    @ExcelProperty(value = "模板类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=文本,2=消息通知")
    private String templateType;

    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;

    @ExcelProperty(value = "备注")
    private String remark;

    // --- 补全结束 ---

    @ExcelProperty(value = "创建者")
    private Long createBy;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @ExcelProperty(value = "更新者")
    private Long updateBy;

    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    @ExcelProperty(value = "文件路径")
    private String filePath; // 必须有

    @ExcelProperty(value = "文件种类")
    private String fileKind; // 必须有
}