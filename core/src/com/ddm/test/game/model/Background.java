package com.ddm.test.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private static Texture texture;
    private static Sprite backgroundSprite;
    
    public Background(){
        texture = new Texture(Gdx.files.internal("core/assets/background.jpeg"));
        backgroundSprite = new Sprite(texture);
        backgroundSprite.setSize(backgroundSprite.getWidth()/100, backgroundSprite.getHeight()/100);
        backgroundSprite.setPosition(-10,-10);
    }

    public void draw(SpriteBatch batch) {
        backgroundSprite.draw(batch);
    }
}
