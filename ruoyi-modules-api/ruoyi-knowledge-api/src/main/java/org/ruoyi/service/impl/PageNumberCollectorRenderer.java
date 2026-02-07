package org.ruoyi.service.impl;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;

import java.util.Map;

/**
 * 收集段落页码的自定义Renderer
 * 用于目录页码的准确计算
 */
public class PageNumberCollectorRenderer extends ParagraphRenderer {
    private final String destinationId;
    private final Map<String, Integer> pageNumberMap;

    public PageNumberCollectorRenderer(Paragraph paragraph, String destinationId, Map<String, Integer> pageNumberMap) {
        super(paragraph);
        this.destinationId = destinationId;
        this.pageNumberMap = pageNumberMap;
    }

    @Override
    public void draw(DrawContext drawContext) {
        super.draw(drawContext);
        if (occupiedArea != null && destinationId != null && pageNumberMap != null) {
            int pageNumber = occupiedArea.getPageNumber();
            if (pageNumber > 0) {
                pageNumberMap.put(destinationId, pageNumber);
            }
        }
    }

    @Override
    public IRenderer getNextRenderer() {
        return new PageNumberCollectorRenderer((Paragraph) getModelElement(), destinationId, pageNumberMap);
    }
}
