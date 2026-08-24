package com.next.engine.ui.style;

import java.util.List;

record StyleRule(Selector selector, List<StyleEntry> entries, int specificity, int order) {

    StyleRule {
        entries = List.copyOf(entries);
    }

    StyleRule(Selector selector, List<StyleEntry> entries, int order) {
        this(selector, entries, selector.specificity(), order);
    }

    void applyTo(ComputedStyle cs) {
        for (int i = 0; i < entries.size(); i++) {
            var e = entries.get(i);
            e.property().apply(cs, e.value());
        }
    }
}
