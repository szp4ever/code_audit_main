package org.ruoyi.system.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.system.domain.SysDbBackup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据库备份视图对象（BackupRecord）
 *
 * 用于接口响应：
 * - /system/backup/list => TableDataInfo<SysDbBackupVo>
 * - /system/backup/create => R<SysDbBackupVo>
 *
 * @author GPT
 */
@Data
@AutoMapper(target = SysDbBackup.class)
public class SysDbBackupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 备份ID
     */
    private Long backupId;

    /**
     * 备份文件名
     */
    private String fileName;

    /**
     * 文件物理路径
     */
    private String filePath;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 备份完成时间
     */
    private Date backupTime;

    /**
     * 备份状态（0失败 1成功），对齐 SYS_COMMON_STATUS
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者（用户名）
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者（用户名）
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}

