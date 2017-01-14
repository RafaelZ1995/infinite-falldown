package core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import core.handlers.Cons;
import core.handlers.Res;

import static core.handlers.Cons.PLAT_HEIGHT;
import static core.handlers.Cons.PLAT_WIDTH;
import static core.handlers.Cons.PPM;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class Plat extends Box2dPlat {

    private ParticleEffect effect;


    public Plat(World world, int width, int height, float initVirX, float initVirY){
        super(world, width, height, initVirX, initVirY);
        body.getFixtureList().first().setUserData("Platform");
        sprite = new Sprite(Res.platRegion);//new Sprite(Res.platTexture, width, height);
        sprite.setColor(Color.WHITE);
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2, height / 2); // needed for sprite to stay on body when rotating
        initParticleEffect();
    }

    void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/platEffect.p"), Gdx.files.internal("objects/"));
        effect.getEmitters().first().setPosition(virX, virY);
        effect.getEmitters().first().getScale().setHigh(PLAT_WIDTH);
        effect.getEmitters().first().getTint().setColors(new float[]{1f, 1f, 1f});
        //effect.getEmitters().first().getRotation().setHigh(body.getAngle());
        //effect.scaleEffect(2);
        effect.start();
    }

    private void updateEffect() {
        if (effect.isComplete())
            effect.reset();
        effect.setPosition(virX + PLAT_WIDTH / 2, virY - PLAT_HEIGHT);
        effect.update(Gdx.graphics.getDeltaTime());
        effect.getEmitters().first().getRotation().setHigh(sprite.getRotation()); // for rotation to work, the rotation button in the particle2d tool has to be on
    }

    public void update() {
        updateEffect();
        virX = body.getPosition().x * PPM - width / 2; // revert the additions done when setting position of the body in construct2d()
        virY = body.getPosition().y * PPM + height / 2; //  revert the substraction done when setting position of the body in construct2d()
        sprite.setPosition(virX, virY - height);
        sprite.setRotation((float) Math.toDegrees(body.getAngle())); // so that sprite is attached to body
    }

    public void render() {
        update();
        sprite.draw(sb);
        effect.draw(sb, Gdx.graphics.getDeltaTime());
    }


    @Override
    public void reset() {
        body.setAngularVelocity(0);
    }
}
