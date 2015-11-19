package de.spas.bottlecollector.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class TheGame extends Game {

    private int lastScore;

    public void onGameOver(int score) {
        lastScore = score;
        showStartScreen();
    }


    @Override
    public void create() {
        Gdx.app.log("thegame","create");
        showStartScreen();
    }

    private void startGame() {
        Gdx.app.log("game","start");
        setScreen(new GameScreen(this));
    }

    private void showStartScreen() {
        setScreen(new StartScreen(lastScore,new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                getScreen().dispose();
                Gdx.app.log("thegame","clicked start");
                startGame();
                return true;
            }
        }));
    }

}
