package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.domain.KnowledgeItem;
import org.ruoyi.domain.KnowledgeItemHistory;
import org.ruoyi.domain.vo.KnowledgeItemHistoryVo;
import org.ruoyi.mapper.KnowledgeItemHistoryMapper;
import org.ruoyi.mapper.KnowledgeItemMapper;
import org.ruoyi.service.IKnowledgeItemHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 知识条目版本历史Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class KnowledgeItemHistoryServiceImpl implements IKnowledgeItemHistoryService {

    private final KnowledgeItemHistoryMapper baseMapper;
    private final KnowledgeItemMapper knowledgeItemMapper;

    @Override
    public List<KnowledgeItemHistoryVo> queryByItemUuid(String itemUuid) {
        return baseMapper.selectByItemUuid(itemUuid);
    }

    @Override
    public KnowledgeItemHistoryVo queryByItemUuidAndVersion(String itemUuid, Integer version) {
        KnowledgeItemHistory entity = baseMapper.selectByItemUuidAndVersion(itemUuid, version);
        if (entity == null) {
            return null;
        }
        return baseMapper.selectVoById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createVersionSnapshot(String itemUuid, String changeType, String changeReason) {
        KnowledgeItem item = knowledgeItemMapper.selectByItemUuid(itemUuid);
        if (item == null) {
            return false;
        }
        KnowledgeItemHistory history = new KnowledgeItemHistory();
        history.setItemUuid(itemUuid);
        history.setVersion(item.getCurrentVersion() + 1);
        history.setIsCurrent("0");
        history.setTitle(item.getTitle());
        history.setSummary(item.getSummary());
        history.setVulnerabilityType(item.getVulnerabilityType());
        history.setLanguage(item.getLanguage());
        history.setSeverity(item.getSeverity());
        history.setProblemDescription(item.getProblemDescription());
        history.setFixSolution(item.getFixSolution());
        history.setExampleCode(item.getExampleCode());
        history.setChangeType(changeType);
        history.setChangeReason(changeReason);
        history.setChangedBy(LoginHelper.getUserId());
        history.setChangedByName(LoginHelper.getUsername());
        boolean flag = baseMapper.insert(history) > 0;
        if (flag) {
            item.setCurrentVersion(item.getCurrentVersion() + 1);
            item.setVersionCount(item.getVersionCount() + 1);
            item.setCurrentVersionId(history.getId());
            knowledgeItemMapper.updateById(item);
        }
        return flag;
    }
}
