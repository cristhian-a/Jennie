package com.next.game.ui.element;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.AbstractContainer;
import com.next.engine.ui.Focusable;
import com.next.engine.ui.Rect;
import com.next.engine.ui.component.ActionComponent;
import com.next.engine.ui.widget.ImageNode;
import com.next.game.model.Item;
import lombok.Getter;
import lombok.Setter;

final class ItemSlot extends AbstractContainer implements Focusable {

    private final Rect contentBounds;
    @Getter private final Item item;

    private boolean focused;
    @Setter private boolean focusable = true;

    ItemSlot(Item item) {
        this(item, 4);
    }

    ItemSlot(Item item, int scale) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        this.item = item;

        var textureData = Registry.sprites[item.getIcon()];
        ImageNode img = new ImageNode(textureData, false);  // img focusable = false
        img.setScale(scale);

        float w = textureData.srcWidth() * scale;
        float h = textureData.srcHeight() * scale;

        this.contentBounds = new Rect(0, 0, w, h);

        super();
        this.localBounds = new Rect(this.contentBounds);

        add(img);
    }

    @Override
    public Rect contentBounds() {
        contentBounds.set(0, 0, localBounds.width, localBounds.height);
        return contentBounds;
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

    @Override
    public void draw(RenderQueue queue) {
        super.draw(queue);

        if (focused) {
            queue.fillRoundRect(
                    Layer.UI_SCREEN,
                    globalBounds.x - 1, globalBounds.y - 1,
                    globalBounds.width + 3, globalBounds.height + 3,
                    0xFFFFFFFF, 16
            );
        }
    }
}
