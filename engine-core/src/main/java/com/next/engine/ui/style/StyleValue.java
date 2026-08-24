package com.next.engine.ui.style;

sealed interface StyleValue {
    record Color(int argb) implements StyleValue {}
    record Length(float value, Unit unit) implements StyleValue {}
    record Percentage(float value) implements StyleValue {}
    record Keyword(String value) implements StyleValue {}
    record NOOP() implements StyleValue {}

    enum Unit { PIXEL, PERCENT, AUTO }

    // This might be later moved to a factory class if it grows too much
    static StyleValue of(StyleProperty prop, Object value) {
        return switch (prop) {
            case BACKGROUND_COLOR, BORDER_COLOR, TEXT_COLOR ->
                    new StyleValue.Color((int) value);

            case FONT_SIZE,
                 CORNER_RADIUS, BORDER_WIDTH,
                 PADDING, PADDING_TOP, PADDING_BOTTOM, PADDING_LEFT, PADDING_RIGHT,
                 MARGIN, MARGIN_TOP, MARGIN_BOTTOM, MARGIN_LEFT, MARGIN_RIGHT ->
                    new StyleValue.Length(Float.parseFloat(value.toString()), StyleValue.Unit.PIXEL);

            case CURSOR_SYMBOL ->
                    new StyleValue.Keyword((String) value);

            default -> new StyleValue.NOOP();
        };
    }
}
