package com.next.engine.ui.style;

import static com.next.engine.ui.style.StyleValue.*;

public enum StyleProperty {
    BACKGROUND_COLOR {
        final void apply(ComputedStyle s, StyleValue v) {
            s.backgroundColor = ((Color) v).argb();
        }
    },
    BORDER_COLOR {
        final void apply(ComputedStyle s, StyleValue v) {
            s.borderColor = ((Color) v).argb();
        }
    },
    BORDER_WIDTH {
        @Override
        void apply(ComputedStyle s, StyleValue v) {
            s.borderWidth = (int) ((Length) v).value();
        }
    },
    TEXT_COLOR {
        final void apply(ComputedStyle s, StyleValue v) {
            s.textColor = ((Color) v).argb();
        }
    },
    FONT_SIZE {
        final void apply(ComputedStyle s, StyleValue v) {
            s.fontSize = (int) ((Length) v).value();
        }
    },
    CORNER_RADIUS {
        final void apply(ComputedStyle s, StyleValue v) {
            s.cornerRadius = (int) ((Length) v).value();
        }
    },
    CURSOR_SYMBOL {
        final void apply(ComputedStyle s, StyleValue v) {
            s.cursorSymbol = ((Keyword) v).value();
        }
    },

    PADDING {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.paddingLeft = ((Length) v).value();
            s.paddingRight = ((Length) v).value();
            s.paddingTop = ((Length) v).value();
            s.paddingBottom = ((Length) v).value();
        }
    },
    PADDING_TOP {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.paddingTop = ((Length) v).value();
        }
    },
    PADDING_BOTTOM {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.paddingBottom = ((Length) v).value();
        }
    },
    PADDING_LEFT {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.paddingLeft = ((Length) v).value();
        }
    },
    PADDING_RIGHT {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.paddingRight = ((Length) v).value();
        }
    },

    MARGIN {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.marginLeft = ((Length) v).value();
            s.marginRight = ((Length) v).value();
            s.marginTop = ((Length) v).value();
            s.marginBottom = ((Length) v).value();
        }
    },
    MARGIN_TOP {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.marginTop = ((Length) v).value();
        }
    },
    MARGIN_BOTTOM {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.marginBottom = ((Length) v).value();
        }
    },
    MARGIN_LEFT {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.marginLeft = ((Length) v).value();
        }
    },
    MARGIN_RIGHT {
        @Override
        final void apply(ComputedStyle s, StyleValue v) {
            s.marginRight = ((Length) v).value();
        }
    }
    ;

    abstract void apply(ComputedStyle s, StyleValue v);

    public static StyleProperty of(String name) {
        return switch (name) {
            case "backgroundColor" -> BACKGROUND_COLOR;
            case "borderColor" -> BORDER_COLOR;
            case "borderWidth" -> BORDER_WIDTH;
            case "textColor" -> TEXT_COLOR;
            case "fontSize" -> FONT_SIZE;
            case "cornerRadius" -> CORNER_RADIUS;
            case "cursorSymbol" -> CURSOR_SYMBOL;
            case "padding" -> PADDING;
            case "paddingTop" -> PADDING_TOP;
            case "paddingBottom" -> PADDING_BOTTOM;
            case "paddingLeft" -> PADDING_LEFT;
            case "paddingRight" -> PADDING_RIGHT;
            case "margin" -> MARGIN;
            case "marginTop" -> MARGIN_TOP;
            case "marginBottom" -> MARGIN_BOTTOM;
            case "marginLeft" -> MARGIN_LEFT;
            case "marginRight" -> MARGIN_RIGHT;
            default -> throw new IllegalArgumentException("Unknown style property: " + name);
        };
    }
}
