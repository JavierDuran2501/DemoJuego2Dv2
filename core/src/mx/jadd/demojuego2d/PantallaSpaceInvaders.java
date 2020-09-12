package mx.jadd.demojuego2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PantallaSpaceInvaders extends Pantalla {


    private final Juego juego;

    //Aliens - enemigo
    private Array <Alien> arrAliens;
    private float timerAnimaAlien;
    private final float TIEMPO_FRAME_ALIEN = 0.3f;
    private float DX_ALIEN = 10; //Cambia de + a -
    private float DY_ALIEN = 60;
    private int pasoAliens = 10; //0 -> 20, inicia en el centro

    //Nave - personaje
    private Nave nave;
    private Texture texturaNave;

    //Disparo
    private Texture texturabtnDisparo;
    private Bala bala;
    private Texture texturaBala;

    public PantallaSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    //Inicializar objetos
    @Override
    public void show() {
        crearAliens();
        crearNave();
        crearDisparo();
        crearBala(); //Solo crea la textura

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearBala() {
        texturaBala = new Texture("space/bala.png");
    }

    private void crearDisparo() {
        texturabtnDisparo = new Texture("space/shoot.png");
    }

    private void crearNave() {
        texturaNave = new Texture("space/nave.png");
        nave = new Nave(texturaNave, ANCHO/2, ALTO*0.1f);
    }

    private void crearAliens() {
        Texture texturaAlienArriba = new Texture("space/enemigoArriba.png");
        Texture texturaAlienAbajo = new Texture("space/enemigoAbajo.png");
        Texture texturaAlienMuriendo = new Texture("space/enemigoExplota.png");
        arrAliens = new Array<>(12*5);
        for (int i = 0; i<5; i++){ //Renglones, y
            for (int j = 0; j<12; j++) { //Columnas, x
                Alien alien = new Alien(texturaAlienArriba,texturaAlienAbajo,texturaAlienMuriendo, 280 + j*60,350 + i*60);
                arrAliens.add(alien);
            }
        }
    }

    @Override
    public void render(float delta) {
        //Actualizar los oobjetos
        actualizarBala(delta);
        verificarChoques();
        actualizarAliens(delta); //1/60

        borrarPantalla(0.2f,0.2f,0.2f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        //Enemigos
        for (Alien alien : arrAliens) {
            alien.render(batch);
        }

        //Nave
        nave.render(batch);

        //Disparo
        batch.draw(texturabtnDisparo, ANCHO-texturabtnDisparo.getWidth()*1.5f, ALTO*0.2f);

        //Bala
        if(bala != null){
            bala.render(batch);
        }

        batch.end();
    }

    private void actualizarAliens(float delta) {
        timerAnimaAlien += delta;
        if (timerAnimaAlien >= TIEMPO_FRAME_ALIEN){
            //Cambia de estado
            for (Alien alien : arrAliens) {
                if (alien.getEstado() == EstadoAlien.ARRIBA) {
                    alien.setEstado(EstadoAlien.ABAJO);
                } else if (alien.getEstado() == EstadoAlien.ABAJO) {
                    alien.setEstado(EstadoAlien.ARRIBA);
                }
                alien.moverHorizontal(DX_ALIEN);
            }
            timerAnimaAlien = 0; //Reinicia el conteo
            //Quitar aliens muriendo
            for (int i = arrAliens.size-1; i>= 0; i--){
                if (arrAliens.get(i).getEstado()==EstadoAlien.MURIENDO){
                    arrAliens.removeIndex(i);
                }
            }
            //Cuenta pasos
            pasoAliens++;
            if (pasoAliens == 20){
                pasoAliens = 0;
                DX_ALIEN = -DX_ALIEN;
                for (Alien alien : arrAliens) {
                    alien.moverVertical(-DY_ALIEN);
                }
            }
        }
    }

    private void verificarChoques() {
        if(bala != null) {
            for (int i = arrAliens.size - 1; i >= 0; i--) {
                Alien alien = arrAliens.get(i);
                Rectangle rectAlien = alien.sprite.getBoundingRectangle();
                Rectangle rectBala = bala.sprite.getBoundingRectangle();
                if (rectAlien.overlaps(rectBala)) {
                    //Colision
                    if (alien.getEstado()!=EstadoAlien.MURIENDO){
                        //arrAliens.removeIndex(i);
                        alien.setEstado(EstadoAlien.MURIENDO);
                        bala = null;
                        break;
                    }
                }
            }
        }
    }

    private void actualizarBala(float tiempo) {
        if (bala != null){
            bala.mover(tiempo);
            if (bala.sprite.getY() > ALTO) {
                bala = null;
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private class ProcesadorEntrada implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX,screenY,0);
            camara.unproject(v);
            //Tocó el boton disparo?
            float xBoton = ANCHO-texturabtnDisparo.getWidth()*1.5f;
            float yBoton = ALTO*0.2f;
            float anchBoton = texturabtnDisparo.getWidth();
            float altoBoton = texturabtnDisparo.getHeight();

            Rectangle rectBoton = new Rectangle(xBoton,yBoton,anchBoton,altoBoton);
            if(rectBoton.contains(v.x,v.y)){
                //Disparar
                if (bala == null){ //Si la bala no existe
                    bala = new Bala(texturaBala,nave.sprite.getX()+nave.sprite.getWidth()/2, nave.sprite.getY() + nave.sprite.getHeight());
                }
            } else if (v.x<=ANCHO/2) {
                //izq
                nave.moverIzquierda();
            } else {
                //derecha
                nave.moverDerecha();
            }

            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
