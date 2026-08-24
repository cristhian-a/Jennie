package com.next.engine.ui.component;

import com.next.engine.ui.AbstractNode;

public interface UIComponent {
    default void onAttach(AbstractNode node) {}
    default void onDetach() {}
}
