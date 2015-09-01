package com.megabyte.game.View;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.TimeUtils;
import com.megabyte.game.Controller.MyContactListener;
import com.megabyte.game.Model.Block;
import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.GameWorld;
import com.megabyte.game.Model.PlayerCharacter;
import com.megabyte.game.Model.SpeechBubble;

import java.util.ArrayList;

public class WorldRenderer extends ApplicationAdapter {

    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;

    // The camera always follows the player - this is where the camera focuses in the screen, and thus
    // where the player character will always be drawn as well.
    private static final int PLAYER_POSITION_IN_SCREEN = 5;

    private GameWorld gameWorld;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private float ppuX;	// pixels per unit on the X axis
    private float ppuY;	// pixels per unit on the Y axis

    // JUST FOR TESTING PHYSICS
    Sprite sprite;
    World physicsWorld;
    Body body;
    Box2DDebugRenderer box2dDebugRenderer;
    Matrix4 debugMatrix;
    TextureRegion textureRegion;
    Body groundBody;
    /** our boxes **/
    private ArrayList<Body> boxes = new ArrayList<Body>();
    public void setSize (int w, int h) {
        ppuX = 1;
        ppuY = 1;
        System.out.println(ppuX + " y: " + ppuY);
    }
    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WorldRenderer(GameWorld gameWorld, boolean debug) {
        this.gameWorld = gameWorld;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(48, 32);
        cam.position.set(0, 16, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        create();
    }

    @Override
    public void create() {

        sprite = new Sprite(new Texture(Gdx.files.internal("images/mom.png")));
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("images/badlogicsmall.jpg")));

        // Center the sprite in the top/middle of the screen
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        physicsWorld = new World(new Vector2(0, -9f), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Set our body to the same position as our sprite
        bodyDef.position.set(sprite.getX(), sprite.getY());

        // Create a body in the world using our definition
        body = physicsWorld.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();

        box2dDebugRenderer = new Box2DDebugRenderer();
        debugMatrix = new Matrix4(cam.combined);
      //  debugMatrix.scale(100f, 100f, 1f);

        createPhysicsWorld();

        gameWorld.getPlayerCharacter().createPhysicsBody(physicsWorld);

        for (Block block : gameWorld.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            block.createPhysicsBody(physicsWorld);
        }

        physicsWorld.setContactListener(new MyContactListener());
    }

    private void createPhysicsWorld () {
        // we instantiate a new World with a proper gravity vector
        // and tell it to sleep when possible.
        physicsWorld = new World(new Vector2(0, -10), true);

        float[] vertices = {-0.07421887f, -0.16276085f, -0.12109375f, -0.22786504f, -0.157552f, -0.7122401f, 0.04296875f,
                -0.7122401f, 0.110677004f, -0.6419276f, 0.13151026f, -0.49869835f, 0.08984375f, -0.3190109f};

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        // next we create a static ground platform. This platform
        // is not moveable and will not react to any influences from
        // outside. It will however influence other bodies. First we
        // create a PolygonShape that holds the form of the platform.
        // it will be 100 meters wide and 2 meters high, centered
        // around the origin
        PolygonShape groundPoly = new PolygonShape();
        groundPoly.setAsBox(50, 1);

        // next we create the body for the ground platform. It's
        // simply a static body.
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBody = physicsWorld.createBody(groundBodyDef);

        // finally we add a fixture to the body using the polygon
        // defined above. Note that we have to dispose PolygonShapes
        // and CircleShapes once they are no longer used. This is the
        // only time you have to care explicitly for memory management.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundPoly;
        fixtureDef.filter.groupIndex = 0;
        groundBody.createFixture(fixtureDef);
        groundPoly.dispose();

        createBoxes();
    }

    private void createBoxes () {
        // next we create 50 boxes at random locations above the ground
        // body. First we create a nice polygon representing a box 2 meters
        // wide and high.
        PolygonShape boxPoly = new PolygonShape();
        boxPoly.setAsBox(1, 1);

        // next we create the 50 box bodies using the PolygonShape we just
        // defined. This process is similar to the one we used for the ground
        // body. Note that we reuse the polygon for each body fixture.
        for (int i = 0; i < 20; i++) {
            // Create the BodyDef, set a random position above the
            // ground and create a new body
            BodyDef boxBodyDef = new BodyDef();
            boxBodyDef.type = BodyDef.BodyType.DynamicBody;
            boxBodyDef.position.x = -24 + (float)(Math.random() * 48);
            boxBodyDef.position.y = 10 + (float)(Math.random() * 100);
            Body boxBody = physicsWorld.createBody(boxBodyDef);

            boxBody.createFixture(boxPoly, 1);

            // add the box to our list of boxes
            boxes.add(boxBody);
        }

        // we are done, all that's left is disposing the boxPoly
        boxPoly.dispose();
    }

