package com.ddm.test.game.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.ddm.test.game.view.GameScreen;

public class PlaneController {
    private Polygon planeBounds;

    public PlaneController(Polygon planeBounds) {
        this.planeBounds = planeBounds;
    }

    private float planeSpeed, velocity = 10f;
    private float maxSpeed = 5.0f;
    private float rotationSpeed = 30f;

    public void handler() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            planeSpeed += velocity * GameScreen.deltaCff;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            planeSpeed -= velocity * GameScreen.deltaCff;
        } else downSpeed();

        planeSpeed = sliceSpeed();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            planeBounds.rotate(rotationSpeed * GameScreen.deltaCff * planeSpeed);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            planeBounds.rotate(-rotationSpeed * GameScreen.deltaCff * planeSpeed);
        }

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
        if(maxSpeed < planeSpeed) {
             return maxSpeed;
        }
        if(-maxSpeed > planeSpeed) {
            return -maxSpeed;
        }
        return planeSpeed;
    }
}
