package com.test.imageload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.test.imageload.kotlin.KotlinActivity;
import com.test.imageload.lifecycle.LifeCycleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends Activity {

    @BindView(R.id.textView)
    TextView textView;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textView)
    public void onViewClicked() {

        Intent intent = new Intent(act, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.splash)
    public void onSplashViewClicked() {
        Intent intent = new Intent(act, KotlinActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_glide)
    public void onGlideViewClicked() {
        Intent intent = new Intent(act, LifeCycleActivity.class);
        startActivity(intent);
    }
}
