package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.megabyte.game.util.MapReader;

public class Level {

    private int width;
    private int height;
    private Block[][] blocks;
    private MapReader mapReader = new MapReader();
    private char map[][];
    Music music;

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
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Necrophageon.wav"));
        mapReader.readMap();
        map = mapReader.getMap();
        height = map.length;
        width = map[0].length;
        blocks = new Block[width][height];

        for (int i = 0; i < width; i++) {
            for (int x = 0; x < height; x++) {
                if (map[x][i] == 'x') {
                    blocks[i][height-1-x] = new Block(new Vector2(i, height-1-x));
                }
            }
        }
        music.setLooping(true);
        music.play();
    }
}
