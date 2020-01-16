package com.test.imageload.kotlin

import android.view.Gravity
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.blankj.utilcode.util.ScreenUtils
import com.test.imageload.R
import com.test.imageload.base.BaseActivity
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }


    override fun initData() {
        super.initData()

        changeWindowHeight((ScreenUtils.getScreenHeight() * 0.9).toInt())

        textView.setOnClickListener {
            //changeWindowHeight((ScreenUtils.getScreenHeight() * 0.6).toInt())
        }

    }


    private fun changeWindowHeight(height: Int) {
        val p = window.attributes
        p.height = height
        p.width = ScreenUtils.getScreenWidth()
        p.gravity = Gravity.BOTTOM
        window.attributes = p
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }


    override fun finish() {
        super.finish()

    }
}
