package com.test.imageload

import android.os.Bundle
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity

import com.test.imageload.imageload.ImageLoad

import butterknife.BindView
import butterknife.ButterKnife

class MainActivity : AppCompatActivity() {

    @BindView(R.id.imageView)
    internal var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        val url = "https://file.yyuehd.com/FuCLS2fyBA4ZcVAHry_meMMG3RW4"
        ImageLoad.loadImage(url, imageView)
    }
}
