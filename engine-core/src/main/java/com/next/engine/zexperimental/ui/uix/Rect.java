package com.next.engine.zexperimental.ui.uix;

public final class Rect {
    public float x, y, width, height;

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Copy constructor.
     * @param source to copy from
     */
    public Rect(Rect source) {
        this(source.x, source.y, source.width, source.height);
    }

    /**
     * Creates a rectangle with zero size in position 0.
     */
    public Rect() {}

    public boolean contains(float px, float py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void set(Rect rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

}
