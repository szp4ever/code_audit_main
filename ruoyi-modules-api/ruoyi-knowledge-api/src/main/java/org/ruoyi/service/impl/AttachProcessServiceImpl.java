package org.ruoyi.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.domain.KnowledgeAttachProcess;
import org.ruoyi.domain.KnowledgeFragment;
import org.ruoyi.domain.KnowledgeItem;
import org.ruoyi.domain.KnowledgeItemTag;
import org.ruoyi.domain.KnowledgeItemVulnerabilityType;
import org.ruoyi.domain.KnowledgeTag;
import org.ruoyi.domain.enums.ProcessingStatus;
import org.ruoyi.domain.vo.KnowledgeAttachProcessVo;
import org.ruoyi.domain.KnowledgeAttach;
import org.ruoyi.domain.KnowledgeInfo;
import org.ruoyi.domain.bo.ExtractionContext;
import org.ruoyi.domain.bo.ExtractedItemData;
import org.ruoyi.mapper.KnowledgeAttachMapper;
import org.ruoyi.mapper.KnowledgeAttachProcessMapper;
import org.ruoyi.mapper.KnowledgeFragmentMapper;
import org.ruoyi.mapper.KnowledgeItemMapper;
import org.ruoyi.mapper.KnowledgeItemTagMapper;
import org.ruoyi.mapper.KnowledgeItemVulnerabilityTypeMapper;
import org.ruoyi.mapper.KnowledgeInfoMapper;
import org.ruoyi.mapper.KnowledgeTagMapper;
import org.ruoyi.service.IAttachProcessService;
import org.ruoyi.service.IKnowledgeItemExtractionService;
import org.ruoyi.service.IKnowledgeItemService;
import org.ruoyi.service.ICweReferenceService;
import org.ruoyi.service.IKnowledgeTagService;
import org.ruoyi.service.IChatModelService;
import org.ruoyi.service.VectorStoreService;
import org.ruoyi.domain.bo.StoreEmbeddingBo;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.domain.vo.CweReferenceVo;
import org.ruoyi.domain.vo.KnowledgeTagVo;
import org.ruoyi.system.service.ISysDictTypeService;
import org.ruoyi.system.domain.vo.SysDictDataVo;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.common.core.utils.SpringUtils;
import org.ruoyi.utils.CvssScoreCalculator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.scheduling.annotation.Async;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Set;

