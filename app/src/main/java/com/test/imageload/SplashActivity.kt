package com.test.imageload

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.test.imageload.kotlin.KotlinActivity
import com.test.imageload.picture.lifecycle.LifeCycleActivity

class SplashActivity : Activity() {

    private var act: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = this
        setContentView(R.layout.activity_splash)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.textView)
    fun onViewClicked() {
        //通过Intent 开启Activity
        val intent = Intent(act, MainActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.splash)
    fun onSplashViewClicked() {
        val intent = Intent(act, KotlinActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.tv_glide)
    fun onGlideViewClicked() {
        val intent = Intent(act, LifeCycleActivity::class.java)
        startActivity(intent)
    }
}
