package com.next.engine.ui.style;

import java.util.*;

public final class StyleSheet {
    private final List<StyleRule> rules = new ArrayList<>();

    public void addRule(String selector, Map<String, Object> props) {
        Selector sel = Selector.parse(selector);

        List<StyleEntry> entries = new ArrayList<>();
        for (String k : props.keySet()) {
            Object v = props.get(k);
            var prop = StyleProperty.of(k);
            entries.add(new StyleEntry(prop, StyleValue.of(prop, v)));
        }

        rules.add(new StyleRule(sel, entries, 0));
    }

    List<StyleRule> getRules() {
        return Collections.unmodifiableList(rules);
    }
}
