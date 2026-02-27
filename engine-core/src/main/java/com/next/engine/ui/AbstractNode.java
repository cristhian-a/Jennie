package com.next.engine.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.component.UIComponent;
import com.next.engine.ui.style.ComputedStyle;
import com.next.engine.ui.style.Style;
import com.next.engine.ui.style.StyleEngine;

import java.util.*;

/// Base class for UI elements.
public abstract class AbstractNode {
    final Style style = new Style(this);

    public final Style style() {
        return style;
    }

    protected ComputedStyle computedStyle;

    protected StyleEngine getStyleEngine() {
        if (parent != null) return parent.getStyleEngine();
        return null;
    }

    public String getTypeName() {
        return this.getClass().getSimpleName();
    }

    protected Rect localBounds = new Rect();
    protected Rect globalBounds = new Rect();
    protected Size preferredSize = new Size();
    protected boolean dirty = true;
    protected boolean visible = true;
    protected AbstractContainer parent;

    public final void markDirty() {
        if (dirty) return;
        dirty = true;
        style.markDirty();
        if (parent != null) parent.markDirty();
    }

    public final boolean isDirty() { return dirty; }

    public final boolean isVisible() { return visible; }

    public final void setVisible(boolean visible) {
        this.visible = visible;
        markDirty();
    }

    public final void setParent(AbstractContainer parent) {
        this.parent = parent;
        markDirty();
    }

    public abstract void measure();

    public final void updateLayout() {
        if (parent != null) {
            Rect parentBounds = parent.globalBounds;
            globalBounds.set(
                    parentBounds.x + localBounds.x,
                    parentBounds.y + localBounds.y,
                    localBounds.width,
                    localBounds.height
            );
        } else {
            globalBounds.set(localBounds);
        }

        dirty = false;
        onLayout();
    }

    public abstract void onLayout();

    public void draw(RenderQueue queue) {
        drawBackground(queue);
    }

    protected void drawBackground(RenderQueue queue) {
        if (computedStyle == null) return;

        int bg = computedStyle.backgroundColor;
        int bw = computedStyle.borderWidth;
        int bc = computedStyle.borderColor;
        int arc = computedStyle.cornerRadius;

        queue.fillRoundRect(Layer.UI_SCREEN, globalBounds, bg, arc);
        queue.roundStrokeRect(Layer.UI_SCREEN, globalBounds, bw, bc, arc);
    }

    // Anchoring
    protected Align anchorX = Align.START;
    protected Align anchorY = Align.START;

    public final void anchorX(Align align) {
        anchorX = align;
        markDirty();
    }

    public final void anchorY(Align align) {
        anchorY = align;
        markDirty();
    }

    public final void setAnchor(Align alignX, Align alignY) {
        anchorX = alignX;
        anchorY = alignY;
        markDirty();
    }

    // Componentization
    private final List<UIComponent> components = new ArrayList<>();

    public final void addComponent(UIComponent component) {
        components.add(component);
        component.onAttach(this);
    }

    public final void removeComponent(UIComponent component) {
        components.remove(component);
        component.onDetach();
    }

    public final <T extends UIComponent> T getComponent(Class<T> clazz) {
        for (int i = 0; i < components.size(); i++) {
            UIComponent component = components.get(i);
            if (clazz.isInstance(component)) {
                return clazz.cast(component);
            }
        }
        return null;
    }
}
