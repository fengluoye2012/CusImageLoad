package com.test.imageload.kotlin

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class CusRequestListener : RequestListener<Any> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Any>?, isFirstResource: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }


    override fun onResourceReady(resource: Any?, model: Any?, target: Target<Any>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }
}