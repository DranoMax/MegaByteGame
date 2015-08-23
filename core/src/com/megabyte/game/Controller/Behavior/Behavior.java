package com.megabyte.game.Controller.Behavior;

import com.megabyte.game.Controller.Controller;

/**
 * Created by ascroggins on 8/23/2015.
 */
public abstract class Behavior {

    private Controller controller;

    /**
     * The base method to be called for updating state and executing AI actions
     */
    public abstract void execute();
    public abstract void collideWithWall();


    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
