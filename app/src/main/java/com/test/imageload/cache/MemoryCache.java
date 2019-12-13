package com.test.imageload.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

public class MemoryCache {


    private final BitmapLruCache<String, Bitmap> bitmapLruCache;

    public MemoryCache() {
        getBitmapCacheSize();
        bitmapLruCache = new BitmapLruCache<>(getBitmapCacheSize());
    }

    private int getBitmapCacheSize() {
        ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = 0;
        if (activityManager != null) {
            memoryClass = activityManager.getMemoryClass();
        }
        return memoryClass / 5;
    }

    public void put(String url) {

    }

    public void get(String url) {

    }
}
