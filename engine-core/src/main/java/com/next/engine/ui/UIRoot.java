package com.next.engine.ui;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.style.ComputedStyle;
import com.next.engine.ui.style.StyleEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public final class UIRoot extends AbstractContainer {

    @Getter private final FocusManager focusManager = new FocusManager(this);
    @Getter @Setter private StyleEngine styleEngine;

    // content bounds for root should always have x and y equal to 0,
    // preventing layouts to break.
    private final Rect contentBounds = new Rect();

    public UIRoot(Rect bounds) {
        this(bounds, new AbsoluteLayout());
    }

    public UIRoot(Rect bounds, Layout layout) {
        Objects.requireNonNull(bounds, "Bounds for UIRoot cannot be null");
        Objects.requireNonNull(layout, "Layout for UIRoot cannot be null");

        super();
        this.localBounds.set(bounds);
        this.layout = layout;

        this.preferredSize.set(bounds.width, bounds.height);
        contentBounds.width = bounds.width;
        contentBounds.height = bounds.height;
    }

    @Override
    public Rect contentBounds() {
        return contentBounds;
    }

    /// Updates layout if needed and collect draw request from the children nodes of this container.
    /// Safe to call every frame.
    public void render(RenderQueue queue) {
        resolveStyles(this);
        if (dirty) {
            measure();
            updateLayout();
            focusManager.rebuild();
        }
        draw(queue);
    }

    private void resolveStyles(AbstractNode node) {
        if (node.style.isDirty()) {
            if (node.computedStyle == null) node.computedStyle = new ComputedStyle();

            node.computedStyle.clear();
            styleEngine.computeStyle(node, node.computedStyle);
            node.style.markClean();
        }

        if (node instanceof AbstractContainer container) {
            for (int i = 0; i < container.children.size(); i++) {
                resolveStyles(container.children.get(i));
            }
        }
    }
}
