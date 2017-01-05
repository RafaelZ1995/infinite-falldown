package core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import core.handlers.Res;
import core.screens.MainScreen;
import core.screens.PlayScreen;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;


/**
 * IMPORTANT:
 * SHOULDNT THIS CLASS BE THE ONE TO COMPLETELY HANDLE WHAT SCREEN IS PLAYING?
 * INSTEAD OF MAINSCREEN BEING THE making a new PlayScreen()
 * BUT THEN AGAIN I DEFEATED THE PURPOSE OF THAT BY MAKING THE GameApp instance static in Cons
 * I think this should still be the one doing it though. just for organization.
 */
public class GameApp extends Game {

    // public
    public static GameApp APP; // Tried making this a final static in Cons... That is a big NO NO.
    public static BitmapFont font128;

    // private
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Res res; // this does need to be initialized
    private OrthographicCamera fontcam;

    @Override
    public void create() {
        APP = this;
        batch = new SpriteBatch();

        // Load assets
        res = new Res();

        // Main Player Cam
        cam = new OrthographicCamera();
        cam.setToOrtho(false, VIR_WIDTH, VIR_HEIGHT);

        // Font Cam
        fontcam = new OrthographicCamera();
        fontcam.setToOrtho(false, VIR_WIDTH, VIR_HEIGHT);
        font128 = new BitmapFont(Gdx.files.internal("fonts/Roboto-LightItalic.fnt"));

        // initialize main screen
        this.setScreen(new MainScreen());
    }

    @Override
    public void render() {
        //System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        super.render();
    }

    @Override
    public void dispose() {
        font128.dispose();
        batch.dispose();
        res.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public OrthographicCamera getFontcam() {
        return fontcam;
    }

    /**
     * In case we want to actually generate the png file from the .ttf files
     */
    private void generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-LightItalic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128;
        parameter.color = Color.WHITE;
        //font128 = generator.generateFont(parameter);
        generator.dispose();
    }
}
