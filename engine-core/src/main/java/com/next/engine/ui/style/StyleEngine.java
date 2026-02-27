package com.next.engine.ui.style;

import com.next.engine.ui.AbstractNode;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public final class StyleEngine {

    private final StyleSheet sheet;

    public void computeStyle(AbstractNode node, ComputedStyle out) {
        List<StyleRule> applicable = new ArrayList<>();
        var sheetRules = sheet.getRules();

        for (int i = 0; i < sheetRules.size(); i++) {
            var rule = sheetRules.get(i);
            if (ruleMatches(rule.selector(), node))
                applicable.add(rule);
        }

        applicable.sort(Comparator.comparingInt(StyleRule::specificity).thenComparing(StyleRule::order));

        for (int i = 0; i < applicable.size(); i++) {
            applicable.get(i).applyTo(out);
        }

//        if (node.inlineStyle != null) sheet.compile(node.inlineStyle, cs); // there is no inlining yet
    }

    private boolean ruleMatches(Selector s, AbstractNode node) {
        if (s.type() != null && !s.type().equals(node.getTypeName())) return false;
        if (s.id() != null && !s.id().equals(node.style().getId())) return false;
        if (s.state() != null && !node.style().hasState(s.state())) return false;
        for (int i = 0; i < s.classes().size(); i++) if (!node.style().hasStyleClass(s.classes())) return false;
        return true;
    }

    private int parseColor(Object v) {
        if (v instanceof Integer i) return i;
        if (v instanceof String s) {
            s = s.trim();
            if (s.startsWith("#")) {
                long val = Long.parseLong(s.substring(1), 16);
                if (s.length() == 7)    // no alpha
                    return (int) (0xFF000000L | val);
                else if(s.length() == 9)    // argb
                    return (int) val;
            }
        }

        return 0;
    }

    private int toInt(Object v) {
        return switch (v) {
            case null -> 0;
            case Number n -> n.intValue();
            default -> Integer.parseInt(v.toString());
        };
    }
}
