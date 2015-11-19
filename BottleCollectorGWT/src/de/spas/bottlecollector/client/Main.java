package de.spas.bottlecollector.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.spas.bottlecollector.core.TheGame;

public class Main extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(480, 800);
        return config;
    }

    @Override
    public ApplicationListener getApplicationListener () {
        return new TheGame();
    }
}
