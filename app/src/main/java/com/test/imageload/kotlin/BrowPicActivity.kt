package com.test.imageload.kotlin

import com.bumptech.glide.Glide
import com.test.imageload.R
import com.test.imageload.base.BaseActivity
import kotlinx.android.synthetic.main.layout_brow_pic_activity.*

class BrowPicActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.layout_brow_pic_activity
    }

    override fun initData() {
        super.initData()

        val url = "http://file02.16sucai.com/d/file/2015/0128/8b0f093a8edea9f7e7458406f19098af.jpg"
        act?.let { Glide.with(it).load(url).into(photoView) }
    }

}