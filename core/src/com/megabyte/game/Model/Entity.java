package com.megabyte.game.Model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.megabyte.game.Controller.NPCController;

import java.util.Observable;

/**
 * Created by ascroggins on 8/21/2015.
 */
public abstract class Entity extends Observable
{

    private Vector2 position = new Vector2();
    private Vector2 acceleration = new Vector2();
    private  Vector2 velocity = new Vector2();
    private Rectangle bounds = new Rectangle();
    private NPCController npcController;

    /** Textures **/
    public TextureRegion entityIdleLeft;
    public TextureRegion entityIdleRight;
    public TextureRegion entityFrame;
    public TextureRegion entityJumpLeft;
    public TextureRegion entityFallLeft;
    public TextureRegion entityJumpRight;
    public TextureRegion entityFallRight;

    /** Animations **/
    public Animation walkLeftAnimation;
    public Animation walkRightAnimation;
    public Animation attackLeftAnimation;
    public Animation attackRightAnimation;

    /** Physics **/
    private Body body;
    public int numFootContacts = 0;

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    private State state = State.IDLE;
    boolean facingLeft = true;
    float stateTime = 0;
    private static float SIZE;
    private boolean isAttacking = false;

    public Entity (Vector2 position, float SIZE) {
        this.SIZE = SIZE;
        this.setPosition(position);
        this.setBounds(new Rectangle(position.x, position.y, SIZE, SIZE));
        loadTextures();
    }

    /**
     * Used for loading textures specific to extending Entity
     */
    public abstract void loadTextures();

    /**
     * Called by WorldRenderer in order to actually draw the Entity
     *
     * @param spriteBatch
     * @param PLAYER_POSITION_IN_SCREEN
     * @param ppuX
     * @param ppuY
     */
    public abstract void drawEntity(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY);

    /**
     * Called in WorldRenderer during Create to create the Entity's physics body
     * @param physicsWorld
     */
    public abstract void createPhysicsBody(World physicsWorld);

    public void createPhysicsFoot(World physicsWorld) {
        Vector2 pos = this.getBody().getPosition();
        UserData userData = (UserData)this.getBody().getUserData();
        float bodySize = userData.bodySize/2;

        UserData footuserData = new UserData();
        footuserData.id = 3;
        footuserData.entity = this;

        //shape definition for main fixture
        PolygonShape polygonShape = new PolygonShape();

        //fixture definition
        FixtureDef myFixtureDef = new FixtureDef();
        myFixtureDef.shape = polygonShape;
        myFixtureDef.density = 1;

        //add foot sensor fixture
        polygonShape.setAsBox(bodySize, 0.3f, new Vector2(pos.x+1, 0), 0);
        myFixtureDef.isSensor = true;
        Fixture footSensorFixture = this.getBody().createFixture(myFixtureDef);
        footSensorFixture.setUserData(footuserData);

        polygonShape.dispose();
    }

    public NPCController getNpcController() {
        return npcController;
    }

    public void setNpcController(NPCController npcController) {
        this.npcController = npcController;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public float getSIZE() {
        return SIZE;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
        this.bounds.setX(position.x);
        this.bounds.setY(position.y);
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void update(float delta) {
//		position.add(velocity.tmp().mul(delta));
//		bounds.x = position.x;
//		bounds.y = position.y;
        stateTime += delta;
        setChanged();
        notifyObservers();
    }

    public boolean isAttacking() {
        return isAttacking;
    }
    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
}