package com.ddm.test.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

public abstract class GameObject {

    private Polygon bounds;
    private Sprite object;
    private Animation<TextureRegion> walkAnimation;

    private float stateTime;

    public GameObject(Texture texture, float x, float y, float width, float height, int frameCols, int frameRows) {
        object = new Sprite();
        object.setSize(width, height);
        object.setOrigin(width, height);
        object.setPosition(x, y);
        object.setOriginCenter();

        bounds = new Polygon(new float[]{0f, 0f, width, 0f, width, height, 0f, height});
        bounds.setPosition(x, y);
        bounds.setOrigin(width, height);

        TextureRegion[][] tmp = TextureRegion.split(texture,  texture.getWidth()/frameCols,  texture.getHeight()/frameRows);
        TextureRegion[] walkFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation<TextureRegion>(0.13f, walkFrames); //0.025f
        stateTime = 0f;
    }

    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        object.setRegion(currentFrame);
        object.setPosition(bounds.getX(), bounds.getY());
        object.setRotation(bounds.getRotation());
        object.draw(batch);
    }

    public Polygon getBounds() {
        return bounds;
    }
}
