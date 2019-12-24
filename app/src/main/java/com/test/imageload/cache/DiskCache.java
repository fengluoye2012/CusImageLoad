package com.test.imageload.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PathUtils;
import com.jakewharton.disklrucache.DiskLruCache;
import com.test.imageload.ImageLoadUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DiskCache {

    private static DiskCache instance;
    private static DiskLruCache diskLruCache;
    private String diskCacheDir = "imageLoad";

    private DiskCache() {
        try {
            File cacheFile = getCacheDirectory(getDiskCacheDir());
            if (!cacheFile.exists()) {
                cacheFile.mkdir();
            }

            //四个参数分别为，1.缓存的路径目录 2.版本号 3.每个节点对应的数据个数，4.缓存的大小，10 * 1024 * 1024 = 10M
            //valueCount 要注意
            diskLruCache = DiskLruCache.open(cacheFile, AppUtils.getAppVersionCode(), 1, 1024 * 1024 * 500);
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
        }
    }

    protected File getCacheDirectory(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !Environment.isExternalStorageRemovable()) {
            cachePath = PathUtils.getExternalAppCachePath();
        } else { //  /data/data/package/cache
            cachePath = PathUtils.getInternalAppCachePath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    public static DiskCache getInstance() {
        if (instance == null) {
            synchronized (DiskCache.class) {
                if (instance == null) {
                    instance = new DiskCache();
                }
            }
        }
        return instance;
    }

    /**
     * @param url
     */
    public Bitmap get(String url) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = diskLruCache.get(ImageLoadUtils.INSTANCE.urlToMd5(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }

    //通过
    public DiskLruCache.Editor getEditor(String url) {
        //创建Editor 对象
        DiskLruCache.Editor edit = null;
        try {
            edit = diskLruCache.edit(ImageLoadUtils.INSTANCE.urlToMd5(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edit;
    }


    public String getDiskCacheDir() {
        return diskCacheDir;
    }

    public void setDiskCacheDir(String diskCacheDir) {
        this.diskCacheDir = diskCacheDir;
    }
}
