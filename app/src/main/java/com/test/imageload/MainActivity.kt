package com.test.imageload

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.test.imageload.imageload.ImageLoad
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var act: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = this
        setContentView(R.layout.activity_main)

        val url = "https://file.yyuehd.com/FuCLS2fyBA4ZcVAHry_meMMG3RW4"
        ImageLoad.loadImage(url, imageView)

        //加载图片
        Glide.with(act as MainActivity).load(url).into(iv_glide)
    }
}
