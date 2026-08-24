package com.next.engine.zexperimental.ui.node;

import java.util.List;

public final class VerticalLayout implements LayoutStrategy {
    private final float spacing;

    public VerticalLayout(float spacing) {
        this.spacing = spacing;
    }

    @Override
    public void calculatePreferredSize(UINodeContainer container, List<UINode> children) {
        float currentY = 0;
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.setBounds(0, currentY, container.width, child.prefHeight);
            currentY += child.prefHeight + spacing;
        }
    }

    @Override
    public void arrange(UINodeContainer container, List<UINode> children) {
        float h = 0;
        float maxW = 0;
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            h += child.prefHeight + spacing;
            maxW = Math.max(maxW, child.prefWidth);
        }
        container.prefWidth = maxW;
        container.prefHeight = h;
    }
}
