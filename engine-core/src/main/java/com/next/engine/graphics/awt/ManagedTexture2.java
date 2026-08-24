package com.next.engine.graphics.awt;

import com.next.engine.data.Registry;
import com.next.engine.graphics.RenderQueue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

/**
 * Yes, this is the second attempt at making a texture that manages itself. It does weird stuff like drawing itself,
 * but the performance boost (from 800 fps with lights to +- 8000 fps (yes, 8 thousand)) is huge.
 * <br/>
 * @apiNote This class is not thread safe, and its drawing methods should only be called from the main thread by
 * {@link Renderer}.
 */
public final class ManagedTexture2 {
    private final BufferedImage ramSource;
    private VolatileImage vRamHandle;

    public ManagedTexture2(BufferedImage ramSource) {
        if (ramSource == null) {
            throw new IllegalArgumentException("Source image cannot be null");
        }
        this.ramSource = ramSource;
    }

    /// Called in tile drawing.
    void draw(Graphics2D g, int dx, int dy, int dx2, int dy2, int sx, int sy, int sx2, int sy2) {
        GraphicsConfiguration config = g.getDeviceConfiguration();

        do {
            validateVRAM(config);

            g.drawImage(vRamHandle, dx, dy, dx2, dy2, sx, sy, sx2, sy2, null);
        } while (vRamHandle.contentsLost());
    }

    void draw(Graphics2D g,
                     float[] dx, float[] dy, float[] dx2, float[] dy2,
                     float[] sx, float[] sy, float[] sx2, float[] sy2,
                     int count) {
        GraphicsConfiguration config = g.getDeviceConfiguration();

        do {
            validateVRAM(config);

            for (int i = 0; i < count; i++) {
                g.drawImage(
                        vRamHandle,
                        (int) dx[i], (int) dy[i], (int) dx2[i], (int) dy2[i],
                        (int) sx[i], (int) sy[i], (int) sx2[i], (int) sy2[i],
                        null
                );
            }
        } while (vRamHandle.contentsLost());
    }

    void draw(Graphics2D g, RenderQueue.SpriteTable table) {
        GraphicsConfiguration config = g.getDeviceConfiguration();

        do {
            validateVRAM(config);

            for (int i = 0; i < table.count; i++) {
                var s = Registry.sprites[table.spriteId[i]];

                int dx = (int) (table.x[i] - s.pivotX());
                int dy = (int) (table.y[i] - s.pivotY());
                int dw = (int) (table.width[i]);
                int dh = (int) (table.height[i]);

                g.drawImage(
                        vRamHandle,
                        dx, dy, dx + dw, dy + dh,
                        s.srcX(), s.srcY(), s.srcX2(), s.srcY2(),
                        null
                );
            }
        } while (vRamHandle.contentsLost());
    }

    private void validateVRAM(GraphicsConfiguration config) {
        if (vRamHandle == null) recreateVRAM(config);
        else {
            // vRamHandle.validate should be called only once.
            // this portion is very thread unsafe, as validate causes race conditions.
            int val = vRamHandle.validate(config);
            if (val == VolatileImage.IMAGE_INCOMPATIBLE) recreateVRAM(config);
            else if (val == VolatileImage.IMAGE_RESTORED) copyToVRAM();
        }
    }

    private void recreateVRAM(GraphicsConfiguration config) {
        vRamHandle = config.createCompatibleVolatileImage(
                ramSource.getWidth(),
                ramSource.getHeight(),
                Transparency.TRANSLUCENT
        );
        copyToVRAM();
    }

    private void copyToVRAM() {
        Graphics2D g = vRamHandle.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(ramSource, 0, 0, null);
        g.dispose();
    }
}
