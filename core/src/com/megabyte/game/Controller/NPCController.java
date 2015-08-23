package com.megabyte.game.Controller;

import com.badlogic.gdx.math.Vector2;
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
        npc.setVelocity(new Vector2(2, npc.getVelocity().y));
    }

    @Override
    public void update(float delta) {
        this.checkCollisionWithBlocks(delta);
    }

}
