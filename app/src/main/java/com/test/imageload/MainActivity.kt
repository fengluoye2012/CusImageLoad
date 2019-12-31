package com.test.imageload

import com.bumptech.glide.Glide
import com.test.imageload.base.BaseActivity
import com.test.imageload.picture.imageload.ImageLoad
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        super.initData()

        val url = "https://file.yyuehd.com/FuCLS2fyBA4ZcVAHry_meMMG3RW4"
        ImageLoad.loadImage(url, imageView)

        //加载图片
        Glide.with(act as MainActivity).load(url).into(iv_glide)
    }
}
