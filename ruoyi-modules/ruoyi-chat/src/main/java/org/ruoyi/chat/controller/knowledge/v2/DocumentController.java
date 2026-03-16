package org.ruoyi.chat.controller.knowledge.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeAttachBo;
import org.ruoyi.knowledge.ingestion.domain.bo.KnowledgeInfoUploadBo;
import org.ruoyi.knowledge.ingestion.domain.vo.AttachFacetStatsVo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeAttachProcessVo;
import org.ruoyi.knowledge.ingestion.domain.vo.KnowledgeAttachVo;
import org.ruoyi.knowledge.ingestion.enums.ProcessingStatus;
import org.ruoyi.knowledge.ingestion.service.IAttachProcessService;
import org.ruoyi.knowledge.ingestion.service.IKnowledgeAttachService;
import org.ruoyi.knowledge.curation.service.IKnowledgeInfoService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文档管理
 * 整合知识库附件上传、附件管理、附件处理状态管理
 *
 * @author system
 * @date 2026-03-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge/document")
@Tag(name = "文档管理")
@Slf4j
public class DocumentController extends BaseController {

    private final IKnowledgeInfoService knowledgeInfoService;

    private final IKnowledgeAttachService attachService;

    private final IAttachProcessService attachProcessService;

    // ==================== 文档上传 ====================

