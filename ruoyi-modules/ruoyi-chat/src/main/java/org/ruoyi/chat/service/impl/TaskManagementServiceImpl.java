package org.ruoyi.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.TaskManagementFile;
import org.ruoyi.chat.domain.TaskManagementTag;
import org.ruoyi.chat.domain.TaskManagementIssue;
import org.ruoyi.chat.domain.vo.*;
import org.ruoyi.chat.mapper.TaskManagementFileMapper;
import org.ruoyi.chat.mapper.TaskManagementIssueMapper;
import org.ruoyi.chat.mapper.TaskManagementMapper;
import org.ruoyi.chat.mapper.TaskManagementTagMapper;
import org.ruoyi.chat.service.ITaskManagementService;
import org.ruoyi.chat.service.ITaskProcessingService;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务管理Service业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskManagementServiceImpl implements ITaskManagementService {

    private final TaskManagementMapper taskManagementMapper;
    private final TaskManagementFileMapper taskManagementFileMapper;
    private final TaskManagementTagMapper taskManagementTagMapper;
    private final TaskManagementIssueMapper taskManagementIssueMapper;
    private final ITaskProcessingService taskProcessingService;

    /**
     * 创建任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(TaskManagement taskManagement) {
        // 设置默认状态
        if (ObjectUtil.isEmpty(taskManagement.getStatus())) {
            taskManagement.setStatus("pending");
        }
        
        // 插入任务主表
        taskManagementMapper.insert(taskManagement);
        Long taskId = taskManagement.getId();
        
        log.info("========== 创建任务开始 ==========");
        log.info("任务ID（自增主键）: {}", taskId);
        log.info("任务标题: {}", taskManagement.getTitle());
        log.info("输入文件数量: {}", taskManagement.getInputFiles() != null ? taskManagement.getInputFiles().size() : 0);

        // 保存标签
        if (CollUtil.isNotEmpty(taskManagement.getTags())) {
            List<TaskManagementTag> tags = new ArrayList<>();
            for (String tagName : taskManagement.getTags()) {
                TaskManagementTag tag = new TaskManagementTag();
                tag.setTaskId(taskId);
                tag.setTagName(tagName);
                tag.setCreateTime(new Date());
                tags.add(tag);
            }
            taskManagementTagMapper.batchInsertTags(tags);
            log.info("标签保存成功，数量: {}", tags.size());
        }

        // 关联输入文件
        if (CollUtil.isNotEmpty(taskManagement.getInputFiles())) {
            log.info("开始关联输入文件，文件数量: {}", taskManagement.getInputFiles().size());
            for (int i = 0; i < taskManagement.getInputFiles().size(); i++) {
                TaskManagementFile file = taskManagement.getInputFiles().get(i);
                log.info("处理第 {} 个文件", i + 1);
                log.info("文件对象详情 - ID: {}, Name: {}, URL: {}", file.getId(), file.getName(), file.getUrl());
                
                Long fileId = file.getId();
                if (fileId == null) {
                    // 尝试从其他字段获取ID（前端可能传入的是字符串ID）
                    if (file.getName() != null || file.getUrl() != null) {
                        log.warn("文件ID为null，但文件有name或url，可能前端传入格式不正确");
                        log.warn("文件完整对象: {}", file);
                    } else {
                        log.warn("文件对象为空或缺少必要字段");
                    }
                    continue;
                }
                
                // 更新文件的taskId和fileCategory
                // taskId 必须关联到 task_management 表的 id（主键）
                TaskManagementFile updateFile = new TaskManagementFile();
                updateFile.setId(fileId);
                updateFile.setTaskId(taskId);  // 使用创建任务时生成的自增ID
                updateFile.setFileCategory("input");
                
                log.info("准备更新文件 - 文件ID: {}, 任务ID: {}, 文件类别: {}", 
                        updateFile.getId(), updateFile.getTaskId(), updateFile.getFileCategory());
                
                // 先查询文件是否存在
                TaskManagementFile existingFile = taskManagementFileMapper.selectById(fileId);
                if (existingFile == null) {
                    log.error("文件不存在，文件ID: {}", fileId);
                    throw new RuntimeException("文件不存在，文件ID: " + fileId);
                }
                log.info("文件存在，当前taskId: {}", existingFile.getTaskId());
                
                int updateCount = taskManagementFileMapper.updateById(updateFile);
                log.info("文件更新结果，影响行数: {}", updateCount);
                
                if (updateCount <= 0) {
                    log.error("更新文件taskId失败，文件ID: {}", fileId);
                    throw new RuntimeException("更新文件taskId失败，文件ID: " + fileId);
                } else {
                    // 验证更新结果
                    TaskManagementFile updatedFile = taskManagementFileMapper.selectById(fileId);
                    log.info("文件taskId更新成功 - 文件ID: {}, 更新后的taskId: {}", fileId, updatedFile.getTaskId());
                }
            }
            log.info("输入文件关联完成");
        } else {
            log.info("没有输入文件需要关联");
        }

        log.info("========== 创建任务完成，任务ID: {} ==========", taskId);
        
        // 异步调用Flask接口处理任务（在后台线程池中执行，不会阻塞当前请求）
        // 确保taskManagement对象有ID，用于后续处理
        taskManagement.setId(taskId);
        try {
            // 异步调用，立即返回，任务在后台线程池中执行
            // 即使前端断开连接或切换界面，任务也会继续在后台执行
            taskProcessingService.processTask(taskManagement);
            log.info("任务处理请求已提交到后台线程池，任务ID: {}，任务将在后台异步执行", taskId);
        } catch (Exception e) {
            log.error("提交任务处理请求失败，任务ID: {}", taskId, e);
            // 不抛出异常，避免影响任务创建
            // 即使提交失败，任务已创建，用户可以手动触发处理
        }
        
        return taskId;
    }

    /**
     * 查询任务列表
     */
    @Override
    public TableDataInfo<TaskManagementVo> selectTaskList(TaskManagement taskManagement, PageQuery pageQuery) {
        // 1. 构建 MyBatis-Plus 的分页对象
        Page<TaskManagement> page = pageQuery.build();

        // 2. 调用 Mapper (传入 page 对象，MP 会自动处理分页 SQL)
        List<TaskManagement> list = taskManagementMapper.selectTaskManagementList(page, taskManagement);

        // 3. 转换 VO (这里记得加上您的 loadFiles 和 convertToVo 逻辑)
        List<TaskManagementVo> voList = list.stream().map(task -> {
            TaskManagementVo vo = convertToVo(task); // 这里面必须有 setTemplateName
            loadFiles(vo, task.getId());
            vo.setTags(taskManagementTagMapper.selectTagsByTaskId(task.getId()));
            return vo;
        }).collect(Collectors.toList());

        // 4. 🔥 直接从 page 对象获取 total，不需要 PageHelper
        return new TableDataInfo<>(voList, page.getTotal());
    }
    /**
     * 查询任务详情
     */
    @Override
    public TaskManagementVo selectTaskById(Long id) {
        // 使用自定义查询方法，会过滤 del_flag = '0'
        TaskManagement task = taskManagementMapper.selectTaskManagementById(id);
        if (task == null) {
            return null;
        }
        
        // 再次检查删除标志
        if ("1".equals(task.getDelFlag())) {
            return null;
        }
        
        TaskManagementVo vo = convertToVo(task);
        // 加载文件
        loadFiles(vo, id);
        // 加载标签
        vo.setTags(taskManagementTagMapper.selectTagsByTaskId(id));
        
        return vo;
    }

    /**
     * 更新任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(TaskManagement taskManagement) {
        int result = taskManagementMapper.updateTaskManagement(taskManagement);
        
        // 更新标签
        if (CollUtil.isNotEmpty(taskManagement.getTags())) {
            // 删除旧标签
            taskManagementTagMapper.deleteTagsByTaskId(taskManagement.getId());
            // 插入新标签
            List<TaskManagementTag> tags = new ArrayList<>();
            for (String tagName : taskManagement.getTags()) {
                TaskManagementTag tag = new TaskManagementTag();
                tag.setTaskId(taskManagement.getId());
                tag.setTagName(tagName);
                tag.setCreateTime(new Date());
                tags.add(tag);
            }
            taskManagementTagMapper.batchInsertTags(tags);
        }
        
        return result;
    }

    /**
     * 删除任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTaskById(Long id) {
        // 逻辑删除
        TaskManagement task = new TaskManagement();
        task.setId(id);
        task.setDelFlag("1");
        return taskManagementMapper.updateById(task);
    }

    /**
     * 重试任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int retryTaskById(Long id) {
        TaskManagement updateTask = new TaskManagement();
        updateTask.setId(id);
        updateTask.setStatus("pending");
        updateTask.setUpdateTime(new Date()); // 更新修改时间

        int updateCount = taskManagementMapper.updateById(updateTask);
        TaskManagement latestTask = taskManagementMapper.selectTaskManagementById(id);
        try {
            taskProcessingService.processTask(latestTask);
            log.info("任务重试请求已提交到后台线程池，任务ID: {}，任务将在后台异步执行", id);
        } catch (Exception e) {
            log.error("提交任务重试请求失败，任务ID: {}", id, e);
        }
        return updateCount;
    }

    /**
     * 转换为VO
     */
    private TaskManagementVo convertToVo(TaskManagement task) {
        TaskManagementVo vo = new TaskManagementVo();
        vo.setId(task.getId());
        vo.setProjectId(task.getProjectId());
        vo.setTitle(task.getTitle());
        vo.setDescription(task.getDescription());
        vo.setPriority(task.getPriority());
        vo.setTaskType(task.getTaskType());
        vo.setStatus(task.getStatus());
        vo.setCreatedAt(task.getCreateTime());
        vo.setUpdatedAt(task.getUpdateTime());
        vo.setTemplateId(task.getTemplateId());
        vo.setTemplateName(task.getTemplateName());
        return vo;
    }

    /**
     * 加载文件列表
     */
    private void loadFiles(TaskManagementVo vo, Long taskId) {
        // 加载输入文件
        List<TaskManagementFile> inputFiles = taskManagementFileMapper.selectFilesByTaskId(taskId, "input");
        vo.setInputFiles(convertFileListToVo(inputFiles));
        
        // 加载输出文件
        List<TaskManagementFile> outputFiles = taskManagementFileMapper.selectFilesByTaskId(taskId, "output");
        vo.setOutputFiles(convertFileListToVo(outputFiles));
    }

    /**
     * 转换文件列表为VO
     */
    private List<TaskManagementFileVo> convertFileListToVo(List<TaskManagementFile> files) {
        if (CollUtil.isEmpty(files)) {
            return new ArrayList<>();
        }
        return files.stream().map(this::convertFileToVo).collect(Collectors.toList());
    }

    /**
     * 转换单个文件为VO
     */
    private TaskManagementFileVo convertFileToVo(TaskManagementFile file) {
        if (file == null) {
            return null;
        }
        TaskManagementFileVo vo = new TaskManagementFileVo();
        vo.setId(file.getId() != null ? String.valueOf(file.getId()) : null);
        vo.setName(file.getName());
        vo.setUrl(file.getUrl());
        vo.setSize(file.getSize());
        vo.setType(file.getType());
        vo.setUploadTime(file.getUploadTime());
        return vo;
    }

    /**
     * 获取任务状态统计
     */
    @Override
    public Map<String, Integer> getStatusStats() {
        List<Map<String, Object>> statsList = taskManagementMapper.selectStatusStats();
        if (CollUtil.isEmpty(statsList)) {
            return new HashMap<>();
        }
        return statsList.stream()
                .collect(Collectors.toMap(
                    item -> String.valueOf(item.get("key")),
                    item -> ((Number) item.get("value")).intValue()
                ));
    }

    /**
     * 获取任务类型统计
     */
    @Override
    public Map<String, Integer> getTypeStats() {
        List<Map<String, Object>> statsList = taskManagementMapper.selectTypeStats();
        if (CollUtil.isEmpty(statsList)) {
            return new HashMap<>();
        }
        return statsList.stream()
                .collect(Collectors.toMap(
                    item -> String.valueOf(item.get("key")),
                    item -> ((Number) item.get("value")).intValue()
                ));
    }

    /**
     * 获取任务耗时统计
     */
    @Override
    public List<TaskDurationStatItem> getDurationStats(String timeRange) {
        // 默认使用day
        if (timeRange == null || timeRange.isEmpty()) {
            timeRange = "day";
        }
        // 验证timeRange参数
        if (!timeRange.equals("day") && !timeRange.equals("hour") && !timeRange.equals("week")) {
            timeRange = "day";
        }
        return taskManagementMapper.selectDurationStats(timeRange);
    }

    /**
     * 获取月度任务统计
     */
    @Override
    public List<TaskMonthlyCountItem> getTaskMonthlyCount(String startMonth, String endMonth) {
        // 1. 参数兜底：防止传入 null 或空字符串，保证后续查询不会出现异常
        if (startMonth == null || startMonth.trim().isEmpty()) {
            // 兜底：默认取当前月的前6个月（和前端默认范围一致，格式 YYYY-MM）
            // 可根据项目需求调整兜底逻辑，此处仅作示例
            LocalDate now = LocalDate.now();
            startMonth = now.minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        if (endMonth == null || endMonth.trim().isEmpty()) {
            // 兜底：默认取当前月（格式 YYYY-MM）
            endMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        // 2. 可选：参数格式验证（保证是 YYYY-MM 格式，避免无效查询）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        try {
            LocalDate.parse(startMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate.parse(endMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeException e) {
            // 格式无效时，重置为默认范围（前6个月 → 当前月）
            LocalDate now = LocalDate.now();
            startMonth = now.minusMonths(6).format(formatter);
            endMonth = now.format(formatter);
        }

        // 3. 保证开始年月不晚于结束年月（避免查询逻辑异常）
        LocalDate startDate = LocalDate.parse(startMonth + "-01");
        LocalDate endDate = LocalDate.parse(endMonth + "-01");
        if (startDate.isAfter(endDate)) {
            // 交换开始和结束年月，保证查询范围有效
            String temp = startMonth;
            startMonth = endMonth;
            endMonth = temp;
        }

        // 4. 调用 Mapper 层方法，传递处理后的合法参数，查询统计数据
        return taskManagementMapper.selectTaskMonthlyCount(startMonth, endMonth);
    }

    /**
     * 季度任务统计实现
     */
    @Override
    public List<TaskQuarterlyStatsItem> getTaskQuarterlyStats(String year) {
        // 1. 参数兜底：防止 null 或空字符串，默认当前年
        if (year == null || year.trim().isEmpty()) {
            year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        }

        // 2. 可选：参数格式验证（保证是 4 位年份，避免无效查询）
        if (!year.matches("^\\d{4}$")) { // 正则匹配 4 位数字
            year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        }

        // 3. 调用 Mapper 层方法，传递处理后的合法年份参数
        return taskManagementMapper.selectTaskQuarterlyStats(year);
    }

    /**
     * 获取任务实时数量统计
     */
    @Override
    public TaskRealTimeCountVO getTaskRealTimeCount() {
        // 1. 初始化返回结果
        TaskRealTimeCountVO realTimeCountVO = new TaskRealTimeCountVO();

        // 2. 调用 Mapper 层查询各状态任务数量（按 status 统计）
        Integer inProgressCount = taskManagementMapper.countTaskByStatus("in_progress");
        Integer pendingCount = taskManagementMapper.countTaskByStatus("pending");
        Integer completeCount = taskManagementMapper.countTaskByStatus("completed");

        // 3. 组装返回结果（为空则设为 0，避免前端展示 null）
        realTimeCountVO.setInProgressCount(inProgressCount == null ? 0 : inProgressCount);
        realTimeCountVO.setPendingCount(pendingCount == null ? 0 : pendingCount);
        realTimeCountVO.setCompleteCount(completeCount == null ? 0 : completeCount);

        // 4. 返回结果
        return realTimeCountVO;
    }

    /**
     * 获取任务漏洞详情
     */
    @Override
    public TaskVulnerabilityDetailVo getTaskVulnerabilities(Long taskId) {
        // 查询任务信息
        TaskManagement task = taskManagementMapper.selectById(taskId);
        if (task == null) {
            return null;
        }

        // 查询issues列表
        List<TaskManagementIssue> issues = taskManagementIssueMapper.selectIssuesByTaskId(taskId);
        
        // 统计severity数量
        List<Map<String, Object>> severityStats = taskManagementIssueMapper.selectSeverityCountByTaskId(taskId);
        Map<String, Integer> severityCount = new HashMap<>();
        if (CollUtil.isNotEmpty(severityStats)) {
            for (Map<String, Object> stat : severityStats) {
                String severity = String.valueOf(stat.get("severity")).toLowerCase();
                Integer count = ((Number) stat.get("count")).intValue();
                severityCount.put(severity, count);
            }
        }

        // 转换为VO
        TaskVulnerabilityDetailVo detailVo = new TaskVulnerabilityDetailVo();
        detailVo.setTaskId(taskId);
        detailVo.setTaskTitle(task.getTitle());
        detailVo.setTotalCount(issues != null ? issues.size() : 0);
        detailVo.setSeverityCount(severityCount);
        
        // 转换issues为VulnerabilityVo列表
        List<VulnerabilityVo> vulnerabilityList = new ArrayList<>();
        if (CollUtil.isNotEmpty(issues)) {
            for (TaskManagementIssue issue : issues) {
                VulnerabilityVo vulnVo = convertIssueToVulnerabilityVo(issue);
                vulnerabilityList.add(vulnVo);
            }
        }
        detailVo.setVulnerabilities(vulnerabilityList);

        return detailVo;
    }

    /**
     * 转换Issue为VulnerabilityVo
     */
    private VulnerabilityVo convertIssueToVulnerabilityVo(TaskManagementIssue issue) {
        VulnerabilityVo vo = new VulnerabilityVo();
        vo.setId(issue.getId());
        vo.setTaskId(issue.getTaskId());
        vo.setTitle(issue.getIssueName());
        vo.setDescription(issue.getDescription());
        
        // 转换severity为小写（Low -> low, Medium -> medium, High -> high, Critical -> critical）
        String severity = issue.getSeverity();
        if (severity != null) {
            vo.setSeverity(severity.toLowerCase());
        } else {
            vo.setSeverity("low");
        }
        
        vo.setFilePath(issue.getFileName());
        
        // 转换lineNumber为Integer（如果是范围，取第一个数字）
        String lineNumberStr = issue.getLineNumber();
        if (lineNumberStr != null && !lineNumberStr.isEmpty()) {
            try {
                // 如果是范围（如"45-48"），取第一个数字
                if (lineNumberStr.contains("-")) {
                    String firstNumber = lineNumberStr.split("-")[0].trim();
                    vo.setLineNumber(Integer.parseInt(firstNumber));
                } else {
                    vo.setLineNumber(Integer.parseInt(lineNumberStr.trim()));
                }
            } catch (NumberFormatException e) {
                log.warn("无法解析行号: {}", lineNumberStr);
                vo.setLineNumber(null);
            }
        } else {
            vo.setLineNumber(null);
        }
        
        vo.setCodeSnippet(null); // 数据库中没有此字段
        vo.setFixSuggestion(issue.getFixSuggestion());
        vo.setCategory(null); // 数据库中没有此字段
        vo.setCreatedAt(issue.getCreateTime());
        
        return vo;
    }
}

