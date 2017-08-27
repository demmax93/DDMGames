package com.ddm.test.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ddm.test.game.MapManager;
import com.ddm.test.game.Utility;

import java.util.UUID;

/**
 * DMKA
 * 27.08.2017
 */
public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private static final String defaultSpritePath ="core/assets/texture/hero_sprites.png";
    private Vector2 velocity;
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
        UP;
    }

    public Entity(){
        initEntity();
    }

    public void initEntity(){
        this.entityID = UUID.randomUUID().toString();
        this.nextPlayerPosition = new Vector2();
        this.currentPlayerPosition = new Vector2();
        this.boundingBox = new Rectangle();
        this.velocity = new Vector2(2f,2f);
        Utility.loadTextureAsset(defaultSpritePath);
        loadDefaultSprite();
        loadAllAnimations();
    }

    public void update(float delta){
        frameTime = (frameTime + delta)%5; //Want to avoid overflow
        //We want the hitbox to be at the feet for a better feel
        setBoundingBoxSize(0f, 0.5f);
    }

    public void init(float startX, float startY){
        this.currentPlayerPosition.x = startX;
        this.currentPlayerPosition.y = startY;
        this.nextPlayerPosition.x = startX;
        this.nextPlayerPosition.y = startY;
    }

    public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced){
        //Update the current bounding box
        float width;
        float height;
        float widthReductionAmount = 1.0f - percentageWidthReduced;
        //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced;
        //.8f for 20% (1 - .20)
        if( widthReductionAmount > 0 && widthReductionAmount < 1){
            width = FRAME_WIDTH * widthReductionAmount;
        } else{
            width = FRAME_WIDTH;
        }

        if( heightReductionAmount > 0 && heightReductionAmount < 1){
            height = FRAME_HEIGHT * heightReductionAmount;
        } else{
            height = FRAME_HEIGHT;
        }

        if( width == 0 || height == 0){
            Gdx.app.debug(TAG, "Width and Height are 0!! " + width +":" + height);
        }

        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;
        if( MapManager.UNIT_SCALE > 0 ) {
            minX = nextPlayerPosition.x / MapManager.UNIT_SCALE;
            minY = nextPlayerPosition.y / MapManager.UNIT_SCALE;
        } else{
            minX = nextPlayerPosition.x;
            minY = nextPlayerPosition.y;
        }
        boundingBox.set(minX, minY, width, height);
    }  private void loadDefaultSprite()
    {
        Texture texture = Utility.getTextureAsset(defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0,0,FRAME_WIDTH, FRAME_HEIGHT);
        currentFrame = textureFrames[0][0];
    }
    private void loadAllAnimations(){
        //Walking animation
        Texture texture = Utility.getTextureAsset(defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        walkUpFrames = new Array<TextureRegion>(39);
        for (int i=0, j = 0; j < 39; j++) {
           TextureRegion region = textureFrames[j][i];
           if( region == null ){
              Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j);
           }
           walkUpFrames.insert(j, region);
        }
        walkUpAnimation = new Animation(0.08f, walkUpFrames,Animation.PlayMode.LOOP);
    }

    public void dispose(){
        Utility.unloadAsset(defaultSpritePath);
    }

    public void setState(State state){
        this.state = state;
    }

    public Sprite getFrameSprite(){
        return frameSprite;
    }

    public TextureRegion getFrame(){
        return currentFrame;
    }

    public Vector2 getCurrentPosition(){
        return currentPlayerPosition;
    }

    public void setCurrentPosition(float currentPositionX, float currentPositionY){
        frameSprite.setX(currentPositionX);
        frameSprite.setY(currentPositionY);
        this.currentPlayerPosition.x = currentPositionX;
        this.currentPlayerPosition.y = currentPositionY;
    }

    public void setDirection(Direction direction,  float deltaTime){
        this.previousDirection = this.currentDirection;
        this.currentDirection = direction;
        //Look into the appropriate variable when changing position
        switch (currentDirection) {
            case UP :
                currentFrame = (TextureRegion) walkUpAnimation.getKeyFrame(frameTime);
                break;
            default:
                break;
        }
    }
    public void setNextPositionToCurrent(){
        setCurrentPosition(nextPlayerPosition.x, nextPlayerPosition.y);
    }

    public void calculateNextPosition(Direction currentDirection, float deltaTime){
        float testX = currentPlayerPosition.x;
        float testY = currentPlayerPosition.y;
        velocity.scl(deltaTime);
        switch (currentDirection) {
            case UP :
                testY += velocity.y;
                break;
            default:
                break;
        }
    nextPlayerPosition.x = testX;
    nextPlayerPosition.y = testY;
    //velocity
    velocity.scl(1 / deltaTime);
}
}