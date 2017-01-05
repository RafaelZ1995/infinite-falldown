package core.Hud;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import core.game.GameApp;
import core.screens.PlayScreen;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafae on 12/26/2016.
 *
 * @@ YOU MOST DEFINITELY NEED TO INITIALIZE THE BMFONT IN THE GAME CLASS NOT HERE.
 *
 * Class will handle the Score, pause button, and any other things that would be considered
 * part of the "hud" in the playscreen
 */
public class PlayHud {

    // private
    private PlayScreen playScreen; // ((PlayScreen) (APP.getScreen())).getScore() could get like this
    private SpriteBatch sb;
    private int score;
    private final float scoreX = VIR_WIDTH/2;
    private final float scoreY = VIR_HEIGHT - VIR_HEIGHT/10;

    // add in ArrowCharger and Rain here?

    // arrow charger
   // private ArrowCharger arrowCharger;

    public PlayHud() {
        this.sb = GameApp.APP.getBatch();
        //arrowCharger = new ArrowCharger();
    }

    /**
     * The FONT CAMERA is set in the PlayScreen's render method
     * still gotta make that 56 in a relation to VIR width or height
     */
    public void render(){
        int depth = ((PlayScreen) (GameApp.APP.getScreen())).getScore();
        String depthString = String.valueOf(depth);
        GameApp.font128.draw(sb, depthString, scoreX - 56, scoreY); // font camera already set

        //arrowCharger.render();
    }

    public void dispose(){

    }
}
