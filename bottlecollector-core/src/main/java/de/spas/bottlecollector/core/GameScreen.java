package de.spas.bottlecollector.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by uwe on 16.10.13.
 */
public class GameScreen implements Screen {

    private final static int BOTTLE_TYPES=6;
    public static final int VIEWPORT_WIDTH = 800;
    public static final int VIEWPORT_HEIGHT = 1280;
    private static final int CRATE_WIDTH = 128;
    private static final int CRATE_HEIGHT = 128;
    private static final int BOTTLE_WIDTH = 25;
    private static final float BOTTLE_START_INTERVAL = 1.2f;
    private static final float BOTTLE_VY = 420f;
    private static final int BOTTLE_VY_RND = 250;
    private static final int BOTTLE_VY_RND_PER_SECOND = 10;
    private static final float CRATE_SPEED = 900f;
    private static final int CRATE_Y = 40;
    private static final int START_THIRST= 10;
    private static final int MAX_THIRST= 100;
    private static final float THIRST_PER_SECOND=3;
    private static final float THIRST_INCREMENT_PER_SECOND=0.1f;
    private static final int BOTTLE_HEIGHT = 100;
    private static final float THIRST_DEC_PER_BOTTLE = 10;

    private final OrthographicCamera camera;
    private final Label labelScore;
    private final Label labelThirst;
    private final Sound sound;
    private int width;
    private int height;
    private final TheGame theGame;
    private final SpriteBatch batch;
    private final Texture[] texture;
    private final Set<Bottle> bottles = new HashSet<Bottle>();
    private final Texture texCrate;
    private float crateX,crateY;
    private float time;
    private float lastBottleStartTime;
    private final Random rnd = new Random();
    private float thirst;
    private int score;
    private Stage stage;
    private ParticleEffect particleEffect;

    class Bottle {
        float x, y;
        float vx,vy;
        int type;
        int value;
    }

    public GameScreen(TheGame theGame) {
        this.theGame = theGame;
        texture = new Texture[BOTTLE_TYPES];
        for(int i=0; i<BOTTLE_TYPES; i++) {
            texture[i] = new Texture(Gdx.files.internal("bottle"+Integer.toString(i+1)+".png"));
        }
        texCrate = new Texture(Gdx.files.internal("beercrate.png"));

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        thirst = START_THIRST;

        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);

        Label.LabelStyle skin = new Label.LabelStyle();
        skin.font = new BitmapFont(Gdx.files.internal("plaincred.fnt"), Gdx.files.internal("plaincred.png"), false);

        labelScore = new Label("", skin);
        table.add(labelScore).expand().top();
        table.row();
        labelThirst = new Label("", skin);
        table.add(labelThirst).fillY().bottom().left();

        stage.addActor(table);

        sound = Gdx.audio.newSound(Gdx.files.internal("gulp.ogg"));
        Gdx.app.log("game","starting game");

        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("bubbles.pe"), Gdx.files.internal(""));
    }

    @Override
    public void render(float v) {
        float t = Gdx.graphics.getDeltaTime(); // seconds
        moveBottles(t);
        moveCrate(t);
        time += t;
        if(time-lastBottleStartTime > BOTTLE_START_INTERVAL) {
            createBottle();
            lastBottleStartTime = time;
        }
        thirst += t*(THIRST_PER_SECOND+time*THIRST_INCREMENT_PER_SECOND);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.

        batch.begin();
        particleEffect.draw(batch, t);
        // Drawing goes here!
        for(Bottle bottle : bottles) {
            batch.draw(texture[bottle.type], bottle.x, bottle.y, BOTTLE_WIDTH, BOTTLE_HEIGHT);
        }
        batch.draw(texCrate, crateX, crateY, CRATE_WIDTH, CRATE_HEIGHT);
        batch.end();

        String formatted = Integer.toString(score);
        labelScore.setText(formatted);
        String s="THIRST ";
        for(int i=0; i<thirst/10; i++) s+="=";
        labelThirst.setText(s);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if(thirst>MAX_THIRST) gameOver();
    }

    private void moveCrate(float t) {
        if(Gdx.input.isTouched()) {
            if(Gdx.input.getX() < crateX+CRATE_WIDTH/2) crateX -= CRATE_SPEED * t;
            if(Gdx.input.getX() > crateX+CRATE_WIDTH/2) crateX += CRATE_SPEED * t;
        }
    }

    private void moveBottles(float t) {
        Iterator<Bottle> i = bottles.iterator();
        while(i.hasNext()) {
            Bottle b = i.next();
            b.x += b.vx*t;
            b.y += b.vy*t;
            if(b.x >= crateX && b.x+BOTTLE_WIDTH<=crateX+CRATE_WIDTH && b.y<crateY+CRATE_HEIGHT/4) {
                // gulp
                sound.play();
                score += b.value;
                thirst -= THIRST_DEC_PER_BOTTLE;
                if(thirst<0) thirst=0;
                i.remove();
                // particle effect
                particleEffect.setPosition(crateX+CRATE_WIDTH/2, crateY+CRATE_HEIGHT/4);
                particleEffect.start();
                Gdx.app.log("game","caught bottle for " + b.value);
            } else {
                if(b.y < 0) {
                    i.remove();
                }
            }
        }
    }

    private void createBottle() {
        Bottle b = new Bottle();
        b.type = rnd.nextInt(BOTTLE_TYPES);
        b.x = rnd.nextInt(Math.round(width-BOTTLE_WIDTH));
        b.y = height;
        int add = rnd.nextInt(Math.round(BOTTLE_VY_RND + BOTTLE_VY_RND_PER_SECOND*time));
        b.value = add;
        b.vy = -(BOTTLE_VY + add);
        b.vx = 0;
        bottles.add(b);
    }

    @Override
    public void resize(int width, int height) {
        this.width= width;
        this.height= height;
        init();
    }


    private void init() {
        crateX = width/2 - CRATE_WIDTH/2;
        crateY = CRATE_Y;
        time = 0;
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
        particleEffect.dispose();
        batch.dispose();
    }

    private void gameOver() {
        theGame.onGameOver(score);
    }
}
