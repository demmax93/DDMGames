package com.ddm.test.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.ddm.test.game.model.Background;
import com.ddm.test.game.model.Plane;

public class GameScreen implements Screen {

    public static final double TEEWS = 0.04; // opozdanie camery

    private SpriteBatch batch;
    private Texture texture;
    private Plane plane;
    private Background background;
    private OrthographicCamera camera;
    public static float deltaCff;

    @Override
    public void show() {
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("core/assets/hero_sprites.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        plane = new Plane(texture, 0,0, 1f, 1.5f, 1, 39);
        background = new Background();
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
        float x = (float) (camera.position.x +((plane.getBounds().getX() - camera.position.x) * TEEWS));
        float y = (float) (camera.position.y +((plane.getBounds().getY() - camera.position.y) * TEEWS));
        camera.position.set(x,y, 0);
        camera.update();

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
        texture.dispose();
        batch.dispose();
    }
}
