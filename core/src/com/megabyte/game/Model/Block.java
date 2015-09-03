package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Block extends Entity{

    public static final float SIZE = 3f;

    public Block(Vector2 pos) {
        super(new Vector2(pos.x*(SIZE/2), pos.y*(SIZE/2)), SIZE);
    }
    private TextureRegion blockTexture;

    @Override
    public void loadTextures() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/kidAndCat.pack"));
        blockTexture = atlas.findRegion("block");
    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        Body body  = this.getBody();

        if (body != null) {
            this.setPosition(this.getBody().getPosition());
        }
        spriteBatch.draw(blockTexture, this.getPosition().x-SIZE/4, this.getPosition().y-SIZE/4, SIZE / 2, SIZE / 2);
    }

    @Override
    public void createPhysicsBody(World physicsWorld) {
        float size = SIZE/4;

        PolygonShape groundPoly = new PolygonShape();
        groundPoly.setAsBox(size, size);


        // next we create the body for the Entity
        BodyDef entityBodyDef = new BodyDef();
        entityBodyDef.type = BodyDef.BodyType.StaticBody;
        entityBodyDef.position.x = this.getPosition().x;
        entityBodyDef.position.y = this.getPosition().y;
        this.setBody(physicsWorld.createBody(entityBodyDef));

        // finally we add a fixture to the body using the polygon
        // defined above. Note that we have to dispose PolygonShapes
        // and CircleShapes once they are no longer used. This is the
        // only time you have to care explicitly for memory management.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundPoly;
        fixtureDef.filter.groupIndex = 0;
        this.getBody().createFixture(fixtureDef);

        UserData user = new UserData();
        user.id = 0;
        user.bodySize = size;
        this.getBody().setUserData(user);
        groundPoly.dispose();
    }
}
