package com.megabyte.game.Model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ascroggins on 8/21/2015.
 */
public abstract class Entity {
    private Sprite sprite;
    private Vector2 acceleration = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);
    private Rectangle bounds = new Rectangle();

    public void update(float delta) {
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void setX(float x) {
        sprite.setX(x);
    }

    public void setY(float y) {
        sprite.setY(y);
    }

    public Vector2 getPosition() {
        return new Vector2(this.getX(), this.getY());
    }

    public void setPosition(Vector2 position) {
        sprite.setX(position.x);
        sprite.setY(position.y);
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

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
