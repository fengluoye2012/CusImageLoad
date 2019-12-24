package com.test.imageload.lifecycle

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ConvertUtils

//open 表示可继承，默认情况下是final 的 不可继承
open class LifeCycleActivity : AppCompatActivity() {

    var act: AppCompatActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        act = this
        var str = "绑定生命周期"
        var textView: TextView = TextView(act)

        textView.gravity = Gravity.CENTER
        textView.setText(str)

        setContentView(textView)

        val layoutParams = textView.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ConvertUtils.dp2px(50F)
        textView.layoutParams

        var test: Test = Test()
        LifeCycleDetector.getInstance().observer(this, test)
    }
}