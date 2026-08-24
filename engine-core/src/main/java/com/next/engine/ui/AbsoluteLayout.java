package com.next.engine.ui;

import java.util.List;

public final class AbsoluteLayout implements Layout {

//    @Override
//    public void calculatePreferredSize(AbstractContainer container, List<AbstractNode> children) {
//        float w = 0, h = 0;
//        for (int i = 0; i < children.size(); i++) {
//            var child = children.get(i);
//            w = Math.max(w, child.preferredSize.width);
//            h = Math.max(h, child.preferredSize.height);
//        }
//        container.preferredSize.set(w, h);
//    }

    private final Rect innerSlot = new Rect();

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        var content = container.contentBounds();

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            float x = child.localBounds.x, y = child.localBounds.y;

            if (x == 0 && y == 0) {
//                x = LayoutUtils.alignX(content, child.preferredSize.width, child.anchorX);
//                y = LayoutUtils.alignY(content, child.preferredSize.height, child.anchorY);
                LayoutUtils.applySpacing(content, innerSlot, child);
            } else
                child.localBounds.set(x, y, child.preferredSize.width, child.preferredSize.height);

        }
    }
}
