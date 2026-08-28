package com.next.engine.data;

import com.next.engine.dto.Frame;
import com.next.engine.dto.Point;
import com.next.engine.dto.Rectangle;
import com.next.engine.dto.TextureMetadata;
import com.next.engine.graphics.Sprite;
import com.next.engine.graphics.awt.ManagedTexture2;

import java.awt.image.BufferedImage;
import java.util.List;

public final class AtlasImporter {

    public static void register(BufferedImage sheet, TextureMetadata metadata) {
        if (sheet == null || metadata == null) {
            throw new IllegalArgumentException("Sheet or metadata cannot be null");
        }

        Registry.masterSheet = new ManagedTexture2(sheet);

        List<Frame> frames = metadata.getFrames();
        Registry.sprites = new Sprite[frames.size()];

        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            String filename = frame.getFilename();
            Rectangle coordinates = frame.getFrame();

            if (coordinates.x + coordinates.width > sheet.getWidth() ||
                    coordinates.y + coordinates.height > sheet.getHeight()) {
                throw new IllegalArgumentException("Frame " + filename + " is out of bounds");
            }

            Point pivot = frame.getPivot();
            float pivotX = 0, pivotY = 0;
            if (pivot != null) {
                pivotX = coordinates.width * frame.getPivot().x;
                pivotY = coordinates.height * frame.getPivot().y;
            }

            Sprite sprite = new Sprite(
                    i, filename,
                    coordinates.x, coordinates.y,
                    coordinates.x + coordinates.width, coordinates.y + coordinates.height,
                    coordinates.width, coordinates.height,
                    pivotX, pivotY
            );

            Registry.textureIds.put(filename, i);
            Registry.sprites[i] = sprite;
        }
    }
}
