package com.megabyte.game.Controller;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.megabyte.game.Model.UserData;

/**
 * Created by ascroggins on 8/27/2015.
 */
public class MyContactListener implements ContactListener {
    int numFootContacts = 0;

    @Override
    public void beginContact (Contact contact) {
        //check if fixture A was the foot sensor
        UserData fixtureUserData = (UserData)contact.getFixtureA().getUserData();
        if (fixtureUserData != null && fixtureUserData.id == 3)
            if (fixtureUserData.entity != null) {
                fixtureUserData.entity.numFootContacts++;
            }
        //check if fixture B was the foot sensor
        fixtureUserData = (UserData)contact.getFixtureB().getUserData();
        if (fixtureUserData != null && fixtureUserData.id == 3) {
            if (fixtureUserData.entity != null) {
                fixtureUserData.entity.numFootContacts++;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        //check if fixture A was the foot sensor
        UserData fixtureUserData = (UserData)contact.getFixtureA().getUserData();
        if (fixtureUserData != null && fixtureUserData.id == 3)
            if (fixtureUserData.entity != null) {
                fixtureUserData.entity.numFootContacts--;
            }
        //check if fixture B was the foot sensor
        fixtureUserData = (UserData)contact.getFixtureB().getUserData();
        if (fixtureUserData != null && fixtureUserData.id == 3) {
            if (fixtureUserData.entity != null) {
                fixtureUserData.entity.numFootContacts--;
            }
        }
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
