package com.test.imageload.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.PathUtils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

public class DiskCache {

    private static DiskCache instance;
    private static DiskLruCache diskLruCache;

    //文件缓存路径（/data/data/package/cache）
    public static String diskCacheDir = PathUtils.getInternalAppCachePath() + File.separator + "imageLoad";


    private DiskCache() {
        try {
            //四个参数分别为，1.缓存的路径目录 2.版本号 3.每个节点对应的数据个数，4.缓存的大小，10 * 1024 * 1024 = 10M
            diskLruCache = DiskLruCache.open(new File(diskCacheDir), 1, Integer.MAX_VALUE, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     *
     * @param imageView
     * @param url
     */
    public void get(ImageView imageView, String url) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = diskLruCache.get(generateFileName(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } else {
            put(url);
        }
    }

    public void put(final String url) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                downloadUrlToStream(url);
            }
        });
    }

    private void downloadUrlToStream(String url) {
        try {
            //创建Editor 对象
            DiskLruCache.Editor edit = diskLruCache.edit(generateFileName(url));

            if (edit != null) {
                //创建输出流
                OutputStream outputStream = edit.newOutputStream(0);
                if (downloadUrlToStream(url, outputStream)) {
                    edit.commit();
                } else {
                    //释放编辑锁
                    edit.abort();
                }
                diskLruCache.flush();
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
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
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


    public String getDiskCacheDir() {
        return diskCacheDir;
    }

    public void setDiskCacheDir(String diskCacheDir) {
        DiskCache.diskCacheDir = diskCacheDir;
    }


    private String generateFileName(String url) {
        return EncryptUtils.encryptMD5ToString(url);
    }

}
