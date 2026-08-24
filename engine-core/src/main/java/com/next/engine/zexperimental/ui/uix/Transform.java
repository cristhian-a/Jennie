package com.next.engine.zexperimental.ui.uix;

public final class Transform {
    public float x, y;
    public float rotation;
    public float scaleX = 1, scaleY = 1;

    public Transform(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Transform() {
        this(0, 0);
    }
}
