package com.test.imageload

import com.bumptech.glide.Glide
import com.test.imageload.base.BaseActivity
import com.test.imageload.picture.imageload.ImageLoad
import com.test.imageload.picture.zoom.ZoomImageView
import kotlinx.android.synthetic.main.activity_cus_zoom_view.*

class CusZoomViewActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_cus_zoom_view
    }

    override fun initData() {
        super.initData()

        val url = "https://file.yyuehd.com/FuCLS2fyBA4ZcVAHry_meMMG3RW4"
        ImageLoad.loadImage(url, imageView)

        //加载图片
        Glide.with(act as CusZoomViewActivity).load(url).into(iv_glide)

        imageView.setSingleClickListener { finish() }
    }
}
