package com.megabyte.game.Model;

import com.badlogic.gdx.math.Vector2;

public class PlayerCharacter extends Entity {

	private boolean longJump = false;
	public static final float SIZE = 0.5f; // half a unit

	public PlayerCharacter(Vector2 position) {
		super(position, SIZE);
	}

	public boolean isLongJump() {
		return longJump;
	}

	public void setLongJump(boolean longJump) {
		this.longJump = longJump;
	}
	
}
