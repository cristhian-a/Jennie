package com.next.engine.zexperimental.ui.node;

public class ClickableComponent implements UIComponent {
    private final Runnable action;
    private UINode owner;
    private boolean isPressed;

    public ClickableComponent(Runnable action) {
        this.action = action;
    }

    @Override
    public void onAttach(UINode node) {
        this.owner = node;
    }

    @Override
    public void onPointerDown(float x, float y) {
        IO.println("Node: " + owner + " clicked");
        action.run();
    }
}
