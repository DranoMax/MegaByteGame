package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by ascroggins on 8/21/2015.
 *
 * A lot of this code (specifically the jump code) came from this project: http://www.javacodegeeks.com/2013/03/android-game-development-with-libgdx-jumping-gravity-and-improved-movement-part-3.html
 * Big thanks!
 */
public class PlayerCharacter extends Entity{

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }
    private State state = State.IDLE;
    boolean	facingLeft = true;
    private static final long LONG_JUMP_PRESS 	= 150l;
    private static final float ACCELERATION 	= 30f;
    private static final float GRAVITY 			= -20f;
    private static final float MAX_JUMP_SPEED	= 7f;
    private static final float DAMP 			= 0.90f;
    private static final float MAX_VEL 			= 10f;

    // these are temporary
    private static final float WIDTH = 10f;
    private long	jumpPressedTime;
    private boolean jumpingPressed;


    public void jumpReleased() {
     //   keys.get(keys.put(Keys.JUMP, false));
        jumpingPressed = false;
    }

    @Override
    public void update(float delta) {
        processInput();

        this.getAcceleration().y = GRAVITY;
        this.getAcceleration().scl(delta);
        this.getVelocity().add(this.getAcceleration().x, this.getAcceleration().y);
        if (this.getAcceleration().x == 0) this.getVelocity().x *= DAMP;
        if (this.getVelocity().x > MAX_VEL) {
            this.getVelocity().x = MAX_VEL;
        }
        if (this.getVelocity().x < -MAX_VEL) {
            this.getVelocity().x = -MAX_VEL;
        }

        // Get Keyboard updates
        this.updatePosition();

        // Don't fall through the floor!
        if (this.getPosition().y < 0) {
            this.setY(0f);
            if (this.getState().equals(State.JUMPING)) {
                this.setState(State.IDLE);
            }
        }
        // Don't run through the left wall!
        if (this.getPosition().x < 0) {
            this.getPosition().x = 0;
            this.setPosition(this.getPosition());
            if (!this.getState().equals(State.JUMPING)) {
                this.setState(State.IDLE);
            }
        }
        // Don't run through the right wall!
        if (this.getPosition().x > WIDTH ) {
            this.getPosition().x = WIDTH ;
            this.setPosition(this.getPosition());
            if (!this.getState().equals(State.JUMPING)) {
                this.setState(State.IDLE);
            }
        }
    }

    /** Change this's state and parameters based on input controls **/
    private boolean processInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (!this.getState().equals(State.JUMPING)) {
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                this.setState(State.JUMPING);
                this.getVelocity().y = MAX_JUMP_SPEED;
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                    jumpingPressed = false;
                } else {
                    if (jumpingPressed) {
                        this.getVelocity().y = MAX_JUMP_SPEED;
                    }
                }
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // left is pressed
            this.setFacingLeft(true);
            if (!this.getState().equals(State.JUMPING)) {
                this.setState(State.WALKING);
            }
            this.getAcceleration().x = -ACCELERATION;
        } else if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // left is pressed
            this.setFacingLeft(false);
            if (!this.getState().equals(State.JUMPING)) {
                this.setState(State.WALKING);
            }
            this.getAcceleration().x = ACCELERATION;
        } else {
            if (!this.getState().equals(State.JUMPING)) {
                this.setState(State.IDLE);
            }
            this.getAcceleration().x = 0;

        }
        return false;
    }

    public PlayerCharacter(Sprite sprite) {
        this.setSprite(sprite);
    }


    private void updatePosition() {
            this.setPosition(this.getPosition().add(this.getVelocity()));
//            bounds.x = position.x;
//            bounds.y = position.y;
//            stateTime += delta;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
}
