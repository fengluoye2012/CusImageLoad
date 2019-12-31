package com.test.imageload.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 抽象类
 */
abstract class BaseActivity : AppCompatActivity() {

    protected var act: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = this

        setContentView(getLayoutId())

        initData()
    }

    abstract fun getLayoutId(): Int

    open fun initData() {}
}