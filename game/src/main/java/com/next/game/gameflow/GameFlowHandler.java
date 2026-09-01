package com.next.game.gameflow;

import com.next.game.Game;
import com.next.game.event.*;
import com.next.engine.event.EventDispatcher;

public class GameFlowHandler {

    private final Game game;

    public GameFlowHandler(EventDispatcher dispatcher, Game game) {
        this.game = game;

        dispatcher.register(DisplayStatsEvent.class, this::onFire);
        dispatcher.register(FinishGameEvent.class, this::onFire);
        dispatcher.register(StartGameEvent.class, this::onFire);
        dispatcher.register(GoToTitleEvent.class, this::onFire);
        dispatcher.register(GameOverEvent.class, this::onFire);
        dispatcher.register(PauseEvent.class, this::onFire);
    }

    public void onFire(FinishGameEvent event) {
        if (game.isRunning()) {
            game.setMode(new FinishedMode());
        }
    }

    public void onFire(PauseEvent event) {
        if (game.isRunning()) {
            game.setMode(new PausedMode());
        } else if (game.isPaused()) {
            game.setMode(new RunningMode());
        }
    }

    public void onFire(StartGameEvent event) {
        game.start();
        game.setMode(new RunningMode());
    }

    public void onFire(DisplayStatsEvent event) {
        if (game.isRunning()) {
            game.setMode(new StatsViewMode());
        } else if (game.isDisplayingStats()){
            game.setMode(new RunningMode());
        }
    }

    public void onFire(GameOverEvent event) {
        game.setMode(new GameOverMode());
    }

    public void onFire(GoToTitleEvent event) {
        game.setMode(new TitleMode());
    }

}
