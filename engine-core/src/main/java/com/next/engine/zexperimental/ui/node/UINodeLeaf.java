package com.next.engine.zexperimental.ui.node;

import com.next.engine.graphics.RenderQueue;

public abstract class UINodeLeaf extends UINode {

    @Override
    public final void measure() {
        if (!dirty) return;
        onMeasure();
    }

    public abstract void onMeasure();
    public abstract void draw(RenderQueue queue);

    public final void add(UINode child) {
        throw new UnsupportedOperationException("Cannot add child to leaf node");
    }
}
