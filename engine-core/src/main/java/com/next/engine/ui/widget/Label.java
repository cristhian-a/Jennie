package com.next.engine.ui.widget;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.TextFont;
import com.next.engine.ui.AbstractNode;
import com.next.engine.ui.Align;

public final class Label extends AbstractNode {

    private final int color;
    private final String fontId;
    private final TextFont font;

    private String text;

    public Label(String text, String fontId, int color, Align alignX, Align alignY) {
        this.text = text;
        this.fontId = fontId;
        this.anchorX = alignX;
        this.anchorY = alignY;
        this.color = color;

        this.font = Registry.fonts.get(fontId);
        recalculateBounds();
    }

    public Label(String text, String fontId, int color) {
        this(text, fontId, color, Align.START, Align.START);
    }

    public Label(float x, float y, String text, String fontId, int color) {
        this(text, fontId, color);
        localBounds.set(x, y, 0, 0);
    }

    private void recalculateBounds() {
        float w = font.measureWidth(text);
        float h = font.getLineHeight();
        localBounds.set(0, 0, w, h);
        markDirty();
    }

    public void setText(String text) {
        this.text = text;
        recalculateBounds();
    }

    @Override
    public void onLayout() {}

    @Override
    public void measure() {
        float w = font.measureWidth(text);
        float h = font.getLineHeight();

        preferredSize.set(w, h);
        localBounds.width = w;
        localBounds.height = h;

        offsetY = font.getAscent();
    }

    private float offsetY;

    @Override
    public void draw(RenderQueue queue) {
        queue.submit(
                Layer.UI_SCREEN,
                text,
                fontId,
                color,
                globalBounds.x, globalBounds.y + offsetY,
                RenderPosition.AXIS,
                0
        );
    }
}
