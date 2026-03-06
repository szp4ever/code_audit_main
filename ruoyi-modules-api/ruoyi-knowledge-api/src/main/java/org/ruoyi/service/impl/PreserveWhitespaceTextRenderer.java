package org.ruoyi.service.impl;

import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

/**
 * 保留空白字符的TextRenderer
 * 用于代码块中保留缩进和空格
 */
public class PreserveWhitespaceTextRenderer extends TextRenderer {
    public PreserveWhitespaceTextRenderer(Text textElement) {
        super(textElement);
    }

    @Override
    public IRenderer getNextRenderer() {
        return new PreserveWhitespaceTextRenderer((Text) getModelElement());
    }

    @Override
    public void trimFirst() {
        //不trim，保留leading whitespace
    }
}
