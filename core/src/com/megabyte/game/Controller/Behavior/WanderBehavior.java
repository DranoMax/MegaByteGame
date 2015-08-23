package com.megabyte.game.Controller.Behavior;

import com.badlogic.gdx.math.Vector2;
import com.megabyte.game.Controller.Controller;
import com.megabyte.game.Model.Entity;

/**
 * Created by ascroggins on 8/23/2015.
 */
public class WanderBehavior extends Behavior {

    private Vector2 moveSpeed;
    private double startTime = 0;
    private double endTime = 0;
    private double waitTime = 0;
    private Entity entity;

    public WanderBehavior(Controller controller) {
        this.setController(controller);
        entity = controller.getEntity();
        moveSpeed = new Vector2(2, entity.getVelocity().y);
        entity.setVelocity(moveSpeed.cpy());
        entity.setFacingLeft(false);
        entity.setState(Entity.State.WALKING);
    }

    @Override
    /**
     * For wandering behavior, if we hit a wall, we'll simply reverse direction.
     */
    public void collideWithWall() {
        entity.setFacingLeft(!(entity.isFacingLeft()));
        entity.setVelocity(new Vector2(-entity.getVelocity().x, entity.getVelocity().y));
    }

    @Override
    /**
     * We will randomly start and stop moving to make the animations more interesting
     */
    public void execute() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
            endTime = startTime + Math.random()*1000+100;
        } else if (waitTime == 0 && startTime > endTime) {
            // Stop moving
            moveSpeed.x = entity.getVelocity().x;
            entity.getVelocity().x = 0;
            entity.setState(Entity.State.IDLE);
            waitTime = startTime + Math.random()*1000+100;
        } else if (waitTime != 0 && startTime > waitTime) {
            waitTime = 0;
            startTime = 0;
          //  randomDirection();
            entity.setState(Entity.State.WALKING);
            entity.setVelocity(moveSpeed.cpy());
        } else {
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Choose left or right.
     */
    private void randomDirection() {
        if (Math.random() == 1) {
            entity.setFacingLeft(!entity.isFacingLeft());
            moveSpeed.x = -moveSpeed.x;
        }
    }

}
