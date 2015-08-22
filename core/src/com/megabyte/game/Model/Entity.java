package com.megabyte.game.Model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ascroggins on 8/21/2015.
 */
public abstract class Entity {

    private Vector2 position = new Vector2();
    private Vector2 acceleration = new Vector2();
    private  Vector2 velocity = new Vector2();
    private Rectangle bounds = new Rectangle();

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    public static final float SIZE = 0.5f; // half a unit
    private State state = State.IDLE;
    boolean facingLeft = true;
    float stateTime = 0;

    public Entity (Vector2 position) {
        this.setPosition(position);
        this.setBounds(new Rectangle(position.x, position.y, SIZE, SIZE));
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public static float getSIZE() {
        return SIZE;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
        this.bounds.setX(position.x);
        this.bounds.setY(position.y);
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public void update(float delta) {
//		position.add(velocity.tmp().mul(delta));
//		bounds.x = position.x;
//		bounds.y = position.y;
        stateTime += delta;
    }
}
