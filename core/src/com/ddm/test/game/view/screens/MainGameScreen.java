package com.ddm.test.game.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.ddm.test.game.MapManager;
import com.ddm.test.game.control.PlayerController;
import com.ddm.test.game.model.Entity;

public class MainGameScreen implements Screen {
    private static final String TAG =
            MainGameScreen.class.getSimpleName();

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private PlayerController controller;
    private TextureRegion currentPlayerFrame;
    private Sprite currentPlayerSprite;
    private OrthogonalTiledMapRenderer mapRenderer = null;
    private OrthographicCamera camera = null;
    private static MapManager mapMgr;
    private static TiledMapTileLayer layerMap = null;
    public static final double TEEWS = 0.04; // opozdanie camery


    public MainGameScreen() {
        mapMgr = new MapManager();
    }

    private static Entity player;

    @Override
    public void show() {
        //camera setup
        setupViewport(10, 10);
        //get the current size
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentMap(), MapManager.UNIT_SCALE);
        mapRenderer.setView(camera);
        Gdx.app.debug(TAG, "UnitScale value is:" + mapRenderer.getUnitScale());
        player = new Entity();
        player.init(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y);
        currentPlayerSprite = player.getFrameSprite();
        controller = new PlayerController(player);
        Gdx.input.setInputProcessor(controller);
        layerMap = (TiledMapTileLayer) mapMgr.getCurrentMap().getLayers().get(0);
    }

    @Override
    public void hide() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        setCameraPosition();
        player.update(delta);
        currentPlayerFrame = player.getFrame();
        updatePortalLayerActivation(player.boundingBox);
        if (!isCollisionWithMapLayer(player.boundingBox)) {
            player.setNextPositionToCurrent();
        }
        controller.update(delta);
        mapRenderer.setView(camera);
        mapRenderer.render();
        mapRenderer.getBatch().begin();
        mapRenderer.getBatch().draw(currentPlayerFrame, currentPlayerSprite.getX(), currentPlayerSprite.getY(), 1, 1);
        mapRenderer.getBatch().end();
    }

    private void setCameraPosition() {
        float x = (float) (camera.position.x + ((currentPlayerSprite.getX() - camera.position.x) * TEEWS));
        float y = (float) (camera.position.y + ((currentPlayerSprite.getY() - camera.position.y) * TEEWS));
        if (x < camera.viewportWidth / 2) {
            x = camera.viewportWidth / 2;
        }
        if (y < camera.viewportHeight / 2) {
            y = camera.viewportHeight / 2;
        }
        if (x > layerMap.getTileWidth() * layerMap.getWidth() / 100 - camera.viewportWidth / 2) {
            x = layerMap.getTileWidth() * layerMap.getWidth() / 100 - camera.viewportWidth / 2;
        }
        if (y > layerMap.getTileHeight() * layerMap.getHeight() / 100 - camera.viewportHeight / 2) {
            y = layerMap.getTileHeight() * layerMap.getHeight() / 100 - camera.viewportHeight / 2;
        }
        camera.position.set(x, y, 0);
        camera.update();
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
    public void dispose() {
        player.dispose();
        controller.dispose();
        Gdx.input.setInputProcessor(null);
        mapRenderer.dispose();
    }

    private void setupViewport(int width, int height) {
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;
        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();
        //aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);
        //update viewport if there could be skewing
        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }
        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")");
    }

    private boolean isCollisionWithMapLayer(Rectangle boundingBox) {
        MapLayer mapCollisionLayer = mapMgr.getCollisionLayer();
        if (mapCollisionLayer == null) {
            return false;
        }
        Rectangle rectangle = null;
        for (MapObject object : mapCollisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rectangle)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean updatePortalLayerActivation(Rectangle boundingBox) {
        MapLayer mapPortalLayer = mapMgr.getPortalLayer();
        if (mapPortalLayer == null) {
            return false;
        }
        Rectangle rectangle = null;
        for (MapObject object : mapPortalLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rectangle)) {
                    String mapName = object.getName();
                    if (mapName == null) {
                        return false;
                    }
                    mapMgr.setClosestStartPositionFromScaledUnits(player.getCurrentPosition());
                    mapMgr.loadMap(mapName);
                    player.init(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y);
                    mapRenderer.setMap(mapMgr.getCurrentMap());
                    Gdx.app.debug(TAG, "Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }

    public static TiledMapTileLayer getLayerMap() {
        return layerMap;
    }
}