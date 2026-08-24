package com.next.engine.ui;

public final class LayoutUtils {
    private LayoutUtils() {}

    public static float alignX(Rect container, float elementWidth, Align align) {
        return switch (align) {
            case START  -> container.x;
            case CENTER -> container.centerX() - (elementWidth / 2f);
            case END    -> container.x + container.width - elementWidth;
        };
    }

    public static float alignY(Rect container, float elementHeight, Align align) {
        return switch (align) {
            case START  -> container.y;
            case CENTER -> container.centerY() - (elementHeight / 2f);
            case END    -> container.y + container.height - elementHeight;
        };
    }

    /**
     * Applies margin offsets to the given slot based on the computed style
     * of the specified child node. This method adjusts the dimensions of
     * both the outer and inner spaces to account for the child's margins.
     *
     * @param outerSpace The outer space rectangle which encapsulates the margin adjustments.
     *                   This rectangle will be expanded by the child's margin values.
     * @param innerSpace The inner space rectangle that represents the adjusted area
     *                   available to the child after accounting for top and left margins.
     * @param child      The child node whose computed style determines the margin values
     *                   to apply to the slot.
     */
    public static void applyMarginToSlot(Rect outerSpace, Rect innerSpace, AbstractNode child) {
        var style = child.computedStyle;
        innerSpace.width = outerSpace.width;
        innerSpace.height = outerSpace.height;
        innerSpace.x = outerSpace.x + style.marginLeft;
        innerSpace.y = outerSpace.y + style.marginTop;

        outerSpace.width += style.marginLeft + style.marginRight;
        outerSpace.height += style.marginTop + style.marginBottom;
    }

    /**
     * Adjusts the position and size of a child node within a specified rectangular slot,
     * considering the child's margin and alignment properties. This method applies spacing
     * offsets to the given slot based on the computed style of the child and aligns the child
     * within the adjusted rectangular space.
     *
     * @param slot  The rectangle representing the layout slot to be adjusted based on
     *              the child's margin values. This rectangle will be expanded by the
     *              child's margin properties.
     * @param innerSlot A helper rectangle to represent the inner space available withing the slot.
     *                  This rectangle will be adjusted according to the slot space and child's
     *                  margin values. Its original values will be disregarded and overwritten,
     *                  as they are not considered in this method.
     * @param child The child node whose computed style and preferred size are used for
     *              calculating spacing offsets and alignment within the slot.
     */
    public static void applySpacing(Rect slot, Rect innerSlot, AbstractNode child) {
        var style = child.computedStyle;

        innerSlot.set(
                slot.x + style.marginLeft,
                slot.y + style.marginTop,
                slot.width,
                slot.height
        );

        slot.width += style.marginLeft + style.marginRight;
        slot.height += style.marginTop + style.marginBottom;

        float nx = alignX(innerSlot, child.preferredSize.width, child.anchorX);
        float ny = alignY(innerSlot, child.preferredSize.height, child.anchorY);

        child.localBounds.set(nx, ny, child.preferredSize.width, child.preferredSize.height);
    }
}
