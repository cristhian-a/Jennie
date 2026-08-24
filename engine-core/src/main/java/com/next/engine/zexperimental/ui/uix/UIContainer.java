package com.next.engine.zexperimental.ui.uix;

import java.util.ArrayList;
import java.util.List;

public class UIContainer extends UINode {

    protected final List<UINode> children = new ArrayList<>();
    protected LayoutStrategy layout;

    public final void add(UINode child) {
        children.add(child);
        child.parent = this;
        markAsDirty();
    }

    public final void remove(UINode child) {
        children.remove(child);
        child.parent = null;
        markAsDirty();
    }

    @Override
    public void measure() {
        if (isDirty()) {
            for (int i = 0; i < children.size(); i++) {
                children.get(i).measure();
            }
            layout.calculatePreferredSize(this, children);
        }
    }

    @Override
    public void update(double delta) {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).update(delta);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(canvas);
        }
    }

    final void layoutChildren() {
        if (!isDirty()) {
            layout.arrange(this, children);
            for (int i = 0; i < children.size(); i++) {
                var child = children.get(i);
                if (child instanceof UIContainer c) c.layoutChildren();
            }
            undirty();
        }
    }
}
