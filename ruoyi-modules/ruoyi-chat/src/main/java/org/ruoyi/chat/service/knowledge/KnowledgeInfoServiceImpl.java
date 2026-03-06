package org.ruoyi.chat.service.knowledge;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.chain.loader.ResourceLoader;
import org.ruoyi.chain.loader.ResourceLoaderFactory;
import org.ruoyi.chat.enums.ChatModeType;
import org.ruoyi.common.core.domain.model.LoginUser;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.DateUtils;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.*;
import org.ruoyi.domain.bo.KnowledgeInfoBo;
import org.ruoyi.domain.vo.KnowledgeAttachVo;
import org.ruoyi.mapper.KnowledgeAttachProcessMapper;
import org.ruoyi.domain.bo.KnowledgeInfoUploadBo;
import org.ruoyi.domain.bo.StoreEmbeddingBo;
import org.ruoyi.domain.bo.QueryVectorBo;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.domain.vo.MatchDetailVo;
import org.ruoyi.embedding.EmbeddingModelFactory;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.ruoyi.domain.enums.ProcessingStatus;
import org.ruoyi.mapper.*;
import org.ruoyi.service.IAttachProcessService;
import org.ruoyi.service.IChatModelService;
import org.ruoyi.service.IKnowledgeInfoService;
import org.ruoyi.service.IKnowledgeItemService;
import org.ruoyi.service.VectorStoreService;
import org.ruoyi.system.service.ISysOssService;
import org.ruoyi.utils.FileUploadValidator;
import org.ruoyi.common.util.RateLimitHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识库Service业务层处理
 *
 * @author ageerle
 * @date 2025-04-08
 */
