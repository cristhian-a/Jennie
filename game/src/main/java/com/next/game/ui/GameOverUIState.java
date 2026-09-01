package com.next.game.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.style.StyleEngine;
import com.next.engine.ui.style.StyleSheet;
import com.next.engine.ui.widget.Button;
import com.next.engine.ui.widget.Label;
import com.next.game.Game;
import com.next.game.event.GoToTitleEvent;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

import java.util.Map;

public final class GameOverUIState implements UIState {

    private final Game game;

    private final UIRoot uiRoot;
    private final InputSolver inputSolver;

    public GameOverUIState(Game game) {
        this.game = game;

        final float WIDTH = game.getSettings().video.WIDTH, HEIGHT = game.getSettings().video.HEIGHT;
        uiRoot = new UIRoot(new Rect(0, 0, WIDTH, HEIGHT));
        inputSolver = new InputSolver(game.getInput(), uiRoot);

        AbstractContainer container =
                new AbstractContainer(new Rect(0, 0, WIDTH, HEIGHT), new VerticalStackLayout(0f));
        uiRoot.add(container);

        final float P_HEIGHT = HEIGHT / 2;

        var topContainer = new AbstractContainer(
                new Rect(0, 0, WIDTH, P_HEIGHT),
                new AlignedLayout(Align.CENTER, Align.CENTER)
        );
        var label = new Label("Game Over", Fonts.DEFAULT_80_BOLD, Colors.RED, Align.CENTER, Align.CENTER);
        topContainer.add(label);

        container.add(topContainer);

        var bottomContainer = new AbstractContainer(
                new Rect(0, 0, WIDTH, P_HEIGHT),
                new VerticalStackLayout(0f)
        );
        bottomContainer.setAnchor(Align.CENTER, Align.CENTER);  // doesn't work with vertical stack layout
        var labelOpts = new Label("What to do?", Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.CENTER);
        bottomContainer.add(labelOpts);

        Button restartBtn = new Button("Restart", Fonts.DEFAULT, (_, _) -> {});
        restartBtn.setAnchor(Align.CENTER, Align.CENTER);
        bottomContainer.add(restartBtn);

        Button quitBtn = new Button("Quit", Fonts.DEFAULT, (_, _) -> {
            game.getDispatcher().dispatch(new GoToTitleEvent());
        });
        quitBtn.setAnchor(Align.CENTER, Align.CENTER);
        bottomContainer.add(quitBtn);

        container.add(bottomContainer);

        StyleSheet styleSheet = new StyleSheet();
        StyleEngine styleEngine = new StyleEngine(styleSheet);
        uiRoot.setStyleEngine(styleEngine);

        styleSheet.addRule(".AbstractContainer", Map.of(
                "borderColor", Colors.RED,
                "borderWidth", 4f
        ));

        styleSheet.addRule(".Button:focused", Map.of(
                "backgroundColor", 0xFF0000FF
        ));
    }

    @Override
    public void update(double delta) {
        inputSolver.update();
    }

    @Override
    public void submitRender(RenderQueue queue) {
        queue.overlay(Layer.UI_SCREEN);
        uiRoot.render(queue);
    }
}
