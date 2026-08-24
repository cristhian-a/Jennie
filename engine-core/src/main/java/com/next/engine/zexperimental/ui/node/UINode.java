package com.next.engine.zexperimental.ui.node;

import java.util.ArrayList;
import java.util.List;

public abstract class UINode {
    protected UINode parent;
    protected boolean dirty = true;

    protected final void markAsDirty() {
        if (dirty) return;
        dirty = true;
        if (parent != null) parent.markAsDirty();
    }

    private final List<UIComponent> components = new ArrayList<>();

    public final void addComponent(UIComponent component) {
        components.add(component);
        component.onAttach(this);
    }

    public void handlePointerDown(float mouseX, float mouseY) {
        // Only trigger if the pointer is inside our global bounds
        if (isPointInside(mouseX, mouseY)) {
            for (int i = 0; i < components.size(); i++) {
                var component = components.get(i);
                component.onPointerDown(mouseX, mouseY);
            }
        }
    }

    public boolean isPointInside(float mx, float my) {
        return  mx >= globalX && mx <= globalX + width &&
                my >= globalY && my <= globalY + height;
    }

    // Pass 1: "What do you need?"
    public abstract void measure();

    protected float prefWidth, prefHeight;  // node preferred size
    protected float x, y, width, height;
    protected float globalX, globalY;

    public final void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        updateGlobalPosition();
    }

    protected final void updateGlobalPosition() {
        if (parent == null) {
            globalX = x;
            globalY = y;
        } else {
            globalX = parent.globalX + x;
            globalY = parent.globalY + y;
        }
    }
}
