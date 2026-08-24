package com.next.engine.graphics.awt;

import com.next.engine.Global;
import com.next.engine.data.Registry;
import com.next.engine.debug.DebugTimers;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Camera;
import com.next.engine.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

final class LightningRenderer {

    private final VideoSettings settings;
    private final AffineTransform identity = new AffineTransform();
    private final AlphaComposite[] compositeCache;
    private final Color[] ambientCache;

    private final int COMPOSITE_BUCKETS = 16;
    private final int AMBIENT_BUCKETS = 16;

    private VolatileImage lightMap;
    private Graphics2D lightGraphics;
    private float ambient = 0.65f;

    public LightningRenderer(VideoSettings settings) {
        this.settings = settings;

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        lightMap = gc.createCompatibleVolatileImage(
                settings.WIDTH / settings.SCALE,
                settings.HEIGHT / settings.SCALE,
                Transparency.TRANSLUCENT
        );

        lightGraphics = lightMap.createGraphics();
        lightGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        compositeCache = new AlphaComposite[COMPOSITE_BUCKETS + 1];
        for (int i = 0; i <= COMPOSITE_BUCKETS; i++) {
            float alpha = (float) i / COMPOSITE_BUCKETS;
            compositeCache[i] = AlphaComposite.getInstance(AlphaComposite.DST_OUT, alpha);
        }

        ambientCache = new Color[AMBIENT_BUCKETS + 1];
        for (int i = 0; i <= AMBIENT_BUCKETS; i++) {
            float alpha = (float) i / AMBIENT_BUCKETS;
            ambientCache[i] = new Color(0, 0, 0, (int) (alpha * 255));
        }
    }

    private int alphaToBucket(float alpha) {
        int bucket = Math.round(alpha * COMPOSITE_BUCKETS);
        if (bucket < 0) bucket = 0;
        if (bucket > COMPOSITE_BUCKETS) bucket = COMPOSITE_BUCKETS;
        return bucket;
    }

    private int ambientToBucket(float ambient) {
        int bucket = Math.round(ambient * AMBIENT_BUCKETS);
        if (bucket < 0) bucket = 0;
        if (bucket > AMBIENT_BUCKETS) bucket = AMBIENT_BUCKETS;
        return bucket;
    }

    private void punchLightMap(Camera camera, RenderQueue.LightTable lights) {
        lightGraphics.setTransform(identity);
        lightGraphics.setClip(null);

        lightGraphics.setComposite(AlphaComposite.Src);
        lightGraphics.setColor(ambientCache[ambientToBucket(ambient)]);
        lightGraphics.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());

        if (lights.count == 0) return;

        // sine-wave calculation to get the flicker effect's size
        final double t = Global.getTime() * 3d;
        final float pulse = (float) (Math.sin(t) * 0.5f + 0.5f);

        final float strength = 0.25f;
        final float flicker = 1f + pulse * strength;

        for (int i = 0; i < lights.count; i++) {
            float x = lights.x[i];
            float y = lights.y[i];
            float radius = lights.radius[i];
            float intensity = lights.intensity[i];

            float finalRadius = flicker * (radius * settings.SCALE);
            float drawX = x - (finalRadius / 2);
            float drawY = y - (finalRadius / 2);

            float alpha = Math.clamp(intensity * Global.alphaBaseFactor, 0, 1);
            int bucket = alphaToBucket(alpha);
            lightGraphics.setComposite(compositeCache[bucket]);

            BufferedImage light = Registry.textures.get(lights.textureIds[i]);
            lightGraphics.drawImage(
                    light,
                    camera.worldToScreenX(drawX),
                    camera.worldToScreenY(drawY),
                    (int) finalRadius, (int) finalRadius,
                    null
            );

//            final int argb = 0xFF00FFFF;
//            final var coloredTexture = RenderCache.INSTANCE.getColoredLight(
//                    argb,
//                    () -> makeColoredLight(light, new Color(argb))
//            );
//
//            lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//            lightGraphics.drawImage(
//                    coloredTexture,
//                    camera.worldToScreenX(drawX),
//                    camera.worldToScreenY(drawY),
//                    (int) finalRadius, (int) finalRadius,
//                    null
//            );
        }
    }

    public void render(Graphics2D g, Camera camera, RenderQueue.LayerBucket bucket) {
        try (var _ = DebugTimers.scope(DebugTimers.RENDER_LIGHTS)) {
            punchLightMap(camera, bucket.lights);
            g.setComposite(AlphaComposite.SrcOver);

            // A fix is needed to avoid the light map being pixels off in the bottom and right corners.
            g.drawImage(
                    lightMap,
                    (int) (camera.getX()) - 1,
                    (int) (camera.getY()) - 1,
                    lightMap.getWidth() + 2,
                    lightMap.getHeight() + 2,
                    null
            );
        }
    }

    void onResize() {
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        lightMap.flush();
        lightGraphics.dispose();

        /* plus 1 is here to fix a problem with the camera letting a thin slice of the rendered world without the
        *  light map covering it */
        lightMap = gc.createCompatibleVolatileImage(
                settings.WIDTH / settings.SCALE,
                settings.HEIGHT / settings.SCALE,
                Transparency.TRANSLUCENT
        );

        lightGraphics = lightMap.createGraphics();
        lightGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    private BufferedImage makeColoredLight(BufferedImage mask, Color color) {
        BufferedImage img = new BufferedImage(
                mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < mask.getHeight(); y++) {
            for (int x = 0; x < mask.getWidth(); x++) {
                int a = (mask.getRGB(x, y) >> 24) & 0xFF;
//                a = (int) compressAlpha(a, 0.45f, 2f);
                a = (int) compressAlpha(a, 0.2f, 0.75f, 0.5f);

                int r = color.getRed() * a / 255;
                int g = color.getGreen() * a / 255;
                int b = color.getBlue() * a / 255;

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, argb);
            }
        }

        return img;
    }

    private float compressAlpha(float alpha, float lowCut, float ceiling, float threshold) {
        float t = alpha / 255f;

        float out;
        if (t < lowCut) return t;
        else if (t < threshold) {
//            out = lowCut + (t / threshold) * (threshold - lowCut);
            float k = (t - lowCut) / (threshold - lowCut);
            k = k * k * (3 - 2 * k);
            out = lowCut + (threshold - lowCut) * k;
        } else {
            out = (float) (threshold + ((t - threshold) / (1.0 - threshold)) * (ceiling - threshold));
        }

        return out * 255f;
    }

    private int compressAlpha(int alpha, float threshold, float ratio) {
        // This is the best one so far; however, it leaves a strong transition zone that's unpleasant to see
        float t = alpha / 255f;

        // EPSILON provides better falloffs but a very edgy sharp
        final float EPSILON = 0.0001f;

        if (t < EPSILON) return alpha;
        float out = threshold + (t - threshold) / ratio;

        return (int) (out * 255);
    }

}
