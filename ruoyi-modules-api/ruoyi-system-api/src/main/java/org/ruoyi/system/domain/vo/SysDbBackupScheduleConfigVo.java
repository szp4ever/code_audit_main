package org.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 定时备份配置视图对象
 *
 * 用于接口响应：
 * - GET /system/backup/schedule/config => R<SysDbBackupScheduleConfigVo>
 *
 * @author GPT
 */
@Data
public class SysDbBackupScheduleConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否启用定时备份
     * true: 启用, false: 不启用
     * 注意：从 sys_config 读取 backup.enabled，0 代表启用，1 代表不启用
     */
    private Boolean enabled;

    /**
     * Cron 表达式
     * 例如：0 0 2 * * ? （每天凌晨2点）
     */
    private String cron;

    /**
     * 保留备份数量
     * 超过此数量的旧备份将被自动删除
     */
    private Integer keepCount;
}
