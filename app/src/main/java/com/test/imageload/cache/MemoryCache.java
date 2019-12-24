package com.test.imageload.cache;

import android.graphics.Bitmap;

import com.test.imageload.ImageLoadUtils;

public class MemoryCache {

    private final BitmapLruCache<String, Bitmap> bitmapLruCache;
    private static MemoryCache instance;

    private MemoryCache() {
        getBitmapCacheSize();
        bitmapLruCache = new BitmapLruCache<>(getBitmapCacheSize());
    }

    public static MemoryCache getInstance() {
        if (instance == null) {
            synchronized (MemoryCache.class) {
                if (instance == null) {
                    instance = new MemoryCache();
                }
            }
        }
        return instance;
    }

    private int getBitmapCacheSize() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        return maxMemory / 5;
    }

    public synchronized void put(String url, Bitmap bitmap) {
        if (bitmap != null) {
            bitmapLruCache.put(ImageLoadUtils.INSTANCE.urlToMd5(url), bitmap);
        }
    }

    public Bitmap get(String url) {
        return bitmapLruCache.get(ImageLoadUtils.INSTANCE.urlToMd5(url));
    }

    public boolean exist(String url) {
        return bitmapLruCache.get(url) != null;
    }
}
