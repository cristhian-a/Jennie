package com.next.engine.ui;

import java.util.List;

public final class AlignedLayout implements Layout {

    private final Align alignX;
    private final Align alignY;

    public AlignedLayout(Align alignX, Align alignY) {
        this.alignX = alignX;
        this.alignY = alignY;
    }

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        var parentContent = container.contentBounds();
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);

            Align ax = child.anchorX == null ? alignX : child.anchorX;
            Align ay = child.anchorY == null ? alignY : child.anchorY;

            float x = LayoutUtils.alignX(parentContent, child.preferredSize.width, ax);
            float y = LayoutUtils.alignY(parentContent, child.preferredSize.height, ay);
            child.localBounds.set(x, y, child.preferredSize.width, child.preferredSize.height);
        }
    }
}
