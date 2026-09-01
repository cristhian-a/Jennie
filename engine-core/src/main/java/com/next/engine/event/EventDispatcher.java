package com.next.engine.event;

import com.next.engine.data.Mailbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventDispatcher {

    private final Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> handlers = new HashMap<>();

    public <T extends GameEvent> void register(Class<T> type, Consumer<T> handler) {
        handlers.computeIfAbsent(type, k -> new ArrayList<>())
                .add(handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void dispatch(T event) {
        var list = handlers.get(event.getClass());
        if (list == null) return;

        for (var handler : list) {
            ((Consumer<T>) handler).accept(event);
        }
    }

    /**
     * Dispatches all events in the mailbox for execution.
     * <p/>
     * While the mailbox is being dispatched, any modification to it will throw an exception.
     * @param mailbox the mailbox to dispatch
     */
    public void dispatch(Mailbox mailbox) {
        for (var supplier : mailbox.eventSuppliers) {
            GameEvent event = supplier.get();
            if (event == null) continue;
            dispatch(event);
        }
    }

}
