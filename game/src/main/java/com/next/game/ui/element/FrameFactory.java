package com.next.game.ui.element;

import com.next.engine.ui.Rect;
import com.next.game.util.Colors;

public final class FrameFactory {

    public static com.next.engine.ui.FramePanel dialog(float x, float y, float width, float height) {
        return new com.next.engine.ui.FramePanel(
                new Rect(x, y, width, height),
                new com.next.engine.ui.AbsoluteLayout(),
                8f,
                16,
                4,
                Colors.WHITE,
                Colors.BLACK
        );
    }
}
