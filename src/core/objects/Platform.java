package core.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import core.game.GameApp;
import core.handlers.Cons;
import core.handlers.Res;

import static core.handlers.Cons.PPM;
import static core.handlers.Cons.RED;
import static core.handlers.Cons.YELLOW;

/**
 * Created by Rafae on 9/19/2016.
 * 12/28/16
 */
public class Platform {

    // Texture to tint
    private Texture texture;
    private Sprite sprite;

    // Body
    private Body body;

    // Position
    private float virX;
    private float virY;

    // Properties
    private float width;
    private float height;
    private boolean wall = false;

    // World
    private World world;
    private SpriteBatch sb;


    /**
     * Normally box2d draws shapes such that (x,y) position are the center of the shape.
     * I modified it (inside construct2D()) such that (x.y) are the top left corner of the shapes.
     *
     */
    public Platform(World world, int width, int height, float initVirX, float initVirY) {
        this.world = world;
        this.width = width;
        this.height = height;
        this.sb = GameApp.APP.getBatch();
        this.virX = initVirX;
        this.virY = initVirY;
        //pos.scl(1/PPM); // convert into b2dbox coordinates
        construct2d();

        // set up texture and sprite to enable tinting (.setColor)
        texture = Res.platTexture;
        sprite = new Sprite(texture, width, height);

        // set color
        if (wall)
            sprite.setColor(Color.WHITE);
        else
            sprite.setColor(RED);
    }

    public Platform(World world, int width, int height, float initVirX, float initVirY, boolean wall) {
        this.world = world;
        this.width = width;
        this.height = height;
        this.sb = GameApp.APP.getBatch();
        this.virX = initVirX;
        this.virY = initVirY;
        this.wall = wall;
        construct2d();

        // set up texture and sprite to enable tinting (.setColor)
        texture = Res.platTexture;
        sprite = new Sprite(texture, width, height);

        // set color
        if (wall)
            sprite.setColor(Color.WHITE);
        else
            sprite.setColor(RED);
    }

    private void construct2d() {
        // Define box2d body
        BodyDef bdef = new BodyDef();
        // (width/2)/PPM so that (x,y) are at top left corner, same thing for height
        bdef.position.set(virX/PPM + (width / 2 / PPM), virY/PPM - (height / 2 / PPM));
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        // set shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2 / PPM, height / 2 / PPM); // divided by 2 on purpose

        // Define fixture
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Cons.BIT_PLAT;
        fdef.filter.maskBits = Cons.BIT_PLAYER;

        // Create the actual fixture onto the body
        Fixture fixture = body.createFixture(fdef);
        if (wall)
            fixture.setUserData("Wall"); // to recognize in contact listener
        else
            fixture.setUserData("Platform"); // to recognize in contact listener

    }

    public void update(){
        virX = body.getPosition().x * PPM - width / 2; // revert the additions done when setting position of the body in construct2d()
        virY = body.getPosition().y * PPM + height / 2; //  revert the substraction done when setting position of the body in construct2d()
        sprite.setPosition(virX , virY - height);
        body.setAngularVelocity(0.2f);
        sprite.setRotation((float) Math.toDegrees(body.getAngle())); // so that sprite is attached to body
    }
    public void render(){
        update();
        //sb.setColor(1,0,0,1);
        sprite.draw(sb);
        // sb.draw(texture, body.getPosition().x * 100 - width, body.getPosition().y * 100 - height*2, width*2, height*4);
    }

    public Body getBody() {
        return body;
    }

    public float getX() {
        return virX;
    }

    public float getY() {
        return virY;
    }
}
