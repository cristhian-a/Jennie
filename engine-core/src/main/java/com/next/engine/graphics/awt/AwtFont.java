package com.next.engine.graphics.awt;

import com.next.engine.graphics.TextFont;

import java.awt.*;
import java.awt.image.BufferedImage;

/// This class provides a wrapper for [java.awt]'s [java.awt.Font] to the engine rendering pipeline
/// using the [TextFont] interface.
public final class AwtFont implements TextFont {

    private final Font font;
    private final FontMetrics metrics;

    public AwtFont(Font font) {
        this.font = font;

        var img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setFont(font);
        this.metrics = g.getFontMetrics();
        g.dispose();
    }

    @Override
    public float measureWidth(String text) {
        return metrics.stringWidth(text);
    }

    @Override
    public float getLineHeight() {
        return metrics.getHeight();
    }

    @Override
    public float getAscent() {
        return metrics.getAscent();
    }

    Font raw() {
        return font;
    }

    void bind(Graphics2D g) {
        g.setFont(font);
    }
}