    /**
     * 上传知识库附件
     * 使用@RequestParam绑定multipart/form-data参数（包括MultipartFile）
     * 对于简单的FormData上传（key-value pairs + file），@RequestParam是标准做法
     */
    @Operation(summary = "上传文档")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<KnowledgeAttachVo> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("kid") String kid,
            @RequestParam(value = "autoCreateItems", required = false) Boolean autoCreateItems,
            @RequestParam(value = "autoClassify", required = false) Boolean autoClassify) throws Exception {

        // 构建BO对象
        KnowledgeInfoUploadBo bo = new KnowledgeInfoUploadBo();
        bo.setFile(file);
        bo.setKid(kid);
        bo.setAutoCreateItems(autoCreateItems);
        bo.setAutoClassify(autoClassify);

        // 先创建附件记录和processId（同步，快速完成）
        KnowledgeAttachVo vo = knowledgeInfoService.uploadAndCreateAttach(bo);

        // 异步执行后续处理（解析、分块、匹配、创建条目、向量化等）
        knowledgeInfoService.processAttachAsync(vo.getId(), vo.getDocId(), kid, autoCreateItems, autoClassify);

        // 立即返回响应，包含attachId和processId，让前端可以开始轮询进度
        return R.ok(vo);
    }

    /**
     * 分片上传接口（前端使用，后端暂不支持分片，直接返回成功）
     * 注意：当前实现中，分片上传功能在前端实现，后端统一接收完整文件
     * 如需实现真正的分片上传，需要添加临时存储和合并逻辑
     */
    @Operation(summary = "分片上传")
    @PostMapping(value = "/upload-chunk")
    public R<Void> uploadChunk(
            @RequestParam("file") MultipartFile chunk,
            @RequestParam("kid") String kid,
            @RequestParam("chunkIndex") Integer chunkIndex,
            @RequestParam("totalChunks") Integer totalChunks,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") Long fileSize,
            @RequestParam(value = "autoCreateItems", required = false) Boolean autoCreateItems,
            @RequestParam(value = "autoClassify", required = false) Boolean autoClassify) {
        // TODO: 实现分片上传逻辑
        // 1. 将分片保存到临时存储
        // 2. 返回成功，等待所有分片上传完成后调用合并接口
        return R.ok();
    }

    /**
     * 合并分片接口（前端使用，后端暂不支持分片，直接返回失败提示使用普通上传）
     * 注意：当前实现中，分片上传功能在前端实现，后端统一接收完整文件
     * 如需实现真正的分片上传，需要添加临时存储和合并逻辑
     */
    @Operation(summary = "合并分片")
    @PostMapping(value = "/merge-chunks")
    public R<KnowledgeAttachVo> mergeChunks(
            @RequestParam("fileName") String fileName,
            @RequestParam("kid") String kid,
            @RequestParam("totalChunks") Integer totalChunks,
            @RequestParam(value = "autoCreateItems", required = false) Boolean autoCreateItems,
            @RequestParam(value = "autoClassify", required = false) Boolean autoClassify) {
        // TODO: 实现分片合并逻辑
        // 1. 从临时存储读取所有分片
        // 2. 合并分片为完整文件
        // 3. 调用 storeContent 处理文件
        // 4. 清理临时存储
        return R.fail("分片上传功能暂未实现，请使用普通上传方式");
    }

    // ==================== 附件管理 ====================

    /**
     * 查询知识库附件列表
     */
    @Operation(summary = "查询附件列表")
    @GetMapping("/attach/list")
    public TableDataInfo<KnowledgeAttachVo> getAttachList(KnowledgeAttachBo bo, PageQuery pageQuery) {
        return attachService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取知识库附件详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取附件详情")
    @GetMapping("/attach/info/{id}")
    public R<KnowledgeAttachVo> getAttachInfo(@NotNull(message = "主键不能为空")
                                              @PathVariable Long id) {
        return R.ok(attachService.queryById(id));
    }

    /**
     * 删除知识库附件（通过docId）
     */
    @Operation(summary = "删除附件（按docId）")
    @DeleteMapping("/attach/{docId}")
    public R<Void> removeAttach(@NotEmpty(message = "文档ID不能为空")
                                @PathVariable String docId) {
        attachService.removeKnowledgeAttach(docId);
        return R.ok();
    }

    /**
     * 删除知识库附件（通过processId）
     * 用于上传过程中删除任务，此时可能还没有docId
     */
    @Operation(summary = "删除附件（按processId）")
    @DeleteMapping("/attach/remove-by-process/{processId}")
    public R<Void> removeAttachByProcessId(@NotEmpty(message = "处理任务ID不能为空")
                                           @PathVariable String processId) {
        attachService.removeKnowledgeAttachByProcessId(processId);
        return R.ok();
    }

    /**
     * 删除知识库附件（通过kid和docName）
     * 最保险的删除方式，因为文件名是用户最直观的标识，且始终存在于任务对象中
     */
    @Operation(summary = "删除附件（按知识库ID和文档名）")
    @DeleteMapping("/attach/remove-by-name")
    public R<Void> removeAttachByKidAndName(@RequestParam("kid") @NotEmpty(message = "知识库ID不能为空") String kid,
                                            @RequestParam("docName") @NotEmpty(message = "文档名称不能为空") String docName) {
        attachService.removeKnowledgeAttachByKidAndName(kid, docName);
        return R.ok();
    }

    /**
     * 重新处理知识库附件
     */
    @Operation(summary = "重新处理附件")
    @PostMapping("/attach/reprocess/{docId}")
    public R<Void> reprocessAttach(@NotEmpty(message = "文档ID不能为空")
                                  @PathVariable String docId) {
        attachService.reprocessAttach(docId);
        return R.ok();
    }

    /**
     * 下载知识库附件
     */
    @Operation(summary = "下载附件")
    @GetMapping("/attach/download/{docId}")
    public void downloadAttach(@NotEmpty(message = "文档ID不能为空")
                              @PathVariable String docId,
                              HttpServletResponse response) throws IOException {
        KnowledgeAttachBo attachBo = new KnowledgeAttachBo();
        attachBo.setDocId(docId);
        List<KnowledgeAttachVo> attachList = attachService.queryList(attachBo);
        if (attachList.isEmpty()) {
            throw new ServiceException("附件不存在");
        }
        KnowledgeAttachVo attach = attachList.get(0);
        attachService.downloadAttach(attach.getId(), response);
    }

    /**
     * 获取附件条目数量分布统计（用于智能分箱）
     */
    @Operation(summary = "附件条目数量分布统计")
    @PostMapping("/attach/item-count-distribution")
    public R<List<Integer>> getItemCountDistribution(@RequestBody KnowledgeAttachBo bo) {
        List<Integer> distribution = attachService.getItemCountDistribution(bo);
        return R.ok(distribution);
    }

    /**
     * 获取附件分面统计（筛选选项和计数）
     */
    @Operation(summary = "附件分面统计")
    @PostMapping("/attach/facet-stats")
    public R<AttachFacetStatsVo> getFacetStats(@RequestBody KnowledgeAttachBo bo) {
        AttachFacetStatsVo stats = attachService.getFacetStats(bo);
        return R.ok(stats);
    }

    // ==================== 处理状态管理 ====================

    /**
     * 获取处理状态
     * 轮询时调用：不尝试加锁，只返回状态和进度
     * 进入审阅界面时调用：尝试加锁（仅在USER_REVIEW_MATCHING或USER_REVIEW_ITEMS状态）
     */
    @Operation(summary = "获取处理状态")
    @GetMapping("/process/{processId}")
    public R<KnowledgeAttachProcessVo> getProcessStatus(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestParam(required = false, defaultValue = "false") Boolean skipLock) {
        KnowledgeAttachProcessVo vo = attachProcessService.getCurrentStatus(processId);

        // 如果skipLock=true（轮询调用），不尝试加锁
        if (skipLock) {
            vo.setLocked(false);
            return R.ok(vo);
        }

        // 只有在需要用户交互的状态下才尝试加锁
        String currentStatus = vo.getCurrentStatus();
        if (ProcessingStatus.USER_REVIEW_MATCHING.getCode().equals(currentStatus) ||
            ProcessingStatus.USER_REVIEW_ITEMS.getCode().equals(currentStatus)) {
            // 尝试加锁（如果锁被占用，返回警告信息，但不阻止访问）
            boolean lockAcquired = attachProcessService.tryLock(processId);
            if (!lockAcquired) {
                vo.setLocked(true);
                vo.setLockMessage("处理任务正被其他用户编辑，请稍后重试");
            } else {
                vo.setLocked(false);
            }
        } else {
            // 非用户交互状态，不需要加锁
            vo.setLocked(false);
        }
        return R.ok(vo);
    }

    /**
     * 批量获取处理状态
     */
    @Operation(summary = "批量获取处理状态")
    @PostMapping("/process/batch")
    public R<List<KnowledgeAttachProcessVo>> getProcessStatusBatch(
            @RequestBody List<String> processIds) {
        return R.ok(attachProcessService.getAttachProcessStatusBatch(processIds));
    }

    /**
     * 确认匹配结果
     */
    @Operation(summary = "确认匹配结果")
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/process/{processId}/confirm-matching")
    public R<Void> confirmMatching(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestBody List<IAttachProcessService.MatchingDecision> decisions) {
        attachProcessService.confirmMatching(processId, decisions);
        return R.ok();
    }

    /**
     * 确认新条目
     */
    @Operation(summary = "确认新条目")
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/process/{processId}/confirm-items")
    public R<Void> confirmItems(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestBody List<IAttachProcessService.ItemModification> modifications) {
        attachProcessService.confirmItems(processId, modifications);
        return R.ok();
    }

    /**
     * 保存草稿
     */
    @Operation(summary = "保存草稿")
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/process/{processId}/save-draft")
    public R<Void> saveDraft(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestBody Map<String, Object> partialData) {
        attachProcessService.saveDraft(processId, partialData);
        return R.ok();
    }

    /**
     * 回退状态
     */
    @Operation(summary = "回退处理状态")
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/process/{processId}/rollback")
    public R<Void> rollback(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId,
            @RequestParam @NotEmpty(message = "目标状态不能为空") String targetStatus) {
        ProcessingStatus status = ProcessingStatus.fromCode(targetStatus);
        attachProcessService.rollback(processId, status);
        return R.ok();
    }

    /**
     * 取消处理任务
     */
    @Operation(summary = "取消处理任务")
    @Log(title = "附件处理", businessType = BusinessType.UPDATE)
    @PostMapping("/process/{processId}/cancel")
    public R<Void> cancelProcess(
            @NotNull(message = "处理任务ID不能为空")
            @PathVariable String processId) {
        attachProcessService.cancelProcess(processId);
        return R.ok();
    }
}
