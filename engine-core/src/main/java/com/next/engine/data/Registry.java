package com.next.engine.data;

import com.next.engine.graphics.Sprite;
import com.next.engine.graphics.TextFont;
import com.next.engine.graphics.awt.ManagedTexture2;
import com.next.engine.sound.SoundClip;
import com.next.engine.sound.SoundData;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class Registry {
    public static final Map<SoundClip, SoundData> audioTracks = new HashMap<>();
    public static final Map<String, Integer> textureIds = new HashMap<>();

    public static Sprite[] sprites = new Sprite[0];

    // Temporary awt stuff
    public static final Map<Integer, BufferedImage> textures = new HashMap<>();
    public static final Map<String, TextFont> fonts = new HashMap<>();
    public static ManagedTexture2 masterSheet;
}
