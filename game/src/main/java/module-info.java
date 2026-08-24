module game {
    requires static engine.annotations;
    requires engine.core;
    requires static lombok;
    requires java.desktop;

    opens configuration;
    opens fonts;
    opens maps;
    opens sounds;
    opens sprites;
    opens textures;

    opens com.next.game;
}