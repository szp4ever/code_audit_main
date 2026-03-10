package org.ruoyi.knowledge.curation.service.util;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;

import java.util.Map;

/**
 * 自定义 iText ParagraphRenderer，在渲染条目标题时收集其所在的页码，
 * 用于在 PDF 导出中生成目录（TOC）的页码映射。
 * <p>
 * 使用方式：
 * <pre>
 *   Map&lt;String, Integer&gt; pageNumberMap = new LinkedHashMap&lt;&gt;();
 *   titlePara.setNextRenderer(new PageNumberCollectorRenderer(titlePara, "item_0", pageNumberMap));
 * </pre>
 * 渲染完成后，pageNumberMap 中会包含 {"item_0": 3, "item_1": 5, ...} 的映射。
 */
public class PageNumberCollectorRenderer extends ParagraphRenderer {

    private final String destinationId;
    private final Map<String, Integer> pageNumberMap;

    /**
     * @param modelElement  被渲染的 Paragraph 元素
     * @param destinationId 条目的锚点 ID（如 "item_0"）
     * @param pageNumberMap 用于收集 destinationId → pageNumber 映射的 Map
     */
    public PageNumberCollectorRenderer(Paragraph modelElement, String destinationId, Map<String, Integer> pageNumberMap) {
        super(modelElement);
        this.destinationId = destinationId;
        this.pageNumberMap = pageNumberMap;
    }

    @Override
    public LayoutResult layout(LayoutContext layoutContext) {
        LayoutResult result = super.layout(layoutContext);
        if (result.getStatus() != LayoutResult.NOTHING && occupiedArea != null) {
            int pageNumber = occupiedArea.getPageNumber();
            if (pageNumber > 0) {
                pageNumberMap.put(destinationId, pageNumber);
            }
        }
        return result;
    }

    @Override
    public IRenderer getNextRenderer() {
        return new PageNumberCollectorRenderer((Paragraph) modelElement, destinationId, pageNumberMap);
    }
}
