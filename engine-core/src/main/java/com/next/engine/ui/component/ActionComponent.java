package com.next.engine.ui.component;

import com.next.engine.ui.AbstractNode;

public final class ActionComponent implements UIComponent {
    private final Action action;
    private AbstractNode node;

    @Override
    public void onAttach(AbstractNode node) {
        this.node = node;
    }

    @Override
    public void onDetach() {
        this.node = null;
    }

    public ActionComponent(Action action) {
        this.action = action;
    }

    public void fire(String input) {
        action.run(node, input);
    }
}
