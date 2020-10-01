package mx.jadd.demojuego2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaRunner extends Pantalla {
    private Juego juego;

    //Personaje / Mario
    private Mario mario;
    private Texture texturaMario;

    //Bolas de fuego
    private Texture texturaBolaFuego;
    private Array<BolaFuego> arrBolasFuego;

    //Enemigo
    private Goomba goomba;
    private Texture texturaGoomba;

    //Fondo
    private Texture texturaFondo;
    private float xFondo = 0;

    //Enemigos
    private Array<Goomba> arrEnemigos;
    private float timerCrearEnemigo;
    private float TIEMP_CREA_ENEMIGO = 1; //Variable; El primer enemigo aparecerá tras 1 seg
    private float tiempoBase = 1;

    //Texto
    private Texto texto; //Dibuja textos en la pantalla
    private float puntos;

    public PantallaRunner(Juego juego) {
        this.juego = juego;
    }

    //HUD
    private Stage escenaHUD; //Tendremos el pad, boton disparo, marcador, etc.
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    @Override
    public void show() {
        crearMario();
        crearFondo();
        crearGoomba();
        crearEnemigos();
        crearBolasFuego();
        crearTexto();
        cargarPuntos();
        crearHUD();

        //Gdx.input.setInputProcessor(new ProcesadorEntrada());
        Gdx.input.setInputProcessor(escenaHUD);
    }

    private void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO,ALTO,camaraHUD);

        //Crear escena
        escenaHUD = new Stage(vistaHUD);
        // Crea el pad
        Skin skin = new Skin(); // Texturas para el pad
        skin.add("fondo", new Texture("runner/fondoPad.png"));
        skin.add("boton", new Texture("runner/botonPad.png"));
        // Configura la vista del pad
        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("fondo");
        estilo.knob = skin.getDrawable("boton");
        // Crea el pad
        Touchpad pad = new Touchpad(64,estilo);     // Radio, estilo
        pad.setBounds(16,16,256,256);               // x,y - ancho,alto
        // Comportamiento del pad
        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad)actor;
                if (pad.getKnobPercentX() > 0.20) { // Más de 20% de desplazamiento DERECHA
                    //mario.setEstadoMover(Personaje.EstadoMovimento.DERECHA);
                    mario.sprite.setX(mario.sprite.getX()+10);
                } else if ( pad.getKnobPercentX() < -0.20 ) {   // Más de 20% IZQUIERDA
                    //mario.setEstadoMover(Personaje.EstadoMovimento.IZQUIERDA);
                    mario.sprite.setX(mario.sprite.getX()-10);
                } else {
                    //mario.setEstadoMover(Personaje.EstadoMovimento.QUIETO);
                }
                //Y
                if (pad.getKnobPercentY()>0.5){
                    mario.saltar();
                }
            }
        });
        pad.setColor(1,1,1,0.7f);   // Transparente
        // Crea la escena y agrega el pad

        escenaHUD.addActor(pad);
    }

    private void cargarPuntos() {
        Preferences prefs = Gdx.app.getPreferences("marcadores");
        puntos = prefs.getFloat("PUNTOS",0);
    }

    private void crearTexto() {
        texto = new Texto("runner/game.fnt");
    }

    private void crearBolasFuego() {
        texturaBolaFuego = new Texture("runner/bolaFuego.png");
        arrBolasFuego = new Array<>();
    }

    private void crearEnemigos() {
        texturaGoomba = new Texture("runner/goomba.png");
        arrEnemigos = new Array<>();

    }

    private void crearGoomba() {
        texturaGoomba = new Texture("runner/goomba.png");
        goomba = new Goomba(texturaGoomba,ANCHO*0.75f,60);
    }

    private void crearFondo() {
        texturaFondo = new Texture("runner/fondoMario_5.jpg");
    }

    private void crearMario() {
        texturaMario = new Texture("runner/marioSprite.png");
        mario = new Mario(texturaMario,ANCHO/2,59);
    }

    @Override
    public void render(float delta) {
        actualizar();
        borrarPantalla(0,0,0.5f);
        batch.setProjectionMatrix(camara.combined);


        batch.begin();
        batch.draw(texturaFondo,xFondo,0);
        batch.draw(texturaFondo,xFondo + texturaFondo.getWidth(),0);
        mario.render(batch);
        goomba.render(batch);
        dibujarEnemigos();
        dibujarBolasFuego();
        dibujarTexto();
        batch.end();

        //HUD *************************************************************************
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
    }

    private void dibujarTexto() {
        texto.mostrarMensaje(batch, "Super Mario Tec", ANCHO/2, 0.9f*ALTO);
        //puntos += Gdx.graphics.getDeltaTime();
        int puntosInt = (int)puntos;
        texto.mostrarMensaje(batch, "" + puntosInt+"",ANCHO/2*0.1f,0.9F*ALTO);
    }

    private void dibujarBolasFuego() {
        for (BolaFuego bola:
             arrBolasFuego) {
            bola.render(batch);
        }
    }

    private void dibujarEnemigos() {
        for (Goomba goomba :
                arrEnemigos) {
            goomba.render(batch);
            //NOOOOOOOOOOOOOOOOOOOOOOO
            goomba.moverIzquierda();
        }
    }

    private void actualizar() {
        /*xFondo-=5;
        if (xFondo == -texturaFondo.getWidth()){
            xFondo = 0;
        }*/

        actualizarMario();
        actualizarCamara();
        actualizarEnemigos();
        actualizarBolasFuego();

        //Colisiones
        verificarColisiones(); //Choque entre bola de fuego - enemigo
        verificarChoqueEnemigosPersonaje();
    }

    private void verificarChoqueEnemigosPersonaje() {
        for (int i = arrEnemigos.size-1; i >= 0; i--) {
            if (mario.sprite.getBoundingRectangle().overlaps(goomba.sprite.getBoundingRectangle())){
                //Perdió o le quita vida
                mario.sprite.setY(ALTO);
                arrEnemigos.removeIndex(i);
                break;
            }
        }
    }

    private void verificarColisiones() {
        for (int i = arrBolasFuego.size-1; i >= 0 ; i--) {
            BolaFuego bola = arrBolasFuego.get(i);
            for (int j = arrEnemigos.size-1; j >= 0 ; j--) {
                Goomba goomba = arrEnemigos.get(j);
                if (bola.sprite.getBoundingRectangle().overlaps(goomba.sprite.getBoundingRectangle())){
                    //Si es cierto, hay colisión
                    arrEnemigos.removeIndex(j);
                    arrBolasFuego.removeIndex(i);
                    //Contar puntos
                    puntos += 25;
                    guardarPreferencias();
                    break;
                }
            }
        }
    }

    private void guardarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("marcadores");
        prefs.putFloat("PUNTOS", puntos);
        prefs.flush(); //Obligatorio cuando se guardan preferencias.
    }

    private void actualizarBolasFuego() {
        for (int i = arrBolasFuego.size-1; i >=0 ; i--) {
            BolaFuego bola = arrBolasFuego.get(i);
            bola.moverDerecha();
            if (bola.sprite.getX()>ANCHO){
                arrBolasFuego.removeIndex(i);
            }
        }
    }

    private void actualizarEnemigos() {
        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if (timerCrearEnemigo >= TIEMP_CREA_ENEMIGO){
            timerCrearEnemigo = 0;
            TIEMP_CREA_ENEMIGO = tiempoBase + MathUtils.random()*2; //Genrra tiempos entre 1 y 3
            if (tiempoBase>0) {
                tiempoBase -= 0.01f;
            }
            Goomba goomba = new Goomba(texturaGoomba,ANCHO,60 + MathUtils.random(0,2)*60); //Alturas de 60,120 y 180
            arrEnemigos.add(goomba);
        }
        for (int i = arrEnemigos.size-1; i >= 0; i--) {
            Goomba goomba = arrEnemigos.get(i);
            if (goomba.sprite.getX()<-goomba.sprite.getWidth()){
                arrEnemigos.removeIndex(i);
            }
        }
    }

    private void actualizarMario() {
        //mario.sprite.setX(mario.sprite.getX()+2);
    }

    private void actualizarCamara() {
        float xCamara = camara.position.x;
        //xCamara++;
        xCamara = mario.sprite.getX();
        camara.position.x = xCamara;
        camara.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private class ProcesadorEntrada implements InputProcessor {

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX,screenY,0);
            camara.unproject(v);
            if (v.x < ANCHO/2 && mario.getEstado()==EstadoMario.CAMINANDO){
                //Salta
                mario.saltar();
            } else if (v.x >= ANCHO/2){
                //Disparar
                if (arrBolasFuego.size < 20) {
                    BolaFuego bolaFuego = new BolaFuego(texturaBolaFuego, mario.sprite.getX(), mario.sprite.getY() + mario.sprite.getHeight()*0.5f);
                    arrBolasFuego.add(bolaFuego);
                }
            }
            return false;
        }

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
