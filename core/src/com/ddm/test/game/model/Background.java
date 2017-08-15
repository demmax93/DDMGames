package com.ddm.test.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background extends GameObject {
    
    public Background(Texture texture, float x, float y, float width, float height, int frameCols, int frameRows){
        super(texture, x, y, width, height, frameCols, frameRows);
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
