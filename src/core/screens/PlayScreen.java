package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Stack;

import core.Hud.ArrowCharger;
import core.Hud.Rain;
import core.game.GameApp;
import core.handlers.Cons;
import core.handlers.PlayContactListener;
import core.objects.Ball;
import core.objects.Platform;

import static core.handlers.Cons.VIR_WIDTH;
import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.PPM;
import static core.handlers.Cons.SCALE;
import static core.handlers.Cons.BALL_DIAM;

/**
 * Created by Rafael on 10/3/2016.
 */
public class PlayScreen implements Screen {

    // debugging
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;
    private Array<Platform> plats;
    private boolean centerOnPlayer = true; // for ortho cams in handlerCamera()
    //private GameApp game;
    private SpriteBatch sb;
    private World world;
    private Stack<Body> bodiesToDel;

    // platforms info
    private final int WALL_WIDTH = 40;
    private int platWidth = VIR_WIDTH / 4;
    private int platHeight = 70; // MAKE IN TERMS OF VIR_HEIGHT OR VIR)WIDTH

    // For Hud
    private core.Hud.PlayHud hud;
    private int n = -1; // current depth (how many VIR_HEIGHT's the player has gone down)
    private ArrowCharger arrowCharger;
    private Rain rain;

    // contact listener
    private PlayContactListener cl;

    // player
    //private Player player;
    private Ball player;

    public PlayScreen() {
        System.out.println("PLAYSCREEN STARTED");
        // this.game = game;
        world = new World(new Vector2(0, -2.81f * 0), true);
        this.sb = GameApp.APP.getBatch();
        plats = new Array<Platform>();
        hud = new core.Hud.PlayHud();
        bodiesToDel = new Stack<Body>();

        cl = new PlayContactListener();
        world.setContactListener(cl);

        // Box2d Cam
        b2dr = new Box2DDebugRenderer();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Gdx.graphics.getWidth() * SCALE / PPM, Gdx.graphics.getHeight() * SCALE / PPM);


        // Map
        initializeMap();

        // spawn location should be fully based on Vir values
        // player = new Player(world, new Vector2(VIR_WIDTH / 2, -VIR_HEIGHT / 2 - BALL_DIAM    ));
        player = new Ball(world, new Vector2(VIR_WIDTH / 2, -VIR_HEIGHT / 2 - BALL_DIAM));

