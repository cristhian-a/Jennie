package com.next.game.gameflow;

import com.next.game.Game;
import com.next.game.ui.GameOverUIState;

public final class GameOverMode implements GameMode {

    @Override
    public void onEnter(Game game) {
        var dispatcher = game.getDispatcher();

        game.getUi().setState(new GameOverUIState(game));
    }

    @Override
    public void onExit(Game game) {

    }

    @Override
    public void update(Game game, double delta) {
        game.getScene().submitRender(game.getMailbox().postRender());
    }
}
