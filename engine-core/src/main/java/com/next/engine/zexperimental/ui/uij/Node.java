package com.next.engine.zexperimental.ui.uij;

public interface Node {
    Style getStyle();
    Rect getBounds();
    Transform getTransform();

    void draw(Canvas canvas);
    void update(double delta);
    boolean hitTest(float globalX, float globalY);

    void setParent(Node parent);
    Node getParent();
}
