package com.ddm.test.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

public abstract class GameObject {

    private Sprite object;
    private Animation<TextureRegion> walkAnimation;

    private float stateTime;

    public GameObject(Texture texture, float x, float y, float width, float height, int frameCols, int frameRows) {
        object = new Sprite();
        object.setSize(width, height);
        object.setOrigin(width, height);
        object.setPosition(x, y);
        object.setOriginCenter();

        TextureRegion[][] tmp = TextureRegion.split(texture,  texture.getWidth()/frameCols,  texture.getHeight()/frameRows);
        TextureRegion[] walkFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation<>(0.13f, walkFrames); //0.025f
        stateTime = 0f;
    }

    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        object.setRegion(currentFrame);
        object.draw(batch);
    }

    public Sprite getBounds() {
        return object;
    }

    public void checkCollisionForBackground(GameObject background) {
        float x = this.getBounds().getX();
        float y = this.getBounds().getY();
        if(x < this.getBounds().getWidth()/2 + 0.5f) {
            x = this.getBounds().getWidth()/2 + 0.5f;
        }
        if(y < this.getBounds().getHeight()/2) {
            y = this.getBounds().getHeight()/2 ;
        }
        if(x > background.getBounds().getWidth() - this.getBounds().getWidth()/2 - 1.5f) {
            x = background.getBounds().getWidth() - this.getBounds().getWidth()/2 - 1.5f;
        }
        if(y > background.getBounds().getHeight() - this.getBounds().getHeight()/2 - 1.3f) {
            y = background.getBounds().getHeight() - this.getBounds().getHeight()/2 - 1.3f;
        }
        this.getBounds().setPosition(x, y);
    }

    public void checkCollision(GameObject object) {
        float x = this.getBounds().getX();
        float y = this.getBounds().getY();

        if(object.getBounds().getBoundingRectangle().overlaps(this.getBounds().getBoundingRectangle())) {

            if(this.getBounds().getX() + this.getBounds().getWidth()/2 > object.getBounds().getX() - object.getBounds().getWidth()/2 ) {
                x = this.getBounds().getX() + 0.1f;
            }
            if(this.getBounds().getX() - this.getBounds().getWidth()/2 < object.getBounds().getX() + object.getBounds().getWidth()/2) {
                x = this.getBounds().getX() - 0.1f;
            }
            if(this.getBounds().getY() + this.getBounds().getHeight()/2 > object.getBounds().getY() - object.getBounds().getHeight()/2 ) {
                y = this.getBounds().getY() + 0.1f;
            }
            if(this.getBounds().getY() - this.getBounds().getHeight()/2 < object.getBounds().getY() + object.getBounds().getHeight()/2 ) {
                y = this.getBounds().getY() - 0.1f;
            }
        }
        this.getBounds().setPosition(x, y);
    }
}
