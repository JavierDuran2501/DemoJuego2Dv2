package mx.jadd.demojuego2d;

import com.badlogic.gdx.graphics.Texture;

public class Bala extends Objeto{

    //Físicas *3*
    private final float VELOCIDAD_Y = Pantalla.ALTO/2; //360 pixeles por segundo

    public Bala(Texture textura, float x, float y){
        super(textura, x, y);

    }

    public void mover(float tiempo) {
        float distancia = VELOCIDAD_Y * tiempo; //Pedacito de tiempo [0.01 segundos]
        sprite.setY(sprite.getY() + distancia);
    }
}
