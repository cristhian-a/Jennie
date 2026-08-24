package com.next.engine.ui.widget;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.TextFont;
import com.next.engine.ui.AbstractNode;

public final class TextBlock extends AbstractNode {

    private static final String LINE_SEPARATOR = "\n";

    private final String fontId;
    private final TextFont font;
    private final int color;

    private String text;
    private String[] lines = new String[0];
    private float lineSpacing = 0f;

    public TextBlock(String text, String fontId, int color) {
        this.fontId = fontId;
        this.color = color;
        this.text = text;

        this.font = Registry.fonts.get(fontId);
    }

    private void recalculateBounds() {
        lines = text.split(LINE_SEPARATOR);

        float maxWidth = 0f;
        for (String line : lines) maxWidth = Math.max(maxWidth, font.measureWidth(line));

        float height = lines.length * font.getLineHeight()
                + (lines.length - 1) * lineSpacing;

        localBounds.set(0, 0, maxWidth, height);
        markDirty();
    }

    public void setText(String text) {
        this.text = text;
        recalculateBounds();
    }

    public void setLineSpacing(float lineSpacing) {
        this.lineSpacing = lineSpacing;
        recalculateBounds();
    }

    @Override
    public void onLayout() {}

    @Override
    public void measure() {
        if (!dirty) return;

        if (lines.length == 0) {
            preferredSize.set(0, 0);
            return;
        }

        preferredSize.set(parent.contentBounds().width, font.getLineHeight() * lines.length);
        localBounds.width = preferredSize.width;
        localBounds.height = preferredSize.height;
        offsetY = font.getAscent();
    }

    private float offsetY;

    @Override
    public void draw(RenderQueue queue) {
        float yy = globalBounds.y + offsetY;

        for (String line : lines) {
            queue.submit(
                    Layer.UI_SCREEN,
                    line,
                    fontId,
                    color,
                    globalBounds.x, yy,
                    RenderPosition.AXIS,
                    0
            );

            yy += font.getLineHeight() + lineSpacing;
        }
    }
}
