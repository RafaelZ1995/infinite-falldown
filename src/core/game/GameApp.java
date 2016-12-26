package core.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import core.handlers.Res;
import core.screens.MainScreen;
import core.screens.PlayScreen;

public class GameApp extends Game {

    // private
	private SpriteBatch batch;
    private OrthographicCamera cam;

    // Helpers
    private int WIDTH;
    private int HEIGHT;

    // creating play button
	Stage stage;
	Skin skin;
    TextureAtlas buttonAtlas;
    Button playButton;
    Button.ButtonStyle buttonStyle;

    // background
    Image bg;
    Image star;
    // there is no point in making separate actors for the title/eachstar...
    // just make all that part of the background. so basically do all that
    // manually in paint.net/inkscape. It wouldn't really matter. Only
    // things that the player actually interacts with need to be actors
    // other wise it can all just be a png file.


    private GameApp game;
    private Res res;

	@Override
	public void create () {
        WIDTH = Gdx.graphics.getWidth();  // 1080
        HEIGHT = Gdx.graphics.getHeight(); // 1920
		batch = new SpriteBatch();
        game = this;

        // initialize main screen
        this.setScreen(new MainScreen(this));
        cam = new OrthographicCamera();
        cam.setToOrtho(false, WIDTH * 2, HEIGHT * 2);

        res = new Res(); // just to actually load the textures in Res()

        /*
        // start
        stage = new Stage();
        Texture bgtext = new Texture("bgtwist.png");
        bg = new Image(bgtext);
        bg.setWidth(WIDTH);
        bg.setHeight(HEIGHT);
        bg.setColor(Color.GRAY);
        stage.addActor(bg);

        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/button.pack");
        skin.addRegions(buttonAtlas);
        buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.getDrawable("playbutton1.up");
        buttonStyle.over = skin.getDrawable("playbutton1.down");
        buttonStyle.down = skin.getDrawable("playbutton1.down");

        // configure the play button
        playButton = new Button(buttonStyle);
        playButton.setHeight(WIDTH / 3);
        playButton.setWidth(WIDTH / 3);
        playButton.setX(WIDTH / 2 - playButton.getWidth()/2);
        playButton.setY(HEIGHT / 4 - playButton.getHeight()/2);
        playButton.setColor(Color.LIGHT_GRAY);
        playButton.setRotation(20);
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);

        // set up what to do when playbutton is clicked
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PlayScreen(game));
                System.out.println("clicked");
                return true;
            }
        });

        Texture starText = new Texture("star1.png");
        star = new Image(starText);
        star.setWidth(WIDTH / 6);
        star.setHeight(WIDTH / 6);
        star.setPosition(WIDTH/2 + 20, HEIGHT - HEIGHT/4);
        stage.addActor(star);
        */
	}

		@Override
		public void render () {
			//Gdx.gl.glClearColor(0, 0, 0, 1);
			//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            super.render();


            //stage.act();

			//batch.begin();

            //stage.draw();
			//batch.end();
		}

		@Override
		public void dispose () {
		batch.dispose();
	}

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCam() {
        return cam;
    }
}
