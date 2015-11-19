package de.spas.bottlecollector.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by uwe on 16.10.13.
 */
public class StartScreen implements Screen {

    private final Button buttonStart;
    private Stage stage;
    private InputListener listener;

    public StartScreen(int lastScore, InputListener listener) {
        super();

        this.listener = listener;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Label.LabelStyle skin = new Label.LabelStyle();
        skin.font = new BitmapFont(Gdx.files.internal("plaincred.fnt"), Gdx.files.internal("plaincred.png"), false);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("start.png"))));

        //String formatted = NumberFormat.getFormat("0000000").format(lastScore);
        //String formatted = String.format("%07d",lastScore);
        String formatted = Integer.toString(lastScore);
        Label labelScore = new Label(formatted, skin);
        table.add(labelScore).center();
        table.row();

        Image imageLogo = new Image();
        imageLogo.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("logo.png")))));
        table.add(imageLogo).expand().fillX();
        table.row();

        buttonStart = new Button(buttonStyle);
        buttonStart.addListener(listener);
        table.add(buttonStart).center().padBottom(50);

        stage.addActor(table);

        //table.debug();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        //Table.drawDebug(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        buttonStart.removeListener(listener);
        stage.dispose();
        Gdx.app.log("startscreen","dispose");
    }

}
