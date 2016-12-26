package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.meteor.MeteorGame;

import handlers.Cons;
import handlers.Res;

import static handlers.Cons.PPM;

/**
 * Created by Rafae on 9/19/2016.
 */
public class Player implements InputProcessor {

    private final int RADIUS = 90;
    // Motion
    private final float ACC = (float) 0.8; // acceleration
    private final float DACC = (float) 0.2; // deacceleration
    private final float MAX_SPEED = (float) 2.3;
    private final float XFORCE = 800; // multiplier for left/rightward swipe
    private final float YForce = 500; // multiplier for upward swipe
    private float x;
    private float y;
    // Swiping
    private int prevTouchDownX = Gdx.graphics.getWidth() / 2;
    private int prevTouchDownY;
    // Body
    private Body body;
    // Position
    private Vector2 pos;
    // Properties
    private float width;
    private float height;
    // World
    private World world;
    // Meteor Game
    private MeteorGame game;
    private SpriteBatch sb;
    private float speed;
    private boolean isFacingRight;

    public Player(World world, MeteorGame game, Vector2 pos) {
        this.world = world;
        this.width = 20;
        this.height = 20;
        this.pos = pos;
        this.game = game;
        this.sb = game.getBatch();
        x = pos.x;
        y = pos.y;
        pos.scl(1 / PPM);
        construct2d();

        Gdx.input.setInputProcessor(this);
    }

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
        fdef.restitution = 0f;
        fdef.friction = 0.1f;
        fdef.filter.categoryBits = Cons.BIT_PLAYER;
        fdef.filter.maskBits = Cons.BIT_PLAT | Cons.BIT_PLAYER;

        // Create the actual fixture onto the body
        Fixture fixture = body.createFixture(fdef);

    }

    public void update() {
        //float linearY = body.getLinearVelocity().y;
        // get the linear Velocity y, and use it for setLinearVelocity
        //body.setLinearVelocity(speed, linearY);
        // that way we are not changing it to 0.

        // update position
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        //System.out.println("player pos (" + x + ", " + y + ")");
    }

    public void render() {
        sb.draw(Res.playerTexture, body.getPosition().x * PPM - RADIUS, body.getPosition().y * PPM - RADIUS, RADIUS * 2, RADIUS * 2);
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        prevTouchDownX = screenX;
        prevTouchDownY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // leftward and rightward swiping
        float xDragDist = Math.abs(prevTouchDownX - screenX);
        float xDragRatio = xDragDist / Gdx.graphics.getWidth();// normalize to get: 0 < ratio < 1

        // up swiping
        float yDragDist = Math.abs(screenY - prevTouchDownY);
        float yDragRatio = yDragDist / Gdx.graphics.getHeight();// normalize to get: 0 < ratio < 1

        // if swipe was downward then no change
        if (prevTouchDownY < screenY)
            yDragRatio = 0;

        if (prevTouchDownX - screenX < 0)
            body.applyForceToCenter(xDragRatio * XFORCE, (yDragRatio * YForce), true);
        else
            body.applyForceToCenter(-xDragRatio * XFORCE, (yDragRatio * YForce), true);


        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //System.out.println("start(" + screenX + ", " + screenY + ")");
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
    public float getY() { return y; }
}
