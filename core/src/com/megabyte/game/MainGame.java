package com.megabyte.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megabyte.game.Entities.PlayerCharacter;

public class MainGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	SpriteBatch batch;
	PlayerCharacter playerCharacter;

	// Sets up our camera and sets the window to a specific width
	private void setupCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	// Sets up all the graphics + spriteBatch for drawing sprites
	private void setupEntities() {
		batch = new SpriteBatch();
		playerCharacter = new PlayerCharacter(new Sprite(new Texture("badlogic.jpg")));
	}
	
	@Override
	public void create () {
		setupCamera();
		setupEntities();
	}

	@Override
	public void render () {
		// Listen for keyboard events so that we move our PC around
		playerCharacter.update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		playerCharacter.getSprite().draw(batch);
		batch.end();
	}
}
