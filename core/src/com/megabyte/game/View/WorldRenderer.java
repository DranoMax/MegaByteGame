package com.megabyte.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.megabyte.game.Model.Block;
import com.megabyte.game.Model.PlayerCharacter;
import com.megabyte.game.Model.World;

public class WorldRenderer {

    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;
    private static final float RUNNING_FRAME_DURATION = 0.06f;

    // The camera always follows the player - this is where the camera focuses in the screen, and thus
    // where the player character will always be drawn as well.
    private static final int PLAYER_POSITION_IN_SCREEN = 5;

    private World world;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    /** Textures **/
    private TextureRegion playerCharacterIdleLeft;
    private TextureRegion playerCharacterIdleRight;
    private TextureRegion blockTexture;
    private TextureRegion playerCharacterFrame;
    private TextureRegion playerCharacterJumpLeft;
    private TextureRegion playerCharacterFallLeft;
    private TextureRegion playerCharacterJumpRight;
    private TextureRegion playerCharacterFallRight;

    /** Animations **/
    private Animation walkLeftAnimation;
    private Animation walkRightAnimation;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX;	// pixels per unit on the X axis
    private float ppuY;	// pixels per unit on the Y axis

    public void setSize (int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }
    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
        playerCharacterIdleLeft = atlas.findRegion("bob-01");
        playerCharacterIdleRight = new TextureRegion(playerCharacterIdleLeft);
        playerCharacterIdleRight.flip(true, false);
        blockTexture = atlas.findRegion("block");
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


    public void render() {
        // We want the camera to follow the player, so we set it to the Player's position.
        cam.position.x = world.getPlayerCharacter().getPosition().x;
        cam.update();
        spriteBatch.begin();
        drawBlocks();
        drawPlayerCharacter();
        spriteBatch.end();
        drawCollisionBlocks();
        if (debug)
            drawDebug();
    }

    private void drawBlocks() {
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            spriteBatch.draw(blockTexture, (PLAYER_POSITION_IN_SCREEN+block.getPosition().x) * ppuX-cam.position.x* ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }
    }

    private void drawPlayerCharacter() {
        PlayerCharacter playerCharacter = world.getPlayerCharacter();
        playerCharacterFrame = playerCharacter.isFacingLeft() ? playerCharacterIdleLeft : playerCharacterIdleRight;
        if(playerCharacter.getState().equals(PlayerCharacter.State.WALKING)) {
            playerCharacterFrame = playerCharacter.isFacingLeft() ? walkLeftAnimation.getKeyFrame(playerCharacter.getStateTime(), true) : walkRightAnimation.getKeyFrame(playerCharacter.getStateTime(), true);
        } else if (playerCharacter.getState().equals(PlayerCharacter.State.JUMPING)) {
            if (playerCharacter.getVelocity().y > 0) {
                playerCharacterFrame = playerCharacter.isFacingLeft() ? playerCharacterJumpLeft : playerCharacterJumpRight;
            } else {
                playerCharacterFrame = playerCharacter.isFacingLeft() ? playerCharacterFallLeft : playerCharacterFallRight;
            }
        }
        spriteBatch.draw(playerCharacterFrame, PLAYER_POSITION_IN_SCREEN*ppuX, playerCharacter.getPosition().y * ppuY, PlayerCharacter.SIZE * ppuX, PlayerCharacter.SIZE * ppuY);
    }

    private void drawDebug() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            Rectangle rect = block.getBounds();
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        // render playerCharacter
        PlayerCharacter playerCharacter = world.getPlayerCharacter();
        Rectangle rect = playerCharacter.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        debugRenderer.end();
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 1, 1, 1));
        for (Rectangle rect : world.getCollisionRects()) {
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        debugRenderer.end();
    }
}
