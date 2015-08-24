package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ascroggins on 8/22/2015.
 */
public class Kid extends Entity {

    public static final float SIZE = .75f; // half a unit
    private static final float RUNNING_FRAME_DURATION = 0.06f;
    private Texture kidTexture;

    public Kid(Vector2 position) {
        super(position, SIZE);
    }

    @Override
    public void loadTextures() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/kidAndCat.pack"));
        entityIdleRight = atlas.findRegion("kid-0");
        entityIdleLeft = new TextureRegion(entityIdleRight);
        entityIdleLeft.flip(true, false);
        TextureRegion[] walkRightFrames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            walkRightFrames[i] = atlas.findRegion("kid-" + i);
        }
        walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);

        TextureRegion[] walkLeftFrames = new TextureRegion[6];

        for (int i = 0; i < 6; i++) {
            walkLeftFrames[i] = new TextureRegion(walkRightFrames[i]);
            walkLeftFrames[i].flip(true, false);
        }
        walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);
        entityJumpRight = atlas.findRegion("kid-1");
        entityJumpLeft = new TextureRegion(entityJumpRight);
        entityJumpLeft.flip(true, false);
        entityFallRight = atlas.findRegion("kid-2");
        entityFallLeft = new TextureRegion(entityFallRight);
        entityFallLeft.flip(true, false);
    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        entityFrame = this.isFacingLeft() ? entityIdleLeft : entityIdleRight;
        if(this.getState().equals(PlayerCharacter.State.WALKING)) {
            entityFrame = this.isFacingLeft() ? walkLeftAnimation.getKeyFrame(this.getStateTime(), true) : walkRightAnimation.getKeyFrame(this.getStateTime(), true);
        } else if (this.getState().equals(PlayerCharacter.State.JUMPING)) {
            if (this.getVelocity().y > 0) {
                entityFrame = this.isFacingLeft() ? entityJumpLeft : entityJumpRight;
            } else {
                entityFrame = this.isFacingLeft() ? entityFallLeft : entityFallRight;
            }
        }
        spriteBatch.draw(entityFrame, (PLAYER_POSITION_IN_SCREEN+this.getPosition().x) * ppuX-cam.position.x* ppuX, this.getPosition().y * ppuY, SIZE * ppuX, SIZE * ppuY);
    }
}
