package com.test.imageload.imageload;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.test.imageload.cache.DiskCache;
import com.test.imageload.cache.MemoryCache;
import com.test.imageload.download.DownLoadUtils;
import com.test.imageload.download.OnDownloadListener;

public class ImageLoad {

    public static void loadImage(String url, ImageView imageView) {
        //先去内存缓存中去查询
        Bitmap bitmap = MemoryCache.getInstance().get(url);

        if (bitmap != null) {
            LogUtils.i("从 内存中 获取内容");
        }

        //再去磁盘缓存中查询
        if (bitmap == null) {
            bitmap = DiskCache.getInstance().get(url);
        }

        if (bitmap != null) {
            MemoryCache.getInstance().put(url, bitmap);
            LogUtils.i("从 磁盘中 获取内容");
        }

        if (bitmap == null) {
            DownLoadUtils.getInstance().downLoadAsync(url, new OnDownloadListener() {
                @Override
                public void startDownload() {

                }

                @Override
                public void onDownloading(int progress) {

                }

                @Override
                public void onDownloadSuccess() {
                    imageView.setImageBitmap(DiskCache.getInstance().get(url));
                }

                @Override
                public void onDownloadFailed() {

                }
            });
        } else {
            imageView.setImageBitmap(bitmap);
        }

    }
}
