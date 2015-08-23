package com.megabyte.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.megabyte.game.Model.Block;
import com.megabyte.game.Model.PlayerCharacter;
import com.megabyte.game.Model.World;

import java.util.HashMap;
import java.util.Map;

public class PlayerCharacterController extends Controller {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS 	= 150l;
    private static final float ACCELERATION 	= 20f;
    private static final float GRAVITY 			= -20f;
    private static final float MAX_JUMP_SPEED	= 7f;
    private static final float DAMP 			= 0.90f;
    private static final float MAX_VEL 			= 4f;

    private PlayerCharacter playerCharacter;
    private long	jumpPressedTime;
    private boolean jumpingPressed;
    private boolean grounded = false;

    // Jumping sound effect
    private Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump_sound.wav"));
    private Array<Sound> touchSound = new Array<Sound>();

    private void setupSound() {
        touchSound.add(Gdx.audio.newSound(Gdx.files.internal("sounds/touch_1.wav")));
        touchSound.add(Gdx.audio.newSound(Gdx.files.internal("sounds/touch_2.wav")));
        touchSound.add(Gdx.audio.newSound(Gdx.files.internal("sounds/touch_3.wav")));
    }

    // This is the rectangle pool used in collision detection
    // Good to avoid instantiation each frame
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    static Map<Keys, Boolean> keys = new HashMap<PlayerCharacterController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    };

    // Blocks that playerCharacter can collide with any given frame
    private Array<Block> collidable = new Array<Block>();

    public PlayerCharacterController(World world) {
        this.setWorld(world);
        this.playerCharacter = world.getPlayerCharacter();
        setupSound();
    }

    // ** Key presses and touches **************** //

    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }

    public void jumpPressed() {
        keys.get(keys.put(Keys.JUMP, true));
    }

    public void firePressed() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    public void leftReleased() {
        keys.get(keys.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        keys.get(keys.put(Keys.RIGHT, false));
    }

    public void jumpReleased() {
        keys.get(keys.put(Keys.JUMP, false));
        jumpingPressed = false;
    }

    public void fireReleased() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    /** The main update method **/
    @Override
    public void update(float delta) {
        // Processing the input - setting the states of playerCharacter
        processInput();

        // If playerCharacter is grounded then reset the state to IDLE
        if (grounded && playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
            playerCharacter.setState(PlayerCharacter.State.IDLE);
        }

        // Setting initial vertical acceleration
        playerCharacter.getAcceleration().y = GRAVITY;

        // Convert acceleration to frame time
        playerCharacter.getAcceleration().scl(delta);

        // apply acceleration to change velocity
        playerCharacter.getVelocity().add(playerCharacter.getAcceleration().x, playerCharacter.getAcceleration().y);

        // checking collisions with the surrounding blocks depending on playerCharacter's velocity
        checkCollisionWithBlocks(delta);

        // apply damping to halt playerCharacter nicely
        playerCharacter.getVelocity().x *= DAMP;

        // ensure terminal velocity is not exceeded
        if (playerCharacter.getVelocity().x > MAX_VEL) {
            playerCharacter.getVelocity().x = MAX_VEL;
        }
        if (playerCharacter.getVelocity().x < -MAX_VEL) {
            playerCharacter.getVelocity().x = -MAX_VEL;
        }

        // simply updates the state time
        playerCharacter.update(delta);

    }

    /** Collision checking **/
    private void checkCollisionWithBlocks(float delta) {
        // scale velocity to frame units
        playerCharacter.getVelocity().scl(delta);

        // Obtain the rectangle from the pool instead of instantiating it
        Rectangle playerCharacterRect = rectPool.obtain();
        // set the rectangle to playerCharacter's bounding box
        playerCharacterRect.set(playerCharacter.getBounds().x, playerCharacter.getBounds().y, playerCharacter.getBounds().width, playerCharacter.getBounds().height);

        // we first check the movement on the horizontal X axis
        int startX, endX;
        int startY = (int) playerCharacter.getBounds().y;
        int endY = (int) (playerCharacter.getBounds().y + playerCharacter.getBounds().height);
        // if playerCharacter is heading left then we check if he collides with the block on his left
        // we check the block on his right otherwise
        if (playerCharacter.getVelocity().x < 0) {
            startX = endX = (int) Math.floor(playerCharacter.getBounds().x + playerCharacter.getVelocity().x);
        } else {
            startX = endX = (int) Math.floor(playerCharacter.getBounds().x + playerCharacter.getBounds().width + playerCharacter.getVelocity().x);
        }

        // get the block(s) playerCharacter can collide with
        populateCollidableBlocks(startX, startY, endX, endY);

        // simulate playerCharacter's movement on the X
        playerCharacterRect.x += playerCharacter.getVelocity().x;

        // clear collision boxes in world
        this.getWorld().getCollisionRects().clear();

        // if playerCharacter collides, make his horizontal velocity 0
        for (Block block : collidable) {
            if (block == null) continue;
            if (playerCharacterRect.overlaps(block.getBounds())) {
                playerCharacter.getVelocity().x = 0;
                this.getWorld().getCollisionRects().add(block.getBounds());
                break;
            }
        }

        // reset the x position of the collision box
        playerCharacterRect.x = playerCharacter.getPosition().x;

        // the same thing but on the vertical Y axis
        startX = (int) playerCharacter.getBounds().x;
        endX = (int) (playerCharacter.getBounds().x + playerCharacter.getBounds().width);
        if (playerCharacter.getVelocity().y < 0) {
            startY = endY = (int) Math.floor(playerCharacter.getBounds().y + playerCharacter.getVelocity().y);
        } else {
            startY = endY = (int) Math.floor(playerCharacter.getBounds().y + playerCharacter.getBounds().height + playerCharacter.getVelocity().y);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        playerCharacterRect.y += playerCharacter.getVelocity().y;

        for (Block block : collidable) {
            if (block == null) continue;
            if (playerCharacterRect.overlaps(block.getBounds())) {
                if (playerCharacter.getVelocity().y < 0) {
                    grounded = true;
                    if (playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                        touchSound.random().play(1.0f);
                    }
                }
                playerCharacter.getVelocity().y = 0;
                this.getWorld().getCollisionRects().add(block.getBounds());
                break;
            }
        }
        // reset the collision box's position on Y
        playerCharacterRect.y = playerCharacter.getPosition().y;

        // update playerCharacter's position
        playerCharacter.getPosition().add(playerCharacter.getVelocity());
        playerCharacter.getBounds().x = playerCharacter.getPosition().x;
        playerCharacter.getBounds().y = playerCharacter.getPosition().y;

        // un-scale velocity (not in frame time)
        playerCharacter.getVelocity().scl(1 / delta);

    }

    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < this.getWorld().getLevel().getWidth() && y >=0 && y < this.getWorld().getLevel().getHeight()) {
                    collidable.add(this.getWorld().getLevel().get(x, y));
                }
            }
        }
    }

    /** Change playerCharacter's state and parameters based on input controls **/
    private boolean processInput() {
        if (keys.get(Keys.JUMP)) {
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                jumpSound.play(0.4f);
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                playerCharacter.setState(PlayerCharacter.State.JUMPING);
                playerCharacter.getVelocity().y = MAX_JUMP_SPEED;
                grounded = false;
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                    jumpingPressed = false;
                } else {
                    if (jumpingPressed) {
                        playerCharacter.getVelocity().y = MAX_JUMP_SPEED;
                    }
                }
            }
        }
        if (keys.get(Keys.LEFT)) {
            // left is pressed
            playerCharacter.setFacingLeft(true);
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                playerCharacter.setState(PlayerCharacter.State.WALKING);
            }
            playerCharacter.getAcceleration().x = -ACCELERATION;
        } else if (keys.get(Keys.RIGHT)) {
            // left is pressed
            playerCharacter.setFacingLeft(false);
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                playerCharacter.setState(PlayerCharacter.State.WALKING);
            }
            playerCharacter.getAcceleration().x = ACCELERATION;
        } else {
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                playerCharacter.setState(PlayerCharacter.State.IDLE);
            }
            playerCharacter.getAcceleration().x = 0;

        }
        return false;
    }

}
