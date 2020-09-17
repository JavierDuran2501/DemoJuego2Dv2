package mx.jadd.demojuego2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Mario extends Objeto {
    private Animation<TextureRegion> animacion;
    private float timerAnimacion;

    public Mario(Texture textura, float x, float y){
        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturasFrame = region.split(32,64);
        //Quieto = IDLE
        sprite = new Sprite(texturasFrame[0][0]);
        sprite.setPosition(x, y);

        //Animación
        TextureRegion[] arrFrames = { texturasFrame[0][1], texturasFrame[0][2], texturasFrame[0][3] };
        animacion = new Animation<TextureRegion>(0.1f, arrFrames);
        timerAnimacion = 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime(); // 1/60
        timerAnimacion += delta;
        TextureRegion frame = animacion.getKeyFrame(timerAnimacion);
        batch.draw(frame, sprite.getX(), sprite.getY());
    }
}