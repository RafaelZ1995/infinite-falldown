package core.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import core.game.GameApp;

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
    private PlayScreen playScreen;
    private SpriteBatch sb;
    private int score;
    private final float scoreX = VIR_WIDTH/2;
    private final float scoreY = VIR_HEIGHT - VIR_HEIGHT/20;

    public PlayHud(PlayScreen playScreen, SpriteBatch sb) {
        this.playScreen = playScreen;
        this.sb = sb;
    }

    /**
     * The FONT CAMERA is set in the PlayScreen's render method
     */
    public void render(){
        String score = String.valueOf(playScreen.getScore());
        GameApp.font128.draw(sb, score, scoreX - 56, scoreY); // font camera already set
    }

    public void dispose(){

    }
}
