package com.next.engine.ui;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Size {
    public float width, height;

    public Size() {
        this(0, 0);
    }

    public void set(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
