package com.test.imageload.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 抽象类
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initData()
    }

    abstract fun getLayoutId(): Int

    open fun initData() {}
}