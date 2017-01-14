package core.screens;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import core.game.GameApp;
import core.handlers.Res;

/**
 * Created by Rafae on 1/13/2017.
 */
public class PermanentText {
    private GlyphLayout layout;
    private String text;
    private float x, y;
    public PermanentText(String text, float x, float y) {
        this.text = text;
        layout = new GlyphLayout(Res.font128, text);
        this.x = x - layout.width / 2;
        this.y = y - layout.height / 2;
    }

    public void render(){
        Res.font128.draw(GameApp.APP.getSb(), layout, x, y);
    }
}
