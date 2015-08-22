/**
 * Copyright 2015 SirsiDynix. All rights reserved.
 */
package com.megabyte.game.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Mom extends Entity
{
    public Mom() {
        this.setSprite(new Sprite(new Texture("mom.png")));
    }

    @Override
    public void update(float delta) {
        setX(getX()+1);
    }

}