/**
 * 附件处理状态管理服务实现
 * 基于LLM与状态改革设计文档 v1.0
 *
 * @author system
 * @date 2026-01-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AttachProcessServiceImpl implements IAttachProcessService {

    private final KnowledgeAttachProcessMapper processMapper;
    private final KnowledgeAttachMapper attachMapper;
    private final KnowledgeFragmentMapper fragmentMapper;
    private final KnowledgeItemMapper knowledgeItemMapper;
    private final KnowledgeInfoMapper knowledgeInfoMapper;
    private final KnowledgeItemVulnerabilityTypeMapper vulnerabilityTypeMapper;
    private final KnowledgeItemTagMapper itemTagMapper;
    private final KnowledgeTagMapper knowledgeTagMapper;
    private final IKnowledgeItemExtractionService extractionService;
    private final ICweReferenceService cweReferenceService;
    private final IKnowledgeTagService knowledgeTagService;
    private final IChatModelService chatModelService;
    private final VectorStoreService vectorStoreService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ISysDictTypeService dictTypeService;
    private final IKnowledgeItemService knowledgeItemService;

    @Override
    public String createProcess(Long attachId, String docId) {
        KnowledgeAttachProcess process = new KnowledgeAttachProcess();
        String processId = RandomUtil.randomString(32);
        process.setAttachId(attachId);
        process.setDocId(docId);
        process.setCurrentStatus(ProcessingStatus.UPLOADING.getCode());
        process.setProgress(0);
        process.setStatusData("{}");
        processMapper.insert(process);
        log.info("创建处理任务: processId={}, attachId={}, docId={}", processId, attachId, docId);
        return String.valueOf(process.getId());
    }

    @Override
    public void updateStatus(String processId, ProcessingStatus status, Object statusData) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            throw new ServiceException("处理任务不存在: processId=" + processId);
        }

        //检查附件是否还存在
        KnowledgeAttach attach = attachMapper.selectById(process.getAttachId());
        if (attach == null) {
            log.warn("附件已删除，停止状态更新: processId={}, attachId={}", processId, process.getAttachId());
            throw new ServiceException("附件已删除，无法继续处理");
        }

        // 验证状态转换
        ProcessingStatus oldStatus = ProcessingStatus.fromCode(process.getCurrentStatus());
        if (!canTransition(oldStatus, status)) {
            throw new ServiceException("不允许的状态转换: " + process.getCurrentStatus() + " -> " + status.getCode());
        }

        //如果状态发生变化，记录新阶段的开始时间
        boolean statusChanged = !oldStatus.equals(status);
        long currentTime = System.currentTimeMillis();
        
        process.setCurrentStatus(status.getCode());
        if (statusData != null) {
            // 如果状态为 FAILED 或 CANCELLED 且 statusData 是字符串，将其作为错误信息或取消原因
            if ((status == ProcessingStatus.FAILED || status == ProcessingStatus.CANCELLED) && statusData instanceof String) {
                process.setErrorMessage((String) statusData);
            } else {
                //将statusData转换为Map，添加阶段开始时间
                Map<String, Object> dataMap;
                if (statusData instanceof Map) {
                    dataMap = new HashMap<>((Map<String, Object>) statusData);
                } else if (statusData instanceof String) {
                    //尝试解析JSON字符串，如果失败则包装为Map
                    try {
                        dataMap = JSON.parseObject((String) statusData, Map.class);
                        if (dataMap == null) {
                            //如果解析结果为null，创建一个包含原始字符串的Map
                            dataMap = new HashMap<>();
                            dataMap.put("message", statusData);
                        }
                    } catch (Exception e) {
                        //JSON解析失败，将字符串包装为Map
                        log.warn("statusData字符串不是有效的JSON，将其包装为Map: processId={}, statusData={}", processId, statusData);
                        dataMap = new HashMap<>();
                        dataMap.put("message", statusData);
                    }
                } else {
                    String jsonStr = JSON.toJSONString(statusData);
                    dataMap = JSON.parseObject(jsonStr, Map.class);
                }
                
                //如果状态变化，记录阶段开始时间
                if (statusChanged) {
                    dataMap.put("stageStartTime", currentTime);
                } else if (!dataMap.containsKey("stageStartTime")) {
                    //如果状态没变化但没有阶段开始时间，使用当前时间（首次记录）
                    dataMap.put("stageStartTime", currentTime);
                }
                
                String statusDataJson = JSON.toJSONString(dataMap);
                process.setStatusData(statusDataJson);
            }
        }
        
        // 更新进度
        // 对于非断点状态（PARSING、CHUNKING、MATCHING、CREATING_ITEMS、VECTORIZING），
        // 如果还没有开始处理（没有currentIndex），使用起始进度而不是结束进度
        // 这样用户看到的是从起始进度开始逐步增长，而不是从结束进度跳回起始进度
        int progress;
        boolean isNonBreakpointStatus = status == ProcessingStatus.PARSING 
            || status == ProcessingStatus.CHUNKING 
            || status == ProcessingStatus.MATCHING 
            || status == ProcessingStatus.CREATING_ITEMS 
            || status == ProcessingStatus.VECTORIZING;
        
        if (isNonBreakpointStatus) {
            // 检查statusData中是否有currentIndex，如果没有，说明刚开始，使用起始进度
            if (statusData != null) {
                Map<String, Object> dataMap;
                if (statusData instanceof Map) {
                    dataMap = (Map<String, Object>) statusData;
                } else if (statusData instanceof String) {
                    try {
                        dataMap = JSON.parseObject((String) statusData, Map.class);
                    } catch (Exception e) {
                        dataMap = null;
                    }
                } else {
                    String jsonStr = JSON.toJSONString(statusData);
                    dataMap = JSON.parseObject(jsonStr, Map.class);
                }
                if (dataMap != null && dataMap.containsKey("currentIndex")) {
                    // 有currentIndex，使用细粒度进度计算
                    progress = calculateDetailedProgress(status.getCode(), dataMap);
                } else {
                    // 没有currentIndex，使用起始进度
                    progress = getBaseProgress(status);
                }
            } else {
                // 没有statusData，使用起始进度
                progress = getBaseProgress(status);
            }
        } else {
            // 断点状态（USER_REVIEW_MATCHING、USER_REVIEW_ITEMS）或其他状态使用默认进度
            progress = calculateProgress(status);
        }
        process.setProgress(progress);
        
        processMapper.updateById(process);
        System.out.println("========== [后端] updateStatus ==========");
        System.out.println("processId=" + processId + ", status=" + status.getCode() + ", progress=" + progress);
        System.out.println("statusData=" + (statusData != null ? JSON.toJSONString(statusData) : "null"));
        log.info("更新处理状态: processId={}, status={}, progress={}", processId, status.getCode(), progress);
    }

    @Override
    public void updateProgress(String processId, Object statusData) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            throw new ServiceException("处理任务不存在: processId=" + processId);
        }

        //检查附件是否还存在
        KnowledgeAttach attach = attachMapper.selectById(process.getAttachId());
        if (attach == null) {
            log.warn("附件已删除，停止进度更新: processId={}, attachId={}", processId, process.getAttachId());
            return;
        }

        // 不改变状态，只更新 statusData 和进度
        if (statusData != null) {
            //将statusData转换为Map，确保包含阶段开始时间
            Map<String, Object> dataMap;
            if (statusData instanceof Map) {
                dataMap = new HashMap<>((Map<String, Object>) statusData);
            } else if (statusData instanceof String) {
                dataMap = JSON.parseObject((String) statusData, Map.class);
            } else {
                String jsonStr = JSON.toJSONString(statusData);
                dataMap = JSON.parseObject(jsonStr, Map.class);
            }
            
            //合并现有statusData，保留matchingResults等关键数据
            String existingStatusData = process.getStatusData();
            Map<String, Object> mergedDataMap = new HashMap<>();
            if (existingStatusData != null && !existingStatusData.trim().isEmpty() && !existingStatusData.equals("{}")) {
                try {
                    Map<String, Object> existingData = JSON.parseObject(existingStatusData, Map.class);
                    if (existingData != null) {
                        mergedDataMap.putAll(existingData);
                    }
                } catch (Exception e) {
                    log.warn("解析现有statusData失败，将使用新数据: processId={}, error={}", processId, e.getMessage());
                }
            }
            
            //用新数据覆盖（保留matchingResults等关键数据）
            mergedDataMap.putAll(dataMap);
            
            //确保有阶段开始时间
            if (!mergedDataMap.containsKey("stageStartTime")) {
                mergedDataMap.put("stageStartTime", System.currentTimeMillis());
            }
            
            String statusDataJson = JSON.toJSONString(mergedDataMap);
            process.setStatusData(statusDataJson);
            
            // 从 statusData 中提取进度信息，计算真实进度
            int calculatedProgress = calculateDetailedProgress(process.getCurrentStatus(), dataMap);
            process.setProgress(calculatedProgress);
            System.out.println("========== [后端] updateProgress ==========");
            System.out.println("processId=" + processId + ", currentStatus=" + process.getCurrentStatus());
            System.out.println("statusData=" + statusDataJson);
            System.out.println("calculatedProgress=" + calculatedProgress);
        } else {
            System.out.println("========== [后端] updateProgress (statusData为null) ==========");
            System.out.println("processId=" + processId + ", currentStatus=" + process.getCurrentStatus() + ", progress=" + process.getProgress());
        }
        
        processMapper.updateById(process);
        System.out.println("更新后的progress=" + process.getProgress());
        log.debug("更新处理进度: processId={}, status={}, progress={}", processId, process.getCurrentStatus(), process.getProgress());
    }

    /**
     * 基于实际处理数据计算细粒度进度
     * statusData 应包含：currentIndex（当前处理索引）、totalCount（总数）等信息
     */
    private int calculateDetailedProgress(String currentStatus, Object statusData) {
        System.out.println("========== [后端] calculateDetailedProgress ==========");
        System.out.println("currentStatus=" + currentStatus + ", statusData=" + (statusData != null ? statusData.toString() : "null"));
        
        if (statusData == null) {
            int defaultProgress = calculateProgress(ProcessingStatus.fromCode(currentStatus));
            System.out.println("statusData为null，返回默认进度=" + defaultProgress);
            return defaultProgress;
        }
        
        try {
            Map<String, Object> dataMap;
            if (statusData instanceof Map) {
                dataMap = (Map<String, Object>) statusData;
            } else if (statusData instanceof String) {
                dataMap = JSON.parseObject((String) statusData, Map.class);
            } else {
                String jsonStr = JSON.toJSONString(statusData);
                dataMap = JSON.parseObject(jsonStr, Map.class);
            }
            
            ProcessingStatus status = ProcessingStatus.fromCode(currentStatus);
            Integer currentIndex = (Integer) dataMap.get("currentIndex");
            Integer totalCount = (Integer) dataMap.get("totalCount");
            
            System.out.println("currentIndex=" + currentIndex + ", totalCount=" + totalCount);
            
            if (currentIndex != null && totalCount != null && totalCount > 0) {
                // 计算阶段内进度（0.0-1.0）
                double stageProgress = Math.min(1.0, (double) currentIndex / totalCount);
                
                // 根据状态计算阶段基础进度和进度范围
                int baseProgress = getBaseProgress(status);
                int progressRange = getProgressRange(status);
                
                System.out.println("baseProgress=" + baseProgress + ", progressRange=" + progressRange + ", stageProgress=" + stageProgress);
                
                // 总进度 = 基础进度 + (进度范围 * 阶段内进度)
                int detailedProgress = baseProgress + (int) (progressRange * stageProgress);
                int finalProgress = Math.min(100, Math.max(0, detailedProgress));
                System.out.println("计算出的详细进度=" + finalProgress);
                return finalProgress;
            } else {
                System.out.println("currentIndex或totalCount无效，使用默认进度");
            }
        } catch (Exception e) {
            System.out.println("计算细粒度进度失败: " + e.getMessage());
            e.printStackTrace();
            log.warn("计算细粒度进度失败，使用默认进度: processId={}, error={}", e.getMessage());
        }
        
        // 降级：使用默认进度
        int defaultProgress = calculateProgress(ProcessingStatus.fromCode(currentStatus));
        System.out.println("降级使用默认进度=" + defaultProgress);
        return defaultProgress;
    }

    /**
     * 获取阶段的基础进度（阶段开始时的进度）
     */
    private int getBaseProgress(ProcessingStatus status) {
        return switch (status) {
            case UPLOADING -> 0;
            case PARSING -> 5;
            case CHUNKING -> 15;
            case MATCHING -> 25;
            case USER_REVIEW_MATCHING -> 50;
            case CREATING_ITEMS -> 60;
            case USER_REVIEW_ITEMS -> 75;
            case VECTORIZING -> 85;
            case COMPLETED -> 100;
            case FAILED, CANCELLED -> 0;
        };
    }

    /**
     * 获取阶段的进度范围（阶段内可增加的进度）
     */
    private int getProgressRange(ProcessingStatus status) {
        return switch (status) {
            case UPLOADING -> 5;      // 0-5%
            case PARSING -> 10;        // 5-15%
            case CHUNKING -> 10;       // 15-25%
            case MATCHING -> 25;       // 25-50%
            case USER_REVIEW_MATCHING -> 10;  // 50-60%
            case CREATING_ITEMS -> 15; // 60-75%
            case USER_REVIEW_ITEMS -> 10;    // 75-85%
            case VECTORIZING -> 15;    // 85-100%
            case COMPLETED -> 0;
            case FAILED, CANCELLED -> 0;
        };
    }

    @Override
    public KnowledgeAttachProcessVo getCurrentStatus(String processId) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            throw new ServiceException("处理任务不存在: processId=" + processId);
        }
        KnowledgeAttachProcessVo vo = processMapper.selectVoById(Long.parseLong(processId));
        if (vo != null) {
            //计算处理速率和ETA
            calculateSpeedAndETA(process, vo);
        }
        return vo;
    }

    /**
     * 计算处理速率和预计剩余时间
     * 基于实际处理的数据量（片段/秒、条目/秒等），而不是进度百分比
     */
    private void calculateSpeedAndETA(KnowledgeAttachProcess process, KnowledgeAttachProcessVo vo) {
        if (process.getCreateTime() == null || process.getProgress() == null) {
            return;
        }
        
        //只在非断点状态计算速率（断点状态等待用户操作，不需要速率）
        String status = process.getCurrentStatus();
        if (status == null) {
            return;
        }
        
        //排除断点状态和完成/失败状态
        ProcessingStatus currentStatus;
        try {
            currentStatus = ProcessingStatus.fromCode(status);
        } catch (IllegalArgumentException e) {
            log.warn("未知的处理状态: {}", status);
            return;
        }
        
        //断点状态：等待用户操作，不需要速率
        if (currentStatus == ProcessingStatus.USER_REVIEW_MATCHING ||
            currentStatus == ProcessingStatus.USER_REVIEW_ITEMS) {
            return;
        }
        
        //完成/失败状态：不需要速率
        if (currentStatus == ProcessingStatus.COMPLETED ||
            currentStatus == ProcessingStatus.FAILED ||
            currentStatus == ProcessingStatus.CANCELLED) {
            return;
        }
        
        //解析statusData获取实际处理数据
        String statusDataStr = process.getStatusData();
        if (statusDataStr == null || statusDataStr.trim().isEmpty() || statusDataStr.equals("{}")) {
            return;//没有statusData，无法计算实际速率
        }
        
        try {
            Map<String, Object> statusData = JSON.parseObject(statusDataStr, Map.class);
            Integer currentIndex = (Integer) statusData.get("currentIndex");
            Integer totalCount = (Integer) statusData.get("totalCount");
            
            if (currentIndex == null || totalCount == null || totalCount <= 0 || currentIndex < 0) {
                return;//缺少必要数据
            }
            
            //获取阶段开始时间（从statusData中获取，如果没有则使用任务创建时间）
            Long stageStartTime = (Long) statusData.get("stageStartTime");
            if (stageStartTime == null) {
                //如果没有记录阶段开始时间，使用任务创建时间（首次计算）
                stageStartTime = process.getCreateTime().getTime();
            }
            
            long currentTime = System.currentTimeMillis();
            long elapsedMillis = currentTime - stageStartTime;
            
            if (elapsedMillis < 1000) {
                return;//时间太短，不计算速率
            }
            
            double elapsedSeconds = elapsedMillis / 1000.0;
            
            //计算实际处理速率（数据量/秒）
            double speed = currentIndex / elapsedSeconds;
            
            //根据阶段确定单位
            String unit = getSpeedUnit(currentStatus);
            
            //计算预计剩余时间（秒）
            int remaining = totalCount - currentIndex;
            long eta = 0;
            if (speed > 0 && remaining > 0) {
                eta = (long) Math.ceil(remaining / speed);
                //ETA合理性检查：不应该超过24小时
                if (eta > 24 * 3600) {
                    eta = 0;//ETA过长，不显示
                }
            }
            
            vo.setProcessingSpeed(speed);
            vo.setSpeedUnit(unit);
            vo.setEta(eta);
            
        } catch (Exception e) {
            log.warn("解析statusData计算速率失败: processId={}, error={}", process.getId(), e.getMessage());
        }
    }
    
    /**
     * 根据处理状态获取速率单位
     */
    private String getSpeedUnit(ProcessingStatus status) {
        return switch (status) {
            case PARSING -> "页/秒";
            case CHUNKING -> "片段/秒";
            case MATCHING -> "片段/秒";
            case CREATING_ITEMS -> "条目/秒";
            case VECTORIZING -> "片段/秒";
            case UPLOADING -> "字节/秒";
            default -> "项/秒";
        };
    }

    @Override
    public List<KnowledgeAttachProcessVo> getAttachProcessStatusBatch(List<String> processIds) {
        if (CollUtil.isEmpty(processIds)) {
            return Collections.emptyList();
        }
        List<Long> ids = processIds.stream()
            .map(Long::parseLong)
            .collect(Collectors.toList());
        LambdaQueryWrapper<KnowledgeAttachProcess> lqw = Wrappers.lambdaQuery();
        lqw.in(KnowledgeAttachProcess::getId, ids);
        List<KnowledgeAttachProcessVo> vos = processMapper.selectVoList(lqw);
        
        //为每个VO计算速率和ETA
        if (CollUtil.isNotEmpty(vos)) {
            List<KnowledgeAttachProcess> processes = processMapper.selectList(lqw);
            Map<Long, KnowledgeAttachProcess> processMap = processes.stream()
                .collect(Collectors.toMap(KnowledgeAttachProcess::getId, p -> p));
            
            for (KnowledgeAttachProcessVo vo : vos) {
                KnowledgeAttachProcess process = processMap.get(vo.getId());
                if (process != null) {
                    calculateSpeedAndETA(process, vo);
                }
            }
        }
        
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmMatching(String processId, List<MatchingDecision> decisions) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            throw new ServiceException("处理任务不存在: processId=" + processId);
        }

        //检查是否已取消
        if (ProcessingStatus.CANCELLED.getCode().equals(process.getCurrentStatus())) {
            log.info("处理任务已取消，停止确认匹配: processId={}", processId);
            throw new ServiceException("处理任务已取消");
        }

        //检查附件是否还存在
        KnowledgeAttach attach = attachMapper.selectById(process.getAttachId());
        if (attach == null) {
            log.warn("附件已删除，停止确认匹配: processId={}, attachId={}", processId, process.getAttachId());
            throw new ServiceException("附件已删除，无法继续处理");
        }

        // 解析当前状态数据
        Map<String, Object> statusDataMap = parseStatusData(process.getStatusData());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> matchingResults = (List<Map<String, Object>>) statusDataMap.getOrDefault("matchingResults", new ArrayList<>());

        // 更新状态为CREATING_ITEMS，保留matchingResults，立即返回，让前端关闭模态框
        // 批量更新片段表和LLM处理将在异步方法中执行
        Map<String, Object> newStatusData = new HashMap<>();
        newStatusData.put("matchingResults", matchingResults);
        updateStatus(processId, ProcessingStatus.CREATING_ITEMS, newStatusData);
        
        // 在异步方法调用前获取用户ID，确保异步线程中能正确设置createBy
        Long userId = LoginHelper.getUserId();
        
        // 异步执行批量更新片段表和LLM处理（通过Spring代理调用，确保@Async生效）
        AttachProcessServiceImpl self = SpringUtils.getAopProxy(this);
        self.updateFragmentsAndCreateItemsAsync(processId, process.getDocId(), decisions, matchingResults, attach.getKid(), userId);
        
        log.info("确认匹配结果完成（已提交异步处理）: processId={}, decisions={}", processId, decisions.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmItems(String processId, List<ItemModification> modifications) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            throw new ServiceException("处理任务不存在: processId=" + processId);
        }
        
        //检查是否已取消
        if (ProcessingStatus.CANCELLED.getCode().equals(process.getCurrentStatus())) {
            log.info("处理任务已取消，停止确认条目: processId={}", processId);
            throw new ServiceException("处理任务已取消");
        }
        
        //检查附件是否还存在
        KnowledgeAttach attach = attachMapper.selectById(process.getAttachId());
        if (attach == null) {
            log.warn("附件已删除，停止确认条目: processId={}, attachId={}", processId, process.getAttachId());
            throw new ServiceException("附件已删除，无法继续处理");
        }

        // 检查Redis分布式锁（允许同一用户重入，避免频繁操作时互斥）
        String lockKey = "attach_process_" + processId;
        String clientId = String.valueOf(LoginHelper.getUserId());
        String currentLockOwner = stringRedisTemplate.opsForValue().get(lockKey);
        
        // 如果锁已被其他用户持有，拒绝操作
        if (currentLockOwner != null && !currentLockOwner.equals(clientId)) {
            throw new ServiceException("处理任务正被其他用户编辑，请稍后重试");
        }
        
        // 如果是同一用户或锁不存在，获取/续期锁
        boolean hasLock = tryLock(lockKey, clientId, 300);
        if (!hasLock && (currentLockOwner == null || !currentLockOwner.equals(clientId))) {
            throw new ServiceException("处理任务正被其他用户编辑，请稍后重试");
        }

        try {
            // 批量创建/更新条目
            List<KnowledgeItem> itemsToInsert = new ArrayList<>();
            List<KnowledgeItem> itemsToUpdate = new ArrayList<>();
            String kid = attach.getKid();

            for (ItemModification modification : modifications) {
                String itemUuid = modification.getItemUuid();
                Map<String, Object> modifiedFields = modification.getModifiedFields();

                KnowledgeItem item = knowledgeItemMapper.selectOne(
                    Wrappers.<KnowledgeItem>lambdaQuery()
                        .eq(KnowledgeItem::getItemUuid, itemUuid)
                        .eq(KnowledgeItem::getDelFlag, "0")
                        .last("LIMIT 1")
                );

                if (item == null) {
                    // 创建新条目
                    item = new KnowledgeItem();
                    item.setItemUuid(itemUuid);
                    item.setKid(kid);
                    applyModifications(item, modifiedFields);
                    itemsToInsert.add(item);
                } else {
                    // 更新已有条目
                    applyModifications(item, modifiedFields);
                    itemsToUpdate.add(item);
                }
            }

            if (CollUtil.isNotEmpty(itemsToInsert)) {
                for (KnowledgeItem item : itemsToInsert) {
                    knowledgeItemMapper.insert(item);
                }
            }
            if (CollUtil.isNotEmpty(itemsToUpdate)) {
                for (KnowledgeItem item : itemsToUpdate) {
                    knowledgeItemMapper.updateById(item);
                }
            }

            // 更新片段关联
            Map<String, Object> statusDataMap = parseStatusData(process.getStatusData());
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> llmCreatedItems = (List<Map<String, Object>>) statusDataMap.getOrDefault("llmCreatedItems", new ArrayList<>());
            
            for (Map<String, Object> itemData : llmCreatedItems) {
                String itemUuid = (String) itemData.get("itemUuid");
                Integer chunkIndex = (Integer) itemData.get("chunkIndex");
                
                KnowledgeFragment fragment = fragmentMapper.selectOne(
                    Wrappers.<KnowledgeFragment>lambdaQuery()
                        .eq(KnowledgeFragment::getDocId, process.getDocId())
                        .eq(KnowledgeFragment::getIdx, chunkIndex)
                        .eq(KnowledgeFragment::getDelFlag, "0")
                        .last("LIMIT 1")
                );
                
                if (fragment != null) {
                    fragment.setItemUuid(itemUuid);
                    fragmentMapper.updateById(fragment);
                }
            }

            // 更新状态为VECTORIZING，保留llmCreatedItems，立即返回，让前端关闭模态框
            // 向量化存储将在异步方法中执行
            Map<String, Object> newStatusData = new HashMap<>();
            newStatusData.put("llmCreatedItems", llmCreatedItems);
            updateStatus(processId, ProcessingStatus.VECTORIZING, newStatusData);
            
            // 异步执行向量化存储（通过Spring代理调用，确保@Async生效）
            AttachProcessServiceImpl self = SpringUtils.getAopProxy(this);
            self.vectorizeItemsAsync(processId, process.getDocId(), attach.getKid());
            
            log.info("确认新条目完成（已提交异步向量化）: processId={}, modifications={}", processId, modifications.size());
        } catch (ServiceException e) {
            // ServiceException已经更新了状态，直接抛出
            throw e;
        } catch (Exception e) {
            log.error("确认条目阶段发生未预期的错误: processId={}, error={}", processId, e.getMessage(), e);
            markProcessFailed(processId, "确认条目阶段发生错误: " + e.getMessage());
            throw new ServiceException("确认条目失败: " + e.getMessage());
        } finally {
            // 释放锁
            unlock(lockKey, clientId);
        }
    }

    @Override
    public void saveDraft(String processId, Map<String, Object> partialData) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            throw new ServiceException("处理任务不存在: processId=" + processId);
        }

        // 检查Redis分布式锁（允许同一用户重入，避免频繁保存时互斥）
        String lockKey = "attach_process_" + processId;
        String clientId = String.valueOf(LoginHelper.getUserId());
        String currentLockOwner = stringRedisTemplate.opsForValue().get(lockKey);
        
        // 如果锁已被其他用户持有，拒绝操作
        if (currentLockOwner != null && !currentLockOwner.equals(clientId)) {
            throw new ServiceException("处理任务正被其他用户编辑，请稍后重试");
        }
        
        // 如果是同一用户或锁不存在，获取/续期锁
        boolean hasLock = tryLock(lockKey, clientId, 300);
        if (!hasLock && (currentLockOwner == null || !currentLockOwner.equals(clientId))) {
            throw new ServiceException("处理任务正被其他用户编辑，请稍后重试");
        }

        try {
            // 使用JSON_SET进行部分更新，提高性能
            // 对于复杂嵌套结构，使用JSON_MERGE_PATCH更合适
            // 但为了简化，这里先使用整体更新，后续可优化为JSON_SET
            
            // 解析当前状态数据
            Map<String, Object> statusDataMap = parseStatusData(process.getStatusData());
            
            // 深度合并部分数据（支持嵌套结构）
            mergeMapDeep(statusDataMap, partialData);
            
            // 保存
            String statusDataJson = JSON.toJSONString(statusDataMap);
            process.setStatusData(statusDataJson);
            processMapper.updateById(process);
            
            log.debug("保存草稿: processId={}", processId);
        } finally {
            // 释放锁（只有当前用户持有锁时才释放）
            if (clientId.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
                unlock(lockKey, clientId);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markProcessFailed(String processId, String message) {
        try {
            updateStatus(processId, ProcessingStatus.FAILED, message);
        } catch (Exception e) {
            log.error("标记处理任务失败状态时出错: processId={}, error={}", processId, e.getMessage(), e);
        }
    }

    /**
     * 异步更新片段表和创建知识条目（LLM提取和条目创建）
     * 此方法在后台线程中执行，不会阻塞HTTP请求
     * 注意：不使用方法级别的事务，避免长时间持有数据库锁
     */
    @Async
    public void updateFragmentsAndCreateItemsAsync(String processId, String docId, List<MatchingDecision> decisions, 
                                                   List<Map<String, Object>> matchingResults, String kid, Long userId) {
        log.info("开始异步更新片段表和创建知识条目: processId={}, 片段数量={}", processId, decisions.size());
        try {
            // 检查处理任务是否还存在且未被取消
            if (!checkProcessStatus(processId)) {
                return;
            }
            
            // 1. 批量更新片段表
            // 1.1 提取所有需要查询的 chunkIndex
            List<Integer> chunkIndexes = decisions.stream()
                .map(MatchingDecision::getChunkIndex)
                .distinct()
                .collect(Collectors.toList());
            
            if (CollUtil.isNotEmpty(chunkIndexes)) {
                // 1.2 批量查询所有需要的片段（一次查询）
                List<KnowledgeFragment> fragments = fragmentMapper.selectList(
                    Wrappers.<KnowledgeFragment>lambdaQuery()
                        .eq(KnowledgeFragment::getDocId, docId)
                        .in(KnowledgeFragment::getIdx, chunkIndexes)
                        .eq(KnowledgeFragment::getDelFlag, "0")
                );
                
                // 1.3 构建 Map 便于查找（key: chunkIndex, value: fragment）
                Map<Integer, KnowledgeFragment> fragmentMap = fragments.stream()
                    .collect(Collectors.toMap(KnowledgeFragment::getIdx, f -> f, (f1, f2) -> f1));
                
                // 1.4 构建 matchingResults 的 Map（key: chunkIndex, value: matchedItemUuid）
                Map<Integer, String> matchedItemUuidMap = new HashMap<>();
                for (Map<String, Object> result : matchingResults) {
                    Integer idx = (Integer) result.get("chunkIndex");
                    String matchedItemUuid = (String) result.get("matchedItemUuid");
                    if (idx != null && StringUtils.isNotBlank(matchedItemUuid)) {
                        matchedItemUuidMap.put(idx, matchedItemUuid);
                    }
                }
                
                // 1.5 遍历 decisions，更新对应的 fragment
                List<KnowledgeFragment> fragmentsToUpdate = new ArrayList<>();
                for (MatchingDecision decision : decisions) {
                    Integer chunkIndex = decision.getChunkIndex();
                    String decisionType = decision.getDecision();
                    
                    KnowledgeFragment fragment = fragmentMap.get(chunkIndex);
                    if (fragment == null) {
                        log.warn("片段不存在: docId={}, chunkIndex={}", docId, chunkIndex);
                        continue;
                    }
                    
                    boolean needUpdate = false;
                    if ("keep".equals(decisionType)) {
                        // 保持原有匹配，从matchedItemUuidMap中获取matchedItemUuid
                        String matchedItemUuid = matchedItemUuidMap.get(chunkIndex);
                        if (StringUtils.isNotBlank(matchedItemUuid)) {
                            fragment.setItemUuid(matchedItemUuid);
                            needUpdate = true;
                        }
                    } else if ("change".equals(decisionType)) {
                        // 关联到用户选择的其他条目
                        if (StringUtils.isNotBlank(decision.getSelectedItemUuid())) {
                            fragment.setItemUuid(decision.getSelectedItemUuid());
                            needUpdate = true;
                        }
                    } else if ("create_new".equals(decisionType)) {
                        // 标记为需要创建新条目，itemUuid暂时为null
                        fragment.setItemUuid(null);
                        needUpdate = true;
                    }
                    
                    if (needUpdate) {
                        fragmentsToUpdate.add(fragment);
                    }
                }
                
                // 1.6 批量更新所有片段（分批更新，每批100条）
                if (CollUtil.isNotEmpty(fragmentsToUpdate)) {
                    int batchSize = 100;
                    for (int i = 0; i < fragmentsToUpdate.size(); i += batchSize) {
                        int end = Math.min(i + batchSize, fragmentsToUpdate.size());
                        List<KnowledgeFragment> batch = fragmentsToUpdate.subList(i, end);
                        for (KnowledgeFragment fragment : batch) {
                            fragmentMapper.updateById(fragment);
                        }
                    }
                    log.info("批量更新片段完成: processId={}, 更新数量={}", processId, fragmentsToUpdate.size());
                }
            }
            
            // 2. 查找需要创建新条目的片段
            List<KnowledgeFragment> fragmentsToCreateItems = fragmentMapper.selectList(
                Wrappers.<KnowledgeFragment>lambdaQuery()
                    .eq(KnowledgeFragment::getDocId, docId)
                    .isNull(KnowledgeFragment::getItemUuid)
                    .eq(KnowledgeFragment::getDelFlag, "0")
            );
            
            // 3. 如果有需要创建新条目的片段，执行LLM提取和条目创建
            if (CollUtil.isNotEmpty(fragmentsToCreateItems)) {
                createItemsAsync(processId, fragmentsToCreateItems, kid, userId);
            } else {
                // 没有需要创建新条目的片段，直接进入向量化
                // 更新状态为VECTORIZING，然后异步执行向量化存储
                updateStatus(processId, ProcessingStatus.VECTORIZING, null);
                // 异步执行向量化存储（通过Spring代理调用，确保@Async生效）
                AttachProcessServiceImpl self = SpringUtils.getAopProxy(this);
                self.vectorizeItemsAsync(processId, docId, kid);
            }
        } catch (Exception e) {
            log.error("异步更新片段表和创建条目失败: processId={}, error={}", processId, e.getMessage(), e);
            markProcessFailed(processId, "更新片段表和创建条目失败: " + e.getMessage());
        }
    }

    /**
     * 异步创建知识条目（LLM提取和条目创建）
     * 此方法在后台线程中执行，不会阻塞HTTP请求
     * 注意：不使用方法级别的事务，避免长时间持有数据库锁
     */
    @Async
    public void createItemsAsync(String processId, List<KnowledgeFragment> fragmentsToCreateItems, String kid, Long userId) {
        log.info("开始异步创建知识条目: processId={}, 片段数量={}", processId, fragmentsToCreateItems.size());
        try {
            // 检查处理任务是否还存在且未被取消（使用独立事务）
            if (!checkProcessStatus(processId)) {
                return;
            }
            
            // 构建提取上下文（不需要事务）
            ExtractionContext context;
            try {
                context = buildExtractionContext(kid);
            } catch (Exception e) {
                log.error("构建提取上下文失败: processId={}, error={}", processId, e.getMessage(), e);
                markProcessFailed(processId, "构建提取上下文失败: " + e.getMessage());
                return;
            }
            
            // 逐个处理：对每个片段，调用LLM提取 -> 创建条目 -> 更新进度
            // 这样每完成一个条目，进度就会更新一次，用户可以看到实时进度
            List<Map<String, Object>> llmCreatedItems = new ArrayList<>();
            int totalCount = fragmentsToCreateItems.size();
            
            // 设置初始进度（currentIndex=0，进度为60%，即CREATING_ITEMS阶段的起始进度）
            // 这样用户看到的是从60%开始逐步增长，而不是从75%跳回60%
            Map<String, Object> initialProgressData = new HashMap<>();
            initialProgressData.put("currentIndex", 0);
            initialProgressData.put("totalCount", totalCount);
            initialProgressData.put("stage", "CREATING_ITEMS");
            try {
                updateProgress(processId, initialProgressData);
            } catch (Exception e) {
                log.warn("设置初始创建条目进度失败: processId={}, error={}", processId, e.getMessage());
            }
            for (int i = 0; i < totalCount; i++) {
                try {
                    // 再次检查任务状态（可能在处理过程中被取消）
                    if (!checkProcessStatus(processId)) {
                        log.info("处理任务状态已变更，停止创建条目: processId={}", processId);
                        return;
                    }
                    
                    KnowledgeFragment fragment = fragmentsToCreateItems.get(i);
                    
                    // 调用LLM提取该片段的数据
                    ExtractedItemData extractedData;
                    try {
                        extractedData = extractionService.extractFromChunk(fragment.getContent(), context);
                        if (extractedData == null) {
                            throw new ServiceException("LLM提取返回数据为空");
                        }
                    } catch (Exception e) {
                        log.error("LLM提取片段失败: processId={}, chunkIndex={}, error={}", 
                                processId, fragment.getIdx(), e.getMessage(), e);
                        markProcessFailed(processId, "LLM提取失败（片段" + fragment.getIdx() + "）: " + e.getMessage());
                        return;
                    }
                    
                    // 在独立事务中创建单个条目
                    Map<String, Object> itemData = createSingleItemInTransaction(fragment, extractedData, kid, userId);
                    if (itemData != null) {
                        llmCreatedItems.add(itemData);
                    }
                    
                    // 每完成一个条目就更新进度和statusData中的llmCreatedItems（确保过滤能立即生效）
                    Map<String, Object> progressData = new HashMap<>();
                    progressData.put("currentIndex", i + 1);
                    progressData.put("totalCount", totalCount);
                    progressData.put("stage", "CREATING_ITEMS");
                    progressData.put("llmCreatedItems", new ArrayList<>(llmCreatedItems)); // 立即更新llmCreatedItems列表
                    try {
                        updateProgress(processId, progressData);
                    } catch (Exception e) {
                        log.warn("更新创建条目进度失败: processId={}, error={}", processId, e.getMessage());
                    }
                } catch (Exception e) {
                    log.error("处理片段失败: processId={}, chunkIndex={}, error={}", 
                            processId, fragmentsToCreateItems.get(i).getIdx(), e.getMessage(), e);
                    markProcessFailed(processId, "处理片段失败（片段" + fragmentsToCreateItems.get(i).getIdx() + "）: " + e.getMessage());
                    return;
                }
            }
            
            // 更新状态数据（使用独立事务）
            Map<String, Object> statusData = new HashMap<>();
            statusData.put("llmCreatedItems", llmCreatedItems);
            updateStatus(processId, ProcessingStatus.USER_REVIEW_ITEMS, statusData);
            log.info("异步创建知识条目完成: processId={}, 创建条目数={}", processId, llmCreatedItems.size());
        } catch (Exception e) {
            log.error("异步创建条目阶段发生未预期的错误: processId={}, error={}", processId, e.getMessage(), e);
            markProcessFailed(processId, "创建条目阶段发生错误: " + e.getMessage());
        }
    }

    /**
     * 异步向量化存储（确认条目后执行）
     * 此方法在后台线程中执行，不会阻塞HTTP请求
     * 注意：不使用方法级别的事务，避免长时间持有数据库锁
     */
    @Async
    public void vectorizeItemsAsync(String processId, String docId, String kid) {
        log.info("开始异步向量化存储: processId={}, docId={}", processId, docId);
        try {
            // 检查处理任务是否还存在且未被取消
            if (!checkProcessStatus(processId)) {
                return;
            }
            
            KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
            if (process == null) {
                log.warn("处理任务不存在，停止向量化: processId={}", processId);
                return;
            }
            
            KnowledgeAttach attachForVector = attachMapper.selectById(process.getAttachId());
            if (attachForVector == null) {
                log.warn("附件已删除，停止向量化: processId={}, attachId={}", processId, process.getAttachId());
                markProcessFailed(processId, "附件已删除，无法继续处理");
                return;
            }
            String kidForVector = attachForVector.getKid();
            
            KnowledgeInfoVo knowledgeInfoVo = knowledgeInfoMapper.selectVoOne(
                Wrappers.<KnowledgeInfo>lambdaQuery()
                    .eq(KnowledgeInfo::getKid, kidForVector)
                    .last("LIMIT 1")
            );
            
            if (knowledgeInfoVo == null) {
                markProcessFailed(processId, "知识库不存在: kid=" + kidForVector);
                return;
            }
            
            ChatModelVo chatModelVo = chatModelService.selectModelByName(knowledgeInfoVo.getEmbeddingModelName());
            if (chatModelVo == null) {
                chatModelVo = chatModelService.selectModelByCategoryWithHighestPriority("vector");
                if (chatModelVo == null) {
                    markProcessFailed(processId, "未找到可用的向量模型，请先在chat_model表中配置category='vector'的模型");
                    return;
                }
            }
            
            // 查询所有片段
            List<KnowledgeFragment> allFragments = fragmentMapper.selectList(
                Wrappers.<KnowledgeFragment>lambdaQuery()
                    .eq(KnowledgeFragment::getDocId, docId)
                    .eq(KnowledgeFragment::getDelFlag, "0")
            );
            
            if (CollUtil.isEmpty(allFragments)) {
                markProcessFailed(processId, "未找到需要向量化的片段");
                return;
            }
            
            List<String> fids = allFragments.stream()
                .map(KnowledgeFragment::getFid)
                .collect(Collectors.toList());
            
            List<String> chunkList = allFragments.stream()
                .sorted(Comparator.comparing(KnowledgeFragment::getIdx))
                .map(KnowledgeFragment::getContent)
                .collect(Collectors.toList());
            
            StoreEmbeddingBo storeEmbeddingBo = new StoreEmbeddingBo();
            storeEmbeddingBo.setKid(kidForVector);
            storeEmbeddingBo.setDocId(docId);
            storeEmbeddingBo.setFids(fids);
            storeEmbeddingBo.setChunkList(chunkList);
            storeEmbeddingBo.setVectorStoreName(knowledgeInfoVo.getVectorModelName());
            storeEmbeddingBo.setEmbeddingModelName(knowledgeInfoVo.getEmbeddingModelName());
            storeEmbeddingBo.setApiKey(chatModelVo.getApiKey());
            storeEmbeddingBo.setBaseUrl(chatModelVo.getApiHost());
            
            try {
                //传递processId用于进度更新
                storeEmbeddingBo.setProcessId(processId);
                vectorStoreService.storeEmbeddings(storeEmbeddingBo, this);
            } catch (Exception e) {
                log.error("向量化存储失败: processId={}, error={}", processId, e.getMessage(), e);
                markProcessFailed(processId, "向量化存储失败: " + e.getMessage());
                return;
            }

            //最后检查一次是否已取消
            process = processMapper.selectById(Long.parseLong(processId));
            if (process == null || ProcessingStatus.CANCELLED.getCode().equals(process.getCurrentStatus())) {
                log.info("处理任务已取消，停止更新完成状态: processId={}", processId);
                return;
            }

            // 更新状态为COMPLETED
            updateStatus(processId, ProcessingStatus.COMPLETED, null);
            
            // 更新知识库统计字段（正式入队）
            if (StringUtils.isNotBlank(kidForVector)) {
                knowledgeItemMapper.updateKnowledgeItemCount(kidForVector);
                knowledgeItemMapper.updateKnowledgeFragmentCount(kidForVector);
                knowledgeItemService.updateKnowledgeDataSize(kidForVector);
            }
            
            log.info("异步向量化存储完成: processId={}", processId);
        } catch (Exception e) {
            log.error("异步向量化阶段发生未预期的错误: processId={}, error={}", processId, e.getMessage(), e);
            markProcessFailed(processId, "向量化阶段发生错误: " + e.getMessage());
        }
    }

    /**
     * 检查处理任务状态（使用独立事务，避免长时间持有锁）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean checkProcessStatus(String processId) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            log.warn("处理任务不存在，停止异步创建条目: processId={}", processId);
            return false;
        }
        if (ProcessingStatus.CANCELLED.getCode().equals(process.getCurrentStatus())) {
            log.info("处理任务已取消，停止异步创建条目: processId={}", processId);
            return false;
        }
        if (ProcessingStatus.FAILED.getCode().equals(process.getCurrentStatus())) {
            log.info("处理任务已失败，停止异步创建条目: processId={}", processId);
            return false;
        }
        return true;
    }

    /**
     * 在独立事务中创建单个知识条目（避免长时间持有锁）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Map<String, Object> createSingleItemInTransaction(KnowledgeFragment fragment, ExtractedItemData extractedData, String kid, Long userId) {
        // 系统字段：由代码强制设置，不受LLM输出影响
        String itemUuid = RandomUtil.randomString(32);
        KnowledgeItem newItem = new KnowledgeItem();
        newItem.setItemUuid(itemUuid);
        newItem.setKid(kid);
        newItem.setCreateTime(new Date());
        newItem.setDelFlag("0");
        newItem.setStatus("draft");
        newItem.setSourceType("import");
        //显式设置 createBy，确保异步线程中也能正确设置创建人
        if (userId != null) {
            newItem.setCreateBy(userId);
            newItem.setUpdateBy(userId);
        }
        
        // LLM生成的字段
        newItem.setTitle(extractedData.getTitle() != null ? extractedData.getTitle() : "新条目-" + fragment.getIdx());
        newItem.setSummary(extractedData.getSummary());
        newItem.setProblemDescription(extractedData.getProblemDescription());
        newItem.setFixSolution(extractedData.getFixSolution());
        newItem.setExampleCode(extractedData.getExampleCode());
        // 设置 vulnerabilityType：优先使用 vulnerabilityTypes 的第一个，否则使用 vulnerabilityType
        if (CollUtil.isNotEmpty(extractedData.getVulnerabilityTypes())) {
            newItem.setVulnerabilityType(extractedData.getVulnerabilityTypes().get(0));
        } else {
            newItem.setVulnerabilityType(extractedData.getVulnerabilityType());
        }
        newItem.setLanguage(extractedData.getLanguage());
        // 根据CVSS维度生成cvssVector字符串并计算评分
        if (StringUtils.isNotBlank(extractedData.getCvssAttackVector()) 
            && StringUtils.isNotBlank(extractedData.getCvssAttackComplexity())
            && StringUtils.isNotBlank(extractedData.getCvssPrivilegesRequired())
            && StringUtils.isNotBlank(extractedData.getCvssUserInteraction())
            && CollUtil.isNotEmpty(extractedData.getCvssImpact())) {
            StringBuilder cvssBuilder = new StringBuilder("CVSS:4.0");
            cvssBuilder.append("/AV:").append(extractedData.getCvssAttackVector());
            cvssBuilder.append("/AC:").append(extractedData.getCvssAttackComplexity());
            cvssBuilder.append("/AT:N");
            cvssBuilder.append("/PR:").append(extractedData.getCvssPrivilegesRequired());
            cvssBuilder.append("/UI:").append(extractedData.getCvssUserInteraction());
            List<String> impacts = extractedData.getCvssImpact();
            boolean hasC = impacts.contains("C");
            boolean hasI = impacts.contains("I");
            boolean hasA = impacts.contains("A");
            cvssBuilder.append("/VC:").append(hasC ? "H" : "N");
            cvssBuilder.append("/VI:").append(hasI ? "H" : "N");
            cvssBuilder.append("/VA:").append(hasA ? "H" : "N");
            cvssBuilder.append("/SC:N/SI:N/SA:N");
            String cvssVector = cvssBuilder.toString();
            if (cvssVector.length() > 255) {
                cvssVector = cvssVector.substring(0, 255);
            }
            newItem.setCvssVector(cvssVector);
            newItem.setCvssVersion("4.0");
            
            //计算CVSS评分
            java.math.BigDecimal cvssScore = CvssScoreCalculator.calculateCvssScore(
                extractedData.getCvssAttackVector(),
                extractedData.getCvssAttackComplexity(),
                extractedData.getCvssPrivilegesRequired(),
                extractedData.getCvssUserInteraction(),
                extractedData.getCvssImpact()
            );
            if (cvssScore != null) {
                newItem.setCvssScore(cvssScore);
            }
            //severity字段只保存LLM生成或用户手动设置的值，不保存CVSS计算出的severity
            //CVSS计算出的severity仅用于前端显示，不存储到数据库
            if (StringUtils.isNotBlank(extractedData.getSeverity())) {
                newItem.setSeverity(extractedData.getSeverity());
            }
        } else {
            //如果CVSS维度不完整，使用LLM返回的severity（如果有）
            if (StringUtils.isNotBlank(extractedData.getSeverity())) {
                newItem.setSeverity(extractedData.getSeverity());
            }
        }
        
        knowledgeItemMapper.insert(newItem);
        
        // 保存漏洞类型到关联表（使用数据库中实际存储的CWE ID格式）
        List<String> vulnerabilityTypesToSave = extractedData.getVulnerabilityTypes();
        if (CollUtil.isEmpty(vulnerabilityTypesToSave) && StringUtils.isNotBlank(extractedData.getVulnerabilityType())) {
            vulnerabilityTypesToSave = Collections.singletonList(extractedData.getVulnerabilityType());
        }
        if (CollUtil.isNotEmpty(vulnerabilityTypesToSave)) {
            // 查询所有可用的CWE ID（从数据库获取实际格式）
            List<CweReferenceVo> allCwes = cweReferenceService.queryList(new org.ruoyi.domain.bo.CweReferenceBo());
            // 创建映射：标准化格式 -> 数据库实际格式
            Map<String, String> cweIdMap = new HashMap<>();
            for (CweReferenceVo cwe : allCwes) {
                if (StringUtils.isNotBlank(cwe.getCweId())) {
                    String dbCweId = cwe.getCweId();
                    // 创建多个可能的匹配键
                    String normalized = dbCweId.trim().toUpperCase();
                    cweIdMap.put(normalized, dbCweId);
                    if (normalized.startsWith("CWE-")) {
                        String numericPart = normalized.replace("CWE-", "");
                        cweIdMap.put(numericPart, dbCweId);
                        cweIdMap.put("CWE-" + numericPart, dbCweId);
                    }
                }
            }
            
            String tenantId = LoginHelper.getTenantId();
            Long tenantIdLong = 0L;
            if (StringUtils.isNotBlank(tenantId)) {
                try {
                    tenantIdLong = Long.parseLong(tenantId);
                } catch (NumberFormatException e) {
                    tenantIdLong = 0L;
                }
            }
            final Long finalTenantId = tenantIdLong;
            List<KnowledgeItemVulnerabilityType> vulnList = vulnerabilityTypesToSave.stream()
                    .filter(StringUtils::isNotBlank)
                    .map(cweId -> {
                        // 标准化输入格式用于匹配
                        String normalized = cweId.trim().toUpperCase();
                        String matchedCweId = null;
                        
                        // 尝试精确匹配
                        if (cweIdMap.containsKey(normalized)) {
                            matchedCweId = cweIdMap.get(normalized);
                        } else if (normalized.startsWith("CWE-")) {
                            // 尝试数字部分匹配
                            String numericPart = normalized.replace("CWE-", "");
                            if (cweIdMap.containsKey(numericPart)) {
                                matchedCweId = cweIdMap.get(numericPart);
                            }
                        } else {
                            // 尝试添加CWE-前缀匹配
                            String withPrefix = "CWE-" + normalized;
                            if (cweIdMap.containsKey(withPrefix)) {
                                matchedCweId = cweIdMap.get(withPrefix);
                            } else if (cweIdMap.containsKey(normalized)) {
                                matchedCweId = cweIdMap.get(normalized);
                            }
                        }
                        
                        return matchedCweId;
                    })
                    .filter(cweId -> cweId != null)
                    .distinct()
                    .map(cweId -> {
                        KnowledgeItemVulnerabilityType item = new KnowledgeItemVulnerabilityType();
                        item.setItemUuid(itemUuid);
                        item.setCweId(cweId);
                        item.setTenantId(finalTenantId);
                        return item;
                    })
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(vulnList)) {
                vulnerabilityTypeMapper.insertBatch(vulnList);
            } else {
                log.warn("[保存漏洞类型] 所有CWE ID验证失败，未保存任何漏洞类型关联，itemUuid={}, 输入的CWE IDs={}", 
                    itemUuid, vulnerabilityTypesToSave);
            }
        }
        
        // 保存标签到关联表
        if (CollUtil.isNotEmpty(extractedData.getTags())) {
            LambdaQueryWrapper<KnowledgeTag> tagLqw = Wrappers.lambdaQuery();
            tagLqw.in(KnowledgeTag::getTagName, extractedData.getTags());
            List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagLqw);
            if (CollUtil.isNotEmpty(tags)) {
                String tenantId = LoginHelper.getTenantId();
                Long tenantIdLong = 0L;
                if (StringUtils.isNotBlank(tenantId)) {
                    try {
                        tenantIdLong = Long.parseLong(tenantId);
                    } catch (NumberFormatException e) {
                        tenantIdLong = 0L;
                    }
                }
                final Long finalTenantId = tenantIdLong;
                List<KnowledgeItemTag> itemTags = tags.stream()
                        .map(tag -> {
                            KnowledgeItemTag itemTag = new KnowledgeItemTag();
                            itemTag.setItemUuid(itemUuid);
                            itemTag.setTagId(tag.getId());
                            itemTag.setTenantId(finalTenantId);
                            return itemTag;
                        })
                        .collect(Collectors.toList());
                if (CollUtil.isNotEmpty(itemTags)) {
                    for (KnowledgeItemTag itemTag : itemTags) {
                        itemTagMapper.insert(itemTag);
                    }
                }
            }
        }
        
        // 关联片段
        fragment.setItemUuid(itemUuid);
        fragmentMapper.updateById(fragment);
        
        // 返回条目数据用于记录到statusData
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("itemUuid", itemUuid);
        itemData.put("chunkIndex", fragment.getIdx());
        itemData.put("fid", fragment.getFid());
        itemData.put("extractedData", extractedData);
        return itemData;
    }

    /**
     * 深度合并Map（支持嵌套结构）
     */
    @SuppressWarnings("unchecked")
    private void mergeMapDeep(Map<String, Object> target, Map<String, Object> source) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (target.containsKey(key) && target.get(key) instanceof Map && value instanceof Map) {
                // 递归合并嵌套Map
                mergeMapDeep((Map<String, Object>) target.get(key), (Map<String, Object>) value);
            } else if (target.containsKey(key) && target.get(key) instanceof List && value instanceof List) {
                // 对于List，直接替换（如果需要合并，可以进一步优化）
                target.put(key, value);
            } else {
                // 直接替换或新增
                target.put(key, value);
            }
        }
    }

    @Override
    public void rollback(String processId, ProcessingStatus targetStatus) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            throw new ServiceException("处理任务不存在: processId=" + processId);
        }

        ProcessingStatus currentStatus = ProcessingStatus.fromCode(process.getCurrentStatus());
        if (!canTransition(currentStatus, targetStatus)) {
            throw new ServiceException("不允许的状态回退: " + currentStatus.getCode() + " -> " + targetStatus.getCode());
        }

        process.setCurrentStatus(targetStatus.getCode());
        process.setProgress(calculateProgress(targetStatus));
        processMapper.updateById(process);
        
        log.info("回退状态: processId={}, targetStatus={}", processId, targetStatus.getCode());
    }

    @Override
    public boolean tryLock(String processId) {
        String lockKey = "attach_process_" + processId;
        String clientId = String.valueOf(LoginHelper.getUserId());
        return tryLock(lockKey, clientId, 300);
    }

    @Override
    public void cancelProcess(String processId) {
        KnowledgeAttachProcess process = processMapper.selectById(Long.parseLong(processId));
        if (process == null) {
            log.warn("取消处理任务失败: 任务不存在, processId={}", processId);
            return;
        }

        ProcessingStatus currentStatus = ProcessingStatus.fromCode(process.getCurrentStatus());
        if (currentStatus == ProcessingStatus.COMPLETED || currentStatus == ProcessingStatus.CANCELLED) {
            log.info("处理任务已完成或已取消，无需再次取消: processId={}, status={}", processId, currentStatus.getCode());
            return;
        }

        try {
            updateStatus(processId, ProcessingStatus.CANCELLED, "用户取消");
            log.info("取消处理任务成功: processId={}", processId);
        } catch (Exception e) {
            log.error("取消处理任务失败: processId={}, error={}", processId, e.getMessage(), e);
            throw new ServiceException("取消处理任务失败: " + e.getMessage());
        }
    }

    /**
     * 尝试获取Redis分布式锁
     */
    private boolean tryLock(String key, String clientId, int expireSeconds) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, clientId, expireSeconds, TimeUnit.SECONDS));
    }

    /**
     * 释放Redis分布式锁
     */
    private boolean unlock(String key, String clientId) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (clientId.equals(value)) {
            return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
        }
        return false;
    }

    /**
     * 验证状态转换是否允许
     */
    private boolean canTransition(ProcessingStatus from, ProcessingStatus to) {
        // 状态转换规则
        Map<ProcessingStatus, Set<ProcessingStatus>> allowedTransitions = new HashMap<>();
        allowedTransitions.put(ProcessingStatus.UPLOADING, Set.of(ProcessingStatus.PARSING, ProcessingStatus.FAILED, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.PARSING, Set.of(ProcessingStatus.CHUNKING, ProcessingStatus.FAILED, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.CHUNKING, Set.of(ProcessingStatus.MATCHING, ProcessingStatus.FAILED, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.MATCHING, Set.of(ProcessingStatus.USER_REVIEW_MATCHING, ProcessingStatus.FAILED, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.USER_REVIEW_MATCHING, Set.of(ProcessingStatus.CREATING_ITEMS, ProcessingStatus.MATCHING, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.CREATING_ITEMS, Set.of(ProcessingStatus.USER_REVIEW_ITEMS, ProcessingStatus.FAILED, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.USER_REVIEW_ITEMS, Set.of(ProcessingStatus.VECTORIZING, ProcessingStatus.CREATING_ITEMS, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.VECTORIZING, Set.of(ProcessingStatus.COMPLETED, ProcessingStatus.FAILED, ProcessingStatus.CANCELLED));
        allowedTransitions.put(ProcessingStatus.COMPLETED, Set.of());
        allowedTransitions.put(ProcessingStatus.FAILED, Set.of(ProcessingStatus.PARSING));
        allowedTransitions.put(ProcessingStatus.CANCELLED, Set.of());

        Set<ProcessingStatus> allowed = allowedTransitions.getOrDefault(from, Set.of());
        return allowed.contains(to);
    }

    /**
     * 计算进度百分比
     */
    private int calculateProgress(ProcessingStatus status) {
        return switch (status) {
            case UPLOADING -> 5;
            case PARSING -> 15;
            case CHUNKING -> 25;
            case MATCHING -> 50;
            case USER_REVIEW_MATCHING -> 60;
            case CREATING_ITEMS -> 75;
            case USER_REVIEW_ITEMS -> 85;
            case VECTORIZING -> 95;
            case COMPLETED -> 100;
            case FAILED, CANCELLED -> 0;
        };
    }

    /**
     * 解析状态数据JSON
     */
    private Map<String, Object> parseStatusData(String statusData) {
        if (StringUtils.isBlank(statusData)) {
            return new HashMap<>();
        }
        try {
            return JSON.parseObject(statusData, Map.class);
        } catch (Exception e) {
            log.error("解析状态数据失败: statusData={}, error={}", statusData, e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * 查找匹配的条目UUID
     */
    private String findMatchedItemUuid(List<Map<String, Object>> matchingResults, Integer chunkIndex, String fid) {
        for (Map<String, Object> result : matchingResults) {
            Integer idx = (Integer) result.get("chunkIndex");
            String resultFid = (String) result.get("fid");
            if (chunkIndex.equals(idx) && (fid == null || fid.equals(resultFid))) {
                return (String) result.get("matchedItemUuid");
            }
        }
        return null;
    }

    /**
     * 应用修改到条目
     */
    private void applyModifications(KnowledgeItem item, Map<String, Object> modifiedFields) {
        if (CollUtil.isEmpty(modifiedFields)) {
            return;
        }
        
        if (modifiedFields.containsKey("title")) {
            item.setTitle((String) modifiedFields.get("title"));
        }
        if (modifiedFields.containsKey("summary")) {
            item.setSummary((String) modifiedFields.get("summary"));
        }
        if (modifiedFields.containsKey("problemDescription")) {
            item.setProblemDescription((String) modifiedFields.get("problemDescription"));
        }
        if (modifiedFields.containsKey("fixSolution")) {
            item.setFixSolution((String) modifiedFields.get("fixSolution"));
        }
        if (modifiedFields.containsKey("exampleCode")) {
            item.setExampleCode((String) modifiedFields.get("exampleCode"));
        }
        if (modifiedFields.containsKey("vulnerabilityType")) {
            item.setVulnerabilityType((String) modifiedFields.get("vulnerabilityType"));
        }
        if (modifiedFields.containsKey("language")) {
            item.setLanguage((String) modifiedFields.get("language"));
        }
        if (modifiedFields.containsKey("severity")) {
            item.setSeverity((String) modifiedFields.get("severity"));
        }
        if (modifiedFields.containsKey("cvssVector")) {
            item.setCvssVector((String) modifiedFields.get("cvssVector"));
        }
        if (modifiedFields.containsKey("cvssScore")) {
            Object score = modifiedFields.get("cvssScore");
            if (score instanceof Number) {
                item.setCvssScore(new java.math.BigDecimal(score.toString()));
            }
        }
        if (modifiedFields.containsKey("cvssVersion")) {
            item.setCvssVersion((String) modifiedFields.get("cvssVersion"));
        }
        if (modifiedFields.containsKey("referenceLink")) {
            item.setReferenceLink((String) modifiedFields.get("referenceLink"));
        }
        if (modifiedFields.containsKey("status")) {
            item.setStatus((String) modifiedFields.get("status"));
        }
    }

    /**
     * 构建提取上下文
     */
    private ExtractionContext buildExtractionContext(String kid) {
        ExtractionContext context = new ExtractionContext();
        context.setKid(kid);
        
        // 查询知识库信息
        KnowledgeInfoVo knowledgeInfoVo = knowledgeInfoMapper.selectVoOne(
            Wrappers.<KnowledgeInfo>lambdaQuery()
                .eq(KnowledgeInfo::getKid, kid)
                .last("LIMIT 1")
        );
        
        if (knowledgeInfoVo != null) {
            context.setKnowledgeBaseName(knowledgeInfoVo.getKname());
            context.setKnowledgeBaseCategory(knowledgeInfoVo.getCategory());
            
            // 优先尝试使用知识库配置的embeddingModelName，但需要验证是否为chat类型
            // 如果embeddingModelName对应的模型不是chat类型，则使用chat分类的最高优先级模型
            String modelName = knowledgeInfoVo.getEmbeddingModelName();
            if (StringUtils.isNotBlank(modelName)) {
                ChatModelVo modelVo = chatModelService.selectModelByName(modelName);
                if (modelVo != null && "chat".equals(modelVo.getCategory())) {
                    // 验证模型配置（但不抛出异常，只记录警告，让后续流程处理）
                    if (!isModelConfigValid(modelVo)) {
                        log.warn("知识库 {} 配置的chat模型 {} 配置不完整（api_key或api_host可能为占位符），将在提取时提示用户", 
                            kid, modelName);
                    }
                    context.setModelName(modelName);
                } else {
                    ChatModelVo chatModelVo = chatModelService.selectChatModelForKnowledgeExtraction();
                    if (chatModelVo != null) {
                        context.setModelName(chatModelVo.getModelName());
                        log.info("知识库 {} 的embeddingModelName({})不是chat类型，使用智能选择的模型: {}", 
                            kid, modelName, chatModelVo.getModelName());
                    } else {
                        log.warn("知识库 {} 未找到可用的chat模型，将在提取时使用降级逻辑", kid);
                    }
                }
            } else {
                ChatModelVo chatModelVo = chatModelService.selectChatModelForKnowledgeExtraction();
                if (chatModelVo != null) {
                    context.setModelName(chatModelVo.getModelName());
                    log.info("知识库 {} 未配置模型，使用智能选择的chat模型: {}", kid, chatModelVo.getModelName());
                }
            }
        }
        
        // 查询所有可用标签
        List<KnowledgeTagVo> allTags = knowledgeTagService.queryList(new org.ruoyi.domain.bo.KnowledgeTagBo());
        context.setAvailableTags(allTags.stream()
            .map(KnowledgeTagVo::getTagName)
            .collect(Collectors.toList()));
        
        // 查询所有CWE参考
        List<CweReferenceVo> allCwes = cweReferenceService.queryList(new org.ruoyi.domain.bo.CweReferenceBo());
        context.setAvailableVulnerabilityTypes(allCwes.stream()
            .map(vo -> {
                org.ruoyi.domain.CweReference cwe = new org.ruoyi.domain.CweReference();
                cwe.setCweId(vo.getCweId());
                cwe.setNameEn(vo.getNameEn());
                cwe.setNameZh(vo.getNameZh());
                cwe.setStatus(vo.getStatus() != null ? vo.getStatus() : "Stable");
                return cwe;
            })
            .collect(Collectors.toList()));
        
        // 从字典服务获取语言和风险等级选项（使用dictValue，与前端一致）
        List<SysDictDataVo> languageDicts = dictTypeService.selectDictDataByType("knowledge_language");
        List<String> languages = languageDicts.stream()
            .map(SysDictDataVo::getDictValue)
            .collect(Collectors.toList());
        context.setAvailableLanguages(languages);
        
        List<SysDictDataVo> severityDicts = dictTypeService.selectDictDataByType("knowledge_severity");
        List<String> severities = severityDicts.stream()
            .map(SysDictDataVo::getDictValue)
            .collect(Collectors.toList());
        context.setAvailableSeverities(severities);
        
        return context;
    }

    /**
     * 检查模型配置是否有效（不抛出异常，仅用于日志记录）
     */
    private boolean isModelConfigValid(ChatModelVo modelVo) {
        if (modelVo == null) {
            return false;
        }
        if (StringUtils.isBlank(modelVo.getApiHost()) || StringUtils.isBlank(modelVo.getApiKey())) {
            return false;
        }
        String apiKey = modelVo.getApiKey().trim().toLowerCase();
        // 检查是否为占位符
        return !apiKey.equals("sk-xx") 
            && !apiKey.equals("sk-xxx")
            && !apiKey.equals("your-api-key")
            && !apiKey.equals("api-key")
            && !apiKey.startsWith("sk-placeholder")
            && !(apiKey.length() < 10 && apiKey.startsWith("sk-"));
    }
}
