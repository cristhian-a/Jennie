package com.next.engine.zexperimental.ui.node;

public interface UIComponent {
    // Whenever added to a node
    void onAttach(UINode node);

    default void update(double delta) {}

    // Input handling
    default void onPointerDown(float x, float y) {}
    default void onPointerEnter() {}
    default void onPointerExit() {}
}
