package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.KnowledgeFragment;

import java.util.List;

/**
 * 知识片段业务对象 knowledge_fragment
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KnowledgeFragment.class, reverseConvertGenerate = false)
public class KnowledgeFragmentBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 知识库ID
     */
    @NotBlank(message = "知识库ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String kid;

    /**
     * 知识条目UUID（用于查询）
     */
    private String itemUuid;

    /**
     * 文档ID
     */
    @NotBlank(message = "文档ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String docId;

    /**
     * 文档ID列表（多选筛选）
     */
    private List<String> docIds;

    /**
     * 知识片段ID
     */
    @NotBlank(message = "知识片段ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String fid;

    /**
     * 片段索引下标
     */
    @NotNull(message = "片段索引下标不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long idx;

    /**
     * 文档内容
     */
    @NotBlank(message = "文档内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    /**
     * 搜索关键词（内容模糊匹配）
     */
    private String searchKeyword;

    /**
     * 排序字段（idx/create_time/content_length）
     */
    private String orderBy;

    /**
     * 排序方向（asc/desc）
     */
    private String order;

    /**
     * 是否包含未完成处理的片段（默认false，表示过滤掉未完成的片段）
     * true: 包含所有片段（包括未完成处理的）
     * false: 只返回处理任务已完成的片段，或没有关联处理任务的片段（旧数据）
     */
    private Boolean includeIncomplete;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String remark;


}
