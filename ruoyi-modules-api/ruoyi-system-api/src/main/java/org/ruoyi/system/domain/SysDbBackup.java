package org.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据库备份记录表 sys_db_backup
 *
 * 对应表：
 * DROP TABLE IF EXISTS sys_db_backup;
 * CREATE TABLE sys_db_backup (
 *     backup_id   bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '备份ID',
 *     file_name   varchar(255) NOT NULL                COMMENT '备份文件名',
 *     file_path   varchar(500) NOT NULL                COMMENT '文件物理路径',
 *     file_size   bigint(20)   DEFAULT 0               COMMENT '文件大小(字节)',
 *     backup_time datetime                              COMMENT '备份完成时间',
 *     status      char(1)      DEFAULT '0'             COMMENT '备份状态（0失败 1成功）',
 *     remark      varchar(500) DEFAULT NULL            COMMENT '备注',
 *     create_by   varchar(64)  DEFAULT ''              COMMENT '创建者',
 *     create_time datetime                              COMMENT '创建时间',
 *     update_by   varchar(64)  DEFAULT ''              COMMENT '更新者',
 *     update_time datetime                              COMMENT '更新时间',
 *     PRIMARY KEY (backup_id)
 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '数据库备份记录表';
 *
 * @author GPT
 */
@Data
@TableName("sys_db_backup")
public class SysDbBackup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 备份ID
     */
    @TableId(value = "backup_id")
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
     * 备份状态（0失败 1成功）
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

