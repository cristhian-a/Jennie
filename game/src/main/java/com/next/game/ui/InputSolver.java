package com.next.game.ui;

import com.next.engine.system.Input;
import com.next.engine.ui.FocusManager;
import com.next.engine.ui.Focusable;
import com.next.engine.ui.UIRoot;
import com.next.game.util.Inputs;

public final class InputSolver {

    private static final String ACTION = "click";

    private final Input input;
    private final FocusManager focusManager;

    public InputSolver(Input input, UIRoot root) {
        this.input = input;
        this.focusManager = root.getFocusManager();
    }

    public void update() {
        if (input.isTyped(Inputs.UP)) focusManager.focusDirection(0, -1);
        if (input.isTyped(Inputs.DOWN)) focusManager.focusDirection(0, 1);
        if (input.isTyped(Inputs.LEFT)) focusManager.focusDirection(-1, 0);
        if (input.isTyped(Inputs.RIGHT)) focusManager.focusDirection(1, 0);
        if (input.isTyped(Inputs.TALK)) focusManager.activateFocused(ACTION);
    }

    public Focusable getFocused() {
        return focusManager.getFocused();
    }

}
