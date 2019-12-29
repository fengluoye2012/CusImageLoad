package com.test.imageload.picture.lifecycle;

import com.blankj.utilcode.util.LogUtils;

public class Test implements LifeCycleListener {

    @Override
    public void onStart() {
        LogUtils.i("onStart");
    }

    @Override
    public void onStop() {
        LogUtils.i("onStop");
    }

    @Override
    public void onDestroy() {
        LogUtils.i("onDestroy");
    }
}
