package com.next.engine.zexperimental.ui.uij;

public final class Rect {
    public float x, y, width, height;

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

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

    public boolean contains(float px, float py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
}
