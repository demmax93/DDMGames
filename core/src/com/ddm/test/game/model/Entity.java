package com.ddm.test.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ddm.test.game.MapManager;
import com.ddm.test.game.Utility;
import com.ddm.test.game.view.screens.MainGameScreen;

import java.util.UUID;

public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private static final String defaultSpritePath = "core/assets/texture/hero_sprites.png";
    private float planeSpeed, velocity = 10.0f;
    private float tempPositionX;
    private float tempPositionY;
    private String entityID;
    private Direction currentDirection = Direction.UP;
    private Direction previousDirection = Direction.UP;
    private Animation walkUpAnimation;
    private Array<TextureRegion> walkUpFrames;
    protected Vector2 nextPlayerPosition;
    protected Vector2 currentPlayerPosition;
    protected State state = State.IDLE;
    protected float frameTime = 0f;
    protected Sprite frameSprite = null;
    protected TextureRegion currentFrame = null;
    public final int FRAME_WIDTH = 100;
    public final int FRAME_HEIGHT = 200;
    public static Rectangle boundingBox;

    public enum State {
        IDLE, WALKING
    }

    public enum Direction {
        LEFT,
        DOWN,
        RIGHT,
        UP;
    }

    public Entity() {
        initEntity();
    }

    public void initEntity() {
        this.entityID = UUID.randomUUID().toString();
        this.nextPlayerPosition = new Vector2();
        this.currentPlayerPosition = new Vector2();
        this.boundingBox = new Rectangle();
        Utility.loadTextureAsset(defaultSpritePath);
        loadDefaultSprite();
        loadAllAnimations();
    }

    public void update(float delta) {
        frameTime = (frameTime + delta) % 5;
        setBoundingBoxSize(0f, 0.5f);
    }

    public void init(float startX, float startY) {
        this.currentPlayerPosition.x = startX;
        this.currentPlayerPosition.y = startY;
        this.nextPlayerPosition.x = startX;
        this.nextPlayerPosition.y = startY;
    }

    public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced) {
        float width;
        float height;
        float widthReductionAmount = 1.0f - percentageWidthReduced;
        float heightReductionAmount = 1.0f - percentageHeightReduced;
        if (widthReductionAmount > 0 && widthReductionAmount < 1) {
            width = FRAME_WIDTH * widthReductionAmount;
        } else {
            width = FRAME_WIDTH;
        }

        if (heightReductionAmount > 0 && heightReductionAmount < 1) {
            height = FRAME_HEIGHT * heightReductionAmount;
        } else {
            height = FRAME_HEIGHT;
        }

        if (width == 0 || height == 0) {
            Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
        }

        float minX;
        float minY;
        if (MapManager.UNIT_SCALE > 0) {
            minX = nextPlayerPosition.x / MapManager.UNIT_SCALE;
            minY = nextPlayerPosition.y / MapManager.UNIT_SCALE;
        } else {
            minX = nextPlayerPosition.x;
            minY = nextPlayerPosition.y;
        }
        boundingBox.set(minX, minY, width, height);
    }

    private void loadDefaultSprite() {
        Texture texture = Utility.getTextureAsset(defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        currentFrame = textureFrames[0][0];
    }

    private void loadAllAnimations() {
        //Walking animation
        Texture texture = Utility.getTextureAsset(defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        walkUpFrames = new Array<TextureRegion>(39);
        for (int i = 0, j = 0; j < 39; j++) {
            TextureRegion region = textureFrames[j][i];
            if (region == null) {
                Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j);
            }
            walkUpFrames.insert(j, region);
        }
        walkUpAnimation = new Animation(0.08f, walkUpFrames, Animation.PlayMode.LOOP);
    }

    public void dispose() {
        Utility.unloadAsset(defaultSpritePath);
    }

    public void setState(State state) {
        this.state = state;
    }

    public Sprite getFrameSprite() {
        return frameSprite;
    }

    public TextureRegion getFrame() {
        return currentFrame;
    }

    public Vector2 getCurrentPosition() {
        return currentPlayerPosition;
    }

    public void setCurrentPosition(float currentPositionX, float currentPositionY) {
        frameSprite.setX(currentPositionX);
        frameSprite.setY(currentPositionY);
        this.currentPlayerPosition.x = currentPositionX;
        this.currentPlayerPosition.y = currentPositionY;
    }

    public void setDirection(Direction direction, float deltaTime) {
        this.previousDirection = this.currentDirection;
        this.currentDirection = direction;
        //Look into the appropriate variable when changing position
        switch (currentDirection) {
            case UP:
                currentFrame = (TextureRegion) walkUpAnimation.getKeyFrame(frameTime);
                break;
            default:
                break;
        }
    }

    public void setNextPositionToCurrent() {
        setCurrentPosition(nextPlayerPosition.x, nextPlayerPosition.y);
    }

    public void calculateNextPosition(Direction currentDirection, float deltaTime) {
        if (currentDirection.equals(Direction.UP)) {
            planeSpeed += velocity * deltaTime;
        } else if (currentDirection.equals(Direction.DOWN)) {
            planeSpeed -= velocity * deltaTime;
        } else downSpeed(deltaTime);

        planeSpeed = sliceSpeed();

        float rotationSpeed = 30f;
        if (currentDirection.equals(Direction.LEFT)) {
            frameSprite.rotate(rotationSpeed * deltaTime * planeSpeed);
        } else if (currentDirection.equals(Direction.RIGHT)) {
            frameSprite.rotate(-rotationSpeed * deltaTime * planeSpeed);
        }
        tempPositionX = frameSprite.getX() + MathUtils.cosDeg(frameSprite.getRotation() + 90) * planeSpeed * deltaTime;
        tempPositionY = frameSprite.getY() + MathUtils.sinDeg(frameSprite.getRotation() + 90) * planeSpeed * deltaTime;
        collisionsWithBackground();

        frameSprite.setPosition(tempPositionX, tempPositionY);

        nextPlayerPosition.x = frameSprite.getX();
        nextPlayerPosition.y = frameSprite.getY();

    }

    private void collisionsWithBackground() {
        float x = tempPositionX;
        float y = tempPositionY;

        if (x < frameSprite.getWidth() / 100 - 1.2f) {
            x = frameSprite.getWidth() / 100 - 1.2f;
        }
        if (y < frameSprite.getHeight() / 200 - 1) {
            y = frameSprite.getHeight() / 200 - 1;
        }
        if (x > MainGameScreen.getLayerMap().getTileWidth() * MainGameScreen.getLayerMap().getWidth() / 100 - 0.8f) {
            x = MainGameScreen.getLayerMap().getTileWidth() * MainGameScreen.getLayerMap().getWidth() / 100 - 0.8f;
        }
        if (y > MainGameScreen.getLayerMap().getTileHeight() * MainGameScreen.getLayerMap().getHeight() / 100 - 1f) {
            y = MainGameScreen.getLayerMap().getTileHeight() * MainGameScreen.getLayerMap().getHeight() / 100 - 1f;
        }
        tempPositionX = x;
        tempPositionY = y;

    }

    private void downSpeed(float delta) {
        if (planeSpeed > velocity * delta) {
            planeSpeed -= velocity * delta;
        } else if (planeSpeed < -velocity * delta) {
            planeSpeed += velocity * delta;
        } else planeSpeed = 0;
    }

    private float sliceSpeed() {
        float maxSpeed = 5.0f;
        if (maxSpeed < planeSpeed) {
            return maxSpeed;
        }
        if (-maxSpeed > planeSpeed) {
            return -maxSpeed;
        }
        return planeSpeed;
    }


}