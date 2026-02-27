package com.next.engine.ui.style;

import com.next.engine.ui.UIState;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

record Selector(String type, String id, Set<String> classes, UIState state) {

    Selector {
        classes = classes == null
                ? Collections.emptySet()
                : Set.copyOf(classes);
    }

    static Selector parse(String raw) {
        String type = null, id = null, state = null;
        Set<String> classes = new HashSet<>();

        String s = raw.trim();
        String[] parts = s.split("(?=[.#:])");
        for (String p : parts) {
            if (p.startsWith(".")) classes.add(p.substring(1));
            else if (p.startsWith("#")) id = p.substring(1);
            else if (p.startsWith(":")) state = p.substring(1);
            else if (!p.isEmpty()) type = p;
        }
        return new Selector(type, id, classes, UIState.parse(state));
    }

    int specificity() {
        int spec = 0;
        if (id != null) spec += 100;
        spec += classes.size() * 10;
        if (type != null) spec += 1;
        if (state != null) spec += 5;
        return spec;
    }
}
