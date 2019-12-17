package com.test.imageload

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.test.imageload.imageload.ImageLoad

class MainActivity : AppCompatActivity() {

    var imageView: ImageView? = null
    var ivGlide: ImageView? = null

    var act: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = this
        setContentView(R.layout.activity_main)

        imageView = findViewById<ImageView>(R.id.imageView)
        ivGlide = findViewById<ImageView>(R.id.iv_glide)

        val url = "https://file.yyuehd.com/FuCLS2fyBA4ZcVAHry_meMMG3RW4"
        //ImageLoad.loadImage(url, imageView)

        //加载图片
        ivGlide?.let { Glide.with(act as MainActivity).load(url).into(it) }
    }
}
