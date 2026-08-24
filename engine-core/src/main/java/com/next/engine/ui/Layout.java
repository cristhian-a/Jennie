package com.next.engine.ui;

import java.util.List;

public interface Layout {

    default void calculatePreferredSize(AbstractContainer container, List<AbstractNode> children) {
        var containerBounds = container.localBounds;
        container.preferredSize.set(containerBounds.width, containerBounds.height);
    }

    void arrange(AbstractContainer container, List<AbstractNode> children);
}
