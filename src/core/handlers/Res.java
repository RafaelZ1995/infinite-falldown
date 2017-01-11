package core.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import static core.handlers.Cons.VIR_WIDTH;


/**
 * Created by Rafae on 10/5/2016.
 * Load resources such as images for textures and sounds
 */
public class Res {

    private AssetManager assetManager;

    public static Texture platTexture;
    public  static  Texture playerTexture;
    public static Texture arrowTexture;
    public static BitmapFont font128;
    private boolean texturesLoaded = false;

    private TextureAtlas textureAtlas;
    public static TextureRegion platRegion;
    public static TextureRegion arrowRegion;
    public static TextureRegion playerRegion;


    // can i load this statically so I don't actually have to create an instance of Res
    public Res() {

        platTexture = new Texture(Gdx.files.internal("objects/PlatTile.png"));
        arrowTexture = new Texture(Gdx.files.internal("objects/blackArrow.png"));
        playerTexture = new Texture(Gdx.files.internal("balls/ball3.png"));

        //generateFontASyn();
        font128 = new BitmapFont(Gdx.files.internal("fonts/roboto128/Roboto-LightItalic.fnt"));

        textureAtlas = new TextureAtlas(Gdx.files.internal("atlas/yay.atlas"));
        platRegion = textureAtlas.findRegion("PlatTile");
        arrowRegion = textureAtlas.findRegion("blackArrow");
        playerRegion = textureAtlas.findRegion("ball3");

        // using Libgdx's assetManager
        /*
        assetManager = new AssetManager();
        loadFont(); // generates the font Asynchronously
        assetManager.load("blankTile.png", Texture.class);
        assetManager.load("balls/ball3.png", Texture.class);
        assetManager.load("blackArrow.png", Texture.class);
        */

    }

    private void generateFontASyn(){
        // generating a font synchronously
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-LightItalic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128;
        parameter.color = Color.WHITE;
        font128 = generator.generateFont(parameter);
        generator.dispose();
    }

    public void setTexturesFromAssetManager(){
        //.... check if they have been already loaded and then call this method.

        font128 = assetManager.get("fonts/Roboto-LightItalic.ttf");
        platTexture = assetManager.get("blankTile.png", Texture.class);
        playerTexture = assetManager.get("balls/ball3.png", Texture.class);
        arrowTexture = assetManager.get("blackArrow.png", Texture.class);
        texturesLoaded = true;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public boolean isDoneLoading(){
        // check how long it's been
        // i think the problem is that generating the font is still not done
        // asynchronously
        long timeStart = System.currentTimeMillis();
        boolean isDone = assetManager.update();
        long time = System.currentTimeMillis() - timeStart;
        float timeElapsed = time / 1000f;
        System.out.println("time Stuck " + timeElapsed + " secs" + "   milli: " + time );
        return isDone;
    }

    /**
     * In case we want to actually generate the png file from the .ttf files
     */
    private void loadFont() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter loadParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        loadParameter.fontFileName = "fonts/Roboto-LightItalic.ttf";
        loadParameter.fontParameters.size = 128;//VIR_WIDTH / 10; // gonna have to make a function that decides whether to use 128/256/512 for this
        loadParameter.fontParameters.color = Color.WHITE;
        loadParameter.fontParameters.borderColor = Color.RED;
        assetManager.load("fonts/Roboto-LightItalic.ttf", BitmapFont.class, loadParameter);



    }

    public void dispose(){
        //font128.dispose(); // this makes assetManager.dispose() to cause a crash.
        platTexture.dispose();
        platTexture.dispose();
        arrowTexture.dispose();
        //assetManager.dispose(); // i think we need to dipose texts both ways.
    }


    public boolean texturesLoaded() {
        return texturesLoaded;
    }
}