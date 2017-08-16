package com.ddm.test.game.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.ddm.test.game.view.GameScreen;

public class PlaneController {
    private Sprite planeBounds;

    public PlaneController(Sprite planeBounds) {
        this.planeBounds = planeBounds;
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
            planeBounds.rotate(rotationSpeed * GameScreen.deltaCff * planeSpeed);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            planeBounds.rotate(-rotationSpeed * GameScreen.deltaCff * planeSpeed);
        } else planeBounds.rotate(0.0f);

        planeBounds.setPosition(planeBounds.getX() + MathUtils.cosDeg(planeBounds.getRotation() + 90) * planeSpeed * GameScreen.deltaCff,
                planeBounds.getY() + MathUtils.sinDeg(planeBounds.getRotation() + 90) * planeSpeed * GameScreen.deltaCff);
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
}
