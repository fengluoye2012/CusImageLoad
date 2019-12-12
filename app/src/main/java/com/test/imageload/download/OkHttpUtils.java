package com.test.imageload.download;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.test.imageload.cache.DiskCache;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {

    private static OkHttpUtils instance;
    private OkHttpClient okHttpClient;
    private Handler handler;

    private OkHttpUtils() {
        initOkHttp();
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


    private void initOkHttp() {
        okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        handler = new Handler(Looper.getMainLooper());
    }

    public void downLoadBitmap(final String url, final OnDownloadListener downloadListener) {
        final Request request = new Request.Builder().url(url).build();

        downloadListener.startDownload();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadListener.onDownloadFailed();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //文件的总长度
                long total = response.body().contentLength();
                InputStream is = null;
                int len = 0;
                long sum = 0;
                byte[] buf = new byte[1024];
                FileOutputStream fos = null;

                try {
                    if (response.code() == 200) {
                        //创建文件
                        FileUtils.createOrExistsDir(DiskCache.getDiskCacheDir());
                        is = response.body().byteStream();

                        //创建文件
                        FileUtils.createOrExistsFile(DiskCache.generateFilePath(url));

                        File file = FileUtils.getFileByPath(DiskCache.generateFilePath(url));
                        fos = new FileOutputStream(file);

                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            downloadListener.onDownloading(progress);
                        }
                        fos.flush();
                        downloadListener.onDownloadSuccess();

                    } else {
                        downloadListener.onDownloadFailed();
                    }
                } catch (Exception e) {
                    downloadListener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        LogUtils.e(Log.getStackTraceString(e));
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        });
    }
}
