package com.megabyte.game.Model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SpeechBubble {
    private final static float BUBBLE_PADDING = 5;
    private String text;
    private float x;
    private float y;
    private Texture backgroundTexture;
    Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

    public SpeechBubble(String text) {
        this.text = text;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
    }
    public void draw(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        Label speechBubble = new Label(text, labelStyle);
        float drawX = (PLAYER_POSITION_IN_SCREEN + x - cam.position.x) * ppuX;
        float drawY = y * ppuY;
        spriteBatch.draw(backgroundTexture, drawX-BUBBLE_PADDING, drawY-BUBBLE_PADDING, speechBubble.getWidth()+BUBBLE_PADDING*2, speechBubble.getHeight()+BUBBLE_PADDING*2);
        speechBubble.setX(drawX);
        speechBubble.setY(drawY);
        speechBubble.draw(spriteBatch, 1);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
