package com.test.imageload.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.Scroller
import com.blankj.utilcode.util.LogUtils

/**
 * 自定义View
 *
 * scrollBy()、scrollTo() 滚动的是View的内容,不能改变当前View 的坐标
 */
class ScrollRelativeView : RelativeLayout, View.OnTouchListener {

    private var gestureDetector: GestureDetector? = null
    private var animator: ObjectAnimator? = null
    private var scroller: Scroller? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LogUtils.i("init")

        scroller = Scroller(context)

        setOnTouchListener(this)
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override
            fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                LogUtils.i("onScroll")
                translation(-distanceY)
                return true
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        LogUtils.i("onTouchEvent")
        gestureDetector!!.onTouchEvent(event)
        return true
    }

    //将View 做竖直方向上滑动
    private fun translation(moveY: Float) {
        LogUtils.i("moveY：：$moveY")
        scrollBy(0, -moveY.toInt())
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

    }
}