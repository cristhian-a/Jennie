package com.next.engine.graphics.awt;

import com.next.engine.Director;
import com.next.engine.data.Mailbox;
import com.next.engine.data.Registry;
import com.next.engine.debug.DebugTimer;
import com.next.engine.debug.DebugTimers;
import com.next.engine.debug.Debugger;
import com.next.engine.event.WorldTransitionEvent;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Camera;
import com.next.engine.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;

public final class Renderer {

    // debug stuff
    private static final DebugTimer renderTimer = DebugTimers.of(DebugTimers.RENDERER);

    // static pre-computed stuff
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 100);

    private final UIRenderer uiRenderer;
    private final Director director;
    private final Mailbox mailbox;
    private final VideoSettings settings;
    private final TileRenderer tileRenderer;
    private final LightningRenderer lightningRenderer;

    public Renderer(Director director, Mailbox mailbox, VideoSettings settings) {
        this.director = director;
        this.mailbox = mailbox;
        this.settings = settings;

        this.uiRenderer = new UIRenderer(settings);
        this.tileRenderer = new TileRenderer();
        this.lightningRenderer = new LightningRenderer(settings);
    }

    // Event Handler    // TODO probably we shouldn't solve it like this
    public void onWorldTransition(WorldTransitionEvent event) {
        tileRenderer.setWorld(event.world());
    }

    public void render(Graphics2D g) {
        renderTimer.begin();

        RenderQueue queue = mailbox.receiveRender();
        Debugger.INSTANCE.enqueueRequests(queue);

        Camera camera = director.getCamera();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        AffineTransform identity = g.getTransform();

        renderLayer(g, Layer.BACKGROUND, queue, camera, identity);
        applySpace(g, Layer.WORLD, camera, identity);
        tileRenderer.render(g, camera);
        renderLayer(g, Layer.WORLD, queue, camera, identity);
        renderLayer(g, Layer.ACTORS, queue, camera, identity);

        try (var _ = DebugTimers.scope(DebugTimers.RENDER_PARTICLES)) {
            renderLayer(g, Layer.PARTICLES, queue, camera, identity);
        }

        // This should be rendered using world space, with camera coordinates (if translate is applied)
        lightningRenderer.render(g, camera, queue.getBucket(Layer.LIGHTS));

        renderLayer(g, Layer.UI_WORLD, queue, camera, identity);
        renderLayer(g, Layer.UI_SCREEN, queue, camera, identity);
        renderLayer(g, Layer.UI_SCR_SCALED, queue, camera, identity);
        applySpace(g, Layer.UI_SCREEN, camera, identity);
        uiRenderer.renderMessages(g);

        try (var _ = DebugTimers.scope(DebugTimers.RENDER_DEBUG_UI)) {
            renderLayer(g, Layer.DEBUG_WORLD, queue, camera, identity);
            renderLayer(g, Layer.DEBUG_SCREEN, queue, camera, identity);
        }

        renderTimer.end();
    }

    private void renderLayer(Graphics2D g, Layer layer, RenderQueue queue, Camera camera, AffineTransform identity) {
        applySpace(g, layer, camera, identity);

        var bucket = queue.getBucket(layer);

        uiRenderer.renderFilledRectangleTable(g, bucket.filledRectangles);
        uiRenderer.renderFilledRoundRectangleTable(g, bucket.filledRoundRects);
        uiRenderer.renderRoundedStrokeRectTable(g, bucket.roundedStrokeRectTable);
        uiRenderer.renderRectangleTable(g, bucket.rectangles);
        renderSpriteTable(g, camera, bucket.sprites);
        renderOverlayTable(g, bucket.overlays);
        uiRenderer.renderTextTable(g, bucket.texts);
    }

    private void applySpace(Graphics2D g, Layer layer, Camera camera, AffineTransform identity) {
        g.setTransform(identity);

        if (layer.scaled) {
            g.scale(settings.SCALE, settings.SCALE);
        }

        switch (layer.space) {
            case WORLD -> {
                g.translate(-camera.getX(), -camera.getY());
            }
            case SCREEN -> {
            }
        }
    }

    private void renderSpriteTable(Graphics2D g, Camera camera, RenderQueue.SpriteTable table) {
        Registry.masterSheet.draw(g, table);
    }

    private void renderOverlayTable(Graphics2D g, RenderQueue.OverlayTable table) {
        if (table.count == 0) return;
        g.setColor(OVERLAY_COLOR);
        for (int i = 0; i < table.count; i++) {
            g.fillRect((int) table.x[i], (int) table.y[i], settings.WIDTH, settings.HEIGHT);
        }
    }

    private void fillPolygon(Graphics2D g, int[] xPoints, int[] yPoints, Color color) {
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    void onResize() {
        director.getCamera().resize(settings.UNSCALED_WIDTH, settings.UNSCALED_HEIGHT);
        lightningRenderer.onResize();
    }
}
