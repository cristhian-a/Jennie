package com.next.engine.ui;

import java.util.List;

public final class HorizontalStackLayout implements Layout {

    private final float spacing;

    public HorizontalStackLayout(float spacing) {
        this.spacing = spacing;
    }

    private final Rect slot = new Rect();
    private final Rect innerSlot = new Rect();

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        Rect content = container.contentBounds();
        float cursorX = content.x;

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);

            float slotW = child.preferredSize.width;
            float slotH = content.height;
            slot.set(cursorX, content.y, slotW, slotH);

            LayoutUtils.applySpacing(slot, innerSlot, child);
            cursorX += slot.width + spacing;
        }
    }
}
