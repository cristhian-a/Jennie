package com.next.game.io;

import com.next.engine.data.Registry;
import com.next.engine.dto.TextureMetadata;
import com.next.engine.graphics.awt.AwtFont;
import com.next.engine.io.FileReader;
import com.next.engine.io.JsonReader;
import com.next.engine.scene.Tile;
import com.next.engine.sound.SoundClip;
import com.next.engine.sound.SoundData;
import com.next.game.rules.Layers;
import com.next.game.util.Fonts;
import com.next.game.util.Sounds;
import com.next.engine.scene.LevelData;
import com.next.engine.scene.WorldRules;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Loader {

    public static class Settings {
        public static com.next.engine.system.Settings load() {
            var file = FileReader.getFile("/settings.json");
            try {
                if (file == null) throw new IOException("Settings file not found");
                return JsonReader.readObject(file, com.next.engine.system.Settings.class);
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    public static class Controls {
        public static Map<String, Integer> loadActionMap() {
            var file = FileReader.getFile("/controls.json");
            try {
                return JsonReader.readMap(file, String.class, Integer.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Founts {
        public static InputStream load() {
            return FileReader.getFile("/fonts/x12y16pxMaruMonica.ttf");
        }

        public static void register() {
            try (InputStream is = load()) {
                var f = Font.createFont(Font.TRUETYPE_FONT, is);
                Registry.fonts.put(Fonts.DEBUG, new AwtFont(f.deriveFont(Font.PLAIN, 32f)));
                Registry.fonts.put(Fonts.DEFAULT, new AwtFont(f.deriveFont(Font.PLAIN, 32f)));
                Registry.fonts.put(Fonts.DEFAULT_80_BOLD, new AwtFont(f.deriveFont(Font.BOLD, 80f)));
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Worlds {
        public static WorldRules load(String fileName) throws IOException {
            var file = FileReader.getFile("/configuration/" + fileName);
            return JsonReader.readObject(file, WorldRules.class);
        }

        public static Tile[] tiles() {
            var tiles = new Tile[10];
            tiles[0] = new Tile(Registry.textureIds.get("grass-1.png"), 0, 0);
            tiles[1] = new Tile(Registry.textureIds.get("wall-1.png"), Layers.WALL, 0);
            tiles[2] = new Tile(Registry.textureIds.get("water-1.png"), Layers.WALL, 0);
            tiles[3] = new Tile(Registry.textureIds.get("dirt-1.png"), 0, 0);
            tiles[4] = new Tile(Registry.textureIds.get("mid-trees.png"), Layers.WALL, 0);
            tiles[5] = new Tile(Registry.textureIds.get("sand-1.png"), 0, 0);
            tiles[6] = new Tile(Registry.textureIds.get("top-trees.png"), Layers.WALL, 0);
            tiles[7] = new Tile(Registry.textureIds.get("bottom-trees.png"), Layers.WALL, 0);

            return tiles;
        }

        public static Integer[][] map1() {
            Integer[][] tileMap = new Integer[50][50];

            InputStream is = FileReader.getFile("/maps/map_01.txt");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                int row = 0;

                while (row < 50) {
                    String line = br.readLine();
                    int col = 0;

                    while (col < 50) {
                        String[] numbers = line.split(" ");
                        tileMap[row][col] = Integer.parseInt(numbers[col]);
                        col++;
                    }

                    row++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return tileMap;
        }
    }

    public static class Levels {
        public static LevelData load(String fileName) throws IOException {
            var file = FileReader.getFile("/configuration/" + fileName);
            return JsonReader.readObject(file, LevelData.class);
        }
    }

    public static class Audio {

        /**
         * Load using javax.sound.AudioInputStream
         * @return {@link Map} of {@link SoundData} indexed by {@link String} track name
         * @throws RuntimeException if an error occurs while loading the audio
         */
        public static Map<SoundClip, SoundData> load() {
            var tracks = new HashMap<SoundClip, String>();
            tracks.put(Sounds.FANFARE, "/sounds/end_sound.wav");
            tracks.put(Sounds.WIND, "/sounds/wind.wav");
            tracks.put(Sounds.PICK_UP, "/sounds/pick_up.wav");
            tracks.put(Sounds.SPELL_UP, "/sounds/spell_up.wav");
            tracks.put(Sounds.OPEN_DOOR, "/sounds/door.wav");

            Map<SoundClip, SoundData> data = new HashMap<>();
            for (var track : tracks.keySet()) {
                URL u = FileReader.getResource(tracks.get(track));
                try {
                    AudioInputStream stream = AudioSystem.getAudioInputStream(u);
                    SoundData sd = new SoundData();
                    sd.format = stream.getFormat();
                    sd.pcmData = stream.readAllBytes();
                    data.put(track, sd);
                } catch (UnsupportedAudioFileException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return data;
        }
    }

    public static final class Textures {

        public static BufferedImage loadImage(String fileName) throws IOException {
            return ImageIO.read(FileReader.getFile("/textures/" + fileName));
        }

        public static TextureMetadata loadMetadata(String fileName) throws IOException {
            return JsonReader.readObject(FileReader.getFile("/textures/" + fileName), TextureMetadata.class);
        }
    }

}
