package com.megabyte.game.Controller.Behavior;

import com.megabyte.game.Model.Entity;

/**
 * Created by ascroggins on 8/23/2015.
 */
public abstract class Behavior {

    private Entity entity;

    /**
     * The base method to be called for updating state and executing AI actions
     */
    public abstract void execute();
    public abstract void collideWithWall();

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
