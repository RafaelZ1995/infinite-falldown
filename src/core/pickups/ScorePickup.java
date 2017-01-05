package core.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Rafael on 1/2/2017.
 *
 */
public class ScorePickup extends Pickup {

    /**
     * Constructor
     *
     * @param world
     * @param initVirX
     * @param initVirY
     */
    public ScorePickup(World world, float initVirX, float initVirY) {
        super(world, initVirX, initVirY);
        body.getFixtureList().first().setUserData(this);
    }

    @Override
    void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/ScorePickup.p"), Gdx.files.internal(""));
        effect.getEmitters().first().setPosition(virX, virY);
        effect.scaleEffect(2);
        effect.start();
    }

    @Override
    public void reset() {
        //this.setBodyPosition(0, 0);
    }
}
