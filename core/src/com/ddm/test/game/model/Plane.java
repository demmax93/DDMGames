package com.ddm.test.game.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.ddm.test.game.control.PlaneController;

public class Plane extends GameObject {
    private ParticleEffect firstFlame;
    private ParticleEffect secondFlame;

    private PlaneController controller;
    public Plane(Texture texture, float x, float y, float width, float height, int frameCols, int frameRows) {
        super(texture, x, y, width, height, frameCols, frameRows);
        controller = new PlaneController(getBounds());
        firstFlame = new ParticleEffect();
        firstFlame.load(Gdx.files.internal("core/assets/sfx/plane_fire.p"), Gdx.files.internal("core/assets/texture/"));
        firstFlame.getEmitters().first().setPosition(this.getBounds().getX(), this.getBounds().getY());
        firstFlame.scaleEffect(.002f);
        firstFlame.start();

        secondFlame = new ParticleEffect();
        secondFlame.load(Gdx.files.internal("core/assets/sfx/plane_fire.p"), Gdx.files.internal("core/assets/texture/"));
        secondFlame.getEmitters().first().setPosition(this.getBounds().getX(), this.getBounds().getY());
        secondFlame.scaleEffect(.002f);
        secondFlame.start();
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        controller.handler();
        flameRotation(); //need fix
        firstFlame.draw(batch, Gdx.graphics.getDeltaTime());
        secondFlame.draw(batch, Gdx.graphics.getDeltaTime());
        if (firstFlame.isComplete()) firstFlame.reset();
        if (secondFlame.isComplete()) secondFlame.reset();
    }

    private void flameRotation(){
        firstFlame.setPosition(this.getBounds().getX()+0.4f, this.getBounds().getY()+0.2f);
        secondFlame.setPosition(this.getBounds().getX()+0.6f, this.getBounds().getY()+ 0.2f);
        firstFlame.getEmitters().first().getAngle().setHigh(this.getBounds().getRotation()-90);
        firstFlame.getEmitters().first().getAngle().setLow(this.getBounds().getRotation()-90);
        secondFlame.getEmitters().first().getAngle().setHigh(this.getBounds().getRotation()-90);
        secondFlame.getEmitters().first().getAngle().setLow(this.getBounds().getRotation()-90);
    }
}
