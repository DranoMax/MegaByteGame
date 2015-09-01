package com.megabyte.game.Screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.megabyte.game.Controller.Behavior.FollowBehavior;
import com.megabyte.game.Controller.NPCController;
import com.megabyte.game.Controller.PlayerCharacterController;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.GameWorld;
import com.megabyte.game.View.WorldRenderer;

import java.util.ArrayList;


public class GameScreen implements Screen, InputProcessor {

    private GameWorld gameWorld;
    private WorldRenderer renderer;
    private PlayerCharacterController playerCharacterController;
    private ArrayList<Entity> enemies;

    private int width, height;

    @Override
    public void show() {
        gameWorld = new GameWorld();
        renderer = new WorldRenderer(gameWorld, false);
        playerCharacterController = new PlayerCharacterController(gameWorld);
        enemies = gameWorld.getEnemies();
        // Update our enemies
        for (Entity enemy : enemies) {
            NPCController controller = new NPCController(enemy, gameWorld);
            enemy.setNpcController(controller);
            enemy.getNpcController().setBehavior(new FollowBehavior(controller));
        }
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playerCharacterController.update(delta);

        // Update our enemies
        for (Entity enemy : enemies) {
            enemy.getNpcController().update(delta);
        }
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    // * InputProcessor methods ***************************//

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.LEFT || keycode == Keys.A)
            playerCharacterController.leftPressed();
        if (keycode == Keys.RIGHT || keycode == Keys.D)
            playerCharacterController.rightPressed();
        if (keycode == Keys.UP || keycode == Keys.SPACE)
            playerCharacterController.jumpPressed();
        if (keycode == Keys.X)
            playerCharacterController.firePressed();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.LEFT || keycode == Keys.A)
            playerCharacterController.leftReleased();
        if (keycode == Keys.RIGHT || keycode == Keys.D)
            playerCharacterController.rightReleased();
        if (keycode == Keys.UP || keycode == Keys.SPACE)
            playerCharacterController.jumpReleased();
        if (keycode == Keys.X)
            playerCharacterController.fireReleased();
        if (keycode == Keys.Z)
            renderer.setDebug(!renderer.isDebug());
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        //TODO: add Android/iOS support
        if (Gdx.app.getType().equals(ApplicationType.Android))
            return false;

        playerCharacterController.attackPressed(renderer.convertScreenRectangleToWorldRectangle(x,y,20,20));
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android))
            return false;
        if (x < width / 2 && y > height / 2) {
            playerCharacterController.leftReleased();
        }
        if (x > width / 2 && y > height / 2) {
            playerCharacterController.rightReleased();
        }
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }


}
