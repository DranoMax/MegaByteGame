package com.megabyte.game;

import com.badlogic.gdx.Game;
import com.megabyte.game.Screens.GameScreen;

public class MainGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}

}
