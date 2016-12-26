package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.meteor.MeteorGame;

import handlers.Cons;
import handlers.Res;

import static handlers.Cons.PPM;

/**
 * Created by Rafae on 9/19/2016.
 */
public class Platform {

    // Texture to tint
    private Texture texture;
    private Sprite sprite;

    // Body
    private Body body;


    // Position
    private Vector2 pos; // box2d
    private float x;
    private float y;

    // Properties
    private float width;
    private float height;

    // World
    private World world;
    private SpriteBatch sb;
    private MeteorGame game;

    /**
     * Normally box2d draws shapes such that (x,y) position are the center of the shape.
     * I modified it (inside construct2D()) such that (x.y) are the top left corner of the shapes.
     * @param world
     * @param game
     * @param width
     * @param height
     * @param pos
     */
    public Platform(World world, MeteorGame game, int width, int height, Vector2 pos) {
        this.world = world;
        this.width = width;
        this.height = height;
        //pos.set(pos.x, pos.y);
        this.pos = pos;
        this.sb = game.getBatch();
        this.game = game;
        pos.scl(1/PPM); // convert into b2dbox coordinates
        construct2d();

        // set up texture and sprite to enable tinting (.setColor)
        texture = Res.platTexture;
        sprite = new Sprite(texture, width, height);
        sprite.setPosition(pos.x * PPM, pos.y * PPM - height);
        //sprite.setColor(0,0,0,1);
        sprite.setColor(0,255,255,1);
    }

    private void construct2d() {
        // Define box2d body
        BodyDef bdef = new BodyDef();
        // (width/2)/PPM so that (x,y) are at top left corner, same thing for height
        bdef.position.set(pos.x + (width / 2 / PPM), pos.y - (height / 2 / PPM));
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

    }

    public void render(){
       // sb.begin();
       // sb.draw(texture, body.getPosition().x * 100 - width, body.getPosition().y * 100 - height*2, width*2, height*4);
        sprite.draw(sb);
        //sb.end();
    }

    public Vector2 getPos() {
        return pos;
    }

    public Body getBody() {
        return body;
    }

    public float getX() {
        return pos.x*PPM;
    }

    public float getY() {
        return pos.y*PPM;
    }
}
