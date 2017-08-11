package com.ddm.test.game.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ddm.test.game.control.PlaneController;

public class Plane extends GameObject {

    private PlaneController controller;
    public Plane(Texture texture, float x, float y, float width, float height, int frameCols, int frameRows) {
        super(texture, x, y, width, height, frameCols, frameRows);
        controller = new PlaneController(bounds);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        controller.handler();
    }
}
