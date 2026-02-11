package com.next.game.ui.element;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.style.StyleEngine;
import com.next.engine.ui.style.StyleSheet;
import com.next.engine.ui.widget.Button;
import com.next.game.Game;
import com.next.game.ui.InputSolver;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

import java.util.Map;

public final class CharacterStatsView {

    private final StyleSheet styleSheet = new StyleSheet();
    private final InputSolver inputSolver;
    private final UIRoot uiroot;

    private final InventoryPanel inventoryPanel;

    public CharacterStatsView(Game game) {
        final float WIDTH = 1024, HEIGHT = 768;
        uiroot = new UIRoot(new Rect(0, 0, WIDTH, HEIGHT));

        inputSolver = new InputSolver(game.getInput(), uiroot);

        inventoryPanel = new InventoryPanel(game, uiroot);

        // Test stuff
        AbstractContainer testContainer =
                new AbstractContainer(new Rect(100, 100, 400, 550), new VerticalStackLayout(0f));
        uiroot.add(testContainer);

        var cont1 = new AbstractContainer(new Rect(0, 0, 386, 200), new VerticalStackLayout(0f));
        testContainer.add(cont1);
        var b1 = new Button("Test1", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var b2 = new Button("Test2", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var b3 = new Button("Test3", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        cont1.add(b1);
        cont1.add(b2);
        cont1.add(b3);
        var cont2 = new AbstractContainer(new Rect(0, 0, 386, 100));
        testContainer.add(cont2);
        var cont3 = new AbstractContainer(new Rect(0, 0, 386, 100), new HorizontalStackLayout(0f));
        testContainer.add(cont3);
        var btn = new Button("Test1", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var btn2 = new Button("Test2", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var btn3 = new Button("Test3", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        cont3.add(btn);
        cont3.add(btn2);
        cont3.add(btn3);

        styleSheet.addRule(".Button", Map.of(
                "backgroundColor", 0x88FF0000,
                "textColor", 0xFFFFFF00
        ));
        styleSheet.addRule(".Button:focused", Map.of(
                "backgroundColor", 0xFF0000FF
        ));
        styleSheet.addRule(".AbstractContainer", Map.of(
                "padding", 8f,
                "backgroundColor", Colors.BLACK,
                "borderColor", Colors.WHITE,
                "borderWidth", 4f,
                "cornerRadius", 5f
        ));
        styleSheet.addRule(".Button", Map.of(
                "marginLeft", 25f,
                "marginBottom", 10f
        ));
        StyleEngine styleEngine = new StyleEngine(styleSheet);
        uiroot.setStyleEngine(styleEngine);
    }

    public void update() {
        inputSolver.update();
        inventoryPanel.update(inputSolver.getFocused());
    }

    public void render(RenderQueue queue) {
        uiroot.render(queue);
    }
}
