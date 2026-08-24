package com.next.engine.ui;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Rect {
    public float x, y, width, height;

    public Rect() {
        this(0, 0, 0, 0);
    }

    /**
     * Copy constructor.
     * @param source to copy from
     */
    public Rect(Rect source) {
        this(source.x, source.y, source.width, source.height);
    }

    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void set(Rect source) {
        set(source.x, source.y, source.width, source.height);
    }

    public float centerX() {
        return x + width / 2f;
    }

    public float centerY() {
        return y + height / 2f;
    }

    public void inset(float padding, Rect out) {
        float t = padding * 2;
        float width = Math.max(0, this.width - t);
        float height = Math.max(0, this.height - t);

        out.set(
                x + padding,
                y + padding,
                width,
                height
        );
    }
}
