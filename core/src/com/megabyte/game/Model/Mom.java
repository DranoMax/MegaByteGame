/**
 * Copyright 2015 SirsiDynix. All rights reserved.
 */
package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Mom extends Entity
{
    public static final float SIZE = 2f; // half a unit
    private Texture momTexture;

    public Mom(Vector2 position) {
        super(position, SIZE);
    }

    @Override
    public void loadTextures() {
        momTexture = new Texture(Gdx.files.internal("images/mom.png"));
    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        spriteBatch.draw(momTexture, (PLAYER_POSITION_IN_SCREEN+this.getPosition().x) * ppuX-cam.position.x* ppuX, this.getPosition().y * ppuY, SIZE * ppuX, SIZE * ppuY);
    }
}