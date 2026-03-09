package org.ruoyi.system.service;

import jakarta.servlet.http.HttpServletResponse;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.system.domain.bo.SysDbBackupBo;
import org.ruoyi.system.domain.vo.SysDbBackupVo;
import org.ruoyi.system.domain.vo.SysDbBackupScheduleConfigVo;

import java.io.IOException;
import java.util.Collection;

/**
 * 数据库备份管理 服务层
 *
 * 提供：
 * - 备份记录分页查询
 * - 立即备份
 * - 下载备份文件
 * - 删除备份及其文件
 *
 * @author GPT
 */
public interface ISysDbBackupService {

    /**
     * 分页查询备份记录
     *
     * @param bo        查询参数（分页 + 筛选 + 排序）
     * @param pageQuery 分页参数
     * @return 分页结果
     */
    TableDataInfo<SysDbBackupVo> queryPageList(SysDbBackupBo bo, PageQuery pageQuery);

    /**
     * 根据ID获取备份记录
     *
     * @param backupId 备份ID
     * @return 备份记录
     */
    SysDbBackupVo getById(Long backupId);

    /**
     * 立即备份
     *
     * 实现思路：
     * - 调用 mysqldump 或其他备份工具导出数据库到指定目录
     * - 统计文件大小、记录备份时间
     * - status：成功为 1，失败为 0
     * - remark：成功时一般为“手动备份”，失败时记录错误信息
     *
     * @return 新增的备份记录
     */
    SysDbBackupVo createBackup();

    /**
     * 下载备份文件
     *
     * @param backupId 备份ID
     * @param response 响应对象
     */
    void download(Long backupId, HttpServletResponse response) throws IOException;

    /**
     * 删除备份记录及其对应文件
     *
     * @param ids 备份ID集合
     * @return 是否删除成功
     */
    Boolean deleteByIds(Collection<Long> ids);

    /**
     * 获取定时备份配置
     *
     * 从 sys_config 表中读取以下配置：
     * - backup.enabled: 是否启用定时备份（0 代表启用，1 代表不启用）
     * - backup.cron: Cron 表达式
     * - backup.keepcount: 保留备份数量
     *
     * @return 定时备份配置
     */
    SysDbBackupScheduleConfigVo getScheduleConfig();

    /**
     * 立即备份（支持指定备注）
     *
     * @param remark 备注信息（如"手动备份"、"定时备份"）
     * @return 新增的备份记录
     */
    SysDbBackupVo createBackup(String remark);

    /**
     * 清理旧备份
     *
     * 根据 keepCount 配置，删除超出数量的旧备份记录及其文件
     * 只删除状态为成功（status='1'）的备份，按备份时间倒序保留最新的 keepCount 个
     *
     * @param keepCount 保留的备份数量
     * @return 删除的备份数量
     */
    int cleanupOldBackups(int keepCount);
}

