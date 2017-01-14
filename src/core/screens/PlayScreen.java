package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import core.Pools.BallExplosionPool;
import core.Pools.BarEffectPool;
import core.Pools.PlatPool;
import core.Pools.WallPool;
import core.game.GameApp;
import core.handlers.PlayContactListener;
import core.handlers.Res;
import core.objects.Ball;
import core.objects.BallGraph;
import core.objects.Plat;
import core.objects.Wall;
import core.particleeffects.BallExplosion;
import core.particleeffects.BarEffect;
import core.pickups.ScorePickup;
import core.Pools.ScorePickupPool;

import static core.handlers.Cons.LEFT_WALL_X;
import static core.handlers.Cons.PLATS_PER_DEPTH;
import static core.handlers.Cons.PLAT_HEIGHT;
import static core.handlers.Cons.RIGHT_WALL_X;
import static core.handlers.Cons.VIR_WIDTH;
import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.PPM;
import static core.handlers.Cons.SCALE;
import static core.handlers.Cons.BALL_DIAM;
import static core.handlers.Cons.PLAT_WIDTH;
import static core.handlers.Cons.WALL_HEIGHT;

/**
 * Created by Rafael on 10/3/2016.
 *
 */
public class PlayScreen implements Screen {




    // debugging
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;
    private boolean centerOnPlayer = true; // for ortho cams in handlerCamera()
    //private GameApp game;
    private SpriteBatch sb;
    private World world;

    // Array objects
    private Array<Plat> platforms;
    private Array<Wall> walls;
    private Array<ScorePickup> scorePickups;
    private Array<BarEffect> barEffects;
    private Array<BallExplosion> ballExplosions;

    // Pools
    private PlatPool platPool;
    private WallPool wallPool;
    private ScorePickupPool spPool;
    private BarEffectPool barEffectPool;
    private BallExplosionPool ballExplosionPool;

    // For Hud
    private core.Hud.PlayHud hud;


    // DEPTH
    private int currentDepth = -1; // current depth (how many VIR_HEIGHT's the player has gone down)

    // contact listener
    private PlayContactListener cl;

    // player
    private Ball player;
    private boolean crashed = false;
    private boolean isPlayerDestroyed = false;
    public int currentScore = 0;

    // BallGraph
    private int ballGraphNumStops = 1; // num stops in the ballGraph
    private BallGraph ballGraph;
    private boolean isDisposed = false;

    // Score stage
    ScoreStage scoreStage;

    // pseudo Screen logic
    private boolean isPlayingScreen = true;
    private boolean isScoreScreen = false;

    private boolean playInitialized = false;

    // Pseudo Score screen
    //private Stage scoreStage;

    public PlayScreen() {
        world = new World(new Vector2(0, 0), true);
        this.sb = GameApp.APP.getSb();


        cl = new PlayContactListener(this);
        world.setContactListener(cl);

        // Box2d Cam
        b2dr = new Box2DDebugRenderer();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Gdx.graphics.getWidth() * SCALE / PPM, Gdx.graphics.getHeight() * SCALE / PPM);


        // platforms array
        platforms = new Array<Plat>();
        walls = new Array<Wall>();
        scorePickups = new Array<ScorePickup>();
        barEffects = new Array<BarEffect>();
        ballExplosions = new Array<BallExplosion>();

        // Pools
        platPool = new PlatPool(world);
        wallPool = new WallPool(world);
        spPool = new ScorePickupPool(world);
        barEffectPool = new BarEffectPool();
        ballExplosionPool = new BallExplosionPool();

        // BallGraph
        ballGraph = new BallGraph(ballGraphNumStops);

        // Map
        initializeMap();

        // spawn location should be fully based on Vir values
        player = new Ball(world, new Vector2(VIR_WIDTH / 2, -VIR_HEIGHT / 2 - BALL_DIAM), ballGraph);
        hud = new core.Hud.PlayHud(this);

