package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Mom extends Entity
{
    public static final float SIZE = 2f; // half a unit
    private Texture momTexture;

    public Mom(Vector2 position) {
        super(position, SIZE);
    }

    @Override
    public void loadTextures() {
        momTexture = new Texture(Gdx.files.internal("images/mom.png"));
    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        spriteBatch.draw(momTexture, (PLAYER_POSITION_IN_SCREEN+this.getPosition().x) * ppuX-cam.position.x* ppuX, this.getPosition().y * ppuY, SIZE * ppuX, SIZE * ppuY);
    }

    @Override
    public void createPhysicsBody(World physicsWorld) {
        Body body = this.getBody();

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2);
        circleShape.setPosition(new Vector2(10,10));

        // next we create the body for the Entity
        BodyDef entityBodyDef = new BodyDef();
        entityBodyDef.type = BodyDef.BodyType.DynamicBody;
        body = physicsWorld.createBody(entityBodyDef);

        // finally we add a fixture to the body using the polygon
        // defined above. Note that we have to dispose PolygonShapes
        // and CircleShapes once they are no longer used. This is the
        // only time you have to care explicitly for memory management.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.groupIndex = 0;
        body.createFixture(fixtureDef);
        circleShape.dispose();
    }
}