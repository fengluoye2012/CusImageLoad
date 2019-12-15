package com.test.imageload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.test.imageload.kotlin.KotlinActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends Activity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textView)
    public void onViewClicked() {
        Activity act = this;
        Intent intent = new Intent(act, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.splash)
    public void onSplashViewClicked() {
        Activity act = this;
        Intent intent = new Intent(act, KotlinActivity.class);
        startActivity(intent);
    }
}