        scoreStage = new ScoreStage(this);


    }

    // called everytime the game sets the screen to this class
    public void show() {

    }

    /**
     * in initialize map we create the side walls at height currentDepth = 0 (VIR_HEIGHT * currentDepth)
     * and the top plat
     */
    private void initializeMap() {

        // create the first 2 walls by default
        Wall leftSide = wallPool.obtain();
        leftSide.setBodyPosition(LEFT_WALL_X, 0f);

        Wall rightSide = wallPool.obtain();
        rightSide.setBodyPosition(RIGHT_WALL_X, 0);

        walls.add(leftSide);
        walls.add(rightSide);
    }


    /*
    Given the depth currentDepth,
    in initialize map we create the side walls at height currentDepth = 0 (VIR_HEIGHT * currentDepth)
    then at every even number of currentDepth (currentDepth starts at -1) this method draws
    both walls and horizonal plats at height (VIR_HEIGHT * (currentDepth+1)) and (VIR_HEIGHT * (currentDepth+2))
    */
    boolean currentNgenerated = false; // have (currentDepth+1) and (currentDepth+2) plats been generated?

    private void autoGenerateMap(){
        currentDepth = Math.abs((int) (player.getVirY() / WALL_HEIGHT));

        if (currentDepth % 2 == 1)
            currentNgenerated = false;

        if (currentDepth % 2 == 0 && !currentNgenerated){
            currentNgenerated = true;
            generateWalls(); // draw currentDepth+1, and currentDepth+2 depth WALLS
            generatePlats();
            System.out.println("GENERATING WHOLE MAP STEP");
            //generateScorePickups();
        }
    }

    private void updateWallGeneration(){
        currentDepth = Math.abs((int) (player.getVirY() / WALL_HEIGHT));

        if (currentDepth % 2 == 1)
            currentNgenerated = false;
        if (currentDepth % 2 == 0 && !currentNgenerated){
            currentNgenerated = true;
            generateWalls(); // draw currentDepth+1, and currentDepth+2 depth WALLS
        }
    }


    private Plat prevPlat = null; // necessary to build a ScorePickup between every platform, (instance variable scope is necessary to link the 8th and the 9th)
    /**
     * generates the n-1, n, n+1 walls (n is currentDepth)
     * so basically generates 8 plats at a time
     */
    private void generatePlats() {
        boolean rightAngularVelocity = false;

        // draw currentDepth+1, and currentDepth+2 depth PLATFORMS
        for (int i = 1; i < 3; i++) { // 3 should always stay as a 3
            // 4 plats per depth
            for (int j = 0; j < PLATS_PER_DEPTH; j++) {
                // (int)(BALL_DIAM*1.5) to leave 1.5 of diam as minimum space for ball to pass thru
                float r = MathUtils.random((float)(BALL_DIAM *1.5), VIR_WIDTH - PLAT_WIDTH - (float)(BALL_DIAM *1.5));

                // Get CURRENT PLAT
                Plat plat = platPool.obtain();
                plat.setBodyPosition(r, (-(currentDepth + i) * VIR_HEIGHT - j * (VIR_HEIGHT / PLATS_PER_DEPTH)));
                //                   r,   -(overall depth, top of screen) - ( depth between each plat)

                // update angular velocity, when player depth (currentDepth) is > 1, start adding angular velocity
                if (currentDepth > 1 && j % 2 == 0){
                    // Handle Angular Velocity
                    float rAngle = MathUtils.random(2f);
                    rightAngularVelocity = !rightAngularVelocity;
                    if (rightAngularVelocity)
                        plat.setAngularVel(rAngle);
                    else
                        plat.setAngularVel(-rAngle);
                }
                platforms.add(plat);


                // Generate Pickups
                if (prevPlat != null){
                    float spX = (prevPlat.getX() + plat.getX()) / 2 + PLAT_WIDTH / 2;
                    float spY = (prevPlat.getY() + plat.getY()) / 2 + PLAT_HEIGHT / 2;
                    ScorePickup sp = spPool.obtain();
                    sp.setBodyPosition(spX, spY);
                    scorePickups.add(sp);
                }

                prevPlat = plat;
            }

        }
    }

    /**
     * generates the n-1, n, n+1 walls (n is currentDepth)
     */
    private void generateWalls() {
        for (int i = 1; i < 3; i++){
            Wall leftSide = wallPool.obtain();
            leftSide.setBodyPosition(LEFT_WALL_X,  -(currentDepth + i) * WALL_HEIGHT);

            Wall rightSide = wallPool.obtain();
            rightSide.setBodyPosition(RIGHT_WALL_X, -(currentDepth + i) * WALL_HEIGHT);

            walls.add(leftSide);
            walls.add(rightSide);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleBackInput();
       // b2dr.render(world, b2dcam.combined);
        if (isDisposed)
            return;

        if (crashed && !isPlayerDestroyed){
            Gdx.input.setInputProcessor(scoreStage.getStage());
            updateBestScore(currentScore);

            isScoreScreen = true;
            isPlayingScreen = false;
            if (!isPlayerDestroyed) {
                world.destroyBody(player.getBody());
                player.dispose();
                player = null;
                isPlayerDestroyed = true;
            }
        }


    if (isPlayingScreen){
            // updating
            autoGenerateMap();

            if (!playInitialized) {
                playInitialized = true;
            }
            updateCameras(!centerOnPlayer); // game and b2d cams

            // rendering
            sb.begin();
            sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
            renderAndRemoveBarEffects();

            // GAME CAM
            sb.setProjectionMatrix(GameApp.APP.getCam().combined);
            renderAndRemovePlats();
            renderAndRemoveWalls();
            renderAndFreeScorePickups();
            player.render();

            sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
            hud.render();
            sb.end();
        } else if (isScoreScreen){
            scoreStage.getStage().act();
            sb.begin();
            // GAME CAM
            sb.setProjectionMatrix(GameApp.APP.getCam().combined);
            renderAndRemovePlats();
            renderAndRemoveWalls();
            renderAndFreeScorePickups();
            //player.render();
            for (BallExplosion b: ballExplosions)
                    b.render();

            // Hud cam
            sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
            scoreStage.render(); // render here
            //hud.render();
            hud.getRain().render();

            scoreStage.getStage().draw();

            sb.end();
        }

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    private void updateBestScore(int currentScore) {
        int bestScore = Res.prefs.getInteger("bestscore");
        if (currentScore > bestScore){
            Res.prefs.putInteger("bestscore", currentScore);
            Res.prefs.flush();
        }
    }

    private void renderAndRemoveBarEffects(){
        for (BarEffect be : barEffects) {
            if (be.isComplete()){
                barEffects.removeValue(be, true);
                barEffectPool.free(be);
            }
            be.render();
        }
    }

    /**
     * Puts Plats and their 2d bodies that were left behind back into the platPool.
     */
    private void renderAndRemovePlats() {

        for (Plat p : platforms){
            p.render();
            if (currentDepth > 2) {
                if (p.getY() > -(currentDepth - 2) * VIR_HEIGHT) {
                    platforms.removeValue(p, true); // true is to use .equals()
                    platPool.free(p); // add back to pool
                }
            }
        }
    }


    /**
     * Puts Walls and their 2d bodies that were left behind back into the wallPool.
     */
    private void renderAndRemoveWalls(){
        for (Wall w : walls){
            w.render();
            if (currentDepth > 2) {
                if (w.getY() > -(currentDepth - 2) * VIR_HEIGHT) {
                    walls.removeValue(w, true); // true is to use .equals()
                    wallPool.free(w); // add back to pool
                }
            }
        }
    }

    /**
     * render ScorePickups and free them if player is already past them
     */
    private void renderAndFreeScorePickups(){
        for (ScorePickup sp : scorePickups){
            sp.render();
            if (currentDepth > 2) {
                if (sp.getY() > -(currentDepth - 2) * VIR_HEIGHT) {
                    scorePickups.removeValue(sp, true); // true is to use .equals()
                    spPool.free(sp); // add back to pool
                }
            }
        }
    }

    // update gamecam and b2dcam
    private void updateCameras(boolean centerOnPlayer) {
        if (centerOnPlayer) {
            GameApp.APP.getCam().position.set(
                    player.getVirX(),
                    player.getVirY() - (Gdx.graphics.getHeight() / 3),
                    0);
            GameApp.APP.getCam().update();

            b2dcam.position.set(player.getVirX() / PPM, player.getVirY() / PPM - (Gdx.graphics.getHeight() / 3) / PPM, 0);
            b2dcam.update();

        } else {
            GameApp.APP.getCam().position.set(VIR_WIDTH / 2, player.getVirY() - (VIR_HEIGHT / 7), 0);
            GameApp.APP.getCam().update();

            b2dcam.position.set(VIR_WIDTH / 2 / PPM, player.getVirY() / PPM - (VIR_HEIGHT / 7) / PPM, 0);
            b2dcam.update();

        }


    }

    /**
     * GOTTA IMPLEMENT THIS IN A DIFF WAY CAUSE U GOTTA MAKE SURE YOU STOP THE CURRENT RENDER
     * LOOP WITH A RETURN, SO THAT NOTHING ELSE IS CALLED ONCE EVERYTHING IS DISPOSED
     */
    private void handleBackInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            if (isScoreScreen){
                GameApp.APP.isBackPressed = true;
                dispose();
                isDisposed = true;
                GameApp.APP.setScreen(new MainScreen());

            }


        }
    }

    // GETTERS

    public int getScore() {
        return currentScore;
    }

    public Ball getPlayer() {
        return player;
    }

    public ScorePickupPool getSpPool() {
        return spPool;
    }

    public Array<ScorePickup> getScorePickups() {
        return scorePickups;
    }

    public BallGraph getBallGraph() {
        return ballGraph;
    }

    // SETTERS

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    public void setBarEffect(float virX){
        BarEffect be = barEffectPool.obtain();
        be.setVirX(virX);
        barEffects.add(be);
    }

    public void renderBallExplosion(float virX, float virY){
        BallExplosion be = ballExplosionPool.obtain();
        be.setPosition(virX, virY);
        ballExplosions.add(be);
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
        if (player != null)
            player.dispose();
        world.dispose();
        hud.dispose();
        // dipose of all scorepickups when finally done, in order to dispose all effects
        for (ScorePickup sp : scorePickups)
            sp.dispose();
        for (BarEffect be : barEffects)
            be.dispose();
        for (BallExplosion be : ballExplosions)
            be.dispose();
    }
}