        // hud camera elements
        arrowCharger = new ArrowCharger(this);
        rain = new Rain();
    }


    // called everytime the game sets the screen to this class
    public void show() {
        System.out.println("show() in playscreen");
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(198, 198, 198, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(Cons.STEP, 6, 2);
        player.update();
        createPlats2();
        deleteFlaggedPlats();
        // game cam and b2dcam
        updateCameras(!centerOnPlayer);

        // render
        //b2dr.render(world, b2dcam.combined);


        sb.begin(); // begin



        // Game's projection matrix origin is top left corner
        sb.setProjectionMatrix(GameApp.APP.getCam().combined);
        renderAndRemovePlats();
        player.render();

        // Fontcams projection matrix (0,0) (the origin) is bottom left corner of screen
        sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
        hud.render();
        arrowCharger.render();
        rain.render();

        sb.end(); // end
    }

    /**
     * Clearing 2d bodies that were left behind.
     */
    private void renderAndRemovePlats() {
        for (int i = 0; i < plats.size; i++) {
            Platform p = plats.get(i);
            p.render();
            if (n > 2) {
                if (p.getY() > -(n - 2) * VIR_HEIGHT) {
                    bodiesToDel.add(p.getBody());
                    plats.removeIndex(i);
                }
            }
        }
    }

    // update gamecam and b2dcam
    private void updateCameras(boolean centerOnPlayer) {
        if (centerOnPlayer) {
            GameApp.APP.getCam().position.set(player.getX(), player.getY() - (Gdx.graphics.getHeight() / 3), 0);
            GameApp.APP.getCam().update();

            b2dcam.position.set(player.getX() / PPM, player.getY() / PPM - (Gdx.graphics.getHeight() / 3) / PPM, 0);
            b2dcam.update();

        } else {
            GameApp.APP.getCam().position.set(Gdx.graphics.getWidth(), player.getY() - (Gdx.graphics.getHeight() / 3), 0);
            GameApp.APP.getCam().update();

            b2dcam.position.set(Gdx.graphics.getWidth() / PPM, player.getY() / PPM - (Gdx.graphics.getHeight() / 3) / PPM, 0);
            b2dcam.update();

        }


    }

    /**
     * in initialize map we create the side walls at height n = 0 (VIR_HEIGHT * n)
     * and the top plat
     */
    private void initializeMap() {
        // create a big block on top
        Platform topSide = new Platform(world,
                VIR_WIDTH, // virtual width
                VIR_HEIGHT / 2, // virtual height
                0, // virtual x
                0); // virtual y

        // create the first 2 walls by default
        Platform leftSide = new Platform(world,
                WALL_WIDTH,
                VIR_HEIGHT,
                0 - WALL_WIDTH / 2,
                0, true);

        Platform rightSide = new Platform(world,
                WALL_WIDTH,
                VIR_HEIGHT,
                VIR_WIDTH - WALL_WIDTH / 2,
                0, true);


        plats.add(topSide);
        plats.add(leftSide);
        plats.add(rightSide);
    }


    /*
        Given the depth n,
        in initialize map we create the side walls at height n = 0 (VIR_HEIGHT * n)
        then at every even number of n (n starts at -1) this method draws
        both walls and horizonal plats at height (VIR_HEIGHT * (n+1)) and (VIR_HEIGHT * (n+2))
     */
    boolean currentNgenerated = false; // have (n+1) and (n+2) plats been generated?
    private void createPlats2(){
        n = Math.abs((int) (player.getY() / VIR_HEIGHT));

        if (n % 2 == 1)
            currentNgenerated = false;

        if (n % 2 == 0 && !currentNgenerated){
            currentNgenerated = true;

            // draw n+1, and n+2 depth platform
            for (int i = 1; i < 3; i++){
                Platform leftSide = new Platform(world,
                        WALL_WIDTH,
                        VIR_HEIGHT,
                        0 - WALL_WIDTH / 2,
                        -(n + i) * VIR_HEIGHT, true); // n+1 and you can see them being created

                Platform rightSide = new Platform(world,
                        WALL_WIDTH,
                        VIR_HEIGHT,
                        VIR_WIDTH - WALL_WIDTH / 2,
                        -(n + i) * VIR_HEIGHT, true);

                plats.add(leftSide);
                plats.add(rightSide);
            }

            // draw n+1, and n+2 depth platform
            for (int i = 1; i < 3; i++) {
                // normal platforms width based on VIR_WIDTH
                for (int j = 0; j < 4; j++) {
                    // (int)(BALL_DIAM*1.5) to leave 1.5 of diam as minimum space for ball to pass thru
                    float r = MathUtils.random((int)(BALL_DIAM *1.5), VIR_WIDTH - platWidth - (int)(BALL_DIAM *1.5));
                    Platform testplat = new Platform(world,
                            platWidth, // width
                            platHeight, // height
                            r, // virtual x
                            -(n + i) * VIR_HEIGHT - j * (VIR_HEIGHT / 4)); // virtual y
                    plats.add(testplat);
                }

            }

        }
    }


    // Destroy the bodies that were pushed into the stack
    private void deleteFlaggedPlats() {
        while (!bodiesToDel.isEmpty()) {
            world.destroyBody(bodiesToDel.pop());
        }
    }

    // GETTERS

    public int getScore() {
        return n;
    }

    public Ball getPlayer() {
        return player;
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
        player.dispose();
        world.dispose();
    }



    /**
     * old way to self generate the platforms. shouldnt really work anymore cause i also
     * changed initializeMap
     */
    private void createPlats() {

        // if the player is now lower than 1 VIR_HEIGHT unit
        if (player.getY() < -(VIR_HEIGHT)) {

            // if player went one VIR_HEIGHT deeper
            // !sameDepthAsBefore to prevent generating the current one again
            if (player.getY() < -(n * VIR_HEIGHT) && !sameDepthAsBefore()) {

                // side plats
                Platform leftSide = new Platform(world,
                        WALL_WIDTH,
                        VIR_HEIGHT,
                        0 - WALL_WIDTH / 2,
                        -(n + 2) * VIR_HEIGHT, true); // n+1 and you can see them being created

                Platform rightSide = new Platform(world,
                        WALL_WIDTH,
                        VIR_HEIGHT,
                        VIR_WIDTH - WALL_WIDTH / 2,
                        -(n + 2) * VIR_HEIGHT, true);

                // normal platforms width based on VIR_WIDTH
                for (int i = 0; i < 4; i++) {
                    float r = MathUtils.random(300, VIR_WIDTH - platWidth);
                    Platform testplat = new Platform(world,
                            platWidth, // width
                            platHeight, // height
                            r, // virtual x
                            -(n + 2) * VIR_HEIGHT - i * (VIR_HEIGHT / 4)); // virtual y
                    plats.add(testplat);
                }

                plats.add(leftSide);
                plats.add(rightSide);

            }
        }

        // updating n (depth)
        n = Math.abs((int) (player.getY() / VIR_HEIGHT));
    }

    /**
     * Old way to do plats
     * Math.abs((int) (player.getY() / VIR_HEIGHT) to get the current depth by dividing player's y position by VIR_HEIGHT
     */
    private boolean sameDepthAsBefore() {
        return n == Math.abs((int) (player.getY() / VIR_HEIGHT));
    }
}