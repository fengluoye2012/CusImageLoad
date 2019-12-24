package com.test.imageload

import android.app.Application

import com.test.imageload.cache.DiskCache

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DiskCache.getInstance()
    }
}
