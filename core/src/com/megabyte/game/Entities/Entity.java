package com.megabyte.game.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by ascroggins on 8/21/2015.
 */
public abstract class Entity {
    private Sprite sprite;

    public float getX() {
        return this.sprite.getX();
    }

    public float getY() {
        return this.sprite.getY();
    }

    public void setX(float x) {
        this.sprite.setX(x);
    }

    public void setY(float y) {
        this.sprite.setY(y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
