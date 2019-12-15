package com.test.imageload.download;

import android.os.Handler;
import android.os.Looper;

import com.jakewharton.disklrucache.DiskLruCache;
import com.test.imageload.cache.DiskCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * 从网络下载图片资源，写入到磁盘中
 */
public class DownLoadUtils {

    private static DownLoadUtils instance;
    private final Handler handler;

    private DownLoadUtils() {
        handler = new Handler(Looper.getMainLooper());
    }


    public static DownLoadUtils getInstance() {
        if (instance == null) {
            synchronized (DownLoadUtils.class) {
                instance = new DownLoadUtils();
            }
        }
        return instance;
    }

    public void downLoadAsync(final String url, OnDownloadListener listener) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                downFormNet(url, listener);
            }
        });
    }

    protected void downFormNet(String url, OnDownloadListener listener) {
        DiskLruCache.Editor editor = DiskCache.getInstance().getEditor(url);
        try {
            if (editor != null) {
                //创建输出流，由于open()方法中valueCount 的值为1，所以传0；
                OutputStream outputStream = editor.newOutputStream(0);
                if (downloadUrlToStream(url, outputStream, listener)) {
                    editor.commit();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadSuccess();
                        }
                    });
                } else {
                    //释放编辑锁
                    editor.abort();
                }
                //diskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从网络中下载图片，并写到缓存中
     *
     * @param urlString    下载图片的url
     * @param outputStream 的作用在于从网络下载图片的时候，图片通过该输出流写到文件系统，也就是说图片下到了磁盘缓存中；
     * @return
     */
    protected boolean downloadUrlToStream(String urlString, OutputStream outputStream, OnDownloadListener listener) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
