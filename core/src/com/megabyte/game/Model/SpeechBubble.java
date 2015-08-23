package com.megabyte.game.Model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SpeechBubble
{
    private String text;
    private float x;
    private float y;

    public SpeechBubble(String text) {
        this.text = text;
    }
    public void draw(SpriteBatch spriteBatch, OrthographicCamera cam, float PLAYER_POSITION_IN_SCREEN, float ppuX, float ppuY) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.YELLOW);
        Label speechBubble = new Label(text, labelStyle);
        speechBubble.setX((PLAYER_POSITION_IN_SCREEN + x) * ppuX - cam.position.x * ppuX);
        speechBubble.setY(y * ppuY);
        speechBubble.draw(spriteBatch, 1);
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}
