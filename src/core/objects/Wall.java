package core.objects;

import com.badlogic.gdx.physics.box2d.World;

import core.handlers.Cons;

/**
 * Created by Rafae on 1/1/2017.
 */
public class Wall extends Box2dPlat {
    public Wall(World world, int width, int height, float initVirX, float initVirY) {
        super(world, width, height, initVirX, initVirY);
        body.getFixtureList().first().setUserData("Wall");
        //sprite.setColor();
    }
}
