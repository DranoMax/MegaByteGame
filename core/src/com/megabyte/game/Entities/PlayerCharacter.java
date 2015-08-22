package com.megabyte.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by ascroggins on 8/21/2015.
 */
public class PlayerCharacter extends Entity{

    public PlayerCharacter(Sprite sprite) {
        this.setSprite(sprite);
    }

    public void keyboardListener() {
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.setY(this.getY() + 1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.setX(this.getX() - 1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.setY(this.getY() - 1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.setX(this.getX() + 1);
        }
    }
}
