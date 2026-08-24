package com.next.game.ui;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.widget.Label;
import com.next.engine.ui.widget.TextBlock;
import com.next.game.model.Item;
import com.next.game.ui.element.*;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

public class ViewItemInfo {

    // Box rectangles and stroke info
    private static final int arc = 15;
    private static final int thickness = 6;
    private static final int x = 570, y = 508;
    private static final int w = 400, h = 150;
    private static final int bx = x + (thickness >> 1), by = y + (thickness >> 1);
    private static final int bw = w - thickness + 1, bh = h - thickness + 1;
    private static final int bArc = Math.max(0, arc - thickness);

    // Static text coordinates
    private static final int nameX = bx + 15, nameY = by + 40;
    private static final int tX = nameX, tY = nameY + 30;
    private static final int infX = tX + 15, infY = tY + 25;

    private static final String EMPTY_TXT = "";
    private static final String LINE_SEP = "\n";
    private static final String LEFT_SQR_BRACKET = "[";
    private static final String RIGHT_SQR_BRACKET = "]";

    // The default string appender for this class;
    // Whenever string append is needed in the loop methods (update and render), use it, and never
    // append using the "+" sign to avoid new string builder allocations.
    private final StringBuilder stringBuilder = new StringBuilder();

    private String infoText;
    private String[] displayText;
    private String displayName = EMPTY_TXT;

    private final Label nameLabel = new Label(EMPTY_TXT, Fonts.DEFAULT, Colors.WHITE, Align.START, Align.START);
    private final TextBlock textBlock = new TextBlock(EMPTY_TXT, Fonts.DEFAULT, Colors.WHITE);

    private boolean itemSelected;
    private String[] options;
    private int optionIndex;

    // UI containers
    private final FramePanel frame;

    public ViewItemInfo() {
        frame = FrameFactory.dialog(x, y, w, h);

        Rect rootRect = frame.contentBounds();

        Panel root = new Panel(
                new Rect(rootRect),
                new VerticalStackLayout(0),
                0f
        );
        frame.add(root);

        Panel header = new Panel(
                new Rect(0, 0, rootRect.width, 30),
                new AbsoluteLayout(),
                0f
        );
        header.add(nameLabel);
        root.add(header);

        Panel body = new Panel(
                new Rect(0, 0, rootRect.width, rootRect.height - 30),
                new VerticalStackLayout(0f),
                0f
        );
        body.add(textBlock);
        root.add(body);

//        Panel footer = new Panel(
//                new Rect(0, 0, rootRect.width, 25),
//                new HorizontalStackLayout(12),
//                0f
//        );
//        footer.add(new Label(">", Fonts.DEFAULT, Colors.WHITE, Align.START, Align.START));
//        footer.add(new Label("OPT1", Fonts.DEFAULT, Colors.WHITE, Align.START, Align.START));
//        footer.add(new Label("OPT2", Fonts.DEFAULT, Colors.WHITE, Align.START, Align.START));
//        root.add(footer);
    }

    public void update(Item item, boolean itemSelected, String[] options, int optionIndex) {
        this.itemSelected = itemSelected;
        this.optionIndex = optionIndex;
        this.options = options;

        String inf = EMPTY_TXT;

        if (itemSelected) {
            inf = options[optionIndex];
        } else {
            if (item != null) inf = item.getInfo();
        }

        if (inf.equals(infoText)) return;
        infoText = inf;
        buildDisplayText();

        if (item != null) {
            stringBuilder.setLength(0);
            stringBuilder.append(LEFT_SQR_BRACKET).append(item.getName()).append(RIGHT_SQR_BRACKET);
            nameLabel.setText(stringBuilder.toString());
        }
        else {
            nameLabel.setText(EMPTY_TXT);
        }
    }

    private void buildDisplayText() {
        displayText = infoText.split(LINE_SEP);
        textBlock.setText(infoText);
    }

    public void render(RenderQueue queue) {
        frame.measure();
        frame.updateLayout();
        frame.draw(queue);

//        if (itemSelected) {
//            queue.submit(l, ">", f, c, tX + (optionIndex * 100), infY, rp, 0); // cursor
//
//            for (int i = 0; i < options.length; i++) {
//                queue.submit(l, options[i], f, c, infX + (i * 100), infY, rp, 0);
//            }
//        } else {
//            ...
//        }
    }
}
