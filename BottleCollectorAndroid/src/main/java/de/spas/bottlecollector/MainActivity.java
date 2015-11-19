package de.spas.bottlecollector;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.spas.bottlecollector.core.TheGame;


public class MainActivity extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
            config.useAccelerometer = false;
            config.useCompass = false;
            config.useWakelock = true;
            initialize(new TheGame(), config);
        } catch(Exception e) {
            Log.e(getClass().getSimpleName(),"Exception: ",e);
        }
    }
}
