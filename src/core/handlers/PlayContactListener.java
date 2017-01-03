package core.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import core.game.GameApp;
import core.objects.Platform;
import core.objects.Player;
import core.screens.PlayScreen;


/**
 * Created by Rafael on 12/28/2016.
 *
 */
public class PlayContactListener implements ContactListener{

    @Override
    public void beginContact(Contact c) {

        // extract colliding features from c
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        // System.out.println("fa: " + fa.getUserData() + " fb: " + fb.getUserData());
        // either fixture might be null sometimes.
        if (fa == null || fb == null) {
            return;
        }
        if (fa.getUserData() == null || fb.getUserData() == null){
            return;
        }


        if (fa.getUserData().equals("Ball") || fb.getUserData().equals("Ball")) {
            if (fa.getUserData().equals("Platform") || fb.getUserData().equals("Platform")) {
                // if here, player collided with a platform.
                //System.out.println("COLLISION, CHANGING TO PLAYSCREEN");
                GameApp.APP.setScreen(new PlayScreen());
            }
        }
    }

    @Override
    public void endContact(Contact c) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
