package com.test.imageload;

import com.test.imageload.download.OnDownloadListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpUtils {

    private final OkHttpClient okHttpClient;
    private static OkHttpUtils instance;

    private OkHttpUtils() {
        okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }


    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }


    public void downLoadByUrl(String url, OnDownloadListener listener) {

    }
}
