package com.next.engine.zexperimental.ui.node;

import java.util.ArrayList;
import java.util.List;

public class UINodeContainer extends UINode {
    protected final List<UINode> children = new ArrayList<>();
    private LayoutStrategy layout;

    public final void setLayout(LayoutStrategy layout) {
        this.layout = layout;
        markAsDirty();
    }

    public final void add(UINode child) {
        children.add(child);
        markAsDirty();
    }

    public final void remove(UINode child) {
        children.remove(child);
        markAsDirty();
    }

    @Override
    public void measure() {
        if (!dirty) return;

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.measure(); // Deep-first measure
        }

        // Container size might depend on children
        // (e.g., a vertical stack's height is the sum of children's heights)
        layout.calculatePreferredSize(this, children);
    }

    public void layoutChildren() {
        if (!dirty) return;

        layout.arrange(this, children);
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            if (child instanceof UINodeContainer c) c.layoutChildren();
        }
        dirty = false;
    }
}
