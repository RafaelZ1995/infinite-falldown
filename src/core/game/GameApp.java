package core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import core.handlers.Res;
import core.screens.MainScreen;
import core.screens.PlayScreen;
import core.screens.SplashScreen;

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


    public boolean isBackPressed = false;
    private boolean isMainScreenSet = false; // after splash screen is done

    // private
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Res res; // this does need to be initialized
    private OrthographicCamera fontcam;
    private boolean isSplashDone;
    private float splashTime = 0;

    FPSLogger logger = new FPSLogger();



    @Override
    public void create() {
        APP = this;
        batch = new SpriteBatch();
        Gdx.input.setCatchBackKey(true);

        // Load assets
        res = new Res();

        // Main Player Cam
        cam = new OrthographicCamera();
        cam.setToOrtho(false, VIR_WIDTH, VIR_HEIGHT);

        // Font Cam
        fontcam = new OrthographicCamera();
        fontcam.setToOrtho(false, VIR_WIDTH, VIR_HEIGHT);
        //font128 = new BitmapFont(Gdx.files.internal("fonts/Roboto-LightItalic.fnt")); // synchronously

        // initialize main screen
        this.setScreen(new SplashScreen());


        System.out.println("Vir_WIDTH: " + VIR_WIDTH + "  Vir_HEIGHT: " + VIR_HEIGHT);
    }

    @Override
    public void render() {

        //System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        logger.log();

        batch.totalRenderCalls = 0;

        if (splashTime > 2){ // if true, then we are done loading
            //APP.getScreen().dispose();
            if (!isMainScreenSet) {
                APP.getScreen().dispose();
                isMainScreenSet = true;
                APP.setScreen(new MainScreen());
            }
            super.render();
        }else{
            super.render();
            splashTime += Gdx.graphics.getDeltaTime();
        }

        int calls = batch.totalRenderCalls;
        //System.out.println("total render calls: " + calls);
    }

    @Override
    public void dispose() {
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


}
