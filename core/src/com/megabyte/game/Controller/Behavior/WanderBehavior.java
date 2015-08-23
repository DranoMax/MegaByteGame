package com.megabyte.game.Controller.Behavior;

import com.badlogic.gdx.math.Vector2;
import com.megabyte.game.Model.Entity;

/**
 * Created by ascroggins on 8/23/2015.
 */
public class WanderBehavior extends Behavior {

    public WanderBehavior(Entity entity) {
        this.setEntity(entity);
        entity.setVelocity(new Vector2(2, entity.getVelocity().y));
        entity.setFacingLeft(false);
        entity.setState(Entity.State.WALKING);
    }

    @Override
    /**
     * For wandering behavior, if we hit a wall, we'll simply reverse direction.
     */
    public void collideWithWall(Entity entity) {
        entity.setFacingLeft(!(entity.isFacingLeft()));
        entity.setVelocity(new Vector2(-entity.getVelocity().x, entity.getVelocity().y));
    }
}
