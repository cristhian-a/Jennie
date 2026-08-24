package com.next.engine.ui;

import java.util.List;

public final class VerticalStackLayout implements Layout {

    private final float spacing;

    public VerticalStackLayout(float spacing) {
        this.spacing = spacing;
    }

//    @Override
//    public void calculatePreferredSize(AbstractContainer container, List<AbstractNode> children) {
//        float totalH = 0, maxW = 0;
//
//        for (int i = 0; i < children.size(); i++) {
//            var child = children.get(i);
//            maxW = Math.max(maxW, child.preferredSize.width);
//            totalH += child.preferredSize.height + spacing;
//        }
//        if (!children.isEmpty()) totalH -= spacing; // removing the last unnecessary spacing
//
//        container.preferredSize.set(maxW, totalH);
//    }

    private final Rect slotRect = new Rect();
    private final Rect innerSlot = new Rect();

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        Rect content = container.contentBounds();
        float cursorY = content.y;

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);

            float slotW = content.width;
            float slotH = child.preferredSize.height;
            slotRect.set(content.x, cursorY, slotW, slotH);

            LayoutUtils.applySpacing(slotRect, innerSlot, child);
            cursorY += slotRect.height + spacing;
        }
    }
}
