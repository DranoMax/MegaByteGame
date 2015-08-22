package com.megabyte.game.Model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ascroggins on 8/22/2015.
 */
public class Kid extends Entity {

    public static final float SIZE = 0.5f; // half a unit

    public Kid(Vector2 position) {
        super(position, SIZE);
    }
}
