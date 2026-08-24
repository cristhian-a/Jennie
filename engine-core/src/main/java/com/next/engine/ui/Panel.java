package com.next.engine.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;

/// Represents a container for user interface elements with specified bounds and optional padding.
/// The Panel serves as a base class for arranging and drawing `UIElement` children within
/// its bounds. It implements the `UIElement` and `UIContainer` interfaces.
///
/// The panel manages child elements by maintaining a list in their insertion order. The children
/// are drawn in the same order during the rendering phase.
///
/// Subclasses may extend this class to provide custom rendering functionality, such as drawing
/// decorative backgrounds or frames before rendering its children.
///
/// The bounds and padding of the panel define its visual appearance and its content area:
/// 1. The bounds represent the outer dimensions of the panel.
/// 2. The content area is derived by applying the padding to the bounds.
public class Panel extends AbstractContainer {
    private final float padding;

    public Panel(Rect bounds, Layout layout, float padding) {
        this.localBounds = bounds;
        this.padding = padding;
        this.layout = layout;

        this.preferredSize.set(bounds.width, bounds.height);
    }

    private final Rect contentBounds = new Rect(0, 0, 0, 0);

    public final float getContentWidth() {
        float t = padding * 2;
        return Math.max(0, localBounds.width - t);
    }

    public final float getContentHeight() {
        float t = padding * 2;
        return Math.max(0, localBounds.height - t);
    }

    @Override
    public final Rect contentBounds() {
        contentBounds.set(padding, padding, getContentWidth(), getContentHeight());
        return contentBounds;
    }

    /// Draws this panel's child elements in the order they were added.
    ///
    /// @param queue buffered renderer in which the draw commands will be submitted.
    ///
    /// @implSpec Subclasses may override to draw custom content, such as a background or a frame.
    /// If overriding, call `super.draw(queue)` to draw this panel's children. Invoke the `super`
    /// method after any custom drawing logic; this way you ensure the children are drawn over it.
    @Override
    public void draw(RenderQueue queue) {
        super.draw(queue);
    }
}
