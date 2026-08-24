package com.next.engine.ui.style;

import com.next.engine.graphics.TextFont;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public final class ComputedStyle {
    // Visual
    public int backgroundColor = 0x00000000;
    public int borderColor = 0x00000000;
    public int textColor = 0xFFFFFFFF;

    // Spacing
    public float paddingLeft, paddingRight, paddingTop, paddingBottom;
    public float marginLeft, marginRight, marginTop, marginBottom;

    // box
    public int cornerRadius;
    public int borderWidth;

    // font
    public int fontSize;
    public TextFont fontFamily;

    // cursor & misc
    public String cursorSymbol;
    public float alpha = 1f;

    public void clear() {
        backgroundColor = 0x00000000;
        borderColor = 0x00000000;
        textColor = 0xFFFFFFFF;
        paddingLeft = paddingRight = paddingBottom = paddingTop = 0;
        marginLeft = marginRight = marginBottom = marginTop = 0;
        cornerRadius = 0;
        borderWidth = 0;
        fontSize = 0;
        fontFamily = null;
        cursorSymbol = null;
        alpha = 1f;
    }

    @Getter private boolean dirty = true;

    public void invalidate() {
        dirty = true;
    }

    void recompute() {
        // ... calculations
        dirty = false;
    }
}
