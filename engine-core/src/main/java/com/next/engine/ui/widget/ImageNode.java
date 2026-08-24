package com.next.engine.ui.widget;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.Sprite;
import com.next.engine.ui.*;
import com.next.engine.ui.component.ActionComponent;

public final class ImageNode extends AbstractNode implements Focusable {

    private final Sprite texture;
    private final boolean focusable;

    private final Rect drawRect = new Rect();
    private boolean focused;

    private int imageScale = 1;

    public ImageNode(String textureName, boolean focusable) {
        int idx = Registry.textureIds.get(textureName);
        var texture = Registry.sprites[idx];
        if (texture == null) throw new IllegalArgumentException("Texture not found: " + textureName);

        this.focusable = focusable;
        this.texture = texture;
        super();
    }

    public ImageNode(Sprite texture, boolean focusable) {
        if (texture == null) throw new IllegalArgumentException("Texture cannot be null");
        this.focusable = focusable;
        this.texture = texture;
        super();
    }

    public ImageNode(int textureId, boolean focusable) {
        if (textureId < 0 || textureId > Registry.sprites.length) throw new IllegalArgumentException("invalid texture id");

        var texture = Registry.sprites[textureId];
        if (texture == null) throw new IllegalArgumentException("Texture cannot be null");
        this.focusable = focusable;
        this.texture = texture;
        super();
    }

    public void setScale(int scale) {
        if (scale < 1) return;
        this.imageScale = scale;
        markDirty();
    }

    @Override
    public void measure() {
        // preferred size = natural image size * scale (duh)
        preferredSize.set(texture.srcWidth() * imageScale, texture.srcHeight() * imageScale);
        localBounds.width = preferredSize.width;
        localBounds.height = preferredSize.height;
    }

    @Override
    public void onLayout() {
        float cellW = globalBounds.width;
        float cellH = globalBounds.height;

        if (texture.srcWidth() == 0 || texture.srcHeight() == 0) {
            drawRect.set(0, 0, cellW, cellH);
            return;
        }

        float tw = preferredSize.width; // texture.srcWidth();
        float th = preferredSize.height; // texture.srcHeight();

        // scaling to fit while preserving the aspect ratio
        float scale = Math.min(cellW / tw, cellH / th);
        float dw = tw * scale;
        float dh = th * scale;

        // computing anchor-based position inside the cell
        Rect cellRect = new Rect(globalBounds.x, globalBounds.y, cellW, cellH);
        float dx = LayoutUtils.alignX(cellRect, dw, anchorX);
        float dy = LayoutUtils.alignY(cellRect, dh, anchorY);
        dx += Math.round(texture.pivotX()) + 1;
        dy += Math.round(texture.pivotY()) + 1;

        drawRect.set(dx, dy, dw, dh);
    }

    @Override
    public void draw(RenderQueue queue) {
        queue.draw(
                Layer.UI_SCREEN, drawRect.x, drawRect.y,
                drawRect.width, drawRect.height, texture.id()
        );

        if (focused) {
            queue.fillRoundRect(
                    Layer.UI_SCREEN,
                    globalBounds.x - 1, globalBounds.y - 1,
                    drawRect.width + 3, drawRect.height + 3,
                    0xFFFFFFFF, 16
            );
        }
    }

    @Override
    public boolean isFocusable() {
        return focusable;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void onFocus() {
        focused = true;
        markDirty();
    }

    @Override
    public void onBlur() {
        focused = false;
        markDirty();
    }

    @Override
    public void onActivate(String input) {
        var action = getComponent(ActionComponent.class);
        if (action != null) action.fire(input);
    }
}
