package com.ddm.test.game.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.ddm.test.game.view.GameScreen;

public class PlaneController {
    private Sprite planeSprite;
    private ParticleEffect firstFlame;
    private ParticleEffect secondFlame;

    public PlaneController(Sprite planeSprite) {
        this.planeSprite = planeSprite;
    }

    private float planeSpeed, velocity = 10.0f;

    public void handler() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            planeSpeed += velocity * GameScreen.deltaCff;
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            planeSpeed -= velocity * GameScreen.deltaCff;
        } else downSpeed();

        planeSpeed = sliceSpeed();

        float rotationSpeed = 30f;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            planeSprite.rotate(rotationSpeed * GameScreen.deltaCff * planeSpeed);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            planeSprite.rotate(-rotationSpeed * GameScreen.deltaCff * planeSpeed);
        }

        planeSprite.setPosition(planeSprite.getX() + MathUtils.cosDeg(planeSprite.getRotation() + 90) * planeSpeed * GameScreen.deltaCff,
                planeSprite.getY() + MathUtils.sinDeg(planeSprite.getRotation() + 90) * planeSpeed * GameScreen.deltaCff);

        if(firstFlame != null && secondFlame != null) {
            flameRotation();
        }
    }

    private void downSpeed() {
        if(planeSpeed > velocity * GameScreen.deltaCff) {
            planeSpeed -= velocity * GameScreen.deltaCff;
        } else if (planeSpeed < -velocity * GameScreen.deltaCff) {
            planeSpeed += velocity * GameScreen.deltaCff;
        } else planeSpeed = 0;
    }

    private float sliceSpeed() {
        float maxSpeed = 5.0f;
        if(maxSpeed < planeSpeed) {
             return maxSpeed;
        }
        if(-maxSpeed > planeSpeed) {
            return -maxSpeed;
        }
        return planeSpeed;
    }

    public void setFirstFlame(ParticleEffect firstFlame) {
        this.firstFlame = firstFlame;
    }

    public void setSecondFlame(ParticleEffect secondFlame) {
        this.secondFlame = secondFlame;
    }

    private void flameRotation(){
        float[] vertices = planeSprite.getVertices();
        float centrX = (vertices[Batch.X4] + vertices[Batch.X1])/2;
        float centrY = (vertices[Batch.Y4] + vertices[Batch.Y1])/2;

        firstFlame.setPosition(centrX + 0.1f * Math.signum(MathUtils.cosDeg(planeSprite.getRotation() + 90)),
                centrY + 0.2f * Math.signum(MathUtils.sinDeg(planeSprite.getRotation() + 90)));
        secondFlame.setPosition(centrX - 0.1f * Math.signum(MathUtils.cosDeg(planeSprite.getRotation() + 90)),
                centrY + 0.2f * Math.signum(MathUtils.sinDeg(planeSprite.getRotation() + 90)));
        firstFlame.getEmitters().first().getAngle().setHigh(planeSprite.getRotation() - 90);
        firstFlame.getEmitters().first().getAngle().setLow(planeSprite.getRotation() - 90);
        secondFlame.getEmitters().first().getAngle().setHigh(planeSprite.getRotation() - 90);
        secondFlame.getEmitters().first().getAngle().setLow(planeSprite.getRotation() - 90);
    }
}
