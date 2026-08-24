package com.next.engine.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.style.ComputedStyle;

import java.util.ArrayList;
import java.util.List;

public class AbstractContainer extends AbstractNode {

    protected final List<AbstractNode> children = new ArrayList<>();
    protected final Rect contentArea;
    protected Layout layout;

    protected AbstractContainer() {
        this(new Rect());
    }

    protected AbstractContainer(float width, float height) {
        this(new Rect(0, 0, width, height));
    }

    public AbstractContainer(Rect rect) {
        this(rect, new AbsoluteLayout());
    }

    public AbstractContainer(Rect rect, Layout layout) {
        this.layout = layout;
        this.contentArea = rect;

        super();
        this.localBounds.set(rect);
        this.preferredSize.set(rect.width, rect.height);
    }

    public final void add(AbstractNode child) {
        children.add(child);
        child.setParent(this);
        child.markDirty();
        markDirty();
    }

    public final void add(List<? extends AbstractNode> children) {
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.parent = this;
            child.markDirty();
        }
        this.children.addAll(children);
        markDirty();
    }

    public final void remove(AbstractNode child) {
        children.remove(child);
        child.parent = null;
        markDirty();
    }

    public final void removeAll() {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).parent = null;
        }
        children.clear();
        markDirty();
    }

    public final boolean contains(AbstractNode node) {
        return children.contains(node);
    }

    public Rect contentBounds() {
        return contentArea;
    }

    protected final List<AbstractNode> visibleChildren = new ArrayList<>();

    @Override
    public final void measure() {
        if (!dirty) return;
        visibleChildren.clear();

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            if (child.visible) {
                // after measurement, the visible children list is stable, so we can use it in all methods
                // that happen after measurement. As measure is invoked before updateLayout in the root, it
                // is safe to use almost everytime after this loop's end.
                visibleChildren.add(child);
                child.measure();
            }
        }
        layout.calculatePreferredSize(this, visibleChildren);
    }

    @Override
    public final void onLayout() {
        ComputedStyle s = computedStyle;

        // X and Y should always start with 0 for the content area (plus any padding or some other calculation result)
        contentArea.set(
                0 + s.paddingLeft,
                0 + s.paddingTop,
                localBounds.width - s.paddingLeft - s.paddingRight,
                localBounds.height - s.paddingTop - s.paddingBottom
        );

        layout.arrange(this, visibleChildren);

        for (int i = 0; i < visibleChildren.size(); i++) {
            var child = visibleChildren.get(i);
            child.updateLayout();
        }
    }

    @Override
    public void draw(RenderQueue queue) {
        if (!visible) return;

        super.draw(queue);

        for (int i = 0; i < visibleChildren.size(); i++) {
            var child = visibleChildren.get(i);
            child.draw(queue);
        }

        // debug stuff
//        queue.rectangle(
//                Layer.DEBUG_SCREEN,
//                globalBounds.x, globalBounds.y,
//                localBounds.width, localBounds.height,
//                0xFFFF0000
//        );
    }
}
