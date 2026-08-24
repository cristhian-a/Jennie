package com.next.engine.zexperimental.ui.uix;

public abstract class UINode {
    protected UINode parent;
    private boolean dirty = true;

    public final boolean isDirty() {
        return dirty;
    }

    public final void markAsDirty() {
        if (dirty) return;
        dirty = true;
        if (parent != null) parent.markAsDirty();
    }

    public final void undirty() {
        dirty = false;
    }

    public abstract void measure();

    protected Transform globalTransform = new Transform();
    protected Rect preferredSize = new Rect();
    protected Rect bounds = new Rect();

    public boolean hitTest(float mx, float my) {
        mx += globalTransform.x;
        my += globalTransform.y;
        return bounds.contains(mx, my);
    }

    protected final void setBounds(Rect bounds) {
        this.bounds.set(bounds);
        updateGlobalPosition();
    }

    protected final void updateGlobalPosition() {
        if (parent == null) {
            globalTransform.x = bounds.x;
            globalTransform.y = bounds.y;
        } else {
            globalTransform.x = parent.globalTransform.x + bounds.x;
            globalTransform.y = parent.globalTransform.y + bounds.y;
        }
    }

    public abstract void update(double delta);
    public abstract void draw(Canvas canvas);
}
