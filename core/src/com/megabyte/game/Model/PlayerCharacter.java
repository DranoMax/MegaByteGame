package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerCharacter extends Entity {

	private boolean longJump = false;
	public static final float SIZE = 0.5f; // half a unit

	private static final float RUNNING_FRAME_DURATION = 0.06f;

	/** Textures **/
	private TextureRegion playerCharacterIdleLeft;
	private TextureRegion playerCharacterIdleRight;
	private TextureRegion playerCharacterFrame;
	private TextureRegion playerCharacterJumpLeft;
	private TextureRegion playerCharacterFallLeft;
	private TextureRegion playerCharacterJumpRight;
	private TextureRegion playerCharacterFallRight;

	/** Animations **/
	private Animation walkLeftAnimation;
	private Animation walkRightAnimation;

	public PlayerCharacter(Vector2 position) {
		super(position, SIZE);
	}

	public boolean isLongJump() {
		return longJump;
	}

	public void setLongJump(boolean longJump) {
		this.longJump = longJump;
	}

	@Override
	public void loadTextures() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
		playerCharacterIdleLeft = atlas.findRegion("bob-01");
		playerCharacterIdleRight = new TextureRegion(playerCharacterIdleLeft);
		playerCharacterIdleRight.flip(true, false);
		TextureRegion[] walkLeftFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			walkLeftFrames[i] = atlas.findRegion("bob-0" + (i + 2));
		}
		walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);

		TextureRegion[] walkRightFrames = new TextureRegion[5];

		for (int i = 0; i < 5; i++) {
			walkRightFrames[i] = new TextureRegion(walkLeftFrames[i]);
			walkRightFrames[i].flip(true, false);
		}
		walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);
		playerCharacterJumpLeft = atlas.findRegion("bob-up");
		playerCharacterJumpRight = new TextureRegion(playerCharacterJumpLeft);
		playerCharacterJumpRight.flip(true, false);
		playerCharacterFallLeft = atlas.findRegion("bob-down");
		playerCharacterFallRight = new TextureRegion(playerCharacterFallLeft);
		playerCharacterFallRight.flip(true, false);
	}

	@Override
	public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
		playerCharacterFrame = this.isFacingLeft() ? playerCharacterIdleLeft : playerCharacterIdleRight;
		if(this.getState().equals(PlayerCharacter.State.WALKING)) {
			playerCharacterFrame = this.isFacingLeft() ? walkLeftAnimation.getKeyFrame(this.getStateTime(), true) : walkRightAnimation.getKeyFrame(this.getStateTime(), true);
		} else if (this.getState().equals(PlayerCharacter.State.JUMPING)) {
			if (this.getVelocity().y > 0) {
				playerCharacterFrame = this.isFacingLeft() ? playerCharacterJumpLeft : playerCharacterJumpRight;
			} else {
				playerCharacterFrame = this.isFacingLeft() ? playerCharacterFallLeft : playerCharacterFallRight;
			}
		}
		spriteBatch.draw(playerCharacterFrame, PLAYER_POSITION_IN_SCREEN*ppuX, this.getPosition().y * ppuY, PlayerCharacter.SIZE * ppuX, PlayerCharacter.SIZE * ppuY);
	}
	
}
