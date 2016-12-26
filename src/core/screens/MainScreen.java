package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import core.game.GameApp;

/**
 * Created by Rafae on 10/3/2016.
 */
public class MainScreen implements Screen {

    // creating play button
    Stage stage;
    Skin skin;
    TextureAtlas buttonAtlas;
    Button playButton;
    Button.ButtonStyle buttonStyle;

    // background
    Image bg;

    // named it mgame instead of game because playButton.addlistener confuses it with the
    // constructor's parameter and asks for it to be a final variable...
    private GameApp mgame;

    public MainScreen(GameApp game){
        mgame= game;


        stage = new Stage();

        // set up background
        Texture bgtext = new Texture("infbg.png");
        bg = new Image(bgtext);
        bg.setWidth(Gdx.graphics.getWidth());
        bg.setHeight(Gdx.graphics.getHeight());
        bg.setColor(Color.GRAY);
        stage.addActor(bg);

        /* complicated way to make a button just for up/down effecs
        // set up button style for playbutton
        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/button.pack");
        skin.addRegions(buttonAtlas);
        buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.getDrawable("playbutton1.up");
        buttonStyle.over = skin.getDrawable("playbutton1.down");
        buttonStyle.down = skin.getDrawable("playbutton1.down");
        */

        // testing button without atlas
        Texture text2 = new Texture("infplay.png");
        Drawable drawable2 = new TextureRegionDrawable(new TextureRegion(text2));

        playButton = new Button(drawable2);

        // configure the play button
        //playButton = new Button(buttonStyle);
        playButton.setHeight(Gdx.graphics.getHeight()/5);
        playButton.setWidth(Gdx.graphics.getWidth());
        playButton.setX(Gdx.graphics.getWidth() / 2 - playButton.getWidth()/2);
        playButton.setY(Gdx.graphics.getHeight() / 4 - playButton.getHeight()/2);
        //playButton.setColor(Color.LIGHT_GRAY);
        //playButton.setRotation(20); this was messing up the input rectangle without actually rotating the png
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);

        // set up what to do when playbutton is clicked
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mgame.setScreen(new PlayScreen(mgame));
                System.out.println("clicked");
                return true;
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act();

        mgame.getBatch().begin();
        stage.draw();
        mgame.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}