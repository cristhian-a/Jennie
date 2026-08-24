package com.next.engine.zexperimental.ui.uij;

public final class Transform {
    public float x, y;
    public float rotation;
    public float scaleX = 1, scaleY = 1;

    public Transform(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void applyToCanvas(Canvas canvas) {
        canvas.translate(x, y);
        canvas.rotate(rotation);
        canvas.scale(scaleX, scaleY);
    }
}
