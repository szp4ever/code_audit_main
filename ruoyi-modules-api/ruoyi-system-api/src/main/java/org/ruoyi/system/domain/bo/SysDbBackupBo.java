package org.ruoyi.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.system.domain.SysDbBackup;

import java.util.Date;

/**
 * 数据库备份查询参数（业务对象）
 *
 * 主要用于 /system/backup/list 的分页 + 筛选 + 排序
 *
 * @author GPT
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysDbBackup.class, reverseConvertGenerate = false)
public class SysDbBackupBo extends BaseEntity {

    /**
     * 备份ID
     */
    private Long backupId;

    /**
     * 备份文件名（模糊查询）
     */
    private String fileName;

    /**
     * 备份状态（0失败 1成功，对齐 SYS_COMMON_STATUS）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 备份时间-开始（用于时间范围筛选）
     */
    private Date beginBackupTime;

    /**
     * 备份时间-结束（用于时间范围筛选）
     */
    private Date endBackupTime;
}

