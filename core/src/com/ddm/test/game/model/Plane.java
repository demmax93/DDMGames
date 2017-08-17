package com.ddm.test.game.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ddm.test.game.control.PlaneController;
import com.ddm.test.game.view.GameScreen;

public class Plane extends GameObject {
    private ParticleEffect firstFlame;
    private ParticleEffect secondFlame;

    private PlaneController controller;
    public Plane(Texture texture, float x, float y, float width, float height, int frameCols, int frameRows) {
        super(texture, x, y, width, height, frameCols, frameRows);
        controller = new PlaneController(getSprite());
        firstFlame = new ParticleEffect();
        firstFlame.load(Gdx.files.internal("core/assets/sfx/plane_fire.p"), Gdx.files.internal("core/assets/texture/"));
        firstFlame.scaleEffect(.002f);
        firstFlame.start();

        secondFlame = new ParticleEffect();
        secondFlame.load(Gdx.files.internal("core/assets/sfx/plane_fire.p"), Gdx.files.internal("core/assets/texture/"));
        secondFlame.scaleEffect(.002f);
        secondFlame.start();
        controller.setFirstFlame(firstFlame);
        controller.setSecondFlame(secondFlame);
    }

    @Override
    public void draw(SpriteBatch batch) {
        firstFlame.draw(batch, GameScreen.deltaCff);
        secondFlame.draw(batch, GameScreen.deltaCff);
        controller.handler();
        if (firstFlame.isComplete()) firstFlame.reset();
        if (secondFlame.isComplete()) secondFlame.reset();
        super.draw(batch);
    }
}
