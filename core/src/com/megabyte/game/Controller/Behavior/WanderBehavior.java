package com.megabyte.game.Controller.Behavior;

import com.badlogic.gdx.math.Vector2;
import com.megabyte.game.Model.Entity;

/**
 * Created by ascroggins on 8/23/2015.
 */
public class WanderBehavior extends Behavior {

    @Override
    /**
     * For wandering behavior, if we hit a wall, we'll simply reverse direction.
     */
    public void collideWithWall(Entity entity) {
        entity.setVelocity(new Vector2(-entity.getVelocity().x, entity.getVelocity().y));
    }
}
