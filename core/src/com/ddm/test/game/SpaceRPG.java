package com.ddm.test.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.ddm.test.game.view.GameScreen;
import com.ddm.test.game.view.screens.MainGameScreen;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

public class SpaceRPG extends Game {
	public static final MainGameScreen mainGameScreen = new MainGameScreen();

	@Override
	public void create() {
		setScreen(mainGameScreen);
	}

	@Override
	public void dispose(){
		mainGameScreen.dispose();
	}
}
