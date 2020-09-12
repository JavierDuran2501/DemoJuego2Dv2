package mx.jadd.demojuego2d;

import com.badlogic.gdx.graphics.Texture;

public class Alien extends Objeto {

    private Texture texturaArriba;
    private Texture texturaAbajo;
    private Texture texturaMuriendo;

    private EstadoAlien estado; //Condicion actual del objeto (manos arriba, abajo y muriendo)

    public Alien(Texture textura, float x, float y){
        super(textura, x, y);
        estado = EstadoAlien.ARRIBA;
    }

    public Alien(Texture texturaArriba, Texture texturaAbajo, Texture texturaMuriendo, float x, float y){
        super(texturaArriba, x, y);
        this.texturaArriba = texturaArriba;
        this.texturaAbajo = texturaAbajo;
        this.texturaMuriendo = texturaMuriendo;
        estado = EstadoAlien.ARRIBA;
    }

    public EstadoAlien getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlien estado) {
        this.estado = estado;
        switch (estado){
            case ARRIBA:
                sprite.setTexture(texturaArriba);
                break;
            case ABAJO:
                sprite.setTexture(texturaAbajo);
                break;
            case MURIENDO:
                sprite.setTexture(texturaMuriendo);
                break;
        }
    }

    public void moverHorizontal(float dx_alien) {
        sprite.setX(sprite.getX() + dx_alien);
    }

    public void moverVertical(float dy_alien) {
        sprite.setY(sprite.getY() + dy_alien);
    }
}
