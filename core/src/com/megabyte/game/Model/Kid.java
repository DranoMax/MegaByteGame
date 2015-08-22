package com.megabyte.game.Model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ascroggins on 8/22/2015.
 */
public class Kid extends Entity {

    public static final float SIZE = 0.5f; // half a unit

    public Kid(Vector2 position) {
        super(position, SIZE);
    }

    @Override
    public void loadTextures() {

    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {

    }
}
