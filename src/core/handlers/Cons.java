package core.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import core.game.GameApp;

/**
 * Created by Rafael on 10/4/2016.
 *
 */
public class Cons {

    // GameAPP will be a final constant
   // public static final GameApp APP = GameApp.game;

    // box2d works better with fixed time steps
    public static final float STEP = 1 / 60f;

    // Scale for ortho cameras
    public static final int SCALE = 2;

    // Width and Height but times the SCALE
    public static final int VIR_WIDTH = Gdx.graphics.getWidth() * SCALE;
    public static final int VIR_HEIGHT = Gdx.graphics.getHeight() * SCALE;

    // pixels per meter
    public static final float PPM = 100;

    // categorybits
    public static final short BIT_PLAYER = 2;
    public static final short BIT_PLAT = 4;

    // Player Diameter
    public static final int BALL_DIAM = (VIR_WIDTH / 25)*2;
    public static final int BALL_FALL_SPEED = -7;

    // Color Pallete
    public static final Color RED = new Color(255/255f,0,0,1); // platforms
    public static final Color BLUE = new Color(18/255f,1/255f,189/255f,1);
    public static final Color YELLOW = new Color(216/255f,168/255f,0/255f,1);

}