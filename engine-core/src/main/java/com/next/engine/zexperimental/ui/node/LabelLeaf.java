package com.next.engine.zexperimental.ui.node;

import com.next.engine.graphics.RenderQueue;
import lombok.Setter;

public final class LabelLeaf extends UINodeLeaf {

    @Setter private String text;
    @Setter private int color;

    @Override
    public void onMeasure() {
    }

    @Override
    public void draw(RenderQueue queue) {
    }
}
