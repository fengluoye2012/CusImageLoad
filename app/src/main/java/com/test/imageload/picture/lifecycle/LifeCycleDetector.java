package com.test.imageload.picture.lifecycle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class LifeCycleDetector {
    private static LifeCycleDetector instance = null;

    private final String FRAGMENT_TAG = "com.test.imageload.picture.lifecycle";

    public static LifeCycleDetector getInstance() {
        if (instance == null) {
            synchronized (LifeCycleDetector.class) {
                if (instance == null) {
                    instance = new LifeCycleDetector();
                }
            }
        }
        return instance;
    }


    public void observer(AppCompatActivity act, LifeCycleListener listener) {
        FragmentManager fm = act.getSupportFragmentManager();
        //注册无UI 的fragment
        RequestManagerFragment curFragment = getRequestManagerFragment(fm);
        //设置监听，一旦activity 的生命周期变化，就调用listener
        curFragment.getLifeCycle().addListener(listener);
    }

    private RequestManagerFragment getRequestManagerFragment(FragmentManager fragmentManager) {
        RequestManagerFragment curFragment = (RequestManagerFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (curFragment == null) {
            curFragment = new RequestManagerFragment();
            fragmentManager.beginTransaction().add(curFragment, FRAGMENT_TAG).commitAllowingStateLoss();
        }
        return curFragment;
    }

}
