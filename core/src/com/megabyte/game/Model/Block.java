package com.megabyte.game.Model;

import com.badlogic.gdx.math.Vector2;

public class Block extends Entity{

    public static final float SIZE = 1f;

    public Block(Vector2 pos) {
        super(pos, SIZE);
    }
}
