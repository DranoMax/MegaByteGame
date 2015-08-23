package com.megabyte.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
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

    // Jumping sound effect
    private Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump_sound.wav"));
    private Array<Sound> touchSound = new Array<Sound>();

    private void setupSound() {
        touchSound.add(Gdx.audio.newSound(Gdx.files.internal("sounds/touch_1.wav")));
        touchSound.add(Gdx.audio.newSound(Gdx.files.internal("sounds/touch_2.wav")));
        touchSound.add(Gdx.audio.newSound(Gdx.files.internal("sounds/touch_3.wav")));
    }

    static Map<Keys, Boolean> keys = new HashMap<PlayerCharacterController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    };

    public PlayerCharacterController(World world) {
        super(world.getPlayerCharacter(), world, null);
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
        if (this.isGrounded() && playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
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

    /** Change playerCharacter's state and parameters based on input controls **/
    private boolean processInput() {
        if (keys.get(Keys.JUMP)) {
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                jumpSound.play(0.4f);
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                playerCharacter.setState(PlayerCharacter.State.JUMPING);
                playerCharacter.getVelocity().y = MAX_JUMP_SPEED;
                this.setGrounded(false);
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
