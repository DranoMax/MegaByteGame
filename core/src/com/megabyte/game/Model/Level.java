package com.megabyte.game.Model;

import com.badlogic.gdx.math.Vector2;

public class Level {

    private int width;
    private int height;
    private Block[][] blocks;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public Level() {
        loadDemoLevel();
    }

    public Block get(int x, int y) {
        return blocks[x][y];
    }

    private void loadDemoLevel() {
        width = 17;
        height = 7;
        blocks = new Block[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                blocks[col][row] = null;
            }
        }

        for (int col = 0; col < 10; col++) {
            blocks[col][0] = new Block(new Vector2(col, 0));
            blocks[col][6] = new Block(new Vector2(col, 6));
            if (col > 2) {
                blocks[col][1] = new Block(new Vector2(col, 1));
            }
        }

        blocks[6][3] = new Block(new Vector2(6, 3));
        blocks[6][4] = new Block(new Vector2(6, 4));
        blocks[6][5] = new Block(new Vector2(6, 5));

        blocks[0][2] = new Block(new Vector2(0,2));
        blocks[9][2] = new Block(new Vector2(9,2));
        blocks[11][2] = new Block(new Vector2(11,2));
        blocks[13][2] = new Block(new Vector2(13,2));
        blocks[16][2] = new Block(new Vector2(16,2));
    }
}
