package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeFavorite;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识收藏视图对象 knowledge_favorite
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KnowledgeFavorite.class)
public class KnowledgeFavoriteVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 知识条目UUID
     */
    @ExcelProperty(value = "条目UUID")
    private String itemUuid;

    /**
     * 知识库ID
     */
    @ExcelProperty(value = "知识库ID")
    private String kid;
}
