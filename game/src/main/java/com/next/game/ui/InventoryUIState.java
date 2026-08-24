package com.next.game.ui;

import com.next.engine.graphics.RenderQueue;
import com.next.game.Game;
import com.next.game.ui.element.CharacterStatsView;

public final class InventoryUIState implements UIState {
    private final CharacterStatsView characterStatsView;

    public InventoryUIState(Game game) {
        characterStatsView = new CharacterStatsView(game);
    }

    @Override
    public void update(double delta) {
        characterStatsView.update();
    }

    @Override
    public void submitRender(RenderQueue queue) {
        characterStatsView.render(queue);
    }
}
