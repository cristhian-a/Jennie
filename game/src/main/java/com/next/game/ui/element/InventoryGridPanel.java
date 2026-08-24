package com.next.game.ui.element;

import com.next.engine.ui.*;
import com.next.engine.ui.component.Action;
import com.next.engine.ui.component.ActionComponent;
import com.next.engine.ui.widget.Label;
import com.next.game.Game;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

final class InventoryGridPanel extends FramePanel {

    // Static data
    private static final String HEADER = "- YOUR STUFF -";

    // UI elements
    private final Panel bodyPanel;

    InventoryGridPanel(Game game, Rect bounds, Action slotAction) {
        super(
                new Rect(bounds),
                new VerticalStackLayout(0f),
                8f,
                16,
                4,
                Colors.WHITE,
                Colors.BLACK
        );

        Panel headerPanel = new Panel(
                new Rect(0, 0, getContentWidth(), 30),
                new AbsoluteLayout(),
                0f
        );
        var label = new Label(HEADER, Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.CENTER);
        headerPanel.add(label);

        bodyPanel = new Panel(
                new Rect(0, 0, getContentWidth(), getContentHeight() - 60),
                new GridLayout(4, 42f, 32f),
                0f
        );

        var inventory = game.getPlayer().getInventory();
        inventory.forEach((item) -> {
            var slot = new ItemSlot(item);
            slot.addComponent(new ActionComponent(slotAction));
            bodyPanel.add(slot);
        });

        add(headerPanel);
        add(bodyPanel);
    }

    void popSlot(ItemSlot slot) {
        if (bodyPanel.contains(slot))
            bodyPanel.remove(slot);
    }
}
