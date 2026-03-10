package org.ruoyi.knowledge.curation.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.knowledge.curation.domain.KnowledgeItem;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemHistory;
import org.ruoyi.knowledge.curation.domain.vo.FieldDiffVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeItemHistoryVo;
import org.ruoyi.knowledge.curation.domain.vo.VersionDiffVo;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemHistoryMapper;
import org.ruoyi.knowledge.curation.mapper.KnowledgeItemMapper;
import org.ruoyi.knowledge.curation.service.IKnowledgeItemHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    /**
     * 字段名 → 中文标签映射（用于 diff 展示）
     */
    private static final Map<String, String> FIELD_LABEL_MAP = new LinkedHashMap<>();
    static {
        FIELD_LABEL_MAP.put("title", "标题");
        FIELD_LABEL_MAP.put("summary", "摘要");
        FIELD_LABEL_MAP.put("vulnerabilityType", "漏洞类型");
        FIELD_LABEL_MAP.put("language", "适用语言");
        FIELD_LABEL_MAP.put("severity", "风险等级");
        FIELD_LABEL_MAP.put("ruleId", "规则ID");
        FIELD_LABEL_MAP.put("problemDescription", "问题描述");
        FIELD_LABEL_MAP.put("fixSolution", "修复方案");
        FIELD_LABEL_MAP.put("exampleCode", "示例代码");
    }

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
            throw new ServiceException("知识条目不存在");
        }

        int currentVersion = item.getCurrentVersion() != null ? item.getCurrentVersion() : 0;
        int versionCount = item.getVersionCount() != null ? item.getVersionCount() : 0;
        int newVersion = currentVersion + 1;

        if (StringUtils.isBlank(changeType)) {
            changeType = "update";
        }

        KnowledgeItemHistory history = new KnowledgeItemHistory();
        history.setItemUuid(itemUuid);
        history.setVersion(newVersion);
        history.setIsCurrent("1");
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
        history.setChangedAt(new Date());

        // 将旧的 isCurrent 标记清除
        baseMapper.update(null,
            Wrappers.<KnowledgeItemHistory>lambdaUpdate()
                .eq(KnowledgeItemHistory::getItemUuid, itemUuid)
                .eq(KnowledgeItemHistory::getIsCurrent, "1")
                .set(KnowledgeItemHistory::getIsCurrent, "0")
        );

        boolean flag = baseMapper.insert(history) > 0;
        if (flag) {
            item.setCurrentVersion(newVersion);
            item.setVersionCount(versionCount + 1);
            item.setCurrentVersionId(history.getId());
            knowledgeItemMapper.updateById(item);
        }
        return flag;
    }

    @Override
    public VersionDiffVo diffVersions(String itemUuid, Integer fromVersion, Integer toVersion) {
        if (StringUtils.isBlank(itemUuid)) {
            throw new ServiceException("条目UUID不能为空");
        }
        if (fromVersion == null || toVersion == null) {
            throw new ServiceException("版本号不能为空");
        }
        if (fromVersion.equals(toVersion)) {
            throw new ServiceException("起始版本和目标版本不能相同");
        }

        KnowledgeItemHistory fromHistory = baseMapper.selectByItemUuidAndVersion(itemUuid, fromVersion);
        KnowledgeItemHistory toHistory = baseMapper.selectByItemUuidAndVersion(itemUuid, toVersion);

        if (fromHistory == null) {
            throw new ServiceException("起始版本 v" + fromVersion + " 不存在");
        }
        if (toHistory == null) {
            throw new ServiceException("目标版本 v" + toVersion + " 不存在");
        }

        VersionDiffVo result = new VersionDiffVo();
        result.setItemUuid(itemUuid);
        result.setFromVersion(fromVersion);
        result.setToVersion(toVersion);
        result.setFromChangedByName(fromHistory.getChangedByName());
        result.setToChangedByName(toHistory.getChangedByName());
        result.setFromChangedAt(fromHistory.getChangedAt());
        result.setToChangedAt(toHistory.getChangedAt());

        List<FieldDiffVo> diffs = new ArrayList<>();
        List<String> changedLabels = new ArrayList<>();

        // 逐字段对比
        compareField(diffs, changedLabels, "title",
            fromHistory.getTitle(), toHistory.getTitle());
        compareField(diffs, changedLabels, "summary",
            fromHistory.getSummary(), toHistory.getSummary());
        compareField(diffs, changedLabels, "vulnerabilityType",
            fromHistory.getVulnerabilityType(), toHistory.getVulnerabilityType());
        compareField(diffs, changedLabels, "language",
            fromHistory.getLanguage(), toHistory.getLanguage());
        compareField(diffs, changedLabels, "severity",
            fromHistory.getSeverity(), toHistory.getSeverity());
        compareField(diffs, changedLabels, "ruleId",
            fromHistory.getRuleId(), toHistory.getRuleId());
        compareField(diffs, changedLabels, "problemDescription",
            fromHistory.getProblemDescription(), toHistory.getProblemDescription());
        compareField(diffs, changedLabels, "fixSolution",
            fromHistory.getFixSolution(), toHistory.getFixSolution());
        compareField(diffs, changedLabels, "exampleCode",
            fromHistory.getExampleCode(), toHistory.getExampleCode());

        result.setDiffs(diffs);
        result.setChangedFieldCount(changedLabels.size());
        result.setChangedFieldLabels(changedLabels);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean restoreToVersion(String itemUuid, Integer targetVersion, String reason) {
        if (StringUtils.isBlank(itemUuid)) {
            throw new ServiceException("条目UUID不能为空");
        }
        if (targetVersion == null) {
            throw new ServiceException("目标版本号不能为空");
        }
        if (StringUtils.isBlank(reason)) {
            throw new ServiceException("恢复原因不能为空");
        }

        KnowledgeItem item = knowledgeItemMapper.selectByItemUuid(itemUuid);
        if (item == null) {
            throw new ServiceException("知识条目不存在");
        }

        KnowledgeItemHistory targetHistory = baseMapper.selectByItemUuidAndVersion(itemUuid, targetVersion);
        if (targetHistory == null) {
            throw new ServiceException("目标版本 v" + targetVersion + " 不存在");
        }

        // 先为当前状态创建快照（保留审计链路）
        createVersionSnapshot(itemUuid, "pre_restore",
            "恢复前自动快照（即将恢复到 v" + targetVersion + "）");

        // 将目标版本的内容写回条目主表
        item.setTitle(targetHistory.getTitle());
        item.setSummary(targetHistory.getSummary());
        item.setVulnerabilityType(targetHistory.getVulnerabilityType());
        item.setLanguage(targetHistory.getLanguage());
        item.setSeverity(targetHistory.getSeverity());
        item.setProblemDescription(targetHistory.getProblemDescription());
        item.setFixSolution(targetHistory.getFixSolution());
        item.setExampleCode(targetHistory.getExampleCode());
        knowledgeItemMapper.updateById(item);

        // 再创建一个"恢复完成"的快照
        createVersionSnapshot(itemUuid, "restore",
            "从 v" + targetVersion + " 恢复，原因：" + reason);

        return true;
    }

    /**
     * 比较单个字段，生成 diff 项
     */
    private void compareField(List<FieldDiffVo> diffs, List<String> changedLabels,
                              String fieldName, String oldValue, String newValue) {
        String label = FIELD_LABEL_MAP.getOrDefault(fieldName, fieldName);
        String normalizedOld = normalizeValue(oldValue);
        String normalizedNew = normalizeValue(newValue);

        String diffType;
        if (normalizedOld.isEmpty() && normalizedNew.isEmpty()) {
            diffType = "unchanged";
        } else if (normalizedOld.isEmpty()) {
            diffType = "added";
        } else if (normalizedNew.isEmpty()) {
            diffType = "removed";
        } else if (normalizedOld.equals(normalizedNew)) {
            diffType = "unchanged";
        } else {
            diffType = "modified";
        }

        FieldDiffVo diff = FieldDiffVo.builder()
            .fieldName(fieldName)
            .fieldLabel(label)
            .oldValue(oldValue)
            .newValue(newValue)
            .diffType(diffType)
            .build();
        diffs.add(diff);

        if (!"unchanged".equals(diffType)) {
            changedLabels.add(label);
        }
    }

    private String normalizeValue(String value) {
        return value == null ? "" : value.trim();
    }
}
