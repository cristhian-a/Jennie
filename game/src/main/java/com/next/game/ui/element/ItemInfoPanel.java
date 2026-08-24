package com.next.game.ui.element;

import com.next.engine.ui.*;
import com.next.engine.ui.component.Action;
import com.next.engine.ui.style.StyleEngine;
import com.next.engine.ui.style.StyleSheet;
import com.next.engine.ui.widget.Button;
import com.next.engine.ui.widget.Label;
import com.next.engine.ui.widget.TextBlock;
import com.next.game.Game;
import com.next.game.model.Consumable;
import com.next.game.model.Equip;
import com.next.game.model.Item;
import com.next.game.model.Player;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class ItemInfoPanel extends FramePanel {

    // String building utilities
    private static final String EMPTY = "";
    private static final String LEFT_SQR_BRACKET = "[";
    private static final String RIGHT_SQR_BRACKET = "]";

    private final StringBuilder stringBuilder = new StringBuilder();

    private final Button BTN_EQUIP = new Button("Equip", Fonts.DEFAULT, equip());
    private final Button BTN_USE = new Button("Use", Fonts.DEFAULT, consume());
    private final Button BTN_DROP = new Button("Drop", Fonts.DEFAULT, drop());
    private final Button BTN_BACK = new Button("Back", Fonts.DEFAULT, back());

    private final Player player;

    // Elements
    private final Panel root;
    private final Panel header;
    private final Panel body;
    private final Panel actionsPanel;
    private final Label headerLabel;
    private final TextBlock textBlock;
    private final List<Button> actions = new ArrayList<>();

    ItemInfoPanel(Game game, float x, float y, float width, float height) {
        this.player = game.getPlayer();

        super(
                new Rect(x, y, width, height),
                new com.next.engine.ui.AbsoluteLayout(),
                8f,
                16,
                4,
                Colors.WHITE,
                Colors.BLACK
        );
        Rect frameBounds = contentBounds();

        header = new Panel(
                new Rect(0, 0, frameBounds.width, 30),
                new AbsoluteLayout(),
                0f
        );
        body = new Panel(
                new Rect(0, 0, frameBounds.width, frameBounds.height - 30),
                new AbsoluteLayout(),
                0f
        );
        actionsPanel = new Panel(
                new Rect(0, 0, frameBounds.width, frameBounds.height - (frameBounds.height - 30)),
                new HorizontalStackLayout(8f),
                0f
        );
        actionsPanel.setAnchor(Align.CENTER, Align.CENTER);
        actionsPanel.setVisible(false);

        root = new Panel(new Rect(frameBounds), new VerticalStackLayout(0), 0f);
        root.add(header);
        root.add(body);
        add(root);

        body.add(actionsPanel);

        headerLabel = new Label(EMPTY, Fonts.DEFAULT, Colors.WHITE);
        header.add(headerLabel);

        textBlock = new TextBlock(EMPTY, Fonts.DEFAULT, Colors.WHITE);
        body.add(textBlock);
    }

    private Focusable prev;

    void update(Focusable focused) {
        if (focused == prev) return;
        prev = focused;

        if (focused instanceof ItemSlot widget) {
            Item item = widget.getItem();
            stringBuilder.setLength(0);
            var s = stringBuilder.append(LEFT_SQR_BRACKET).append(item.getName()).append(RIGHT_SQR_BRACKET).toString();
            headerLabel.setText(s);

            var txt = item.getInfo();
            textBlock.setText(txt);
        } else {
            prev = null;
            headerLabel.setText(EMPTY);
            textBlock.setText(EMPTY);
        }
    }

    private Item selectedItem;

    void setActionsFor(Item item) {
        actions.clear();
        selectedItem = item;

        if (item instanceof Equip) {
            actions.add(BTN_EQUIP);
            actions.add(BTN_DROP);
            actions.add(BTN_BACK);
        } else if (item instanceof Consumable) {
            actions.add(BTN_USE);
            actions.add(BTN_DROP);
            actions.add(BTN_BACK);
        } else {
            actions.add(BTN_DROP);
            actions.add(BTN_BACK);
        }

        // swap panels
        actionsPanel.removeAll();
        actionsPanel.add(actions);

        textBlock.setVisible(false);
        actionsPanel.setVisible(true);

        markDirty();
    }


    Focusable getFirstAction() {
        if (actions.isEmpty()) return null;
        return actions.getFirst();
    }

    private Runnable onDrop;
    private Runnable onBack;

    void onDrop(Runnable action) {
        onDrop = action;
    }

    void onBack(Runnable action) {
        onBack = action;
    }

    private Action equip() {
        return (_, _) -> {
            hideActions();
        };
    }

    private Action consume() {
        return (_, _) -> {
            ((Consumable) selectedItem).use();
            player.getInventory().pop(selectedItem);
            onDrop.run();
            hideActions();
        };
    }

    private Action drop() {
        return (_, _) -> {
            player.getInventory().pop(selectedItem);
            onDrop.run();
            hideActions();
        };
    }

    private Action back() {
        return (_, _) -> hideActions();
    }

    private void hideActions() {
        actions.clear();
        actionsPanel.removeAll();

        actionsPanel.setVisible(false);
        textBlock.setVisible(true);

        markDirty();
        onBack.run();
    }

}
