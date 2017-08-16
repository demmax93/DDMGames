package com.ddm.test.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ddm.test.game.model.Background;
import com.ddm.test.game.model.Brick;
import com.ddm.test.game.model.Plane;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    public static final double TEEWS = 0.04; // opozdanie camery

    private SpriteBatch batch;
    private Texture texture;
    private Texture backgroundTexture;
    private Texture textureBrick;
    private Plane plane;
    private Background background;
    private OrthographicCamera camera;
    public static float deltaCff;
    private List<Brick> bricks;
    private ParticleEffect firstFlame;
    private ParticleEffect secondFlame;

    @Override
    public void show() {
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("core/assets/texture/hero_sprites.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        plane = new Plane(texture, 3,3, texture.getWidth()/100f, texture.getHeight()/5850f, 1, 39);
        backgroundTexture = new Texture(Gdx.files.internal("core/assets/texture/background.jpeg"));
        background = new Background(backgroundTexture, 0, 0, backgroundTexture.getWidth()/200, backgroundTexture.getHeight()/200, 1, 1);
        bricks = new ArrayList<>();
        textureBrick = new Texture(Gdx.files.internal("core/assets/texture/badlogic.jpg"));
        bricks.add(new Brick(textureBrick, 8f, 8f, textureBrick.getWidth()/150, textureBrick.getHeight()/150, 1, 1));
        bricks.add(new Brick(textureBrick, 15f, 15f, textureBrick.getWidth()/100, textureBrick.getHeight()/100, 1, 1));
        bricks.add(new Brick(textureBrick, 14f, 7f, textureBrick.getWidth()/200, textureBrick.getHeight()/200, 1, 1));
        bricks.add(new Brick(textureBrick, 3f, 11f, textureBrick.getWidth()/125, textureBrick.getHeight()/125, 1, 1));

        firstFlame = new ParticleEffect();
        firstFlame.load(Gdx.files.internal("core/assets/sfx/plane_fire.p"), Gdx.files.internal("core/assets/texture/"));
        firstFlame.getEmitters().first().setPosition(plane.getBounds().getX(), plane.getBounds().getY());
        firstFlame.scaleEffect(.002f);
        firstFlame.start();

        secondFlame = new ParticleEffect();
        secondFlame.load(Gdx.files.internal("core/assets/sfx/plane_fire.p"), Gdx.files.internal("core/assets/texture/"));
        secondFlame.getEmitters().first().setPosition(plane.getBounds().getX(), plane.getBounds().getY());
        secondFlame.scaleEffect(.002f);
        secondFlame.start();
    }

    @Override
    public void render(float delta) {
        deltaCff = delta;
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        flameRotation(); //need fix
        batch.begin();
        background.draw(batch);
        bricks.forEach(brick -> brick.draw(batch));
        firstFlame.draw(batch, delta);
        secondFlame.draw(batch, delta);
        plane.draw(batch);
        plane.checkCollisionForBackground(background);
        bricks.forEach(brick -> plane.checkCollision(brick));
        batch.end();
        setPositionCamera();
        if (firstFlame.isComplete()) firstFlame.reset();
        if (secondFlame.isComplete()) secondFlame.reset();
    }

    private void setPositionCamera() {
        float x = (float) (camera.position.x +((plane.getBounds().getX() - camera.position.x) * TEEWS));
        float y = (float) (camera.position.y +((plane.getBounds().getY() - camera.position.y) * TEEWS));
        if(x < camera.viewportWidth/2) {
            x = camera.viewportWidth/2;
        }
        if(y < camera.viewportHeight/2) {
            y = camera.viewportHeight/2;
        }
        if(x > background.getBounds().getWidth() - camera.viewportWidth/2) {
            x = background.getBounds().getWidth() - camera.viewportWidth/2;
        }
        if(y > background.getBounds().getHeight() - camera.viewportHeight/2) {
            y = background.getBounds().getHeight() - camera.viewportHeight/2;
        }
        camera.position.set(x,y, 0);
        camera.update();
    }

    private void flameRotation(){
        firstFlame.setPosition(plane.getBounds().getX()+0.4f, plane.getBounds().getY()+0.2f);
        secondFlame.setPosition(plane.getBounds().getX()+0.6f, plane.getBounds().getY()+ 0.2f);
        firstFlame.getEmitters().first().getAngle().setHigh(plane.getBounds().getRotation()-90);
        firstFlame.getEmitters().first().getAngle().setLow(plane.getBounds().getRotation()-90);
        secondFlame.getEmitters().first().getAngle().setHigh(plane.getBounds().getRotation()-90);
        secondFlame.getEmitters().first().getAngle().setLow(plane.getBounds().getRotation()-90);
    }

    @Override
    public void resize(int width, int height) {
        float aspectRatio = (float) height / width;
        camera = new OrthographicCamera(20f, 20f * aspectRatio);
        camera.zoom = 0.9f;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        texture.dispose();
        textureBrick.dispose();
        batch.dispose();
    }
}
