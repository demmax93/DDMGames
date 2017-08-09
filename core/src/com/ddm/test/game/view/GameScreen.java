package com.ddm.test.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ddm.test.game.model.TestObject;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Texture texture;
    private TestObject testObject;

    @Override
    public void show() {
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("core/assets/badlogic.jpg"));
        testObject = new TestObject(texture, 0,0, 256, 256);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        testObject.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

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
