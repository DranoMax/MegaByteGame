package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class World {

    private static int WIDTH;
    private static int HEIGHT;

    /** The blocks making up the world **/
    private Array<Block> blocks = new Array<Block>();
    /** Our player controlled hero **/
    private PlayerCharacter playerCharacter;
    private Mom mom;
    /** A world has a level through which Bob needs to go through **/
    Level level;

    /** The collision boxes **/
    Array<Rectangle> collisionRects = new Array<Rectangle>();

    // Getters -----------

    public Array<Rectangle> getCollisionRects() {
        return collisionRects;
    }

    // Getters -----------
    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }
    public Mom getMom() {
        return mom;
    }
    public Level getLevel() {
        return level;
    }
    // --------------------

    public World(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        createDemoWorld();
    }

    // Sets up all the graphics + spriteBatch for drawing sprites
    private void setupEntities() {
        playerCharacter = new PlayerCharacter(new Sprite(new Texture("badlogic.jpg")), this);
        mom = new Mom();
    }

    private void createDemoWorld() {
        setupEntities();
        level = new Level();
    }

    /** Return only the blocks that need to be drawn **/
    public List<Block> getDrawableBlocks(int width, int height) {
        int x = (int)playerCharacter.getPosition().x - width;
        int y = (int)playerCharacter.getPosition().y - height;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int x2 = x + 2 * width;
        int y2 = y + 2 * height;
        if (x2 > 800) {
            x2 = 800 - 1;
        }
        if (y2 > 480) {
            y2 = 480 - 1;
        }

        List<Block> blocks = new ArrayList<Block>();
        Block block;
//        for (int col = x; col <= x2; col++) {
//            for (int row = y; row <= y2; row++) {
//                block = level.getBlocks()[col][row];
//                if (block != null) {
//                    blocks.add(block);
//                }
//            }
//        }
        return blocks;
    }

    public void update() {
        // Listen for keyboard events so that we move our PC around
        playerCharacter.update(Gdx.graphics.getDeltaTime());
        mom.update(Gdx.graphics.getDeltaTime());
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }
}
