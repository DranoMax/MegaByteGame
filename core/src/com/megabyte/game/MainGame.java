package com.megabyte.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megabyte.game.Model.World;


public class MainGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private World world;
	SpriteBatch batch;

	// Sets up our camera and sets the window to a specific width
	private void setupCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}
	
	@Override
	public void create () {
		setupCamera();
		batch = new SpriteBatch();
		world = new World();
	}

	@Override
	public void render () {
		world.update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		world.getPlayerCharacter().getSprite().draw(batch);
		world.getMom().getSprite().draw(batch);
		batch.end();
	}
}
