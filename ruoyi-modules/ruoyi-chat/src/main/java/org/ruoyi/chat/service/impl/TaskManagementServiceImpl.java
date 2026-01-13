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
import org.ruoyi.chat.domain.vo.TaskDurationStatItem;
import org.ruoyi.chat.domain.vo.TaskManagementFileVo;
import org.ruoyi.chat.domain.vo.TaskManagementVo;
import org.ruoyi.chat.mapper.TaskManagementFileMapper;
import org.ruoyi.chat.mapper.TaskManagementMapper;
import org.ruoyi.chat.mapper.TaskManagementTagMapper;
import org.ruoyi.chat.service.ITaskManagementService;
import org.ruoyi.chat.service.ITaskProcessingService;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        LambdaQueryWrapper<TaskManagement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(taskManagement.getStatus()), TaskManagement::getStatus, taskManagement.getStatus())
               .eq(ObjectUtil.isNotEmpty(taskManagement.getPriority()), TaskManagement::getPriority, taskManagement.getPriority())
               .eq(ObjectUtil.isNotEmpty(taskManagement.getTaskType()), TaskManagement::getTaskType, taskManagement.getTaskType())
               .eq(TaskManagement::getDelFlag, "0")
               .orderByDesc(TaskManagement::getCreateTime);

        Page<TaskManagement> page = taskManagementMapper.selectPage(pageQuery.build(), wrapper);
        
        // 转换为VO并加载文件和标签
        List<TaskManagementVo> voList = page.getRecords().stream().map(task -> {
            TaskManagementVo vo = convertToVo(task);
            // 加载文件
            loadFiles(vo, task.getId());
            // 加载标签
            vo.setTags(taskManagementTagMapper.selectTagsByTaskId(task.getId()));
            return vo;
        }).collect(Collectors.toList());

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
        int result = taskManagementMapper.updateById(taskManagement);
        
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
     * 转换为VO
     */
    private TaskManagementVo convertToVo(TaskManagement task) {
        TaskManagementVo vo = new TaskManagementVo();
        vo.setId(task.getId());
        vo.setTitle(task.getTitle());
        vo.setDescription(task.getDescription());
        vo.setPriority(task.getPriority());
        vo.setTaskType(task.getTaskType());
        vo.setStatus(task.getStatus());
        vo.setCreatedAt(task.getCreateTime());
        vo.setUpdatedAt(task.getUpdateTime());
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
}

