package screens;

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
import com.meteor.MeteorGame;

import java.util.Stack;

import handlers.Cons;
import objects.Platform;
import objects.Player;


import static handlers.Cons.VIR_WIDTH;
import static handlers.Cons.VIR_HEIGHT;
import static handlers.Cons.PPM;
import static handlers.Cons.SCALE;

/**
 * Created by Rafae on 10/3/2016.
 */
public class PlayScreen implements Screen {

    // debugging
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;
    Array<Platform> plats;
    Platform ptest;
    private boolean centerOnPlayer = true; // for ortho cams in handlerCamera()

    private Player player;

    private MeteorGame game;
    private SpriteBatch sb;

    private World world;

    private Stack<Body> bodiesToDel;

    // platforms info
    private final int WALL_WIDTH = 40;
    private int platWidth = 200;
    private int platHeight = 40;
    //private final int pHeight = VIR_HEIGHT;


    public PlayScreen(MeteorGame game){
        this.game = game;
        world = new World(new Vector2(0, -2.81f * 2), true);
        this.sb = game.getBatch();
        plats = new Array<Platform>();

        //debugging
        b2dr = new Box2DDebugRenderer();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Gdx.graphics.getWidth() * SCALE / PPM, Gdx.graphics.getHeight() * SCALE / PPM);

        bodiesToDel = new Stack<Body>();

        // Map
        initializeMap();

        // spanned at (world, game, vec(middleOfScreen, )
        player = new Player(world, game, new Vector2(VIR_WIDTH/2, -VIR_HEIGHT/4 - 200 ));
    }

    //make constants for virtual WIDTH and HEIGHT (actual width and height of the phone * SCALE)
    private void initializeMap(){
        // create a big block on top
        Platform topSide = new Platform(world, game,
                VIR_WIDTH ,
                VIR_HEIGHT / 4,
                new Vector2(0, 0));

        // create the first 2 walls by default
        Platform leftSide = new Platform(world, game,
                WALL_WIDTH,
                VIR_HEIGHT,
                new Vector2(0 - WALL_WIDTH/2, 0));
        Platform leftSide2 = new Platform(world, game,
                WALL_WIDTH,
                VIR_HEIGHT,
                new Vector2(0 - WALL_WIDTH/2, -VIR_HEIGHT));

        Platform rightSide = new Platform(world, game,
                WALL_WIDTH,
                VIR_HEIGHT,
                new Vector2(VIR_WIDTH - WALL_WIDTH/2, 0));
        Platform rightSide2 = new Platform(world, game,
                WALL_WIDTH,
                VIR_HEIGHT,
                new Vector2(VIR_WIDTH - WALL_WIDTH/2, -VIR_HEIGHT));

        plats.add(topSide);
        plats.add(leftSide);
        plats.add(leftSide2);
        plats.add(rightSide);
        plats.add(rightSide2);
    }

    private int n = 1; // current H (how many VIR_HEIGHT's the player has gone down)

    // create platforms that need to be created
    private void createPlats(){


        System.out.println("N: " + n);

        if (player.getY() < -(VIR_HEIGHT)){
            // side plats
            if (player.getY() < -(n * VIR_HEIGHT) && n != Math.abs((int) (player.getY() / VIR_HEIGHT))){
                Platform leftSide = new Platform(world, game,
                        WALL_WIDTH,
                        VIR_HEIGHT,
                        new Vector2(0 - WALL_WIDTH/2, -(n+2)*VIR_HEIGHT)); // n+1 and you can see them being created

                Platform rightSide = new Platform(world, game,
                        WALL_WIDTH,
                        VIR_HEIGHT,
                        new Vector2(VIR_WIDTH - WALL_WIDTH/2, -(n+2)*VIR_HEIGHT));

                // normal platforms width=200
                for (int i = 0; i < 4; i++){
                    float r = MathUtils.random(300, VIR_WIDTH - 200);
                    Platform testplat = new Platform(world, game,
                            platWidth, // width
                            platHeight, // height
                            new Vector2(r, -(n+2)*VIR_HEIGHT - i * (VIR_HEIGHT / 4)));
                    plats.add(testplat);
                }

                plats.add(leftSide);
                plats.add(rightSide);

            }
        }

        n = Math.abs((int) (player.getY() / VIR_HEIGHT));
    }

    private void deleteFlaggedPlats(){
        while (!bodiesToDel.isEmpty()) {
            world.destroyBody(bodiesToDel.pop());
        }
    }

    private void createTestingMap() {
        for (int i = 0; i < 50; i++){
            float r = MathUtils.random(100, Gdx.graphics.getWidth()*SCALE - 100);
            Platform testplat = new Platform(world, game, 200, 20, new Vector2(r, -(i*(Gdx.graphics.getHeight()/2) + 50)));
            plats.add(testplat);
        }

        for (int i = 0; i < 50; i++) {
            Platform leftSide = new Platform(world, game,
                    40,
                    Gdx.graphics.getHeight() / 2,
                    new Vector2(0, -(i*Gdx.graphics.getHeight())));

            Platform rightSide = new Platform(world, game,
                    40,
                    Gdx.graphics.getHeight() / 2,
                    new Vector2(Gdx.graphics.getWidth() * SCALE,
                            -(i*Gdx.graphics.getHeight())));
        }
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
        createPlats();
        deleteFlaggedPlats();
        // game cam and b2dcam
        updateCameras(!centerOnPlayer);

        // render
        b2dr.render(world, b2dcam.combined);


        sb.setProjectionMatrix(game.getCam().combined);

        sb.begin();

        for (int i = 0; i < plats.size; i++){
            Platform p = plats.get(i);
            p.render();

            //System.out.println("p.y: " +  p.getY() + "   -2VHeight " + (-2*VIR_HEIGHT) +  "     (n-2)*Vheight: " + (-(n-2) * VIR_HEIGHT));
            if (n > 2){

                if (p.getY() > -(n-2) * VIR_HEIGHT){
                    //System.out.println("##############################");
                    bodiesToDel.add(p.getBody());
                    plats.removeIndex(i);
                }
            }
        }

        System.out.println("numplats: " + plats.size);
        // render player
        player.render();

        sb.end();



    }

    // update gamecam and b2dcam
    private void updateCameras(boolean centerOnPlayer) {
        if (centerOnPlayer){
            game.getCam().position.set(player.getX(), player.getY() - (Gdx.graphics.getHeight()/3), 0);
            game.getCam().update();

            b2dcam.position.set(player.getX() / PPM, player.getY() / PPM - (Gdx.graphics.getHeight()/3) / PPM, 0);
            b2dcam.update();
        }else{
            game.getCam().position.set(Gdx.graphics.getWidth(), player.getY() - (Gdx.graphics.getHeight()/3), 0);
            game.getCam().update();

            b2dcam.position.set(Gdx.graphics.getWidth() / PPM, player.getY() / PPM - (Gdx.graphics.getHeight()/3) / PPM, 0);
            b2dcam.update();
        }


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
        world.dispose();
    }
}