package com.next.engine.ui;

public interface Focusable {
    boolean isFocusable();
    boolean isFocused();

    void onFocus();
    void onBlur();
    default void onActivate(String action) {}
}
