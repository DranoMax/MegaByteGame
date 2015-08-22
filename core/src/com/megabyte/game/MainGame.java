package com.megabyte.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.megabyte.game.Model.Block;
import com.megabyte.game.Model.PlayerCharacter;
import com.megabyte.game.Model.World;


public class MainGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private World world;
	public static int CAMERA_WIDTH = 840;
	public static int CAMERA_HEIGHT = 480;

	SpriteBatch batch;
	/** for debug rendering **/
	ShapeRenderer debugRenderer;

	// Sets up our camera and sets the window to a specific width
	private void setupCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
	}
	
	@Override
	public void create () {
		setupCamera();
		batch = new SpriteBatch();
		debugRenderer = new ShapeRenderer();
		world = new World(CAMERA_WIDTH, CAMERA_HEIGHT);
	}

	@Override
	public void render () {
		world.update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		drawDebug();
		batch.begin();
		world.getPlayerCharacter().getSprite().draw(batch);
		world.getMom().getSprite().draw(batch);
		batch.end();
	}

	private void drawDebug() {
		// render blocks
		debugRenderer.setProjectionMatrix(camera.combined);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		for (Block block : world.getDrawableBlocks(CAMERA_WIDTH, CAMERA_HEIGHT)) {
			Rectangle rect = block.getBounds();
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
		// render Bob
		PlayerCharacter bob = world.getPlayerCharacter();
		Rectangle rect = bob.getBounds();
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		debugRenderer.end();
	}
}