@RequiredArgsConstructor
@Service
public class KnowledgeInfoServiceImpl implements IKnowledgeInfoService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeInfoServiceImpl.class);

    private final KnowledgeInfoMapper baseMapper;

    private final VectorStoreService vectorStoreService;

    private final ResourceLoaderFactory resourceLoaderFactory;

    private final KnowledgeFragmentMapper fragmentMapper;

    private final KnowledgeAttachMapper attachMapper;

    private final IChatModelService chatModelService;

    private final ISysOssService ossService;

    private final KnowledgeRoleMapper knowledgeRoleMapper;

    private final KnowledgeRoleRelationMapper knowledgeRoleRelationMapper;

    private final KnowledgeItemMapper knowledgeItemMapper;

    private final EmbeddingModelFactory embeddingModelFactory;

    private final IAttachProcessService attachProcessService;
    
    private final KnowledgeAttachProcessMapper attachProcessMapper;
    
    private final org.ruoyi.service.IKnowledgeItemService knowledgeItemService;

    /**
     * 查询知识库
     */
    @Override
    public KnowledgeInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 根据kid查询知识库
     */
    @Override
    public KnowledgeInfoVo queryByKid(String kid) {
        return baseMapper.selectVoByKid(kid);
    }

    /**
     * 查询知识库列表
     */
    @Override
    public TableDataInfo<KnowledgeInfoVo> queryPageList(KnowledgeInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeInfo> lqw = buildQueryWrapper(bo);
        Page<KnowledgeInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        if (result.getRecords() != null) {
            LoginUser loginUser = LoginHelper.getLoginUser();
            Long currentUserId = loginUser.getUserId();
            for (KnowledgeInfoVo vo : result.getRecords()) {
                vo.setCanEdit(canEditKnowledge(vo, currentUserId));
            }
            if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
                sortByRelevance(result.getRecords(), bo.getSearchKeyword());
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 根据知识库角色查询知识库列表
     */
    @Override
    public TableDataInfo<KnowledgeInfoVo> queryPageListByRole(KnowledgeInfoBo bo, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        Long currentUserId = loginUser.getUserId();

        LambdaQueryWrapper<KnowledgeInfo> lqw = buildQueryWrapper(bo);

        if (Objects.equals(loginUser.getUserId(), 1L)) {
            String ownershipType = bo.getOwnershipType();
            if ("mine".equals(ownershipType)) {
                lqw.eq(KnowledgeInfo::getUid, loginUser.getUserId());
            } else if ("assigned".equals(ownershipType)) {
                lqw.eq(KnowledgeInfo::getId, -1L);
            }
            
            Page<KnowledgeInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
            if (result.getRecords() != null) {
                for (KnowledgeInfoVo vo : result.getRecords()) {
                    vo.setCanEdit(canEditKnowledge(vo, currentUserId));
                }
            }
            return TableDataInfo.build(result);
        }

        // 检查用户是否配置了角色信息
        if (StringUtils.isNotEmpty(loginUser.getKroleGroupIds()) && StringUtils.isNotEmpty(loginUser.getKroleGroupType())) {
            // 角色/角色组id列表
            List<String> groupIdList = Arrays.stream(loginUser.getKroleGroupIds().split(","))
                    .filter(StringUtils::isNotEmpty)
                    .toList();

            List<KnowledgeRole> knowledgeRoles = new ArrayList<>();
            LambdaQueryWrapper<KnowledgeRole> roleLqw = Wrappers.lambdaQuery();
            if ("role".equals(loginUser.getKroleGroupType())) {
                roleLqw.in(KnowledgeRole::getId, groupIdList);
            } else {
                roleLqw.in(KnowledgeRole::getGroupId, groupIdList);
            }
            knowledgeRoles = knowledgeRoleMapper.selectList(roleLqw);

            // 如果用户有关联角色
            if (!CollectionUtils.isEmpty(knowledgeRoles)) {
                LambdaQueryWrapper<KnowledgeRoleRelation> relationLqw = Wrappers.lambdaQuery();
                relationLqw.in(KnowledgeRoleRelation::getKnowledgeRoleId,
                        knowledgeRoles.stream().map(KnowledgeRole::getId).filter(Objects::nonNull).collect(Collectors.toList()));
                List<KnowledgeRoleRelation> knowledgeRoleRelations = knowledgeRoleRelationMapper.selectList(relationLqw);

                // 如果角色关联了知识库
                if (!CollectionUtils.isEmpty(knowledgeRoleRelations)) {
                    List<Long> assignedKnowledgeIds = knowledgeRoleRelations.stream()
                            .map(KnowledgeRoleRelation::getKnowledgeId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    
                    String ownershipType = bo.getOwnershipType();
                    if ("mine".equals(ownershipType)) {
                        lqw.eq(KnowledgeInfo::getUid, loginUser.getUserId());
                    } else if ("assigned".equals(ownershipType)) {
                        if (!assignedKnowledgeIds.isEmpty()) {
                            lqw.and(q -> q.in(KnowledgeInfo::getId, assignedKnowledgeIds)
                                    .and(w -> w.ne(KnowledgeInfo::getUid, loginUser.getUserId())));
                        } else {
                            lqw.eq(KnowledgeInfo::getId, -1L);
                        }
                    } else {
                        lqw.and(q -> q.eq(KnowledgeInfo::getUid, loginUser.getUserId())
                                .or()
                                .in(KnowledgeInfo::getId, assignedKnowledgeIds));
                    }
                } else {
                    // 用户没有关联任何知识库
                    String ownershipType = bo.getOwnershipType();
                    if ("assigned".equals(ownershipType)) {
                        // 如果筛选"分配给我的"但没有分配的知识库，返回空结果
                        lqw.eq(KnowledgeInfo::getId, -1L); // 永远不匹配的条件
                    } else {
                        // mine 或 all：只显示自己的
                        lqw.eq(KnowledgeInfo::getUid, loginUser.getUserId());
                    }
                }
            } else {
                // 用户没有关联角色
                String ownershipType = bo.getOwnershipType();
                if ("assigned".equals(ownershipType)) {
                    // 如果筛选"分配给我的"但没有角色，返回空结果
                    lqw.eq(KnowledgeInfo::getId, -1L); // 永远不匹配的条件
                } else {
                    // mine 或 all：只显示自己的
                    lqw.eq(KnowledgeInfo::getUid, loginUser.getUserId());
                }
            }
        } else {
            // 用户没有配置角色信息
            String ownershipType = bo.getOwnershipType();
            if ("assigned".equals(ownershipType)) {
                // 如果筛选"分配给我的"但没有配置角色，返回空结果
                lqw.eq(KnowledgeInfo::getId, -1L); // 永远不匹配的条件
            } else {
                // mine 或 all：只显示自己的
                lqw.eq(KnowledgeInfo::getUid, loginUser.getUserId());
            }
        }

        Page<KnowledgeInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        if (result.getRecords() != null && StringUtils.isNotBlank(bo.getSearchKeyword())) {
            sortByRelevance(result.getRecords(), bo.getSearchKeyword());
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询知识库列表
     */
    @Override
    public List<KnowledgeInfoVo> queryList(KnowledgeInfoBo bo) {
        LambdaQueryWrapper<KnowledgeInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KnowledgeInfo> buildQueryWrapper(KnowledgeInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KnowledgeInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeInfo::getKid, bo.getKid());
        lqw.eq(bo.getUid() != null, KnowledgeInfo::getUid, bo.getUid());
        
        if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
            String keyword = bo.getSearchKeyword();
            lqw.and(wrapper -> wrapper
                .like(KnowledgeInfo::getKname, keyword)
                .or()
                .like(KnowledgeInfo::getDescription, keyword)
            );
        } else {
            lqw.like(StringUtils.isNotBlank(bo.getKname()), KnowledgeInfo::getKname, bo.getKname());
            lqw.like(StringUtils.isNotBlank(bo.getDescription()), KnowledgeInfo::getDescription, bo.getDescription());
        }
        
        lqw.eq(bo.getShare() != null, KnowledgeInfo::getShare, bo.getShare());
        lqw.eq(StringUtils.isNotBlank(bo.getKnowledgeSeparator()), KnowledgeInfo::getKnowledgeSeparator,
                bo.getKnowledgeSeparator());
        lqw.eq(StringUtils.isNotBlank(bo.getQuestionSeparator()), KnowledgeInfo::getQuestionSeparator,
                bo.getQuestionSeparator());
        lqw.eq(bo.getOverlapChar() != null, KnowledgeInfo::getOverlapChar, bo.getOverlapChar());
        lqw.eq(bo.getRetrieveLimit() != null, KnowledgeInfo::getRetrieveLimit, bo.getRetrieveLimit());
        lqw.eq(bo.getTextBlockSize() != null, KnowledgeInfo::getTextBlockSize, bo.getTextBlockSize());
        //分类筛选（多选）
        if (CollectionUtils.isNotEmpty(bo.getCategories())) {
            lqw.in(KnowledgeInfo::getCategory, bo.getCategories());
        }
        //创建人筛选（多选）
        if (CollectionUtils.isNotEmpty(bo.getCreateBys())) {
            lqw.in(KnowledgeInfo::getCreateBy, bo.getCreateBys());
        }
        //创建部门筛选（多选）
        if (CollectionUtils.isNotEmpty(bo.getCreateDepts())) {
            lqw.in(KnowledgeInfo::getCreateDept, bo.getCreateDepts());
        }
        //条目数区间筛选
        lqw.ge(bo.getItemCountMin() != null, KnowledgeInfo::getItemCount, bo.getItemCountMin());
        lqw.le(bo.getItemCountMax() != null, KnowledgeInfo::getItemCount, bo.getItemCountMax());
        //片段数区间筛选
        lqw.ge(bo.getFragmentCountMin() != null, KnowledgeInfo::getFragmentCount, bo.getFragmentCountMin());
        lqw.le(bo.getFragmentCountMax() != null, KnowledgeInfo::getFragmentCount, bo.getFragmentCountMax());
        //存储大小区间筛选
        lqw.ge(bo.getDataSizeMin() != null, KnowledgeInfo::getDataSize, bo.getDataSizeMin());
        lqw.le(bo.getDataSizeMax() != null, KnowledgeInfo::getDataSize, bo.getDataSizeMax());
        //创建时间区间筛选
        if (StringUtils.isNotBlank(bo.getCreateTimeStart())) {
            lqw.ge(KnowledgeInfo::getCreateTime, DateUtils.parseDate(bo.getCreateTimeStart()));
        }
        if (StringUtils.isNotBlank(bo.getCreateTimeEnd())) {
            lqw.le(KnowledgeInfo::getCreateTime, DateUtils.parseDate(bo.getCreateTimeEnd()));
        }
        //更新时间区间筛选
        if (StringUtils.isNotBlank(bo.getUpdateTimeStart())) {
            lqw.ge(KnowledgeInfo::getUpdateTime, DateUtils.parseDate(bo.getUpdateTimeStart()));
        }
        if (StringUtils.isNotBlank(bo.getUpdateTimeEnd())) {
            lqw.le(KnowledgeInfo::getUpdateTime, DateUtils.parseDate(bo.getUpdateTimeEnd()));
        }
        //排序
        if (StringUtils.isNotBlank(bo.getOrderBy())) {
            boolean isDesc = "desc".equalsIgnoreCase(bo.getOrder());
            switch (bo.getOrderBy()) {
                case "create_time":
                    lqw.orderBy(true, isDesc, KnowledgeInfo::getCreateTime);
                    lqw.orderByAsc(KnowledgeInfo::getKname);
                    break;
                case "update_time":
                    lqw.orderBy(true, isDesc, KnowledgeInfo::getUpdateTime);
                    lqw.orderByAsc(KnowledgeInfo::getKname);
                    break;
                case "item_count":
                    lqw.orderBy(true, isDesc, KnowledgeInfo::getItemCount);
                    lqw.orderByAsc(KnowledgeInfo::getKname);
                    break;
                case "fragment_count":
                    lqw.orderBy(true, isDesc, KnowledgeInfo::getFragmentCount);
                    lqw.orderByAsc(KnowledgeInfo::getKname);
                    break;
                case "data_size":
                    lqw.orderBy(true, isDesc, KnowledgeInfo::getDataSize);
                    lqw.orderByAsc(KnowledgeInfo::getKname);
                    break;
                case "kname":
                    lqw.orderBy(true, isDesc, KnowledgeInfo::getKname);
                    break;
                case "category":
                    lqw.orderBy(true, isDesc, KnowledgeInfo::getCategory);
                    lqw.orderByAsc(KnowledgeInfo::getKname);
                    break;
                default:
                    lqw.orderByDesc(KnowledgeInfo::getUpdateTime);
                    lqw.orderByAsc(KnowledgeInfo::getKname);
                    break;
            }
        } else {
            lqw.orderByDesc(KnowledgeInfo::getUpdateTime);
            lqw.orderByAsc(KnowledgeInfo::getKname);
        }
        return lqw;
    }

    /**
     * 新增知识库
     */
    @Override
    public Boolean insertByBo(KnowledgeInfoBo bo) {
        KnowledgeInfo add = MapstructUtils.convert(bo, KnowledgeInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改知识库
     */
    @Override
    public Boolean updateByBo(KnowledgeInfoBo bo) {
        KnowledgeInfo update = MapstructUtils.convert(bo, KnowledgeInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KnowledgeInfo entity) {
        // TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除知识库
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOne(KnowledgeInfoBo bo) {
        KnowledgeInfo knowledgeInfo = MapstructUtils.convert(bo, KnowledgeInfo.class);
        if (StringUtils.isBlank(bo.getKid())) {
            String kid = RandomUtil.randomString(10);
            if (knowledgeInfo != null) {
                knowledgeInfo.setKid(kid);
                knowledgeInfo.setUid(LoginHelper.getLoginUser().getUserId());
            }
            baseMapper.insert(knowledgeInfo);
            if (knowledgeInfo != null) {
                vectorStoreService.createSchema(knowledgeInfo.getKid(), bo.getEmbeddingModelName());
            }
        } else {
            baseMapper.updateById(knowledgeInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeKnowledge(String kid) {
        Map<String, Object> map = new HashMap<>();
        KnowledgeInfo knowledgeInfo = baseMapper.selectByKid(kid);

        check(knowledgeInfo);
        map.put("kid", knowledgeInfo.getKid());
        
        // 尝试删除向量数据（失败不影响数据库记录删除）
        try {
        vectorStoreService.removeById(knowledgeInfo.getKid(), knowledgeInfo.getVectorModelName());
        } catch (Exception e) {
            log.warn("删除向量数据失败: kid={}, error={}", kid, e.getMessage());
        }
        
        // 删除附件和知识片段
        fragmentMapper.deleteByMap(map);
        attachMapper.deleteByMap(map);
        
        // 删除知识库
        map.put("kid", knowledgeInfo.getKid());
        baseMapper.deleteByMap(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeKnowledgeBatch(List<String> kids) {
        if (CollectionUtils.isEmpty(kids)) {
            return;
        }
        for (String kid : kids) {
            removeKnowledge(kid);
        }
    }

    @Override
    public void upload(KnowledgeInfoUploadBo bo) {
        Boolean autoCreateItems = bo.getAutoCreateItems() != null ? bo.getAutoCreateItems() : true;
        Boolean autoClassify = bo.getAutoClassify() != null ? bo.getAutoClassify() : false;
        storeContent(bo.getFile(), bo.getKid(), autoCreateItems, autoClassify);
    }

    /**
     * 上传并创建附件记录（同步，快速完成）
     * 只读取文件内容并保存到数据库，创建processId，立即返回
     */
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeAttachVo uploadAndCreateAttach(KnowledgeInfoUploadBo bo) {
        MultipartFile file = bo.getFile();
        String kid = bo.getKid();
        
        FileUploadValidator.validateFile(file);
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new ServiceException("文件名不能为空");
        }
        String sanitizedFileName = FileUploadValidator.sanitizeFileName(originalFileName);
        if (!sanitizedFileName.equals(originalFileName)) {
            log.warn("文件名已自动清理：{} → {}", originalFileName, sanitizedFileName);
        }
        if (!sanitizedFileName.contains(".")) {
            throw new ServiceException("文件名必须包含扩展名");
        }
        String fileType = sanitizedFileName.substring(sanitizedFileName.lastIndexOf(".") + 1).toLowerCase();
        if (!FileUploadValidator.isSupportedFileType(fileType)) {
            throw new ServiceException(String.format("不支持的文件格式：%s", fileType.toUpperCase()));
        }
        
        // 检查重名
        long duplicateCount = attachMapper.selectCount(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getKid, kid)
                .eq(KnowledgeAttach::getDocName, sanitizedFileName)
        );
        if (duplicateCount > 0) {
            throw new ServiceException(String.format("该知识库下已存在同名文件：%s，请重命名后上传", sanitizedFileName));
        }
        
        // 检查附件数量上限
        long currentCount = attachMapper.selectCount(Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getKid, kid));
        int maxAttachments = FileUploadValidator.getMaxAttachmentsPerKnowledgeBase();
        if (currentCount >= maxAttachments) {
            throw new ServiceException(String.format("该知识库附件数量已达上限（%d个），无法继续上传", maxAttachments));
        }
        
        // 读取文件内容（用于快速返回给前端，但不强制保存到数据库）
        ResourceLoader resourceLoader = resourceLoaderFactory.getLoaderByFileType(fileType);
        log.info("[uploadAndCreateAttach] 开始读取文件内容: kid={}, fileName={}, fileType={}", 
                kid, sanitizedFileName, fileType);
        
        String content = null;
        try {
            content = resourceLoader.getContent(file.getInputStream());
            if (content == null) {
                log.warn("[uploadAndCreateAttach] 文件内容提取为空: kid={}, fileName={}", kid, sanitizedFileName);
            } else {
                log.info("[uploadAndCreateAttach] 文件内容读取完成: kid={}, fileName={}, contentLength={}", 
                        kid, sanitizedFileName, content.length());
            }
        } catch (Exception e) {
            log.warn("[uploadAndCreateAttach] 读取文件内容失败（不影响上传）: kid={}, fileName={}, error={}", 
                    kid, sanitizedFileName, e.getMessage());
        }
        
        // 上传文件到OSS
        log.info("[uploadAndCreateAttach] 开始上传文件到OSS: kid={}, fileName={}, fileType={}", 
                kid, sanitizedFileName, fileType);
        org.ruoyi.system.domain.vo.SysOssVo ossVo;
        try {
            ossVo = ossService.upload(file);
            log.info("[uploadAndCreateAttach] 文件上传到OSS成功: ossId={}, url={}", 
                    ossVo.getOssId(), ossVo.getUrl());
        } catch (Exception e) {
            log.error("[uploadAndCreateAttach] 上传文件到OSS失败: kid={}, fileName={}, error={}", 
                    kid, sanitizedFileName, e.getMessage(), e);
            throw new ServiceException(String.format("文件上传失败：%s", e.getMessage()));
        }
        
        // 创建附件记录
        KnowledgeAttach knowledgeAttach = new KnowledgeAttach();
        knowledgeAttach.setKid(kid);
        String docId = RandomUtil.randomString(10);
        knowledgeAttach.setDocId(docId);
        knowledgeAttach.setDocName(sanitizedFileName);
        knowledgeAttach.setDocType(fileType);
        knowledgeAttach.setOssId(ossVo.getOssId());
        knowledgeAttach.setContent(content);
        knowledgeAttach.setCreateTime(new Date());
        try {
        attachMapper.insert(knowledgeAttach);
            log.info("[uploadAndCreateAttach] 附件记录创建成功: id={}, docId={}, kid={}, fileName={}", 
                    knowledgeAttach.getId(), docId, kid, sanitizedFileName);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            log.error("[uploadAndCreateAttach] 上传文件重名：kid={}, docName={}", kid, sanitizedFileName);
            throw new ServiceException(String.format("该知识库下已存在同名文件：%s，请重命名后上传", sanitizedFileName));
        }
        
        // 创建processId
        String processId = attachProcessService.createProcess(knowledgeAttach.getId(), docId);
        log.info("[uploadAndCreateAttach] processId创建成功: attachId={}, docId={}, processId={}", 
                knowledgeAttach.getId(), docId, processId);
        
        // 直接构造VO，避免循环依赖（不通过attachService.queryById）
        KnowledgeAttachVo vo = new KnowledgeAttachVo();
        vo.setId(knowledgeAttach.getId());
        vo.setKid(knowledgeAttach.getKid());
        vo.setDocId(knowledgeAttach.getDocId());
        vo.setDocName(knowledgeAttach.getDocName());
        vo.setDocType(knowledgeAttach.getDocType());
        vo.setContent(knowledgeAttach.getContent());
        vo.setProcessId(processId);//直接设置processId
        log.info("[uploadAndCreateAttach] 附件VO构造成功: id={}, docId={}, processId={}", 
                vo.getId(), vo.getDocId(), vo.getProcessId());
        return vo;
    }
    
    /**
     * 异步处理附件（解析、分块、匹配、创建条目、向量化等）
     */
    @Async
    public void processAttachAsync(Long attachId, String docId, String kid, Boolean autoCreateItems, Boolean autoClassify) {
        log.info("[processAttachAsync] 开始异步处理: attachId={}, docId={}, kid={}", attachId, docId, kid);
        try {
            // 从数据库读取附件信息
            KnowledgeAttach attach = attachMapper.selectById(attachId);
            if (attach == null) {
                log.error("[processAttachAsync] 附件不存在: attachId={}", attachId);
                return;
            }
            
            // 通过docId查询processId（通过KnowledgeAttachProcessMapper，避免循环依赖）
            String processId = findProcessIdByDocId(docId);
            if (processId == null) {
                log.error("[processAttachAsync] 未找到processId: attachId={}, docId={}", attachId, docId);
                return;
            }
            
            // 获取附件内容（统一方法，优先从OSS读取，兼容旧数据）
            String content;
            try {
                content = getAttachContent(attach);
                if (content == null || content.trim().isEmpty()) {
                    log.error("[processAttachAsync] 附件内容为空: attachId={}, docId={}", attachId, docId);
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, "附件内容为空");
                    return;
                }
            } catch (IOException e) {
                log.error("[processAttachAsync] 获取附件内容失败: attachId={}, docId={}, error={}", 
                        attachId, docId, e.getMessage(), e);
                attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                        "获取附件内容失败: " + e.getMessage());
                return;
            }
            
            // 更新状态为解析中
            Map<String, Object> parsingData = new HashMap<>();
            parsingData.put("stage", "PARSING");
            parsingData.put("fileName", attach.getDocName());
            parsingData.put("totalCount", 1);//解析是单步操作
            parsingData.put("currentIndex", 0);
            attachProcessService.updateStatus(processId, ProcessingStatus.PARSING, parsingData);
            
            //解析完成，更新进度
            parsingData.put("currentIndex", 1);
            attachProcessService.updateProgress(processId, parsingData);
            
            // 执行后续处理（调用原来的storeContent的后续逻辑，但使用数据库中的content）
            processContentAfterUpload(attach, content, processId, kid, autoCreateItems, autoClassify);
            
        } catch (Exception e) {
            log.error("[processAttachAsync] 异步处理失败: attachId={}, docId={}, error={}", 
                    attachId, docId, e.getMessage(), e);
            try {
                // 通过docId查询processId（通过KnowledgeAttachProcessMapper，避免循环依赖）
                String processId = findProcessIdByDocId(docId);
                if (processId != null) {
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                            "异步处理失败: " + e.getMessage());
                }
            } catch (Exception updateEx) {
                log.error("[processAttachAsync] 更新失败状态时发生错误: {}", updateEx.getMessage(), updateEx);
            }
        }
    }
    
    /**
     * 通过docId查询processId（避免循环依赖，不通过attachService）
     * 通过KnowledgeAttachProcessMapper直接查询
     */
    private String findProcessIdByDocId(String docId) {
        KnowledgeAttachProcess process = attachProcessMapper.selectOne(
            Wrappers.<KnowledgeAttachProcess>lambdaQuery()
                .eq(KnowledgeAttachProcess::getDocId, docId)
                .orderByDesc(KnowledgeAttachProcess::getCreateTime)
                .last("LIMIT 1")
        );
        if (process != null) {
            return String.valueOf(process.getId());
        }
        return null;
    }
    
    /**
     * 获取附件内容（统一方法，向后兼容）
     * 优先从OSS读取，如果没有ossId则从content字段读取（兼容旧数据）
     */
    private String getAttachContent(KnowledgeAttach attach) throws IOException {
        if (attach.getOssId() != null) {
            log.info("[getAttachContent] 从OSS读取文件内容: docId={}, ossId={}", attach.getDocId(), attach.getOssId());
            try {
                MultipartFile ossFile = ossService.downloadByFile(attach.getOssId());
                ResourceLoader resourceLoader = resourceLoaderFactory.getLoaderByFileType(attach.getDocType());
                String content = resourceLoader.getContent(ossFile.getInputStream());
                log.info("[getAttachContent] 从OSS读取文件内容成功: docId={}, contentLength={}", 
                        attach.getDocId(), content != null ? content.length() : 0);
                return content;
            } catch (Exception e) {
                log.error("[getAttachContent] 从OSS读取文件内容失败: docId={}, ossId={}, error={}", 
                        attach.getDocId(), attach.getOssId(), e.getMessage(), e);
                throw new IOException("从OSS读取文件内容失败: " + e.getMessage(), e);
            }
        } else {
            log.info("[getAttachContent] 从content字段读取（兼容旧数据）: docId={}", attach.getDocId());
            String content = attach.getContent();
            if (content == null || content.trim().isEmpty()) {
                log.warn("[getAttachContent] content字段为空: docId={}", attach.getDocId());
            }
            return content;
        }
    }
    
    /**
     * 处理上传后的内容（分块、匹配、创建条目、向量化等）
     * 这是从storeContent方法中提取出来的后续处理逻辑
     */
    private void processContentAfterUpload(KnowledgeAttach attach, String content, String processId, 
                                          String kid, Boolean autoCreateItems, Boolean autoClassify) {
        String docId = attach.getDocId();
        String sanitizedFileName = attach.getDocName();
        String fileType = attach.getDocType();
        
        // 分块处理
        ResourceLoader resourceLoader = resourceLoaderFactory.getLoaderByFileType(fileType);
        List<String> chunkList;
        try {
            chunkList = resourceLoader.getChunkList(content, kid);
            if (chunkList == null) {
                throw new ServiceException("文件分块失败：分块结果为null");
            }
            log.info("[processContentAfterUpload] 分块完成: kid={}, docId={}, chunkListSize={}", 
                    kid, docId, chunkList != null ? chunkList.size() : 0);
        } catch (Exception e) {
            log.error("[processContentAfterUpload] 分块失败: kid={}, docId={}, error={}", 
                    kid, docId, e.getMessage(), e);
            attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                    "文件分块失败: " + e.getMessage());
            throw new ServiceException("文件分块失败: " + e.getMessage());
        }
        
        if (CollUtil.isEmpty(chunkList)) {
            log.warn("[processContentAfterUpload] 文档分块为空: kid={}, docId={}", kid, docId);
            attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, "文档分块为空，无法继续处理");
            return;
        }
        
        // 后续处理逻辑（从原来的storeContent方法中复制）
        List<String> fids = new ArrayList<>();
        
        try {
            //检查是否已取消
            org.ruoyi.domain.vo.KnowledgeAttachProcessVo processCheck = attachProcessService.getCurrentStatus(processId);
            if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                log.info("[processContentAfterUpload] 处理任务已取消，停止处理: processId={}", processId);
                return;
            }
            
            //更新状态为分块中，分块已完成（在创建processId之前已完成），直接设置为完成
            Map<String, Object> chunkingData = new HashMap<>();
            chunkingData.put("stage", "CHUNKING");
            chunkingData.put("totalCount", chunkList.size());
            chunkingData.put("currentIndex", chunkList.size());//分块已完成
            attachProcessService.updateStatus(processId, ProcessingStatus.CHUNKING, chunkingData);
            
        KnowledgeInfoVo knowledgeInfoVo = baseMapper.selectVoOne(Wrappers.<KnowledgeInfo>lambdaQuery()
                    .eq(KnowledgeInfo::getKid, kid));
            if (knowledgeInfoVo == null) {
                throw new ServiceException("知识库不存在: kid=" + kid);
            }
            
        ChatModelVo chatModelVo = chatModelService.selectModelByName(knowledgeInfoVo.getEmbeddingModelName());
        if (chatModelVo == null) {
            chatModelVo = chatModelService.selectModelByCategoryWithHighestPriority(ChatModeType.VECTOR.getCode());
                if (chatModelVo == null) {
                    throw new ServiceException("未找到可用的向量模型，请先在chat_model表中配置category='vector'的模型");
                }
            }
            
            //更新状态为匹配中，并传递总数信息
            Map<String, Object> matchingInitData = new HashMap<>();
            matchingInitData.put("stage", "MATCHING");
            matchingInitData.put("totalCount", chunkList.size());
            matchingInitData.put("currentIndex", 0);
            attachProcessService.updateStatus(processId, ProcessingStatus.MATCHING, matchingInitData);
            
            // 相似度匹配
            List<Map<String, Object>> matchingResults = new ArrayList<>();
        Map<Integer, String> chunkIndexToItemUuidMap = new HashMap<>();
            
        if (Boolean.TRUE.equals(autoClassify)) {
                try {
            EmbeddingModel embeddingModel = embeddingModelFactory.createModel(
                    knowledgeInfoVo.getEmbeddingModelName(), null);
            double threshold = 0.85;
                    int totalChunks = chunkList.size();
                    for (int i = 0; i < totalChunks; i++) {
                        try {
                String chunk = chunkList.get(i);
                            
                            Embedding embedding = RateLimitHandler.executeWithRetry(() -> {
                                RateLimitHandler.addCallInterval();
                                return embeddingModel.embed(chunk).content();
                            }, "片段" + i + "向量化");
                            
                QueryVectorBo queryBo = new QueryVectorBo();
                queryBo.setKid(kid);
                queryBo.setQuery(chunk);
                queryBo.setMaxResults(5);
                queryBo.setEmbeddingModelName(knowledgeInfoVo.getEmbeddingModelName());
                List<String> similarTexts = vectorStoreService.getQueryVector(queryBo);
                if (CollUtil.isNotEmpty(similarTexts)) {
                                String similarText = similarTexts.get(0);
                                List<KnowledgeFragment> similarFragments = fragmentMapper.selectList(
                            Wrappers.<KnowledgeFragment>lambdaQuery()
                                    .eq(KnowledgeFragment::getKid, kid)
                                                .eq(KnowledgeFragment::getContent, similarText)
                                    .isNotNull(KnowledgeFragment::getItemUuid)
                                    .eq(KnowledgeFragment::getDelFlag, "0")
                                    .last("LIMIT 1")
                    );
                                if (CollUtil.isNotEmpty(similarFragments)) {
                                    KnowledgeFragment similarFragment = similarFragments.get(0);
                    if (similarFragment != null && StringUtils.isNotBlank(similarFragment.getItemUuid())) {
                        float[] queryVector = embedding.vector();
                                        
                                        Embedding similarEmbedding = RateLimitHandler.executeWithRetry(() -> {
                                            RateLimitHandler.addCallInterval();
                                            return embeddingModel.embed(similarText).content();
                                        }, "片段" + i + "相似文本向量化");
                                        
                        float[] similarVector = similarEmbedding.vector();
                        double similarity = calculateCosineSimilarity(queryVector, similarVector);
                        if (similarity >= threshold) {
                            chunkIndexToItemUuidMap.put(i, similarFragment.getItemUuid());
                                            
                                            // 记录匹配结果（不存储完整内容，只存储fid）
                                            String fid = RandomUtil.randomString(10);
                                            fids.add(fid);
                                            Map<String, Object> matchResult = new HashMap<>();
                                            matchResult.put("chunkIndex", i);
                                            matchResult.put("fid", fid);
                                            matchResult.put("matchedItemUuid", similarFragment.getItemUuid());
                                            matchResult.put("matchedItemTitle", getItemTitle(similarFragment.getItemUuid()));
                                            matchResult.put("similarity", similarity);
                                            matchResult.put("userDecision", "keep");
                                            matchingResults.add(matchResult);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            if (RateLimitHandler.isRateLimitError(e)) {
                                log.warn("[processContentAfterUpload] 片段{}遇到速率限制，已重试但仍失败，跳过: {}", i, e.getMessage());
                            } else {
                                log.warn("[processContentAfterUpload] 片段{}相似度匹配失败，跳过: {}", i, e.getMessage());
                            }
                        }
                        
                        //检查是否已取消并更新进度（在循环中定期检查）
                        if (i % 5 == 0 || i == totalChunks - 1) {
                            processCheck = attachProcessService.getCurrentStatus(processId);
                            if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                                log.info("[processContentAfterUpload] 处理任务已取消，停止匹配: processId={}", processId);
                                return;
                            }
                            
                            //实时更新匹配进度
                            Map<String, Object> progressData = new HashMap<>();
                            progressData.put("currentIndex", i + 1);
                            progressData.put("totalCount", totalChunks);
                            progressData.put("stage", "MATCHING");
                            try {
                                attachProcessService.updateProgress(processId, progressData);
                            } catch (Exception e) {
                                log.warn("[processContentAfterUpload] 更新匹配进度失败: processId={}, error={}", processId, e.getMessage());
                            }
                        }
                        
                        // 如果没有匹配到，也需要记录fid
                        if (!chunkIndexToItemUuidMap.containsKey(i)) {
                            String fid = RandomUtil.randomString(10);
                            fids.add(fid);
                        }
                    }
                } catch (Exception e) {
                    log.error("[processContentAfterUpload] 相似度匹配阶段失败: {}", e.getMessage(), e);
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                            "相似度匹配失败: " + e.getMessage());
                    throw e;
                }
                
                //为所有未匹配的片段也创建matchingResults条目
            for (int i = 0; i < chunkList.size(); i++) {
                    if (!chunkIndexToItemUuidMap.containsKey(i)) {
                        String fid = fids.get(i);
                        Map<String, Object> unmatchedResult = new HashMap<>();
                        unmatchedResult.put("chunkIndex", i);
                        unmatchedResult.put("fid", fid);
                        unmatchedResult.put("matchedItemUuid", null);
                        unmatchedResult.put("matchedItemTitle", null);
                        unmatchedResult.put("similarity", null);
                        unmatchedResult.put("userDecision", null);
                        matchingResults.add(unmatchedResult);
                    }
                }
            } else {
                // 如果没有启用相似度匹配，为所有片段生成fid
                for (int i = 0; i < chunkList.size(); i++) {
                    String fid = RandomUtil.randomString(10);
                    fids.add(fid);
                    //为所有片段创建matchingResults条目（未匹配状态）
                    Map<String, Object> unmatchedResult = new HashMap<>();
                    unmatchedResult.put("chunkIndex", i);
                    unmatchedResult.put("fid", fid);
                    unmatchedResult.put("matchedItemUuid", null);
                    unmatchedResult.put("matchedItemTitle", null);
                    unmatchedResult.put("similarity", null);
                    unmatchedResult.put("userDecision", null);
                    matchingResults.add(unmatchedResult);
                }
            }
            
            // 创建片段（不立即关联条目）
            List<KnowledgeFragment> knowledgeFragmentList = new ArrayList<>();
            for (int i = 0; i < chunkList.size(); i++) {
                String fid = fids.get(i);
                KnowledgeFragment knowledgeFragment = new KnowledgeFragment();
                knowledgeFragment.setKid(kid);
                knowledgeFragment.setDocId(docId);
                knowledgeFragment.setFid(fid);
                knowledgeFragment.setIdx(i);
                knowledgeFragment.setContent(chunkList.get(i));
                knowledgeFragment.setCreateTime(new Date());
                // 如果有匹配结果，先关联；否则等待用户确认
                if (chunkIndexToItemUuidMap.containsKey(i)) {
                    knowledgeFragment.setItemUuid(chunkIndexToItemUuidMap.get(i));
                } else {
                    knowledgeFragment.setItemUuid(null);
                }
                knowledgeFragmentList.add(knowledgeFragment);
            }
            try {
                fragmentMapper.insertBatch(knowledgeFragmentList);
            } catch (Exception e) {
                log.error("[processContentAfterUpload] 创建片段失败: {}", e.getMessage(), e);
                attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                        "创建片段失败: " + e.getMessage());
                throw e;
            }
            
            // 根据autoCreateItems决定是否立即创建条目
            if (Boolean.TRUE.equals(autoCreateItems) && Boolean.FALSE.equals(autoClassify)) {
                // 旧逻辑：如果没有启用相似度匹配，立即创建条目
                //更新状态为创建条目中，并传递总数信息
                Map<String, Object> creatingItemsData = new HashMap<>();
                creatingItemsData.put("stage", "CREATING_ITEMS");
                creatingItemsData.put("totalCount", chunkList.size());
                creatingItemsData.put("currentIndex", 0);
                attachProcessService.updateStatus(processId, ProcessingStatus.CREATING_ITEMS, creatingItemsData);
                
                try {
                    int totalChunks = chunkList.size();
                    for (int i = 0; i < totalChunks; i++) {
                if (!chunkIndexToItemUuidMap.containsKey(i)) {
                    KnowledgeItem newItem = new KnowledgeItem();
                    String uuid = RandomUtil.randomString(32);
                    newItem.setItemUuid(uuid);
                    newItem.setKid(kid);
                            newItem.setTitle(sanitizedFileName + "-片段" + (i + 1));
                    newItem.setCreateTime(new Date());
                    newItem.setDelFlag("0");
                    knowledgeItemMapper.insert(newItem);
                    chunkIndexToItemUuidMap.put(i, uuid);
                            
                            // 更新片段关联
                            KnowledgeFragment fragment = knowledgeFragmentList.get(i);
                            fragment.setItemUuid(uuid);
                            fragmentMapper.updateById(fragment);
                        }
                        
                        //实时更新创建条目进度（每5个或最后一个）
                        if (i % 5 == 0 || i == totalChunks - 1) {
                            try {
                                Map<String, Object> progressData = new HashMap<>();
                                progressData.put("currentIndex", i + 1);
                                progressData.put("totalCount", totalChunks);
                                progressData.put("stage", "CREATING_ITEMS");
                                attachProcessService.updateProgress(processId, progressData);
                            } catch (Exception e) {
                                log.warn("[processContentAfterUpload] 更新创建条目进度失败: processId={}, error={}", processId, e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("[processContentAfterUpload] 创建条目失败: {}", e.getMessage(), e);
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                            "创建条目失败: " + e.getMessage());
                    throw e;
                }
                
                //再次检查是否已取消
                processCheck = attachProcessService.getCurrentStatus(processId);
                if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                    log.info("[processContentAfterUpload] 处理任务已取消，停止向量化: processId={}", processId);
                    return;
                }
                
                // 直接向量化
                try {
                    //更新状态为向量化，并传递总数信息
                    Map<String, Object> vectorizingData = new HashMap<>();
                    vectorizingData.put("totalCount", chunkList.size());
                    vectorizingData.put("currentIndex", 0);
                    attachProcessService.updateStatus(processId, ProcessingStatus.VECTORIZING, vectorizingData);
                    
                    StoreEmbeddingBo storeEmbeddingBo = new StoreEmbeddingBo();
                    storeEmbeddingBo.setKid(kid);
                    storeEmbeddingBo.setDocId(docId);
                    storeEmbeddingBo.setFids(fids);
                    storeEmbeddingBo.setChunkList(chunkList);
                    storeEmbeddingBo.setVectorStoreName(knowledgeInfoVo.getVectorModelName());
                    storeEmbeddingBo.setEmbeddingModelName(knowledgeInfoVo.getEmbeddingModelName());
                    storeEmbeddingBo.setApiKey(chatModelVo.getApiKey());
                    storeEmbeddingBo.setBaseUrl(chatModelVo.getApiHost());
                    
                    //传递processId用于进度更新
                    storeEmbeddingBo.setProcessId(processId);
                    vectorStoreService.storeEmbeddings(storeEmbeddingBo, attachProcessService);
                    
                    //最后检查一次是否已取消
                    processCheck = attachProcessService.getCurrentStatus(processId);
                    if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                        log.info("[processContentAfterUpload] 处理任务已取消，停止更新完成状态: processId={}", processId);
                        return;
                    }
                    
                    attachProcessService.updateStatus(processId, ProcessingStatus.COMPLETED, null);
                } catch (Exception e) {
                    log.error("[processContentAfterUpload] 向量化存储失败: {}", e.getMessage(), e);
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                            "向量化存储失败: " + e.getMessage());
                    throw e;
            }
        } else {
                // 新逻辑：进入用户审阅匹配结果状态
                Map<String, Object> statusData = new HashMap<>();
                statusData.put("matchingResults", matchingResults);
                attachProcessService.updateStatus(processId, ProcessingStatus.USER_REVIEW_MATCHING, statusData);
            }
        } catch (ServiceException e) {
            // ServiceException已经更新了状态，直接抛出
            throw e;
        } catch (Exception e) {
            log.error("[processContentAfterUpload] 附件处理过程中发生未预期的错误: processId={}, error={}", processId, e.getMessage(), e);
            try {
                attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                        "处理过程中发生错误: " + e.getMessage());
            } catch (Exception updateEx) {
                log.error("[processContentAfterUpload] 更新失败状态时发生错误: {}", updateEx.getMessage(), updateEx);
            }
            throw new ServiceException("附件处理失败: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void storeContent(MultipartFile file, String kid, Boolean autoCreateItems, Boolean autoClassify) {
        FileUploadValidator.validateFile(file);
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new ServiceException("文件名不能为空");
        }
        String sanitizedFileName = FileUploadValidator.sanitizeFileName(originalFileName);
        if (!sanitizedFileName.equals(originalFileName)) {
            log.warn("文件名已自动清理：{} → {}", originalFileName, sanitizedFileName);
        }
        if (!sanitizedFileName.contains(".")) {
            throw new ServiceException("文件名必须包含扩展名");
        }
        String fileType = sanitizedFileName.substring(sanitizedFileName.lastIndexOf(".") + 1).toLowerCase();
        if (!FileUploadValidator.isSupportedFileType(fileType)) {
            throw new ServiceException(String.format("不支持的文件格式：%s", fileType.toUpperCase()));
        }
        // 检查重名（业务层面检查，作为数据库唯一约束的双重保险）
        long duplicateCount = attachMapper.selectCount(
            Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getKid, kid)
                .eq(KnowledgeAttach::getDocName, sanitizedFileName)
        );
        
        if (duplicateCount > 0) {
            throw new ServiceException(String.format("该知识库下已存在同名文件：%s，请重命名后上传", sanitizedFileName));
        }
        
        long currentCount = attachMapper.selectCount(Wrappers.<KnowledgeAttach>lambdaQuery()
                .eq(KnowledgeAttach::getKid, kid));
        int maxAttachments = FileUploadValidator.getMaxAttachmentsPerKnowledgeBase();
        if (currentCount >= maxAttachments) {
            throw new ServiceException(String.format("该知识库附件数量已达上限（%d个），无法继续上传", maxAttachments));
        }
        ResourceLoader resourceLoader = resourceLoaderFactory.getLoaderByFileType(fileType);
        log.info("[storeContent] 文件类型: kid={}, fileName={}, fileType={}, loaderType={}", 
                kid, sanitizedFileName, fileType, resourceLoader.getClass().getSimpleName());
        
        String content;
        List<String> chunkList;
        try {
            content = resourceLoader.getContent(file.getInputStream());
            log.info("[storeContent] 内容提取完成: kid={}, fileName={}, contentLength={}, contentIsNull={}, contentIsEmpty={}", 
                    kid, sanitizedFileName, 
                    content != null ? content.length() : 0, 
                    content == null, 
                    content != null && content.trim().isEmpty());
            
            if (content == null) {
                log.error("[storeContent] 内容提取结果为null: kid={}, fileName={}", kid, sanitizedFileName);
                throw new ServiceException("文件内容提取失败：内容为空");
            }
            
            if (content.trim().isEmpty()) {
                log.warn("[storeContent] 内容提取结果为空或只包含空白字符: kid={}, fileName={}, originalLength={}", 
                        kid, sanitizedFileName, content.length());
            }
            
            chunkList = resourceLoader.getChunkList(content, kid);
            log.info("[storeContent] 分块完成: kid={}, fileName={}, chunkListSize={}, chunkListIsNull={}", 
                    kid, sanitizedFileName, 
                    chunkList != null ? chunkList.size() : 0, 
                    chunkList == null);
            
            if (chunkList == null) {
                log.error("[storeContent] 分块结果为null: kid={}, fileName={}, loaderType={}", 
                        kid, sanitizedFileName, resourceLoader.getClass().getSimpleName());
                throw new ServiceException("文件分块失败：分块结果为null");
            }
            
            if (chunkList.isEmpty()) {
                log.warn("[storeContent] 分块结果为空: kid={}, fileName={}, contentLength={}, loaderType={}", 
                        kid, sanitizedFileName, content.length(), resourceLoader.getClass().getSimpleName());
            } else {
                log.debug("[storeContent] 分块详情: kid={}, fileName={}, chunks={}", 
                        kid, sanitizedFileName, 
                        chunkList.stream().map(c -> c.length() > 50 ? c.substring(0, 50) + "..." : c).toList());
            }
        } catch (IOException e) {
            log.error("[storeContent] 解析文档失败: kid={}, fileName={}, error={}", kid, sanitizedFileName, e.getMessage(), e);
            throw new ServiceException(String.format("文件解析失败：%s", e.getMessage()));
        } catch (RuntimeException e) {
            log.error("[storeContent] 解析文档异常: kid={}, fileName={}, error={}", kid, sanitizedFileName, e.getMessage(), e);
            throw new ServiceException(String.format("文件解析异常：%s", e.getMessage()));
        }
        KnowledgeAttach knowledgeAttach = new KnowledgeAttach();
        knowledgeAttach.setKid(kid);
        String docId = RandomUtil.randomString(10);
        knowledgeAttach.setDocId(docId);
        knowledgeAttach.setDocName(sanitizedFileName);
        knowledgeAttach.setDocType(fileType);
        knowledgeAttach.setContent(content);
        knowledgeAttach.setCreateTime(new Date());
        try {
            attachMapper.insert(knowledgeAttach);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            log.error("上传文件重名：kid={}, docName={}", kid, sanitizedFileName);
            throw new ServiceException(String.format("该知识库下已存在同名文件：%s，请重命名后上传", sanitizedFileName));
        }
        String processId = attachProcessService.createProcess(knowledgeAttach.getId(), docId);
        
        //更新状态为解析中，并传递文档信息
        Map<String, Object> parsingData = new HashMap<>();
        parsingData.put("stage", "PARSING");
        parsingData.put("fileName", sanitizedFileName);
        parsingData.put("totalCount", 1);//解析是单步操作
        parsingData.put("currentIndex", 0);
        attachProcessService.updateStatus(processId, ProcessingStatus.PARSING, parsingData);
        
        //解析完成，更新进度
        parsingData.put("currentIndex", 1);
        attachProcessService.updateProgress(processId, parsingData);
        
        List<String> fids = new ArrayList<>();
        
        if (CollUtil.isEmpty(chunkList)) {
            log.warn("文档分块为空，kid={}, docId={}", kid, docId);
            attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, "文档分块为空，无法继续处理");
            return;
        }
        
        try {
            //检查是否已取消
            org.ruoyi.domain.vo.KnowledgeAttachProcessVo processCheck = attachProcessService.getCurrentStatus(processId);
            if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                log.info("处理任务已取消，停止处理: processId={}", processId);
                return;
            }
            
            //更新状态为分块中，分块已完成（在创建processId之前已完成），直接设置为完成
            Map<String, Object> chunkingData = new HashMap<>();
            chunkingData.put("stage", "CHUNKING");
            chunkingData.put("totalCount", chunkList.size());
            chunkingData.put("currentIndex", chunkList.size());//分块已完成
            attachProcessService.updateStatus(processId, ProcessingStatus.CHUNKING, chunkingData);
            
            KnowledgeInfoVo knowledgeInfoVo = baseMapper.selectVoOne(Wrappers.<KnowledgeInfo>lambdaQuery()
                    .eq(KnowledgeInfo::getKid, kid));
            if (knowledgeInfoVo == null) {
                throw new ServiceException("知识库不存在: kid=" + kid);
            }
            
            ChatModelVo chatModelVo = chatModelService.selectModelByName(knowledgeInfoVo.getEmbeddingModelName());
            if (chatModelVo == null) {
                chatModelVo = chatModelService.selectModelByCategoryWithHighestPriority(ChatModeType.VECTOR.getCode());
                if (chatModelVo == null) {
                    throw new ServiceException("未找到可用的向量模型，请先在chat_model表中配置category='vector'的模型");
                }
            }
            
            //更新状态为匹配中，并传递总数信息
            Map<String, Object> matchingInitData = new HashMap<>();
            matchingInitData.put("stage", "MATCHING");
            matchingInitData.put("totalCount", chunkList.size());
            matchingInitData.put("currentIndex", 0);
            attachProcessService.updateStatus(processId, ProcessingStatus.MATCHING, matchingInitData);
            
            // 相似度匹配
            List<Map<String, Object>> matchingResults = new ArrayList<>();
            Map<Integer, String> chunkIndexToItemUuidMap = new HashMap<>();
            
            if (Boolean.TRUE.equals(autoClassify)) {
                try {
                    EmbeddingModel embeddingModel = embeddingModelFactory.createModel(
                            knowledgeInfoVo.getEmbeddingModelName(), null);
                    double threshold = 0.85;
                    int totalChunks = chunkList.size();
                    for (int i = 0; i < totalChunks; i++) {
                        try {
                            String chunk = chunkList.get(i);
                            
                            Embedding embedding = RateLimitHandler.executeWithRetry(() -> {
                                RateLimitHandler.addCallInterval();
                                return embeddingModel.embed(chunk).content();
                            }, "片段" + i + "向量化");
                            
                            QueryVectorBo queryBo = new QueryVectorBo();
                            queryBo.setKid(kid);
                            queryBo.setQuery(chunk);
                            queryBo.setMaxResults(5);
                            queryBo.setEmbeddingModelName(knowledgeInfoVo.getEmbeddingModelName());
                            List<String> similarTexts = vectorStoreService.getQueryVector(queryBo);
                            if (CollUtil.isNotEmpty(similarTexts)) {
                                String similarText = similarTexts.get(0);
                                List<KnowledgeFragment> similarFragments = fragmentMapper.selectList(
                                        Wrappers.<KnowledgeFragment>lambdaQuery()
                                                .eq(KnowledgeFragment::getKid, kid)
                                                .eq(KnowledgeFragment::getContent, similarText)
                                                .isNotNull(KnowledgeFragment::getItemUuid)
                                                .eq(KnowledgeFragment::getDelFlag, "0")
                                                .last("LIMIT 1")
                                );
                                if (CollUtil.isNotEmpty(similarFragments)) {
                                    KnowledgeFragment similarFragment = similarFragments.get(0);
                                    if (similarFragment != null && StringUtils.isNotBlank(similarFragment.getItemUuid())) {
                                        float[] queryVector = embedding.vector();
                                        
                                        Embedding similarEmbedding = RateLimitHandler.executeWithRetry(() -> {
                                            RateLimitHandler.addCallInterval();
                                            return embeddingModel.embed(similarText).content();
                                        }, "片段" + i + "相似文本向量化");
                                        
                                        float[] similarVector = similarEmbedding.vector();
                                        double similarity = calculateCosineSimilarity(queryVector, similarVector);
                                        if (similarity >= threshold) {
                                            chunkIndexToItemUuidMap.put(i, similarFragment.getItemUuid());
                                            
                                            // 记录匹配结果（不存储完整内容，只存储fid）
                                            String fid = RandomUtil.randomString(10);
                                            fids.add(fid);
                                            Map<String, Object> matchResult = new HashMap<>();
                                            matchResult.put("chunkIndex", i);
                                            matchResult.put("fid", fid);
                                            matchResult.put("matchedItemUuid", similarFragment.getItemUuid());
                                            matchResult.put("matchedItemTitle", getItemTitle(similarFragment.getItemUuid()));
                                            matchResult.put("similarity", similarity);
                                            matchResult.put("userDecision", "keep");
                                            matchingResults.add(matchResult);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            if (RateLimitHandler.isRateLimitError(e)) {
                                log.warn("片段{}遇到速率限制，已重试但仍失败，跳过: {}", i, e.getMessage());
                            } else {
                                log.warn("片段{}相似度匹配失败，跳过: {}", i, e.getMessage());
                            }
                        }
                        
                        //检查是否已取消并更新进度（在循环中定期检查）
                        if (i % 5 == 0 || i == totalChunks - 1) {
                            processCheck = attachProcessService.getCurrentStatus(processId);
                            if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                                log.info("处理任务已取消，停止匹配: processId={}", processId);
                                return;
                            }
                            
                            //实时更新匹配进度
                            Map<String, Object> progressData = new HashMap<>();
                            progressData.put("currentIndex", i + 1);
                            progressData.put("totalCount", totalChunks);
                            progressData.put("stage", "MATCHING");
                            try {
                                attachProcessService.updateProgress(processId, progressData);
                            } catch (Exception e) {
                                log.warn("更新匹配进度失败: processId={}, error={}", processId, e.getMessage());
                            }
                        }
                        
                        // 如果没有匹配到，也需要记录fid
                if (!chunkIndexToItemUuidMap.containsKey(i)) {
                            String fid = RandomUtil.randomString(10);
                            fids.add(fid);
                        }
                    }
                } catch (Exception e) {
                    log.error("相似度匹配阶段失败: {}", e.getMessage(), e);
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                            "相似度匹配失败: " + e.getMessage());
                    throw e;
                }
            } else {
                // 如果没有启用相似度匹配，为所有片段生成fid
        for (int i = 0; i < chunkList.size(); i++) {
            String fid = RandomUtil.randomString(10);
            fids.add(fid);
                }
            }
            
            // 创建片段（不立即关联条目）
            List<KnowledgeFragment> knowledgeFragmentList = new ArrayList<>();
            for (int i = 0; i < chunkList.size(); i++) {
                String fid = fids.get(i);
            KnowledgeFragment knowledgeFragment = new KnowledgeFragment();
            knowledgeFragment.setKid(kid);
            knowledgeFragment.setDocId(docId);
            knowledgeFragment.setFid(fid);
            knowledgeFragment.setIdx(i);
            knowledgeFragment.setContent(chunkList.get(i));
            knowledgeFragment.setCreateTime(new Date());
                // 如果有匹配结果，先关联；否则等待用户确认
                if (chunkIndexToItemUuidMap.containsKey(i)) {
                    knowledgeFragment.setItemUuid(chunkIndexToItemUuidMap.get(i));
                } else {
                    knowledgeFragment.setItemUuid(null);
                }
            knowledgeFragmentList.add(knowledgeFragment);
        }
            try {
        fragmentMapper.insertBatch(knowledgeFragmentList);
            } catch (Exception e) {
                log.error("创建片段失败: {}", e.getMessage(), e);
                attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                        "创建片段失败: " + e.getMessage());
                throw e;
            }
            
            // 根据autoCreateItems决定是否立即创建条目
            if (Boolean.TRUE.equals(autoCreateItems) && Boolean.FALSE.equals(autoClassify)) {
                // 旧逻辑：如果没有启用相似度匹配，立即创建条目
                //更新状态为创建条目中，并传递总数信息
                Map<String, Object> creatingItemsData = new HashMap<>();
                creatingItemsData.put("stage", "CREATING_ITEMS");
                creatingItemsData.put("totalCount", chunkList.size());
                creatingItemsData.put("currentIndex", 0);
                attachProcessService.updateStatus(processId, ProcessingStatus.CREATING_ITEMS, creatingItemsData);
                
                try {
                    int totalChunks = chunkList.size();
                    for (int i = 0; i < totalChunks; i++) {
                        if (!chunkIndexToItemUuidMap.containsKey(i)) {
                            KnowledgeItem newItem = new KnowledgeItem();
                            String uuid = RandomUtil.randomString(32);
                            newItem.setItemUuid(uuid);
                            newItem.setKid(kid);
                            newItem.setTitle(sanitizedFileName + "-片段" + (i + 1));
                            newItem.setCreateTime(new Date());
                            newItem.setDelFlag("0");
                            knowledgeItemMapper.insert(newItem);
                            chunkIndexToItemUuidMap.put(i, uuid);
                            
                            // 更新片段关联
                            KnowledgeFragment fragment = knowledgeFragmentList.get(i);
                            fragment.setItemUuid(uuid);
                            fragmentMapper.updateById(fragment);
                        }
                        
                        //实时更新创建条目进度（每5个或最后一个）
                        if (i % 5 == 0 || i == totalChunks - 1) {
                            try {
                                Map<String, Object> progressData = new HashMap<>();
                                progressData.put("currentIndex", i + 1);
                                progressData.put("totalCount", totalChunks);
                                progressData.put("stage", "CREATING_ITEMS");
                                attachProcessService.updateProgress(processId, progressData);
                            } catch (Exception e) {
                                log.warn("更新创建条目进度失败: processId={}, error={}", processId, e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("创建条目失败: {}", e.getMessage(), e);
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                            "创建条目失败: " + e.getMessage());
                    throw e;
                }
                
                //再次检查是否已取消
                processCheck = attachProcessService.getCurrentStatus(processId);
                if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                    log.info("处理任务已取消，停止向量化: processId={}", processId);
                    return;
                }
                
                //再次检查是否已取消
                processCheck = attachProcessService.getCurrentStatus(processId);
                if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                    log.info("处理任务已取消，停止向量化: processId={}", processId);
                    return;
                }
                
                // 直接向量化
                try {
                    //更新状态为向量化，并传递总数信息
                    Map<String, Object> vectorizingData = new HashMap<>();
                    vectorizingData.put("totalCount", chunkList.size());
                    vectorizingData.put("currentIndex", 0);
                    attachProcessService.updateStatus(processId, ProcessingStatus.VECTORIZING, vectorizingData);
                    
        StoreEmbeddingBo storeEmbeddingBo = new StoreEmbeddingBo();
        storeEmbeddingBo.setKid(kid);
        storeEmbeddingBo.setDocId(docId);
        storeEmbeddingBo.setFids(fids);
        storeEmbeddingBo.setChunkList(chunkList);
        storeEmbeddingBo.setVectorStoreName(knowledgeInfoVo.getVectorModelName());
        storeEmbeddingBo.setEmbeddingModelName(knowledgeInfoVo.getEmbeddingModelName());
        storeEmbeddingBo.setApiKey(chatModelVo.getApiKey());
        storeEmbeddingBo.setBaseUrl(chatModelVo.getApiHost());
                    
                    //传递processId用于进度更新
                    storeEmbeddingBo.setProcessId(processId);
                    vectorStoreService.storeEmbeddings(storeEmbeddingBo, attachProcessService);
                    
                    //最后检查一次是否已取消
                    processCheck = attachProcessService.getCurrentStatus(processId);
                    if (ProcessingStatus.CANCELLED.getCode().equals(processCheck.getCurrentStatus())) {
                        log.info("处理任务已取消，停止更新完成状态: processId={}", processId);
                        return;
                    }
                    
                    attachProcessService.updateStatus(processId, ProcessingStatus.COMPLETED, null);
                } catch (Exception e) {
                    log.error("向量化存储失败: {}", e.getMessage(), e);
                    attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                            "向量化存储失败: " + e.getMessage());
                    throw e;
                }
            } else {
                // 新逻辑：进入用户审阅匹配结果状态
                Map<String, Object> statusData = new HashMap<>();
                statusData.put("matchingResults", matchingResults);
                attachProcessService.updateStatus(processId, ProcessingStatus.USER_REVIEW_MATCHING, statusData);
            }
        } catch (ServiceException e) {
            // ServiceException已经更新了状态，直接抛出
            throw e;
        } catch (Exception e) {
            log.error("附件处理过程中发生未预期的错误: processId={}, error={}", processId, e.getMessage(), e);
            try {
                attachProcessService.updateStatus(processId, ProcessingStatus.FAILED, 
                        "处理过程中发生错误: " + e.getMessage());
            } catch (Exception updateEx) {
                log.error("更新失败状态时发生错误: {}", updateEx.getMessage(), updateEx);
            }
            throw new ServiceException("附件处理失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取条目标题
     */
    private String getItemTitle(String itemUuid) {
        KnowledgeItem item = knowledgeItemMapper.selectOne(
            Wrappers.<KnowledgeItem>lambdaQuery()
                .eq(KnowledgeItem::getItemUuid, itemUuid)
                .eq(KnowledgeItem::getDelFlag, "0")
                .last("LIMIT 1")
        );
        return item != null ? item.getTitle() : "";
    }

    private double calculateCosineSimilarity(float[] vector1, float[] vector2) {
        if (vector1.length != vector2.length) {
            return 0.0;
        }
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
        }
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * 检查用户是否有删除知识库权限
     *
     * @param knowledgeInfo 知识库
     */
    public void check(KnowledgeInfo knowledgeInfo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        // 超级管理员可以删除所有知识库
        if (Long.valueOf(1).equals(loginUser.getUserId())) {
            return;
        }
        if (!knowledgeInfo.getUid().equals(loginUser.getUserId())) {
            throw new SecurityException("权限不足");
        }
    }

    private void sortByRelevance(List<KnowledgeInfoVo> items, String keyword) {
        if (items == null || items.isEmpty() || StringUtils.isBlank(keyword)) {
            return;
        }
        String lowerKeyword = keyword.toLowerCase().trim();
        String[] keywords = lowerKeyword.split("\\s+");
        items.sort((a, b) -> {
            double scoreA = calculateRelevanceScore(a, keywords);
            double scoreB = calculateRelevanceScore(b, keywords);
            return Double.compare(scoreB, scoreA);
        });
    }

    private double calculateRelevanceScore(KnowledgeInfoVo item, String[] keywords) {
        double score = 0.0;
        for (String keyword : keywords) {
            score += countMatches(item.getKname(), keyword) * 3.0;
            score += countMatches(item.getDescription(), keyword) * 1.0;
            if (item.getKname() != null && item.getKname().toLowerCase().startsWith(keyword)) {
                score += 10.0;
            }
            if (item.getKname() != null && item.getKname().toLowerCase().equals(keyword)) {
                score += 20.0;
            }
        }
        return score;
    }

    private int countMatches(String text, String keyword) {
        if (text == null || keyword == null) {
            return 0;
        }
        String lowerText = text.toLowerCase();
        int count = 0;
        int index = 0;
        while ((index = lowerText.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }
        return count;
    }

    private boolean canEditKnowledge(KnowledgeInfoVo vo, Long currentUserId) {
        if (currentUserId == null || vo == null) {
            return false;
        }
        if (Long.valueOf(1).equals(currentUserId)) {
            return true;
        }
        return vo.getUid() != null && vo.getUid().equals(currentUserId);
    }

    @Override
    public void refreshAllKnowledgeStatistics() {
        List<KnowledgeInfo> allKnowledgeBases = baseMapper.selectList(Wrappers.lambdaQuery());
        for (KnowledgeInfo knowledgeInfo : allKnowledgeBases) {
            String kid = knowledgeInfo.getKid();
            if (StringUtils.isBlank(kid)) {
                continue;
            }
            try {
                knowledgeItemMapper.updateKnowledgeItemCount(kid);
                knowledgeItemMapper.updateKnowledgeFragmentCount(kid);
                knowledgeItemService.updateKnowledgeDataSize(kid);
            } catch (Exception e) {
                log.warn("刷新知识库统计失败: kid={}, error={}", kid, e.getMessage());
            }
        }
    }

}
