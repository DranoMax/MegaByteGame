package com.megabyte.game.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ascroggins on 8/21/2015.
 */
public abstract class Entity {
    private Sprite sprite;
    private Vector2 acceleration = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);

    public void update(float delta) {
    }

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

    public Vector2 getPosition() {
        return new Vector2(this.getX(), this.getY());
    }

    public void setPosition(Vector2 position) {
        this.sprite.setPosition(position.x, position.y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
