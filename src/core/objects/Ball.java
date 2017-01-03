package core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import core.game.GameApp;
import core.handlers.Cons;
import core.handlers.Res;

import static core.handlers.Cons.PPM;
import static core.handlers.Cons.BALL_DIAM;
import static core.handlers.Cons.BALL_FALL_SPEED;


/**
 * Created by Rafael on 12/29/2016.
 * Player class but going to implement different input method (hold based)
 */
public class Ball implements InputProcessor {

    // Properties
    private final int RADIUS = BALL_DIAM / 2;

    private final float XFORCE = 800; // multiplier for left/rightward swipe
    private final float YForce = 500; // multiplier for upward swipe
    private float x; // virtual coordinates
    private float y; // virtual coordinates

    // Swiping
    private int prevTouchDownX = Gdx.graphics.getWidth() / 2;
    private int prevTouchDownY;

    // Body
    private Body body;

    // Position
    private Vector2 pos;

    // World
    private World world;

    private SpriteBatch sb;

    // TESTING PARTICLE EFFECTS
    ParticleEffect effect;  // Change the "life" property of the effect to change how long the tail

    // variables for hold-based input
    private float maxHoldTime = 800; // in milliseconds
    private float currentHoldTime; // in milliseconds
    private long holdStartTime;
    private boolean leftSideTouchDown;
    private float xForceRatio;
    private boolean touchedDown = false;

    public Ball(World world, Vector2 pos) {
        this.world = world;
        this.pos = pos;
        this.sb = GameApp.APP.getBatch();
        x = pos.x;
        y = pos.y;
        pos.scl(1 / PPM);
        xForceRatio = 0;
        construct2d();
        Gdx.input.setInputProcessor(this);

        initParticleEffect();
    }

    private void initParticleEffect(){
        // TESTING PARTICLE EFFECTS
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/ballTail.p"), Gdx.files.internal(""));
        effect.scaleEffect(3);
        effect.getEmitters().first().setPosition(x, y);
        effect.start();
    }

    /**
     * Update
     */
    public void update() {
        body.setLinearVelocity(body.getLinearVelocity().x, BALL_FALL_SPEED);

        // update position
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        updateTailEffect(); // 2d particle effect
        updateLaunch(); // updating hold down time
    }

    /**
     * updateTailEffect
     */
    private void updateTailEffect() {
        if (effect.isComplete())
            effect.reset();
        effect.setPosition(x, y);
        effect.update(Gdx.graphics.getDeltaTime());
    }

    /**
     * Update Launch
     *
     * I could also just use the boolean that keeps track of the finger being down, and just increase the xForceRatio however i want as long as the boolean is true.
     */
    private void updateLaunch() {
        if (touchedDown) {
            currentHoldTime = System.currentTimeMillis() - holdStartTime;
            if (currentHoldTime > maxHoldTime)
                currentHoldTime = maxHoldTime;

        }
        xForceRatio = currentHoldTime / maxHoldTime;
        //System.out.println("currentHoldTime:" + currentHoldTime + "   maxHoldTime: " + maxHoldTime + "  xforceratio " + xForceRatio);
    }


    /**
     * Render
     */
    public void render() {
        effect.draw(sb, Gdx.graphics.getDeltaTime());
        sb.setColor(1, 1, 1, 1);
        sb.draw(Res.playerTexture, body.getPosition().x * PPM - RADIUS, body.getPosition().y * PPM - RADIUS, RADIUS * 2, RADIUS * 2);

    }

    /**
     * Construct2d
     */
    private void construct2d() {
        // Define box2d body
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        // set shape
        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS / PPM);


        // Define fixture
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 1f;
        fdef.friction = 0f;
        fdef.filter.categoryBits = Cons.BIT_PLAYER;
        fdef.filter.maskBits = Cons.BIT_PLAT | Cons.BIT_PLAYER;

        // Create the actual fixture onto the body
        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData("Ball"); // to recognize in contact listener

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchedDown = true;

        holdStartTime = System.currentTimeMillis();
        if (touchDownLeftSide(screenX)){
            leftSideTouchDown = true;
        } else {
            leftSideTouchDown = false;
        }
        return false;
    }


    /*
     * so this is the method is called right after hitting the play button
     * so that the mainscreen takes you to the playscreen
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchedDown = false;
        currentHoldTime = 0;
        // avoid the input proble when transitioning from main to play screen
        if (holdStartTime == 0) {
            return false;
        }

        if (leftSideTouchDown) {
            body.applyForceToCenter(-xForceRatio * XFORCE, 0, true);
        }
        else {
            body.applyForceToCenter(xForceRatio * XFORCE, 0, true);
        }


        return false;
    }

    private boolean touchDownLeftSide(int screenX) {
        if (screenX < Gdx.graphics.getWidth() / 2)
            return true;
        else
            return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    // getters

    // virtual coordinates (already multiplied * PPM)
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getxForceRatio() {
        return xForceRatio;
    }

    public boolean isLeftSideTouchDown() {
        return leftSideTouchDown;
    }


    /**
     * Dispose
     */
    public void dispose(){
        effect.dispose();
    }
}