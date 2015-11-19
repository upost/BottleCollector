package de.spas.bottlecollector;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.spas.bottlecollector.core.TheGame;

public class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Bottle Collector";
        cfg.width = 480;
        cfg.height = 800;
        new LwjglApplication(new TheGame(), cfg);
    }

}
