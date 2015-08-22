package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

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
    public static final float SIZE = 0.5f; // half a unit

    private State state = State.IDLE;
    boolean	facingLeft = true;
    private static final long LONG_JUMP_PRESS 	= 150l;
    private static final float ACCELERATION 	= 30f;
    private static final float GRAVITY 			= -20f;
    private static final float MAX_JUMP_SPEED	= 7f;
    private static final float DAMP 			= 0.90f;
    private static final float MAX_VEL 			= 10f;
    private boolean grounded = false;
    // these are temporary
    private static final float WIDTH = 10f;
    private long	jumpPressedTime;
    private boolean jumpingPressed;
    private World world;

    // Blocks that Bob can collide with any given frame
    private Array<Block> collidable = new Array<Block>();

    // This is the rectangle pool used in collision detection
    // Good to avoid instantiation each frame
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };


    public void jumpReleased() {
     //   keys.get(keys.put(Keys.JUMP, false));
        jumpingPressed = false;
    }

    public PlayerCharacter(Sprite sprite, World world) {
        this.setSprite(sprite);
        this.getBounds().x = this.getPosition().x;
        this.getBounds().y = this.getPosition().y;
        this.getBounds().height = SIZE;
        this.getBounds().width = SIZE;

        this.world = world;
    }

    @Override
    public void update(float delta) {
        processInput();

        // If this is grounded then reset the state to IDLE 
        if (grounded && this.getState().equals(State.JUMPING)) {
            this.setState(State.IDLE);
        }

        // Setting initial vertical acceleration 
        this.getAcceleration().y = GRAVITY;

        // Convert acceleration to frame time
        this.getAcceleration().scl(delta);

        // apply acceleration to change velocity
        this.getVelocity().add(this.getAcceleration().x, this.getAcceleration().y);

        // checking collisions with the surrounding blocks depending on this's velocity
        checkCollisionWithBlocks(delta);

        // apply damping to halt this nicely 
        this.getVelocity().x *= DAMP;

        // ensure terminal velocity is not exceeded
        if (this.getVelocity().x > MAX_VEL) {
            this.getVelocity().x = MAX_VEL;
        }
        if (this.getVelocity().x < -MAX_VEL) {
            this.getVelocity().x = -MAX_VEL;
        }
        updatePosition();
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

    /** Collision checking **/
    private void checkCollisionWithBlocks(float delta) {
        // scale velocity to frame units 
        this.getVelocity().scl(delta);

        // Obtain the rectangle from the pool instead of instantiating it
        Rectangle bobRect = rectPool.obtain();
        // set the rectangle to bob's bounding box
        bobRect.set(this.getBounds().x, this.getBounds().y, this.getBounds().width, this.getBounds().height);

        // we first check the movement on the horizontal X axis
        int startX, endX;
        int startY = (int) this.getBounds().y;
        int endY = (int) (this.getBounds().y + this.getBounds().height);
        // if Bob is heading left then we check if he collides with the block on his left
        // we check the block on his right otherwise
        if (this.getVelocity().x < 0) {
            startX = endX = (int) Math.floor(this.getBounds().x + this.getVelocity().x);
        } else {
            startX = endX = (int) Math.floor(this.getBounds().x + this.getBounds().width + this.getVelocity().x);
        }

        // get the block(s) bob can collide with
        populateCollidableBlocks(startX, startY, endX, endY);

        // simulate bob's movement on the X
        bobRect.x += this.getVelocity().x;

        // clear collision boxes in world
        world.getCollisionRects().clear();

        // if bob collides, make his horizontal velocity 0
        for (Block block : collidable) {
            if (block == null) continue;
            if (bobRect.overlaps(block.getBounds())) {
                this.getVelocity().x = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        // reset the x position of the collision box
        bobRect.x = this.getPosition().x;

        // the same thing but on the vertical Y axis
        startX = (int) this.getBounds().x;
        endX = (int) (this.getBounds().x + this.getBounds().width);
        if (this.getVelocity().y < 0) {
            startY = endY = (int) Math.floor(this.getBounds().y + this.getVelocity().y);
        } else {
            startY = endY = (int) Math.floor(this.getBounds().y + this.getBounds().height + this.getVelocity().y);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        bobRect.y += this.getVelocity().y;

        for (Block block : collidable) {
            if (block == null) continue;
            if (bobRect.overlaps(block.getBounds())) {
                if (this.getVelocity().y < 0) {
                    grounded = true;
                }
                this.getVelocity().y = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }
        // reset the collision box's position on Y
        bobRect.y = this.getPosition().y;

        // update Bob's position
        this.getPosition().add(this.getVelocity());
        this.getBounds().x = this.getPosition().x;
        this.getBounds().y = this.getPosition().y;

        // un-scale velocity (not in frame time)
        this.getVelocity().scl(1 / delta);

    }

    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
                   collidable.add(world.getLevel().get(x, y));
                }
            }
        }
    }

    private void updatePosition() {
            this.setPosition(this.getPosition().add(this.getVelocity()));
            this.getBounds().x = this.getPosition().x;
            this.getBounds().y = this.getPosition().y;
        System.out.println(this.getPosition());
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
