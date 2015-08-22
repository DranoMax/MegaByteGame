package com.megabyte.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.megabyte.game.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Kid and Monster Game";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new MainGame(), config);
	}
}
