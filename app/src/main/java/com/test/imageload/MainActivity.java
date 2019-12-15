package com.test.imageload;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.test.imageload.imageload.ImageLoad;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String url = "https://file.yyuehd.com/FuCLS2fyBA4ZcVAHry_meMMG3RW4";
        ImageLoad.loadImage(url, imageView);
    }
}
