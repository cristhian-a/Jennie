package com.next.engine.ui;

public enum UIState {
    HOVERED,
    FOCUSED,
    PRESSED,
    DISABLED,
    ACTIVE,
    SELECTED,
    HIGHLIGHTED;

    public static UIState parse(String name) {
        if (name == null) return null;

        return switch (name) {
            case "hovered" -> HOVERED;
            case "focused" -> FOCUSED;
            case "pressed" -> PRESSED;
            case "disabled" -> DISABLED;
            case "active" -> ACTIVE;
            case "selected" -> SELECTED;
            case "highlighted" -> HIGHLIGHTED;
            default -> throw new IllegalArgumentException("Unknown UIState: " + name);
        };
    }
}
