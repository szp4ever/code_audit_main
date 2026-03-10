package org.ruoyi.knowledge.curation.service.util;

import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;

/**
 * 自定义 iText TextRenderer，保留文本中的空白字符（空格、换行等），
 * 防止 iText 默认的空白折叠行为将连续空格或换行符合并。
 * <p>
 * 用于 PDF 导出中代码块等需要保留原始格式的场景。
 */
public class PreserveWhitespaceTextRenderer extends TextRenderer {

    public PreserveWhitespaceTextRenderer(Text textElement) {
        super(textElement);
    }

    @Override
    public LayoutResult layout(LayoutContext layoutContext) {
        // 设置属性以保留空白
        setProperty(com.itextpdf.layout.properties.Property.NO_SOFT_WRAP_INLINE, Boolean.TRUE);
        return super.layout(layoutContext);
    }

    @Override
    public IRenderer getNextRenderer() {
        return new PreserveWhitespaceTextRenderer((Text) modelElement);
    }
}
