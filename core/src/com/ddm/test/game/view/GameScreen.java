package com.ddm.test.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ddm.test.game.model.Background;
import com.ddm.test.game.model.Plane;

public class GameScreen implements Screen {

    public static final double TEEWS = 0.04; // opozdanie camery

    private SpriteBatch batch;
    private Texture texture;
    private Texture backgroundTexture;
    private Plane plane;
    private Background background;
    private OrthographicCamera camera;
    public static float deltaCff;

    @Override
    public void show() {
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("core/assets/hero_sprites.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        plane = new Plane(texture, 3,3, 1f, 1.5f, 1, 39);
        backgroundTexture = new Texture(Gdx.files.internal("core/assets/background.jpeg"));
        background = new Background(backgroundTexture, 0, 0, backgroundTexture.getWidth()/200, backgroundTexture.getHeight()/200, 1, 1);
    }

    @Override
    public void render(float delta) {
        deltaCff = delta;
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.draw(batch);
        plane.draw(batch);
        batch.end();
        setPositionCamera();
        camera.update();

    }

    private void setPositionCamera() {
        float x = (float) (camera.position.x +((plane.getBounds().getX() - camera.position.x) * TEEWS));
        float y = (float) (camera.position.y +((plane.getBounds().getY() - camera.position.y) * TEEWS));
        if(x < camera.viewportWidth/2) {
            x = camera.viewportWidth/2;
        }
        if(y <  camera.viewportHeight/2) {
            y = camera.viewportHeight/2;
        }
        if(x > background.getBounds().getOriginX() - camera.viewportWidth/2) {
            x = background.getBounds().getOriginX() - camera.viewportWidth/2;
        }
        if(y >  background.getBounds().getOriginY() - camera.viewportHeight/2) {
            y = background.getBounds().getOriginY() - camera.viewportHeight/2;
        }
        camera.position.set(x,y, 0);
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
        batch.dispose();
    }
}
