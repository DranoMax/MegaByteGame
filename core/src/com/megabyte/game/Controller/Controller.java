package com.megabyte.game.Controller;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.megabyte.game.Model.Block;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.GameWorld;

/**
 * Created by ascroggins on 8/22/2015.
 */
public abstract class Controller {

    private GameWorld gameWorld;

    // Blocks that playerCharacter can collide with any given frame
    private Array<Block> collidable = new Array<Block>();

    // This is the rectangle pool used in collision detection
    // Good to avoid instantiation each frame
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    public abstract void update(float delta);
    private Entity entity;

    public Controller(Entity entity, GameWorld gameWorld) {
        this.entity = entity;
        this.gameWorld = gameWorld;
    }

    public Entity getEntity() {
        return entity;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

//    /** Collision checking **/
//    public void checkCollisionWithBlocks(float delta) {
//        // scale velocity to frame units
//        entity.getVelocity().scl(delta);
//
//        // Obtain the rectangle from the pool instead of instantiating it
//        Rectangle npcRect = rectPool.obtain();
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
//        populateCollidableBlocks(startX, startY, endX, endY);
//
//        // simulate entity's movement on the X
//        npcRect.x += entity.getVelocity().x;
//
//        // clear collision boxes in gameWorld
//        this.getGameWorld().getCollisionRects().clear();
//
//        // if entity collides, make his horizontal velocity 0
//        for (Block block : collidable) {
//            if (block == null) continue;
//            if (npcRect.overlaps(block.getBounds())) {
//                entity.getVelocity().x = 0;
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
//        for (Block block : collidable) {
//            if (block == null) continue;
//            if (npcRect.overlaps(block.getBounds())) {
//                if (entity.getVelocity().y < 0) {
//                    grounded = true;
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
//
//    }

    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    public void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < this.getGameWorld().getLevel().getWidth() && y >=0 && y < this.getGameWorld().getLevel().getHeight()) {
                    collidable.add(this.getGameWorld().getLevel().get(x, y));
                }
            }
        }
    }

    public Pool<Rectangle> getRectPool() {
        return rectPool;
    }

    public void setRectPool(Pool<Rectangle> rectPool) {
        this.rectPool = rectPool;
    }

    public Array<Block> getCollidable() {
        return collidable;
    }

    public void setCollidable(Array<Block> collidable) {
        this.collidable = collidable;
    }
}
