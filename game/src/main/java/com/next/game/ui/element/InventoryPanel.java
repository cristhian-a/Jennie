package com.next.game.ui.element;

import com.next.engine.ui.*;
import com.next.engine.ui.component.Action;
import com.next.game.Game;

public final class InventoryPanel {

    // Inventory bounds
    private static final int x = 570, y = 100;
    private static final int w = 400, h = 400;

    // Info panel bounds
    private static final int infoX = 570, infoY = 508;
    private static final int infoW = 400, infoH = 150;

    // Inventory mode
    private enum Mode { GRID, ACTIONS }
    private Mode mode = Mode.GRID;

    // Element's panels
    private final UIRoot uiroot;
    private final InventoryGridPanel grid;
    private final ItemInfoPanel infoPanel;

    public InventoryPanel(Game game, UIRoot root) {
        grid = new InventoryGridPanel(game, new Rect(x, y, w, h), inventoryAction());
        infoPanel = new ItemInfoPanel(game, infoX, infoY, infoW, infoH);
        infoPanel.onBack(this::hideActions);
        infoPanel.onDrop(this::removeFocusedSlot);

        uiroot = root;
        uiroot.add(grid);
        uiroot.add(infoPanel);
    }

    private Focusable focusedSlot;

    public void update(Focusable focused) {
        if (mode == Mode.GRID) {
            focusedSlot = focused;
        }
        infoPanel.update(focusedSlot);
    }

    private Action inventoryAction() {
        return (AbstractNode node, String _) -> {
            if (!(node instanceof ItemSlot slot)) return;

            mode = Mode.ACTIONS;
            infoPanel.setActionsFor(slot.getItem());

            uiroot.getFocusManager().pushScope(infoPanel);
            uiroot.getFocusManager().requestFocus(infoPanel.getFirstAction());
            uiroot.markDirty();
        };
    }

    private void hideActions() {
        mode = Mode.GRID;
        uiroot.markDirty();
        uiroot.getFocusManager().requestFocus(focusedSlot);
        uiroot.getFocusManager().popScope();
    }

    private void removeFocusedSlot() {
        if (focusedSlot instanceof ItemSlot i) {
            grid.popSlot(i);

            var fm = uiroot.getFocusManager();
            fm.rebuild();
            fm.focusNext();
            focusedSlot = fm.getFocused();
        }
    }

}
