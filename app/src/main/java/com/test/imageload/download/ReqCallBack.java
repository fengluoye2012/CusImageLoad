package com.test.imageload.download;

import okhttp3.Response;

public interface ReqCallBack {

    void onFailure();

    void onSuccess(Response response);
}
