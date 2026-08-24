package com.next.engine.graphics.awt;

import com.next.engine.data.Registry;
import com.next.engine.graphics.*;
import com.next.engine.system.Settings.VideoSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

final class UIRenderer {

    private final Stroke defaultStroke = new BasicStroke(1);

    private final VideoSettings settings;
    private final List<UIMessage> messages;

    UIRenderer(VideoSettings settings) {
        this.settings = settings;
        messages = new ArrayList<>();
    }

    void renderMessages(Graphics2D g) {
        for (int i = 0; i < messages.size(); i++) {
            final var message = messages.get(i);
            if (message.remainingFrames < 1) {
                messages.remove(message);
                continue;
            }

            renderText(g, message);
            message.remainingFrames--;
        }
    }

    void renderText(Graphics2D g, UIMessage m) {
        renderText(g, m.text, m.x, m.y, RenderCache.INSTANCE.getColor(m.color), Registry.fonts.get(m.font), m.position);
    }

    void renderText(Graphics2D g, String text, int x, int y, Color color, TextFont font, RenderPosition position) {
        g.setColor(color);
        g.setFont(((AwtFont) font).raw());

        if (position == RenderPosition.CENTERED) {
            FontMetrics fm = g.getFontMetrics();

            int textWidth = fm.stringWidth(text);

            x += (settings.WIDTH - textWidth) / 2;
            y += ((settings.HEIGHT - fm.getHeight()) / 2) + fm.getAscent();
        }

        g.drawString(text, x, y);
    }

    void renderTextTable(Graphics2D g, RenderQueue.TextTable table) {
        for (int i = 0; i < table.count; i++) {
            if (table.frames[i] > 0) {
                messages.add(new UIMessage(table.message[i], table.font[i], table.colors[i], (int) table.x[i], (int) table.y[i], table.positions[i], table.frames[i]));
            } else {
                renderText(g, table.message[i], (int) table.x[i], (int) table.y[i], RenderCache.INSTANCE.getColor(table.colors[i]), Registry.fonts.get(table.font[i]), table.positions[i]);
            }
        }
    }

    void renderRectangleTable(Graphics2D g, RenderQueue.RectangleTable table) {
        g.setStroke(defaultStroke);
        for (int i = 0; i < table.count; i++) {
            g.setColor(RenderCache.INSTANCE.getColor(table.colors[i]));
            g.drawRect((int) table.x[i], (int) table.y[i], (int) table.width[i], (int) table.height[i]);
        }
    }

    void renderRoundedStrokeRectTable(Graphics2D g, RenderQueue.RoundedStrokeRectTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(RenderCache.INSTANCE.getColor(table.colors[i]));
            g.setStroke(RenderCache.INSTANCE.getStroke(table.thickness[i]));
            g.drawRoundRect(
                    (int) table.x[i],
                    (int) table.y[i],
                    (int) table.width[i],
                    (int) table.height[i],
                    table.arc[i],
                    table.arc[i]
            );
        }
    }

    void renderFilledRectangleTable(Graphics2D g, RenderQueue.FilledRectangleTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(RenderCache.INSTANCE.getColor(table.colors[i]));
            g.fillRect((int) table.x[i], (int) table.y[i], (int) table.width[i], (int) table.height[i]);
        }
    }

    void renderFilledRoundRectangleTable(Graphics2D g, RenderQueue.RoundedFilledRectTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(RenderCache.INSTANCE.getColor(table.colors[i]));
            g.fillRoundRect(
                    (int) table.x[i],
                    (int) table.y[i],
                    (int) table.width[i],
                    (int) table.height[i],
                    table.arc[i],
                    table.arc[i]
            );
        }
    }

}
