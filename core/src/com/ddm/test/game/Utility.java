package com.ddm.test.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * DMKA
 * 25.08.2017
 */
public final class Utility {
    private static final String TAG = Utility.class.getSimpleName();
    private static InternalFileHandleResolver filePathResolver = new InternalFileHandleResolver();

    public static final AssetManager assetManager = new AssetManager();

    public static void unloadAsset(String assetFileNamePath) {
        if (assetManager.isLoaded(assetFileNamePath)) {
            assetManager.unload(assetFileNamePath);
        } else {
            Gdx.app.debug(TAG, "Asset is not loading: " + assetFileNamePath);
        }
    }

    public static float loadCompleted() {
        return assetManager.getProgress();
    }

    public static int numberAssetsQueued() {
        return assetManager.getQueuedAssets();
    }

    public static boolean updateAssetLoading() {
        return assetManager.update();
    }

    public static boolean isAssetLoaded(String fileName) {
        return assetManager.isLoaded(fileName);
    }

    public static void loadMapAsset(String mapFilenamePath) {
        if (mapFilenamePath == null || mapFilenamePath.isEmpty()) {
            return;
        }
        if (filePathResolver.resolve(mapFilenamePath).exists()) {
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
            assetManager.load(mapFilenamePath, TiledMap.class);
            assetManager.finishLoadingAsset(mapFilenamePath);
            Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Map doesn’t exist!: " + mapFilenamePath);
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        TiledMap map = null;
        // once the asset manager is done loading
        if (assetManager.isLoaded(mapFilenamePath)) {
            map = assetManager.get(mapFilenamePath, TiledMap.class);
        } else {
            Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath);
        }
        return map;
    }

    public static void loadTextureAsset(String textureFilenamePath) {
        if (textureFilenamePath == null ||
                textureFilenamePath.isEmpty()) {
            return;
        }
        if (filePathResolver.resolve(textureFilenamePath).exists()) {
            assetManager.setLoader(Texture.class,
                    new TextureLoader(filePathResolver));
            assetManager.load(textureFilenamePath, Texture.class);
            assetManager.finishLoadingAsset(textureFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Texture doesn’t exist!: " + textureFilenamePath);
        }
    }

    public static Texture getTextureAsset(String textureFilenamePath) {
        Texture texture = null;
        if (assetManager.isLoaded(textureFilenamePath)) {
            texture =
                    assetManager.get(textureFilenamePath, Texture.class);
        } else {
            Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath);
        }
        return texture;
    }
}
