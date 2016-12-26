package core.handlers;

import com.badlogic.gdx.Gdx;

/**
 * Created by Rafae on 10/4/2016.
 */
public class Cons {
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

}