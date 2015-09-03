package com.megabyte.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.GameWorld;
import com.megabyte.game.Model.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;

public class PlayerCharacterController extends Controller {

    enum Keys {
        LEFT, RIGHT, JUMP, ATTACK
    }

    private static final float MAX_VEL = 5f;

    private PlayerCharacter playerCharacter;
    private boolean jumpingPressed;
    private Rectangle attackRectangle;

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
        keys.put(Keys.ATTACK, false);
    };

    public PlayerCharacterController(GameWorld gameWorld) {
        super(gameWorld.getPlayerCharacter(), gameWorld);
        this.playerCharacter = gameWorld.getPlayerCharacter();
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
        keys.get(keys.put(Keys.ATTACK, false));
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
        keys.get(keys.put(Keys.ATTACK, false));
    }

    public void attackPressed(Rectangle attackRectangle) {
        this.attackRectangle = attackRectangle;
        this.playerCharacter.setIsAttacking(true);
    }

    /** The main update method **/
    @Override
    public void update(float delta) {
        // Processing the input - setting the states of playerCharacter
        processInput();

        // If playerCharacter is grounded then reset the state to IDLE
        if (playerCharacter.numFootContacts > 0 && playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
            jumpingPressed = false;
            playerCharacter.setState(PlayerCharacter.State.IDLE);
        }

        // Make sure we don't move too fast
        checkMaxVel();

        // simply updates the state time
        playerCharacter.update(delta);

    }

    /** Change playerCharacter's state and parameters based on input controls **/
    private boolean processInput() {
        Body body = playerCharacter.getBody();
        float impulse = body.getMass() * .4f;

        if (keys.get(Keys.JUMP)) {
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING) && !jumpingPressed) {
                jumpSound.play(0.4f);
                jumpingPressed = true;
                playerCharacter.setState(PlayerCharacter.State.JUMPING);
                body.applyLinearImpulse(new Vector2(0, .3f), body.getWorldCenter(), true);
            }
        }
        if (keys.get(Keys.LEFT)) {
            // left is pressed
            playerCharacter.setFacingLeft(true);
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                playerCharacter.setState(PlayerCharacter.State.WALKING);
            }
            applyHorizontalImpulse(body, -impulse);
        } else if (keys.get(Keys.RIGHT)) {
            // left is pressed
            playerCharacter.setFacingLeft(false);
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                playerCharacter.setState(PlayerCharacter.State.WALKING);
            }
            applyHorizontalImpulse(body, impulse);
        } else {
            if (!playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
                playerCharacter.setState(PlayerCharacter.State.IDLE);
            }
            playerCharacter.getAcceleration().x = 0;

        }

        //I know this shouldn't go here, so get off my case!
        if (attackRectangle != null) {
            //set attack state

            //check if enemy in area
            for (Entity enemy : getGameWorld().getEnemies()) {
                //TODO: use a rectangle pool
                Rectangle enemyRect = new Rectangle(enemy.getBounds().x - playerCharacter.getPosition().x+5, enemy.getBounds().y, enemy.getBounds().width, enemy.getBounds().height);
                boolean hitEnemy = enemyRect.overlaps(attackRectangle);
                if (hitEnemy) {
                    System.out.println("hit");
                }
            }
            attackRectangle = null;
        }
        return false;
    }

    private void applyHorizontalImpulse(Body body, float impulse) {
        body.applyLinearImpulse(new Vector2(impulse, 0), body.getWorldCenter(), true);
    }

    /**
     * Ensure we don't go faster than we're allowed!
     */
    private void checkMaxVel() {
        Body body = playerCharacter.getBody();
        float velY = body.getLinearVelocity().y;

        if (body.getLinearVelocity().x > MAX_VEL) {
            body.setLinearVelocity(MAX_VEL, velY);
        }
        else if (body.getLinearVelocity().x < -MAX_VEL) {
            body.setLinearVelocity(-MAX_VEL, velY);
        }
    }

}
