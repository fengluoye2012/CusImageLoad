package com.test.imageload.lifecycle;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * 创建一个空UI 的fragment 和 Activity 的生命周期绑定
 */
public class RequestManagerFragment extends Fragment {

    private ActivityFragmentLifeCycle lifeCycle;

    public RequestManagerFragment() {
        this(new ActivityFragmentLifeCycle());
    }

    public RequestManagerFragment(ActivityFragmentLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public ActivityFragmentLifeCycle getLifeCycle() {
        return lifeCycle;
    }


    @Override
    public void onStart() {
        super.onStart();
        lifeCycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifeCycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeCycle.onDestroy();
    }
}
