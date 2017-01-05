package core.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import core.game.GameApp;
import core.handlers.Cons;

import static core.handlers.Cons.BALL_DIAM;
import static core.handlers.Cons.PPM;

/**
 * Created by Rafael on 1/2/2017.
 *
 */
public abstract class Pickup implements Pool.Poolable {

    // -------------------------------------- FINAL VARIABLES --------------------------------------
    private final int RADIUS = BALL_DIAM / 2;

    // -------------------------------------- PROTECTED VARIABLES ----------------------------------
    protected Body body;
    protected float virX; // x position
    protected float virY; // y position

    // -------------------------------------- PRIVATE VARIABLES ------------------------------------
    private World world;
    private SpriteBatch sb;
    ParticleEffect effect;

    // -------------------------------------- PUBLIC METHODS --------------------------------------

    /**
     * Constructor
     * @param world
     * @param initVirX
     * @param initVirY
     */
    public Pickup(World world, float initVirX, float initVirY) {
        this.world = world;
        this.sb = GameApp.APP.getBatch();
        this.virX = initVirX;
        this.virY = initVirY;
        construct2d();
        initParticleEffect();
    }

    public void update() {
        updatePickupEffect();
    }

    public void render() {
        update();
        effect.draw(sb, Gdx.graphics.getDeltaTime());
    }

    // -------------------------------------- PRIVATE METHODS --------------------------------------

    private void construct2d() {
        // Define box2d body
        BodyDef bdef = new BodyDef();
        bdef.position.set(virX / PPM, virY / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        // set shape
        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS / PPM);

        // Define fixture
        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;
        fdef.shape = shape;
        fdef.filter.categoryBits = Cons.BIT_SCOREPICKUP;
        fdef.filter.maskBits = Cons.BIT_PLAYER;

        // Create the actual fixture onto the body
        Fixture fixture = body.createFixture(fdef);

    }

    abstract void initParticleEffect();

    /**
     * you always have to update the position here apparently.
     */
    private void updatePickupEffect() {
        if (effect.isComplete())
            effect.reset();
        effect.setPosition(virX, virY);
        effect.update(Gdx.graphics.getDeltaTime());
    }

    // -------------------------------------- GETTER METHODS ---------------------------------------
    public float getX() {
        return virX;
    }

    public float getY() {
        return virY;
    }

    public Body getBody() {
        return body;
    }

    // -------------------------------------- SETTER METHODS ---------------------------------------

    /**
     * Wrap around body.setTransform()
     * @param x
     * @param y
     */
    public void setBodyPosition(float x, float y) {
        body.setTransform(x / PPM, y / PPM, 0);
        virX = body.getPosition().x * PPM;
        virY = body.getPosition().y * PPM;
    }

    public void dispose(){
        effect.dispose();
    }
}