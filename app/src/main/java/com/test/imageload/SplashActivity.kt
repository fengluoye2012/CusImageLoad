package com.test.imageload

import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.test.imageload.base.BaseActivity
import com.test.imageload.db.BaseDaoFactory
import com.test.imageload.db.DbTest
import com.test.imageload.db.UserInfo
import com.test.imageload.db.UserInfoDao
import com.test.imageload.kotlin.MainActivity
import com.test.imageload.picture.lifecycle.LifeCycleActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    @OnClick(R.id.splash)
    fun onSplashViewClicked() {
        val intent = Intent(act, MainActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.tv_glide)
    fun onGlideViewClicked() {
        val intent = Intent(act, LifeCycleActivity::class.java)
        startActivity(intent)
    }
}
