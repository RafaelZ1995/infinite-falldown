package core.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import core.handlers.Cons;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class Plat extends Box2dPlat {


    public Plat(World world, int width, int height, float initVirX, float initVirY){
        super(world, width, height, initVirX, initVirY);
        body.getFixtureList().first().setUserData("Platform");
        //sprite.setColor(Cons.RED);

    }


}
