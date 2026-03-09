package org.ruoyi.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.file.FileUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import cn.hutool.core.convert.Convert;
import org.ruoyi.system.domain.SysDbBackup;
import org.ruoyi.system.domain.bo.SysDbBackupBo;
import org.ruoyi.system.domain.vo.SysDbBackupVo;
import org.ruoyi.system.domain.vo.SysDbBackupScheduleConfigVo;
import org.ruoyi.system.mapper.SysDbBackupMapper;
import org.ruoyi.system.service.ISysDbBackupService;
import org.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.Objects;

/**
 * 数据库备份管理 服务实现
 *
 * @author GPT
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysDbBackupServiceImpl implements ISysDbBackupService {

    private final SysDbBackupMapper baseMapper;
    private final ISysConfigService configService;

    /**
     * 备份目录，默认 /data/backup/db
     */
    @Value("${backup.db.dir:/data/backup/db}")
    private String backupDir;

    /**
     * mysqldump 命令路径，默认在 PATH 中
     */
    @Value("${backup.db.mysqldump-path:mysqldump}")
    private String mysqldumpPath;

    /**
     * 备份超时时间（秒）
     */
    @Value("${backup.db.timeout-seconds:300}")
    private long backupTimeoutSeconds;

    /**
     * 数据源配置（用于执行 mysqldump）
     */
    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String masterJdbcUrl;

    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String masterUsername;

    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String masterPassword;

    @Override
    public TableDataInfo<SysDbBackupVo> queryPageList(SysDbBackupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDbBackup> lqw = buildQueryWrapper(bo);
        Page<SysDbBackupVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<SysDbBackup> buildQueryWrapper(SysDbBackupBo bo) {
        LambdaQueryWrapper<SysDbBackup> lqw = Wrappers.lambdaQuery();
        lqw.eq(ObjectUtil.isNotNull(bo.getBackupId()), SysDbBackup::getBackupId, bo.getBackupId());
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), SysDbBackup::getFileName, bo.getFileName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysDbBackup::getStatus, bo.getStatus());
        if (bo.getBeginBackupTime() != null && bo.getEndBackupTime() != null) {
            lqw.between(SysDbBackup::getBackupTime, bo.getBeginBackupTime(), bo.getEndBackupTime());
        }
        return lqw;
    }

    @Override
    public SysDbBackupVo getById(Long backupId) {
        return baseMapper.selectVoById(backupId);
    }

    @Override
    public SysDbBackupVo createBackup() {
        return createBackup("手动备份");
    }

    @Override
    public SysDbBackupVo createBackup(String remark) {
        // 构造备份文件名和路径
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "backup_" + timestamp + ".sql";
        Path dirPath = Paths.get(backupDir);
        Path filePath = dirPath.resolve(fileName);

        SysDbBackup record = new SysDbBackup();
        record.setFileName(fileName);
        record.setFilePath(filePath.toString());
        record.setStatus("0"); // 默认失败，对齐 SYS_COMMON_STATUS：0 失败，1 成功
        record.setRemark(StringUtils.isNotBlank(remark) ? remark : "手动备份");
        record.setCreateTime(new Date());
        try {
            String username = LoginHelper.isLogin() ? LoginHelper.getUsername() : "system";
            record.setCreateBy(username);
        } catch (Exception e) {
            record.setCreateBy("system");
        }

        try {
            // 确保目录存在
            Files.createDirectories(dirPath);

            // 解析 JDBC URL，获取 host、port、database
            JdbcInfo jdbcInfo = parseJdbcUrl(masterJdbcUrl);

            // 构造 mysqldump 命令
            List<String> command = new ArrayList<>();
            command.add("docker");
            command.add("exec");
// 注意：这里不要加 -it，否则 Java 捕获流时会报错
            command.add("db0e");
            command.add(mysqldumpPath);
            command.add("--no-tablespaces");
            command.add("-h" + jdbcInfo.host());
            command.add("-P" + jdbcInfo.port());
            command.add("-u" + masterUsername);
            if (StringUtils.isNotEmpty(masterPassword)) {
                // 注意：-p 与密码之间不要加空格
                command.add("-p" + masterPassword);
            }
            ;
            command.add(jdbcInfo.database());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.redirectOutput(filePath.toFile());

            log.info("开始执行数据库备份，命令：{}", String.join(" ", command));
            Process process = pb.start();
            boolean finished = process.waitFor(backupTimeoutSeconds, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                String msg = "数据库备份超时（" + backupTimeoutSeconds + "s）";
                record.setRemark(msg);
                log.error(msg);
            } else {
                int exitCode = process.exitValue();
                if (exitCode == 0) {
                    long size = Files.exists(filePath) ? Files.size(filePath) : 0L;
                    record.setFileSize(size);
                    record.setBackupTime(new Date());
                    record.setStatus("1");
                    // remark 已在前面设置，这里不需要再修改
                } else {
                    String msg = "数据库备份失败，exitCode=" + exitCode;
                    record.setRemark(msg);
                    log.error(msg);
                }
            }
        } catch (Exception e) {
            String msg = "执行数据库备份发生异常: " + e.getMessage();
            record.setRemark(msg);
            log.error(msg, e);
        }

        // 无论成功或失败都记录一条备份记录
        baseMapper.insert(record);
        return MapstructUtils.convert(record, SysDbBackupVo.class);
    }

    @Override
    public void download(Long backupId, HttpServletResponse response) throws IOException {
        SysDbBackupVo backup = getById(backupId);
        if (backup == null) {
            throw new ServiceException("备份记录不存在");
        }

        if (StringUtils.isBlank(backup.getFilePath())) {
            throw new ServiceException("备份文件路径为空");
        }

        File file = new File(backup.getFilePath());
        if (!file.exists() || !file.isFile()) {
            throw new ServiceException("备份文件不存在或已被删除");
        }

        FileUtils.setAttachmentResponseHeader(response, backup.getFileName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        Files.copy(file.toPath(), response.getOutputStream());
        response.flushBuffer();
    }

    @Override
    public Boolean deleteByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Boolean.FALSE;
        }
        List<SysDbBackup> list = baseMapper.selectBatchIds(ids);
        for (SysDbBackup backup : list) {
            if (backup == null || StringUtils.isBlank(backup.getFilePath())) {
                continue;
            }
            try {
                File file = new File(backup.getFilePath());
                if (file.exists() && file.isFile()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        log.warn("删除备份文件失败，但继续删除记录。path={}", backup.getFilePath());
                    }
                } else {
                    // 文件不存在时容错，只记录日志不抛异常
                    log.warn("备份文件不存在，跳过物理删除。path={}", backup.getFilePath());
                }
            } catch (Exception e) {
                // 容错处理，不影响其他记录删除
                log.error("删除备份文件发生异常，path=" + backup.getFilePath(), e);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 解析 JDBC URL，提取 host、port、database
     *
     * 示例：
     * jdbc:mysql://mysql:3306/ruoyi-ai?useUnicode=true&characterEncoding=utf8
     */
    private JdbcInfo parseJdbcUrl(String url) {
        if (StringUtils.isBlank(url) || !url.startsWith("jdbc:mysql://")) {
            throw new ServiceException("不支持的 JDBC URL：" + url);
        }
        try {
            String withoutPrefix = url.substring("jdbc:mysql://".length());
            // mysql:3306/ruoyi-ai?...
            String[] hostAndDb = withoutPrefix.split("/", 2);
            String hostPort = hostAndDb[0];
            String dbAndParams = hostAndDb.length > 1 ? hostAndDb[1] : "";

            String database = dbAndParams.split("\\?")[0];
            String[] hostPortArr = hostPort.split(":", 2);
            String host = hostPortArr[0];
            String port = hostPortArr.length > 1 ? hostPortArr[1] : "3306";

            return new JdbcInfo(host, port, database);
        } catch (Exception e) {
            throw new ServiceException("解析 JDBC URL 失败: " + e.getMessage());
        }
    }

    @Override
    public SysDbBackupScheduleConfigVo getScheduleConfig() {
        SysDbBackupScheduleConfigVo vo = new SysDbBackupScheduleConfigVo();

        // 读取 backup.enabled
        // 注意：0 代表启用，1 代表不启用（逻辑反转）
        String enabledStr = configService.selectConfigByKey("backup.enabled");
        if (StringUtils.isNotBlank(enabledStr)) {
            // 如果配置值是 "0"，则 enabled = true（启用）
            // 如果配置值是 "1"，则 enabled = false（不启用）
            vo.setEnabled("0".equals(enabledStr));
        } else {
            // 默认不启用
            vo.setEnabled(false);
        }

        // 读取 backup.cron
        String cron = configService.selectConfigByKey("backup.cron");
        vo.setCron(StringUtils.isNotBlank(cron) ? cron : "");

        // 读取 backup.keepcount
        String keepCountStr = configService.selectConfigByKey("backup.keepcount");
        vo.setKeepCount(Convert.toInt(keepCountStr, 0));

        return vo;
    }

    @Override
    public int cleanupOldBackups(int keepCount) {
        if (keepCount <= 0) {
            log.warn("保留备份数量配置无效（keepCount={}），跳过清理", keepCount);
            return 0;
        }

        try {
            // 查询所有成功的备份，按备份时间倒序
            LambdaQueryWrapper<SysDbBackup> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(SysDbBackup::getStatus, "1") // 只处理成功的备份
                    .orderByDesc(SysDbBackup::getBackupTime);
            List<SysDbBackup> allBackups = baseMapper.selectList(queryWrapper);

            if (allBackups == null || allBackups.size() <= keepCount) {
                log.debug("备份数量（{}）未超过保留数量（{}），无需清理", 
                        allBackups != null ? allBackups.size() : 0, keepCount);
                return 0;
            }

            // 需要删除的备份（超出保留数量的部分）
            List<SysDbBackup> toDelete = allBackups.subList(keepCount, allBackups.size());
            List<Long> idsToDelete = toDelete.stream()
                    .map(SysDbBackup::getBackupId)
                    .filter(Objects::nonNull)
                    .toList();

            if (idsToDelete.isEmpty()) {
                return 0;
            }

            log.info("开始清理旧备份，保留数量：{}，当前备份数：{}，将删除：{} 个", 
                    keepCount, allBackups.size(), idsToDelete.size());

            // 删除备份记录及其文件
            Boolean deleted = deleteByIds(idsToDelete);
            int deletedCount = deleted ? idsToDelete.size() : 0;
            log.info("清理旧备份完成，删除了 {} 个备份", deletedCount);
            return deletedCount;
        } catch (Exception e) {
            log.error("清理旧备份时发生异常", e);
            return 0;
        }
    }

    private record JdbcInfo(String host, String port, String database) {
    }
}

