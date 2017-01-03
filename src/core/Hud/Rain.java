package core.Hud;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.g2d.ParticleEffect;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;

        import core.game.GameApp;

        import static core.handlers.Cons.VIR_HEIGHT;
        import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafael on 12/30/2016.
 *
 */
public class Rain {

    //private
    private ParticleEffect effect;
    private float effectWidth = VIR_WIDTH;
    private float effectHeight = VIR_HEIGHT / 5;
    private float effectSize= VIR_WIDTH / 60;

    // make them final in Cons later
    private float x = VIR_WIDTH / 2;
    private float y = effectHeight / 2;

    private SpriteBatch sb;
    private String particlePath = "particles/rain.p";

    public Rain() {
        sb = GameApp.APP.getBatch();
        initParticleEffect();
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(particlePath), Gdx.files.internal(""));
        effect.getEmitters().first().setPosition(x, y);
        effect.getEmitters().first().getScale().setHigh(effectSize);
        effect.getEmitters().first().getSpawnWidth().setHigh(effectWidth);
        effect.getEmitters().first().getSpawnHeight().setHigh(effectHeight);
        effect.start();
    }

    private void update(){
        if (effect.isComplete())
            effect.reset();

        effect.update(Gdx.graphics.getDeltaTime());
    }

    public void render(){
        update();
        effect.draw(sb, Gdx.graphics.getDeltaTime());
    }
}
