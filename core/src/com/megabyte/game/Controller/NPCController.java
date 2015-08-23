package com.megabyte.game.Controller;

import com.megabyte.game.Controller.Behavior.Behavior;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.World;

/**
 * Created by ascroggins on 8/22/2015.
 *
 * My idea for this class is to provide a bunch of behaviors that can be swapped in and out for different
 * enemies at different times.  I believe this is known as the "Adaptor Pattern".
 */
public class NPCController extends Controller {

    public NPCController(Entity npc, World world, Behavior behavior) {
        super(npc, world, behavior);
    }

    @Override
    public void update(float delta) {
        this.checkCollisionWithBlocks(delta);
        this.getEntity().update(delta);

    }

}
