package com.ddm.test.game;

import com.badlogic.gdx.Game;
import com.ddm.test.game.view.screens.MainGameScreen;

public class SpaceRPG extends Game {
    public static final MainGameScreen mainGameScreen = new MainGameScreen();

    @Override
    public void create() {
        setScreen(mainGameScreen);
    }

    @Override
    public void dispose() {
        mainGameScreen.dispose();
    }
}
