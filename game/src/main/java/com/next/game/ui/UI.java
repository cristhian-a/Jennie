package com.next.game.ui;

import com.next.engine.ui.AbsoluteLayout;
import com.next.engine.ui.Panel;
import com.next.engine.ui.Rect;

public final class UI {
    public static final Panel ROOT = new Panel(
            new Rect(0, 0, 1024, 768),
            new AbsoluteLayout(),
            0f
    );
}
