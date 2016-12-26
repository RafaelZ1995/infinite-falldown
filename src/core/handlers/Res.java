package core.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Rafae on 10/5/2016.
 * Load resources such as images for textures and sounds
 */
public class Res {
    public static Texture platTexture;
    public  static  Texture playerTexture;

    // can i load this statically so I don't actually have to create an instance of Res
    public Res() {
        platTexture = new Texture(Gdx.files.internal("blankTile.png"));
        playerTexture = new Texture(Gdx.files.internal("ball.png"));
    }
}
