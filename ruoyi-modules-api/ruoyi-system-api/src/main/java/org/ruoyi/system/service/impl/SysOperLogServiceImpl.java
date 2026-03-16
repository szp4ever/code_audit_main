package org.ruoyi.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.system.domain.vo.UserOperationHeatmapItem;
import org.ruoyi.system.convert.SysOperLogConvert;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.ip.AddressUtils;
import org.ruoyi.common.log.event.OperLogEvent;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.system.domain.SysOperLog;
import org.ruoyi.system.domain.bo.SysOperLogBo;
import org.ruoyi.system.domain.vo.SysOperLogVo;
import org.ruoyi.system.mapper.SysOperLogMapper;
import org.ruoyi.system.service.ISysOperLogService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 操作日志 服务层处理
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {

    private final SysOperLogMapper baseMapper;
    private final SysOperLogConvert operLogConvert;

    /**
     * 操作日志记录
     *
     * @param operLogEvent 操作日志事件
     */
    @Async
    @EventListener
    @Override
    public void recordOper(OperLogEvent operLogEvent) {
        SysOperLogBo operLog = operLogConvert.toBo(operLogEvent);
        // 远程查询操作地点
        operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
        insertOperlog(operLog);
    }

    @Override
    public TableDataInfo<SysOperLogVo> selectPageOperLogList(SysOperLogBo operLog, PageQuery pageQuery) {
        Map<String, Object> params = operLog.getParams();
        LambdaQueryWrapper<SysOperLog> lqw = new LambdaQueryWrapper<SysOperLog>()
                .like(StringUtils.isNotBlank(operLog.getTitle()), SysOperLog::getTitle, operLog.getTitle())
                .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                        SysOperLog::getBusinessType, operLog.getBusinessType())
                .func(f -> {
                    if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                        f.in(SysOperLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                    }
                })
                .eq(operLog.getStatus() != null,
                        SysOperLog::getStatus, operLog.getStatus())
                .like(StringUtils.isNotBlank(operLog.getOperName()), SysOperLog::getOperName, operLog.getOperName())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysOperLog::getOperTime, params.get("beginTime"), params.get("endTime"))
                // 关键字模糊查询：对多个常用字段进行 like 匹配
                .and(StringUtils.isNotBlank(operLog.getKeyword()), w -> {
                    String keyword = operLog.getKeyword();
                    w.like(SysOperLog::getTitle, keyword)
                            .or().like(SysOperLog::getOperName, keyword)
                            .or().like(SysOperLog::getOperIp, keyword)
                            .or().like(SysOperLog::getOperUrl, keyword)
                            .or().like(SysOperLog::getOperLocation, keyword)
                            .or().like(SysOperLog::getRequestMethod, keyword)
                            .or().like(SysOperLog::getOperParam, keyword)
                            .or().like(SysOperLog::getJsonResult, keyword)
                            .or().like(SysOperLog::getErrorMsg, keyword);
                });
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("oper_id");
            pageQuery.setIsAsc("desc");
        }
        Page<SysOperLogVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 新增操作日志
     *
     * @param bo 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLogBo bo) {
        SysOperLog operLog = operLogConvert.toEntity(bo);
        operLog.setOperTime(new Date());
        baseMapper.insert(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLogVo> selectOperLogList(SysOperLogBo operLog) {
        Map<String, Object> params = operLog.getParams();
        return baseMapper.selectVoList(new LambdaQueryWrapper<SysOperLog>()
                .like(StringUtils.isNotBlank(operLog.getTitle()), SysOperLog::getTitle, operLog.getTitle())
                .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                        SysOperLog::getBusinessType, operLog.getBusinessType())
                .func(f -> {
                    if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                        f.in(SysOperLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                    }
                })
                .eq(operLog.getStatus() != null && operLog.getStatus() > 0,
                        SysOperLog::getStatus, operLog.getStatus())
                .like(StringUtils.isNotBlank(operLog.getOperName()), SysOperLog::getOperName, operLog.getOperName())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysOperLog::getOperTime, params.get("beginTime"), params.get("endTime"))
                // 导出时同样支持关键字模糊查询
                .and(StringUtils.isNotBlank(operLog.getKeyword()), w -> {
                    String keyword = operLog.getKeyword();
                    w.like(SysOperLog::getTitle, keyword)
                            .or().like(SysOperLog::getOperName, keyword)
                            .or().like(SysOperLog::getOperIp, keyword)
                            .or().like(SysOperLog::getOperUrl, keyword)
                            .or().like(SysOperLog::getOperLocation, keyword)
                            .or().like(SysOperLog::getRequestMethod, keyword)
                            .or().like(SysOperLog::getOperParam, keyword)
                            .or().like(SysOperLog::getJsonResult, keyword)
                            .or().like(SysOperLog::getErrorMsg, keyword);
                })
                .orderByDesc(SysOperLog::getOperId));
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return baseMapper.deleteBatchIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLogVo selectOperLogById(Long operId) {
        return baseMapper.selectVoById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        baseMapper.delete(new LambdaQueryWrapper<>());
    }

    /**
     * 获取用户操作热力图数据
     *
     * @param timeRange 时间范围：day|week|month
     * @param startDate 开始日期（格式：YYYY-MM-DD）
     * @param endDate 结束日期（格式：YYYY-MM-DD）
     * @return 用户操作热力图数据
     */
    @Override
    public List<UserOperationHeatmapItem> getUserOperationHeatmap(String timeRange, String startDate, String endDate) {
        log.info("获取用户操作热力图数据 - timeRange: {}, startDate: {}, endDate: {}", timeRange, startDate, endDate);
        
        Date startTime;
        Date endTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        try {
            // 如果提供了 startDate 和 endDate，使用自定义时间范围
            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                startTime = sdf.parse(startDate + " 00:00:00");
                endTime = sdf.parse(endDate + " 23:59:59");
                log.info("使用自定义时间范围: {} 到 {}", startTime, endTime);
            } else {
                // 使用 timeRange 参数（默认 week）
                if (StringUtils.isBlank(timeRange)) {
                    timeRange = "week";
                }

                // 设置结束时间为当前时间
                endTime = new Date();
                calendar.setTime(endTime);

                // 根据 timeRange 计算开始时间
                switch (timeRange) {
                    case "day":
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case "week":
                        calendar.add(Calendar.DAY_OF_MONTH, -7);
                        break;
                    case "month":
                        calendar.add(Calendar.DAY_OF_MONTH, -30);
                        break;
                    default:
                        calendar.add(Calendar.DAY_OF_MONTH, -7);
                }
                startTime = calendar.getTime();
                log.info("使用 timeRange={}, 时间范围: {} 到 {}", timeRange, startTime, endTime);
            }
        } catch (ParseException e) {
            log.error("日期格式错误", e);
            throw new RuntimeException("日期格式错误", e);
        }

        // 查询操作日志数据
        List<Map<String, Object>> rawData = baseMapper.selectUserOperationHeatmapData(startTime, endTime);
        log.info("查询到原始数据条数: {}", rawData != null ? rawData.size() : 0);

        // 将 URL 映射为模块标识，并按模块、日期和小时分组统计
        // 使用复合键：module + "|" + date + "|" + hour
        Map<String, Integer> groupedData = new HashMap<>();
        
        int skippedEmpty = 0;
        int skippedNoModule = 0;
        int skippedInvalidHour = 0;
        int processedCount = 0;

        for (Map<String, Object> item : rawData) {
            String operUrl = (String) item.get("operUrl");
            String date = (String) item.get("date");
            Object hourObj = item.get("hour");

            if (StringUtils.isBlank(operUrl) || StringUtils.isBlank(date) || hourObj == null) {
                skippedEmpty++;
                if (log.isDebugEnabled()) {
                    log.debug("跳过空数据项 - operUrl: {}, date: {}, hour: {}", operUrl, date, hourObj);
                }
                continue;
            }

            // 提取模块标识
            String module = extractModuleFromUrl(operUrl);
            if (StringUtils.isBlank(module)) {
                skippedNoModule++;
                if (log.isDebugEnabled()) {
                    log.debug("跳过无模块标识的URL: {}", operUrl);
                }
                continue;
            }

            // 处理小时字段（可能是 Integer 或 Long）
            Integer hour;
            if (hourObj instanceof Integer) {
                hour = (Integer) hourObj;
            } else if (hourObj instanceof Long) {
                hour = ((Long) hourObj).intValue();
            } else {
                try {
                    hour = Integer.parseInt(hourObj.toString());
                } catch (NumberFormatException e) {
                    skippedInvalidHour++;
                    if (log.isDebugEnabled()) {
                        log.debug("跳过无效小时数据: {}", hourObj);
                    }
                    continue;
                }
            }

            // 使用复合键进行分组统计
            String key = module + "|" + date + "|" + hour;
            groupedData.merge(key, 1, Integer::sum);
            processedCount++;
        }
        
        log.info("数据处理统计 - 总数据: {}, 已处理: {}, 跳过空数据: {}, 跳过无模块: {}, 跳过无效小时: {}", 
                rawData.size(), processedCount, skippedEmpty, skippedNoModule, skippedInvalidHour);

        // 转换为返回格式
        List<UserOperationHeatmapItem> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : groupedData.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            if (parts.length == 3) {
                UserOperationHeatmapItem item = new UserOperationHeatmapItem();
                item.setModule(parts[0]);
                item.setTimeSlot(parts[1]);
                item.setHour(Integer.parseInt(parts[2]));
                item.setCount(entry.getValue());
                result.add(item);
            }
        }

        log.info("返回热力图数据条数: {}", result.size());
        if (log.isDebugEnabled() && result.size() > 0) {
            log.debug("热力图数据示例: {}", result.get(0));
        }
        
        return result;
    }

    /**
     * 从 URL 中提取模块标识
     * 只统计以下四个功能模块：
     * 1. 工作流 (workflow)
     * 2. 对话 (chat)
     * 3. 任务管理 (taskmanagement)
     * 4. 知识库 (knowledge)
     *
     * @param operUrl 操作URL
     * @return 模块标识，如果不是目标模块则返回 null
     */
    private String extractModuleFromUrl(String operUrl) {
        if (StringUtils.isBlank(operUrl)) {
            return null;
        }

        // 移除查询参数和锚点
        String url = operUrl.split("\\?")[0].split("#")[0];

        // 移除开头的斜杠，统一处理
        if (url.startsWith("/")) {
            url = url.substring(1);
        }

        // 检查是否以目标模块开头（只统计四个功能模块）
        // 1. 工作流模块
        if (url.startsWith("workflow/") || url.equals("workflow")) {
            return "workflow";
        }
        
        // 2. 对话模块
        if (url.startsWith("chat/") || url.equals("chat")) {
            return "chat";
        }
        
        // 3. 任务管理模块
        if (url.startsWith("taskmanagement") || url.startsWith("task/")) {
            return "taskmanagement";
        }
        
        // 4. 知识库模块
        if (url.startsWith("knowledge/") || url.equals("knowledge")) {
            return "knowledge";
        }

        // 如果不是目标模块，返回 null（会被过滤掉）
        return null;
    }
}
