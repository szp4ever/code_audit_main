package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.KnowledgeFeedback;
import org.ruoyi.domain.KnowledgeItem;
import org.ruoyi.domain.bo.KnowledgeFeedbackBo;
import org.ruoyi.domain.bo.KnowledgeItemBo;
import org.ruoyi.domain.vo.KnowledgeFeedbackVo;
import org.ruoyi.domain.vo.KnowledgeItemVo;
import org.ruoyi.mapper.KnowledgeFeedbackMapper;
import org.ruoyi.service.IKnowledgeFeedbackService;
import org.ruoyi.service.IKnowledgeItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 知识反馈Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class KnowledgeFeedbackServiceImpl implements IKnowledgeFeedbackService {

    private final KnowledgeFeedbackMapper baseMapper;
    private final IKnowledgeItemService knowledgeItemService;

    @Override
    public KnowledgeFeedbackVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public KnowledgeFeedbackVo queryByFeedbackUuid(String feedbackUuid) {
        KnowledgeFeedback entity = baseMapper.selectByFeedbackUuid(feedbackUuid);
        if (entity == null) {
            return null;
        }
        return baseMapper.selectVoById(entity.getId());
    }

    @Override
    public TableDataInfo<KnowledgeFeedbackVo> queryPageList(KnowledgeFeedbackBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeFeedback> lqw = buildQueryWrapper(bo);
        Page<KnowledgeFeedbackVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<KnowledgeFeedbackVo> queryByStatus(String status) {
        return baseMapper.selectByStatus(status);
    }

    private LambdaQueryWrapper<KnowledgeFeedback> buildQueryWrapper(KnowledgeFeedbackBo bo) {
        LambdaQueryWrapper<KnowledgeFeedback> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFeedbackUuid()), KnowledgeFeedback::getFeedbackUuid, bo.getFeedbackUuid());
        lqw.eq(StringUtils.isNotBlank(bo.getResultUuid()), KnowledgeFeedback::getResultUuid, bo.getResultUuid());
        lqw.eq(bo.getTaskId() != null, KnowledgeFeedback::getTaskId, bo.getTaskId());
        lqw.eq(bo.getProjectId() != null, KnowledgeFeedback::getProjectId, bo.getProjectId());
        lqw.eq(StringUtils.isNotBlank(bo.getFeedbackType()), KnowledgeFeedback::getFeedbackType, bo.getFeedbackType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), KnowledgeFeedback::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getFilePath()), KnowledgeFeedback::getFilePath, bo.getFilePath());
        return lqw;
    }

    @Override
    public Boolean insertByBo(KnowledgeFeedbackBo bo) {
        KnowledgeFeedback add = MapstructUtils.convert(bo, KnowledgeFeedback.class);
        if (StringUtils.isBlank(add.getFeedbackUuid())) {
            add.setFeedbackUuid(UUID.randomUUID().toString().replace("-", ""));
        }
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(KnowledgeFeedbackBo bo) {
        KnowledgeFeedback update = MapstructUtils.convert(bo, KnowledgeFeedback.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void validEntityBeforeSave(KnowledgeFeedback entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveFeedback(String feedbackUuid, String targetKid, Boolean autoPublish) {
        KnowledgeFeedback feedback = baseMapper.selectByFeedbackUuid(feedbackUuid);
        if (feedback == null || !"pending".equals(feedback.getStatus())) {
            return false;
        }
        KnowledgeItemBo itemBo = new KnowledgeItemBo();
        itemBo.setItemUuid(UUID.randomUUID().toString().replace("-", ""));
        itemBo.setKid(targetKid);
        itemBo.setTitle(feedback.getUserComment() != null ? feedback.getUserComment() : "来自反馈的知识条目");
        itemBo.setSummary(feedback.getUserComment());
        itemBo.setVulnerabilityType(feedback.getFeedbackType());
        itemBo.setLanguage(feedback.getLanguage());
        itemBo.setProblemDescription(feedback.getCodeSnippet());
        itemBo.setFixSolution(feedback.getFixSuggestion() != null ? feedback.getFixSuggestion() : feedback.getCorrectedCode());
        itemBo.setExampleCode(feedback.getContextCode());
        itemBo.setSourceType("feedback");
        itemBo.setSourceFeedbackId(feedback.getId());
        itemBo.setStatus(autoPublish ? "published" : "draft");
        if (autoPublish) {
            itemBo.setPublishTime(new Date());
        }
        Boolean flag = knowledgeItemService.insertByBo(itemBo);
        if (flag) {
            KnowledgeItemVo itemVo = knowledgeItemService.queryById(itemBo.getId());
            feedback.setStatus("approved");
            feedback.setConvertedItemUuid(itemVo.getItemUuid());
            feedback.setApproveTime(new Date());
            feedback.setApproveBy(LoginHelper.getUserId());
            baseMapper.updateById(feedback);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rejectFeedback(String feedbackUuid, String rejectReason) {
        KnowledgeFeedback feedback = baseMapper.selectByFeedbackUuid(feedbackUuid);
        if (feedback == null || !"pending".equals(feedback.getStatus())) {
            return false;
        }
        feedback.setStatus("rejected");
        feedback.setRejectReason(rejectReason);
        feedback.setApproveBy(LoginHelper.getUserId());
        return baseMapper.updateById(feedback) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
