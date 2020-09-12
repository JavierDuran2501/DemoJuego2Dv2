package mx.jadd.demojuego2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*
Juego principal (aplicación)
Autor: Javier Durán
 */

public class Juego extends Game{
	
	@Override
	public void create () {
		//Poner la primer ventana
		setScreen(new PantallaMenu(this)); //Pasamos el controlador para que despues el controlador haga .setScreen()
	}

	@Override
	public void render () {
		super.render();
	}

}
