package com.next.engine.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;

public class FramePanel extends Panel {

    private final int arc;
    private final int thickness;
    private final int borderColor;
    private final int backgroundColor;

    public FramePanel(Rect bounds, Layout layout, float padding,
                      int arc, int thickness, int borderColor, int backgroundColor
    ) {
        this.arc = arc;
        this.thickness = thickness;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;

        super(bounds, layout, padding);
    }

    @Override
    public void draw(RenderQueue queue) {
        Rect b = globalBounds;

        float bx = b.x - thickness;
        float by = b.y - thickness;
        float bw = b.width + thickness * 2;
        float bh = b.height + thickness * 2;

        queue.fillRoundRect(
                Layer.UI_SCREEN,
                bx, by, bw, bh,
                backgroundColor,
                arc + thickness * 2
        );

        queue.roundStrokeRect(
                Layer.UI_SCREEN,
                b.x, b.y,
                b.width, b.height,
                thickness,
                borderColor,
                arc
        );

        super.draw(queue);
    }
}
