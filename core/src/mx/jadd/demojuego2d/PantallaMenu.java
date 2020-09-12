package mx.jadd.demojuego2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaMenu extends Pantalla {

    private final Juego juego; //Para setScreen

    //Crear un fondo y el menú (botones)
    //Fondo
    private Texture texturaFondo;

    //Menú(botones)
    private Stage escenaMenu; //Contenedor de objetos (botones)

    public PantallaMenu(Juego juego) {
        this.juego = juego;
    }

    //Se ejecuta cuando la pantalla se va a mostrar e INICIALIZAMOS los objetos (lleva tiempo)
    @Override
    public void show() {
        texturaFondo = new Texture("fondoMenu.jpg");

        crearMenu();
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);

        //Crear el boton jugar
        Texture texturaBtnJugar = new Texture("botonesMenu/btnJugar.png");
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));

        //Crear boton jugar inverso (feedback)
        Texture texturaBtnJugarInverso = new Texture("botonesMenu/btnJugarInverso.png");
        TextureRegionDrawable trdBtnJugarInverso = new TextureRegionDrawable(new TextureRegion(texturaBtnJugarInverso));

        ImageButton btnJugar = new ImageButton(trdBtnJugar,trdBtnJugarInverso);
        btnJugar.setPosition(ANCHO/2,ALTO/2, Align.center);

        //Programar evento del clic
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cambiamos de pantalla
                juego.setScreen(new PantallaSpaceInvaders(juego));
            }
        });

        escenaMenu.addActor(btnJugar);
        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined); //Escalar correctamente la info.

        batch.begin();
        batch.draw(texturaFondo,0,0);
        batch.end();

        escenaMenu.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondo.dispose();
        batch.dispose();
    }
}