package com.next.engine.zexperimental.ui.node;

import com.next.engine.graphics.RenderQueue;

public final class UIEngine {
    private final UINodeContainer root;   // "canvas"

    public UIEngine(UINodeContainer root) {
        this.root = root;
    }

    public void render(RenderQueue queue) {
        renderNode(root, queue);
    }

    private void renderNode(UINode node, RenderQueue queue) {
        if (node instanceof UINodeLeaf leaf) {
            leaf.draw(queue);
        }

        if (node instanceof UINodeContainer container) {
            var children = container.children;
            for (int i = 0; i < children.size(); i++) {
                renderNode(children.get(i), queue);
            }
        }
    }
}
