package com.ddm.test.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.ddm.test.game.view.GameScreen;

public class MyGameTest extends Game {

	@Override
	public void create() {
		Screen gameScreen = new GameScreen();
		setScreen(gameScreen);
	}
}
