package com.next.engine.zexperimental.ui.uij;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget implements Node {
    protected Node parent;
    protected final List<Node> children = new ArrayList<>();
    protected Rect rect;
    protected Style style;
    private boolean dirty = true;

    public final boolean isDirty() { return dirty; }

    public final void markAsDirty() {
        if (dirty) return;
        dirty = true;
        if (parent != null && parent instanceof Widget widget) widget.markAsDirty();
    }

    public abstract void measure();
    public abstract void arrange(Rect available);


    // Future Event Stuff
}
