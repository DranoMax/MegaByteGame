package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {

    /** The blocks making up the world **/
    Array<Block> blocks = new Array<Block>();
    /** Our player controlled hero **/
    PlayerCharacter playerCharacter;
    Mom mom;

    // Getters -----------
    public Array<Block> getBlocks() {
        return blocks;
    }
    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }
    public Mom getMom() {
        return mom;
    }
    // --------------------

    public World() {
        createDemoWorld();
    }

    // Sets up all the graphics + spriteBatch for drawing sprites
    private void setupEntities() {
        playerCharacter = new PlayerCharacter(new Sprite(new Texture("badlogic.jpg")));
        mom = new Mom();
    }

    private void createDemoWorld() {
        setupEntities();

        for (int i = 0; i < 10; i++) {
            blocks.add(new Block(new Vector2(i, 0)));
            blocks.add(new Block(new Vector2(i, 6)));
            if (i > 2)
                blocks.add(new Block(new Vector2(i, 1)));
        }
        blocks.add(new Block(new Vector2(9, 2)));
        blocks.add(new Block(new Vector2(9, 3)));
        blocks.add(new Block(new Vector2(9, 4)));
        blocks.add(new Block(new Vector2(9, 5)));

        blocks.add(new Block(new Vector2(6, 3)));
        blocks.add(new Block(new Vector2(6, 4)));
        blocks.add(new Block(new Vector2(6, 5)));
    }

    public void update() {
        // Listen for keyboard events so that we move our PC around
        playerCharacter.update(Gdx.graphics.getDeltaTime());
        mom.update(Gdx.graphics.getDeltaTime());
    }
}
