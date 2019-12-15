package com.test.imageload;

import android.app.Application;

import com.test.imageload.cache.DiskCache;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DiskCache.getInstance();
    }
}
