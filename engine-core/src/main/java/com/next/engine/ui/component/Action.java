package com.next.engine.ui.component;

import com.next.engine.ui.AbstractNode;

/**
 * Represents a functional interface for defining actions in response to user interactions
 * or other system events in a UI context. This interface can be implemented or used
 * as a lambda expression to encapsulate behavior that will be triggered by a specific source.
 */
@FunctionalInterface
public interface Action {
    /**
     * Runs the action.
     * @param source the node that triggered the action.
     * @param input optional input description like "click" or "enter".
     */
    void run(AbstractNode source, String input);
}
