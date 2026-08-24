package com.next.game.ui;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.widget.Label;
import com.next.game.model.Player;
import com.next.game.ui.element.*;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

public final class ViewInventory {

    // Box rectangles
    private static final int x = 570, y = 100;
    private static final int w = 400, h = 400;

    // Static data
    private static final String HEADER = "- YOUR STUFF -";

    // Root panel
    private final FramePanel frame;
    private final UIRoot root;

    public ViewInventory(Player player) {
        frame = FrameFactory.dialog(0, 0, w, h);

        Panel root = new Panel(new Rect(frame.contentBounds()), new VerticalStackLayout(0f), 0f);
        frame.add(root);

        Panel headerPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), 30),
                new AbsoluteLayout(),
                0f
        );
        var label = new Label(HEADER, Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.CENTER);
        headerPanel.add(label);

        Panel bodyPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), root.getContentHeight() - 60),
                new VerticalStackLayout(4f),
                0f
        );
        var bodyTxt = new Label("Hello World!", Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.CENTER);
        var bodyTxt2 = new Label("How are you there?", Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.CENTER);
        bodyPanel.add(bodyTxt);
        bodyPanel.add(bodyTxt2);

        Panel footerPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), 30),
                new HorizontalStackLayout(0f),
                0f
        );
        var footerTxt = new Label(0, 0, "Hello World!", Fonts.DEFAULT, Colors.WHITE);
        var footerTxt2 = new Label(0, 0, "Hi there!", Fonts.DEFAULT, Colors.WHITE);
        var footerTxt3 = new Label(0, 0, "Wuh?!", Fonts.DEFAULT, Colors.WHITE);
        footerPanel.add(footerTxt);
        footerPanel.add(footerTxt2);
        footerPanel.add(footerTxt3);

        root.add(headerPanel);
        root.add(bodyPanel);
        root.add(footerPanel);

        this.root = new UIRoot(new Rect(x, y, w, h));
        this.root.add(frame);
    }

    public void render(RenderQueue queue) {
//        frame.measure();
//        frame.updateLayout();
//        frame.draw(queue);
        root.render(queue);
    }
}
