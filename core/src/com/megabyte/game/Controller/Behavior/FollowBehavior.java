package com.megabyte.game.Controller.Behavior;

import com.badlogic.gdx.math.Vector2;
import com.megabyte.game.Controller.Controller;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.PlayerCharacter;

/**
 * Created by ascroggins on 8/23/2015.
 */
public class FollowBehavior extends Behavior {

    private Entity entity;
    private Vector2 moveSpeed;
    private boolean jumpingPressed = false;
    private long	jumpPressedTime;
    private static final float MAX_JUMP_SPEED	= 7f;
    private static final long LONG_JUMP_PRESS 	= 150l;

    public FollowBehavior(Controller controller) {
        this.setController(controller);
        entity = controller.getEntity();
        moveSpeed = new Vector2(2, entity.getVelocity().y);
   //     moveSpeed = new Vector2(2, entity.getVelocity().y);
        entity.setVelocity(moveSpeed.cpy());
        entity.setFacingLeft(false);
        entity.setState(Entity.State.WALKING);
    }

    @Override
    public void execute() {
        PlayerCharacter pc = this.getController().getGameWorld().getPlayerCharacter();
        float pcSize = pc.getSIZE();

        if (entity.getPosition().x < pc.getPosition().x-pcSize/2) {
            entity.getVelocity().x = moveSpeed.cpy().x;
            entity.setFacingLeft(false);
            entity.setState(Entity.State.WALKING);
        } else if (entity.getPosition().x > pc.getPosition().x+pcSize/2){
            entity.getVelocity().x = -moveSpeed.cpy().x;
            entity.setFacingLeft(true);
            entity.setState(Entity.State.WALKING);
        } else {
            // Set the IDLE state if we've slowed down enough (velocity will never be exactly 0)
            if (Math.abs(entity.getVelocity().cpy().x) < 1) {
                entity.setState(Entity.State.IDLE);
            }
        }
    }

    @Override
    public void collideWithWall() {
        entity.getVelocity().x = 0;
        if (!entity.getState().equals(PlayerCharacter.State.JUMPING)) {
            jumpingPressed = true;
            jumpPressedTime = System.currentTimeMillis();
            entity.setState(PlayerCharacter.State.JUMPING);
            entity.getVelocity().y = .05f;
        } else {
            if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                jumpingPressed = false;
            } else {
                if (jumpingPressed) {
                    entity.getVelocity().y = .05f;
                }
            }
        }
    }
}
