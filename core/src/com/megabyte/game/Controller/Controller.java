package com.megabyte.game.Controller;

import com.megabyte.game.Model.World;

/**
 * Created by ascroggins on 8/22/2015.
 */
public abstract class Controller {

    private World world;

    public abstract void update(float delta);

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
