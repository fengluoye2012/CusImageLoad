package com.test.imageload

import android.app.Application
import android.content.Context

import com.test.imageload.picture.cache.DiskCache
import com.danikula.videocache.HttpProxyCacheServer

class MyApplication : Application() {

    public var proxy: HttpProxyCacheServer? = null

    companion object {
        public var app: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        DiskCache.getInstance()
    }


    public fun getProxyCache(): HttpProxyCacheServer? {
        if (proxy == null) {
            proxy = this.newProxy()
        }
        return proxy
    }

    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer(this)
    }

}


