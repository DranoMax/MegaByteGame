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

        // If entity is grounded then reset the state to IDLE
        if (!entity.getState().equals(Entity.State.JUMPING)) {
            entity.setState(Entity.State.IDLE);
        }

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
//        Entity entity = this.getEntity();
//
//        // scale velocity to frame units
//        entity.getVelocity().scl(delta);
//
//        // Obtain the rectangle from the pool instead of instantiating it
//        Rectangle npcRect = this.getRectPool().obtain();
//        // set the rectangle to entity's bounding box
//        npcRect.set(entity.getBounds().x, entity.getBounds().y, entity.getBounds().width, entity.getBounds().height);
//
//        // we first check the movement on the horizontal X axis
//        int startX, endX;
//        int startY = (int) entity.getBounds().y;
//        int endY = (int) (entity.getBounds().y + entity.getBounds().height);
//        // if entity is heading left then we check if he collides with the block on his left
//        // we check the block on his right otherwise
//        if (entity.getVelocity().x < 0) {
//            startX = endX = (int) Math.floor(entity.getBounds().x + entity.getVelocity().x);
//        } else {
//            startX = endX = (int) Math.floor(entity.getBounds().x + entity.getBounds().width + entity.getVelocity().x);
//        }
//
//        // get the block(s) entity can collide with
//        this.populateCollidableBlocks(startX, startY, endX, endY);
//
//        // simulate entity's movement on the X
//        npcRect.x += entity.getVelocity().x;
//
//        // clear collision boxes in world
//        this.getGameWorld().getCollisionRects().clear();
//
//        // if entity collides, make his horizontal velocity 0
//        for (Block block : this.getCollidable()) {
//            if (block == null) continue;
//            if (npcRect.overlaps(block.getBounds())) {
//
//                /**
//                 * What should we do when we hit a wall?  Some NPC's might try to jump over it.  Some might reverse direction.
//                 * Let's see what our behavior tells us to do.
//                 */
//                if (behavior != null) {
//                    behavior.collideWithWall();
//                } else {
//                    // The player is controlling this entity, so we don't want to do anything special.
//                    entity.getVelocity().x = 0;
//                }
//                this.getGameWorld().getCollisionRects().add(block.getBounds());
//                break;
//            }
//        }
//
//        // reset the x position of the collision box
//        npcRect.x = entity.getPosition().x;
//
//        // the same thing but on the vertical Y axis
//        startX = (int) entity.getBounds().x;
//        endX = (int) (entity.getBounds().x + entity.getBounds().width);
//        if (entity.getVelocity().y < 0) {
//            startY = endY = (int) Math.floor(entity.getBounds().y + entity.getVelocity().y);
//        } else {
//            startY = endY = (int) Math.floor(entity.getBounds().y + entity.getBounds().height + entity.getVelocity().y);
//        }
//
//        populateCollidableBlocks(startX, startY, endX, endY);
//
//        npcRect.y += entity.getVelocity().y;
//
//        for (Block block : this.getCollidable()) {
//            if (block == null) continue;
//            if (npcRect.overlaps(block.getBounds())) {
//                if (entity.getVelocity().y < 0) {
//                    this.setGrounded(true);
//                }
//                entity.getVelocity().y = 0;
//                this.getGameWorld().getCollisionRects().add(block.getBounds());
//                break;
//            }
//        }
//        // reset the collision box's position on Y
//        npcRect.y = entity.getPosition().y;
//
//        // update entity's position
//        entity.getPosition().add(entity.getVelocity());
//        entity.getBounds().x = entity.getPosition().x;
//        entity.getBounds().y = entity.getPosition().y;
//
//        // un-scale velocity (not in frame time)
//        entity.getVelocity().scl(1 / delta);
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

}
