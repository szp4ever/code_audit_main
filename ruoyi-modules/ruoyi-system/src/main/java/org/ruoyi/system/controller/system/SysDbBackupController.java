package org.ruoyi.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.system.domain.bo.SysDbBackupBo;
import org.ruoyi.system.domain.vo.SysDbBackupVo;
import org.ruoyi.system.domain.vo.SysDbBackupScheduleConfigVo;
import org.ruoyi.system.service.ISysDbBackupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 数据库备份管理
 *
 * 接口：
 * 2.1 GET  /system/backup/list           获取备份列表（分页 + 筛选 + 排序）
 * 2.2 POST /system/backup/create        立即备份
 * 2.3 GET  /system/backup/download/{id} 下载备份
 * 2.4 DELETE /system/backup/remove/{ids} 删除备份
 * 2.5 GET  /system/backup/schedule/config 获取定时备份配置
 *
 * 权限建议：
 * - system:backup:list
 * - system:backup:create
 * - system:backup:download
 * - system:backup:remove
 * - system:backup:query
 *
 * @author GPT
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/backup")
public class SysDbBackupController extends BaseController {

    private final ISysDbBackupService dbBackupService;

    /**
     * 获取备份列表（分页 + 筛选 + 排序）
     *
     * 对应：GET /backup/list
     *
     * 请求参数：
     * - QueryString => SysDbBackupBo + PageQuery
     * 响应结构：
     * - TableDataInfo<SysDbBackupVo>
     *   - total: 总数
     *   - rows: BackupRecord[]
     */
    @SaCheckPermission("system:backup:list")
    @GetMapping("/list")
    public TableDataInfo<SysDbBackupVo> list(SysDbBackupBo bo, PageQuery pageQuery) {
        return dbBackupService.queryPageList(bo, pageQuery);
    }

    /**
     * 立即备份
     *
     * 对应：POST /backup/create
     *
     * 行为：
     * - 触发一次数据库备份（调用 mysqldump 等）
     * - 生成备份文件到配置目录（默认：/data/backup/db/backup_yyyyMMddHHmmss.sql）
     * - 写入 sys_db_backup 记录（无论成功或失败）
     *
     * 响应：
     * - SysDbBackupVo（前端主要用于提示）
     */
    @SaCheckPermission("system:backup:create")
    @Log(title = "数据库备份", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<SysDbBackupVo> create() {
        SysDbBackupVo vo = dbBackupService.createBackup();
        return R.ok(vo);
    }

    /**
     * 下载备份
     *
     * 对应：GET /backup/download/{backupId}
     *
     * 行为：
     * - 根据 backupId 查询记录，获取 filePath、fileName
     * - 以 application/octet-stream 输出，并设置 Content-Disposition
     *
     * 错误处理：
     * - 记录不存在或文件丢失时抛出 ServiceException，由全局异常处理为业务错误码
     */
    @SaCheckPermission("system:backup:download")
    @GetMapping("/download/{backupId}")
    public void download(@PathVariable Long backupId, HttpServletResponse response) throws IOException {
        dbBackupService.download(backupId, response);
    }

    /**
     * 删除备份（支持批量）
     *
     * 对应：DELETE /backup/remove/{backupIds}
     *
     * 行为：
     * - backupIds 可能为单个或数组（前端通常传数组）
     * - 批量查出记录，逐个删除对应文件 + 数据表记录
     * - 对不存在的文件容错，只记录日志，不影响整批删除
     */
    @SaCheckPermission("system:backup:remove")
    @Log(title = "数据库备份", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{backupIds}")
    public R<Void> remove(@NotEmpty(message = "备份ID不能为空") @PathVariable Long[] backupIds) {
        boolean result = dbBackupService.deleteByIds(List.of(backupIds));
        return toAjax(result);
    }

    /**
     * 获取定时备份配置
     *
     * 对应：GET /backup/schedule/config
     *
     * 从 sys_config 表中读取以下配置：
     * - backup.enabled: 是否启用定时备份（0 代表启用，1 代表不启用）
     * - backup.cron: Cron 表达式
     * - backup.keepcount: 保留备份数量
     *
     * 响应结构：
     * {
     *   "code": 200,
     *   "data": {
     *     "enabled": true,      // true 表示启用，false 表示不启用
     *     "cron": "0 0 2 * * ?",  // Cron 表达式
     *     "keepCount": 30       // 保留备份数量
     *   }
     * }
     */
    @SaCheckPermission("system:backup:query")
    @GetMapping("/schedule/config")
    public R<SysDbBackupScheduleConfigVo> getScheduleConfig() {
        SysDbBackupScheduleConfigVo config = dbBackupService.getScheduleConfig();
        return R.ok(config);
    }
}