    @Override
    public void render() {
        // first we update the world. For simplicity
        // we use the delta time provided by the Graphics
        // instance. Normally you'll want to fix the time
        // step.
        long start = TimeUtils.nanoTime();
        physicsWorld.step(Gdx.graphics.getDeltaTime(), 8, 3);
        float updateTime = (TimeUtils.nanoTime() - start) / 1000000000.0f;
        renderBox(groundBody, 50, 1);

        // next we render each box via the SpriteBatch.
        // for this we have to set the projection matrix of the
        // spritebatch to the camera's combined matrix. This will
        // make the spritebatch work in world coordinates
        spriteBatch.getProjectionMatrix().set(cam.combined);
        spriteBatch.begin();
        for (int i = 0; i < boxes.size(); i++) {
            Body box = boxes.get(i);
            Vector2 position = box.getPosition(); // that's the box's center position
            float angle = MathUtils.radiansToDegrees * box.getAngle(); // the rotation angle around the center
            spriteBatch.draw(textureRegion, position.x - 1, position.y - 1, // the bottom left corner of the box, unrotated
                    1f, 1f, // the rotation center relative to the bottom left corner of the box
                    2, 2, // the width and height of the box
                    1, 1, // the scale on the x- and y-axis
                    angle); // the rotation angle
        }
        spriteBatch.end();
        // next we use the debug renderer. Note that we
        // simply apply the camera again and then call
        // the renderer. the camera.apply() call is actually
        // not needed as the opengl matrices are already set
        // by the spritebatch which in turn uses the camera matrices :)
        box2dDebugRenderer.render(physicsWorld, cam.combined);

        // finally we render all contact points
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Point);
        debugRenderer.setColor(0, 1, 0, 1);
        for (int i = 0; i < physicsWorld.getContactCount(); i++) {
            Contact contact = physicsWorld.getContactList().get(i);
            // we only render the contact if it actually touches
            if (contact.isTouching()) {
                // get the world manifold from which we get the
                // contact points. A manifold can have 0, 1 or 2
                // contact points.
                WorldManifold manifold = contact.getWorldManifold();
                int numContactPoints = manifold.getNumberOfContactPoints();
                for (int j = 0; j < numContactPoints; j++) {
                    Vector2 point = manifold.getPoints()[j];
                    debugRenderer.point(point.x, point.y, 0);
                }
            }
        }
        debugRenderer.end();



        // We want the camera to follow the player, so we set it to the Player's position.
        cam.position.x = gameWorld.getPlayerCharacter().getBody().getPosition().x;
        cam.update();
        spriteBatch.begin();
        drawBlocks();
        drawEnemies();
        drawPlayerCharacter();
        drawSpeechBubbles();
        spriteBatch.draw(sprite, sprite.getX(), sprite.getY());
    //    box2dDebugRenderer.render(physicsWorld, debugMatrix);
        spriteBatch.end();
        drawCollisionBlocks();
        if (debug)
            drawDebug();
    }

    Matrix4 transform = new Matrix4();

    private void renderBox (Body body, float halfWidth, float halfHeight) {
        // get the bodies center and angle in world coordinates
        Vector2 pos = body.getWorldCenter();
        float angle = body.getAngle();

        // set the translation and rotation matrix
        transform.setToTranslation(pos.x, pos.y, 0);
        transform.rotate(0, 0, 1, (float)Math.toDegrees(angle));

        // render the box
        debugRenderer.begin(ShapeType.Line);
        debugRenderer.setTransformMatrix(transform);
        debugRenderer.setColor(1, 1, 1, 1);
        debugRenderer.rect(-halfWidth, -halfHeight, halfWidth * 2, halfHeight * 2);
        debugRenderer.end();
    }

    private void drawBlocks() {
        for (Block block : gameWorld.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            block.drawEntity(spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
        }
    }

    private void drawPlayerCharacter() {
        gameWorld.getPlayerCharacter().drawEntity(this.spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
    }

    private void drawEnemies() {
        for (Entity enemy : gameWorld.getEnemies()) {
            enemy.drawEntity(spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
        }
    }

    private void drawSpeechBubbles() {
        for (SpeechBubble bubble : gameWorld.getSpeechBubbles()) {
            bubble.draw(spriteBatch, cam, PLAYER_POSITION_IN_SCREEN, ppuX, ppuY);
        }
    }

    private void drawDebug() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        for (Block block : gameWorld.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            Rectangle rect = block.getBounds();
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        // render playerCharacter
        PlayerCharacter playerCharacter = gameWorld.getPlayerCharacter();
        Rectangle rect = playerCharacter.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        debugRenderer.end();
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 1, 1, 1));
        for (Rectangle rect : gameWorld.getCollisionRects()) {
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        debugRenderer.end();
    }

    public Rectangle convertScreenRectangleToWorldRectangle(int x, int y, int width, int height) {
        //TODO: use a rectangle pool
        return new Rectangle(x/ppuX, CAMERA_HEIGHT-y/ppuY, width/ppuX,height/ppuY);
    }

    @Override
    public void dispose () {
        physicsWorld.dispose();
        box2dDebugRenderer.dispose();
        debugRenderer.dispose();
        textureRegion.getTexture().dispose();
    }
}
