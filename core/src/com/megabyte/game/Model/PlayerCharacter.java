package com.megabyte.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerCharacter extends Entity {

    private boolean longJump = false;
    public static final float SIZE = 1; // half a unit

    private static final float RUNNING_FRAME_DURATION = 0.06f;

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
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/kidAndCat.pack"));
        entityIdleRight = atlas.findRegion("cat-2");
        entityIdleLeft = new TextureRegion(entityIdleRight);
        entityIdleLeft.flip(true, false);
        TextureRegion[] walkRightFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            walkRightFrames[i] = atlas.findRegion("cat-" + i);
        }
        walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);

        TextureRegion[] walkLeftFrames = new TextureRegion[4];

        for (int i = 0; i < 4; i++) {
            walkLeftFrames[i] = new TextureRegion(walkRightFrames[i]);
            walkLeftFrames[i].flip(true, false);
        }
        walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);
        entityJumpRight = atlas.findRegion("cat-3");
        entityJumpLeft = new TextureRegion(entityJumpRight);
        entityJumpLeft.flip(true, false);
        entityFallRight = atlas.findRegion("cat-1");
        entityFallLeft = new TextureRegion(entityFallRight);
        entityFallLeft.flip(true, false);

        // Load attack animations
        TextureRegion[] attackRightFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            attackRightFrames[i] = atlas.findRegion("tentacle-attack-" + i);
        }
        attackRightAnimation = new Animation(RUNNING_FRAME_DURATION, attackRightFrames);

        TextureRegion[] attackLeftFrames = new TextureRegion[5];

        for (int i = 0; i < 5; i++) {
            attackLeftFrames[i] = new TextureRegion(attackRightFrames[i]);
            attackLeftFrames[i].flip(true, false);
        }
        attackLeftAnimation = new Animation(RUNNING_FRAME_DURATION, attackLeftFrames);
    }

    @Override
    public void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        Vector2 position = this.getBody().getPosition();

        entityFrame = this.isFacingLeft() ? entityIdleLeft : entityIdleRight;
        if(this.getState().equals(Entity.State.WALKING)) {
            entityFrame = this.isFacingLeft() ? walkLeftAnimation.getKeyFrame(this.getStateTime(), true) : walkRightAnimation.getKeyFrame(this.getStateTime(), true);
        } else if (this.getState().equals(Entity.State.JUMPING)) {
            if (this.getVelocity().y > 0) {
                entityFrame = this.isFacingLeft() ? entityJumpLeft : entityJumpRight;
            } else {
                entityFrame = this.isFacingLeft() ? entityFallLeft : entityFallRight;
            }
        }
        spriteBatch.draw(entityFrame, position.x+2.5f, position.y+1.5f, SIZE, SIZE);
        if (this.isAttacking()) {
            drawAttack(spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
        }
    }

    public void drawAttack(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        entityFrame = this.isFacingLeft() ? attackLeftAnimation.getKeyFrame(this.getStateTime(), true) : attackRightAnimation.getKeyFrame(this.getStateTime(), true);
        spriteBatch.draw(entityFrame, PLAYER_POSITION_IN_SCREEN * ppuX, this.getPosition().y * ppuY, SIZE * ppuX, SIZE * ppuY);

        if ( attackLeftAnimation.isAnimationFinished(this.getStateTime())) {
            this.setIsAttacking(false);
        }
    }


    @Override
    public void createPhysicsBody(World physicsWorld) {
        float size = SIZE/4;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(size);
        circleShape.setPosition(this.getPosition());

        // next we create the body for the Entity
        BodyDef entityBodyDef = new BodyDef();
        entityBodyDef.type = BodyDef.BodyType.DynamicBody;
        this.setBody(physicsWorld.createBody(entityBodyDef));
        this.getBody().setFixedRotation(true);

        // finally we add a fixture to the body using the polygon
        // defined above. Note that we have to dispose PolygonShapes
        // and CircleShapes once they are no longer used. This is the
        // only time you have to care explicitly for memory management.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.friction = 5f;
        fixtureDef.filter.groupIndex = 0;
        fixtureDef.density = 1;
        this.getBody().createFixture(fixtureDef);

        UserData user = new UserData();
        user.id = 3;
        user.bodySize = size;
        this.getBody().setUserData(user);
        circleShape.dispose();

        this.createPhysicsFoot(physicsWorld);
    }
}