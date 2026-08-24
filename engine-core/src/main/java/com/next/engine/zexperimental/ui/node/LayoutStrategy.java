package com.next.engine.zexperimental.ui.node;

import java.util.List;

public interface LayoutStrategy {
    void calculatePreferredSize(UINodeContainer container, List<UINode> children);
    void arrange(UINodeContainer container, List<UINode> children);
}
