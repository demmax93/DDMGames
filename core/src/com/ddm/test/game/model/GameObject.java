package com.ddm.test.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class GameObject {

    private Sprite object;
    private Animation<TextureRegion> walkAnimation;

    private float stateTime;

    public GameObject(Texture texture, float x, float y, float width, float height, int frameCols, int frameRows) {
        object = new Sprite();
        object.setBounds(x, y, width, height);
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

    public Sprite getSprite() {
        return object;
    }

    public void checkCollisionForBackground(GameObject background) {
        float x = this.getSprite().getX();
        float y = this.getSprite().getY();
        if(x < this.getSprite().getWidth()/2 + 0.5f) {
            x = this.getSprite().getWidth()/2 + 0.5f;
        }
        if(y < this.getSprite().getHeight()/2) {
            y = this.getSprite().getHeight()/2 ;
        }
        if(x > background.getSprite().getWidth() - this.getSprite().getWidth()/2 - 1.5f) {
            x = background.getSprite().getWidth() - this.getSprite().getWidth()/2 - 1.5f;
        }
        if(y > background.getSprite().getHeight() - this.getSprite().getHeight()/2 - 1.3f) {
            y = background.getSprite().getHeight() - this.getSprite().getHeight()/2 - 1.3f;
        }
        this.getSprite().setPosition(x, y);
    }

    public void checkCollision(GameObject object) {
        float x = this.getSprite().getX();
        float y = this.getSprite().getY();

        if(object.getSprite().getBoundingRectangle().overlaps(this.getSprite().getBoundingRectangle())) {

            if(this.getSprite().getX() + this.getSprite().getWidth()/2 > object.getSprite().getX() - object.getSprite().getWidth()/2 ) {
                x = this.getSprite().getX() + 0.1f;
            }
            if(this.getSprite().getX() - this.getSprite().getWidth()/2 < object.getSprite().getX() + object.getSprite().getWidth()/2) {
                x = this.getSprite().getX() - 0.1f;
            }
            if(this.getSprite().getY() + this.getSprite().getHeight()/2 > object.getSprite().getY() - object.getSprite().getHeight()/2 ) {
                y = this.getSprite().getY() + 0.1f;
            }
            if(this.getSprite().getY() - this.getSprite().getHeight()/2 < object.getSprite().getY() + object.getSprite().getHeight()/2 ) {
                y = this.getSprite().getY() - 0.1f;
            }
        }
        this.getSprite().setPosition(x, y);
    }
}
