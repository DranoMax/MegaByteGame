package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Block extends Entity{

    public static final float SIZE = .5f;

    public Block(Vector2 pos) {
        super(pos, SIZE);
    }
    private TextureRegion blockTexture;

    @Override
    public void loadTextures() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/kidAndCat.atlas"));
        blockTexture = atlas.findRegion("block");
    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        spriteBatch.draw(blockTexture, (PLAYER_POSITION_IN_SCREEN+this.getPosition().x) * ppuX-cam.position.x* ppuX, this.getPosition().y * ppuY, SIZE * ppuX, SIZE * ppuY);
    }
}
