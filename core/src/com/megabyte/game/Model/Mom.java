/**
 * Copyright 2015 SirsiDynix. All rights reserved.
 */
package com.megabyte.game.Model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Mom extends Entity
{
    public static final float SIZE = 0.5f; // half a unit

    public Mom(Vector2 position) {
     //   this.setSprite(new Sprite(new Texture("mom.png")));
        super(position, SIZE);
    }

    @Override
    public void loadTextures() {

    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {

    }

//    @Override
   // public void update(float delta) {
  //      setX(getX()+1);
 //   }

}