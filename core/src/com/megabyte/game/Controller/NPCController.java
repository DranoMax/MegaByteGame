package com.megabyte.game.Controller;

import com.megabyte.game.Controller.Behavior.Behavior;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.GameWorld;

/**
 * Created by ascroggins on 8/22/2015.
 *
 * My idea for this class is to provide a bunch of behaviors that can be swapped in and out for different
 * enemies at different times.  I believe this is known as the "Adaptor Pattern".
 */
public class NPCController extends Controller {
    // NPC's will have a behavior assigned to them that dictates how the react to certain situations
    private Behavior behavior;
    private Entity entity;
    private static final float GRAVITY 			= -20f;
    private static final float DAMP 			= 0.90f;
    private static final float MAX_VEL 			= 4f;

    public NPCController(Entity npc, GameWorld gameWorld) {
        super(npc, gameWorld);
        entity = npc;
    }

    @Override
    public void update(float delta) {
        this.behavior.execute();

        // Setting initial vertical acceleration
        entity.getAcceleration().y = GRAVITY;

        // Convert acceleration to frame time
        entity.getAcceleration().scl(delta);

        // apply acceleration to change velocity
        entity.getVelocity().add(entity.getAcceleration().x, entity.getAcceleration().y);

        // checking collisions with the surrounding blocks depending on entity's velocity
        checkCollisionWithBlocks(delta);

        // apply damping to halt entity nicely
        entity.getVelocity().x *= DAMP;

        this.checkCollisionWithBlocks(delta);
        entity.update(delta);

    }

    /** Collision checking **/
    public void checkCollisionWithBlocks(float delta) {
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

}
