package com.next.engine.ui.style;

import org.junit.jupiter.api.Test;

import java.util.Map;

final class StyleTest {

    @Test
    void test() {
        StyleSheet sheet = new StyleSheet();
        sheet.addRule("Button", Map.of(
                "backgroundColor", 0x00FF0000,
                "fontSize", 30,
                "cornerRadius", 5,
                "cursorSymbol", ">"
        ));

//        sheet.compile();
        IO.println("testing");
    }
}
