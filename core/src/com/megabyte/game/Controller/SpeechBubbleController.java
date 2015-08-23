package com.megabyte.game.Controller;

import com.megabyte.game.Model.Entity;
import com.megabyte.game.Model.SpeechBubble;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SpeechBubbleController implements Observer
{
    private Map<String, SpeechBubble> speechBubbleMap = new HashMap<String, SpeechBubble>();
    @Override
    public void update(Observable o, Object arg)
    {
        //TODO: this method never gets called
        Entity entity = (Entity) o;
        SpeechBubble speechBubble = speechBubbleMap.get(entity.toString());
        if (speechBubble != null) {
            System.out.println(entity.getPosition().x + "," + entity.getPosition().y);
            speechBubble.setPosition(entity.getPosition().x, entity.getPosition().y);
        }
    }

    public void setSpeechBubble(Entity e, String text) {
        e.addObserver(this);
        if (text == null) {
            speechBubbleMap.remove(e.toString());
        } else {
            speechBubbleMap.put(e.toString(), new SpeechBubble(text));
        }
    }

    public Collection<SpeechBubble> getSpeechBubbles() {
        return speechBubbleMap.values();
    }
}
