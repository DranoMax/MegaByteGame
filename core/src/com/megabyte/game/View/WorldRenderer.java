package com.megabyte.game.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.megabyte.game.Model.Block;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.PlayerCharacter;
import com.megabyte.game.Model.World;

public class WorldRenderer {

    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;

    // The camera always follows the player - this is where the camera focuses in the screen, and thus
    // where the player character will always be drawn as well.
    private static final int PLAYER_POSITION_IN_SCREEN = 5;

    private World world;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private float ppuX;	// pixels per unit on the X axis
    private float ppuY;	// pixels per unit on the Y axis

    public void setSize (int w, int h) {
        ppuX = (float)w / CAMERA_WIDTH;
        ppuY = (float)h / CAMERA_HEIGHT;
    }
    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
    }

    public void render() {
        // We want the camera to follow the player, so we set it to the Player's position.
        cam.position.x = world.getPlayerCharacter().getPosition().x;
        cam.update();
        spriteBatch.begin();
        drawBlocks();
        drawPlayerCharacter();
        drawEnemies();
        spriteBatch.end();
        drawCollisionBlocks();
        if (debug)
            drawDebug();
    }

    private void drawBlocks() {
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            block.drawEntity(spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
        }
    }

    private void drawPlayerCharacter() {
        world.getPlayerCharacter().drawEntity(this.spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
    }

    private void drawEnemies() {
        for (Entity enemy : world.getEnemies()) {
            enemy.drawEntity(spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
        }
    }

    private void drawDebug() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            Rectangle rect = block.getBounds();
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        // render playerCharacter
        PlayerCharacter playerCharacter = world.getPlayerCharacter();
        Rectangle rect = playerCharacter.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        debugRenderer.end();
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 1, 1, 1));
        for (Rectangle rect : world.getCollisionRects()) {
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        debugRenderer.end();
    }

    public Rectangle convertScreenRectangleToWorldRectangle(int x, int y, int width, int height) {
        //TODO: use a rectangle pool
        return new Rectangle(x/ppuX, CAMERA_HEIGHT-y/ppuY, width/ppuX,height/ppuY);
    }
}
