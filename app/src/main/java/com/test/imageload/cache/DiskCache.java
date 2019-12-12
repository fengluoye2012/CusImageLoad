package com.test.imageload.cache;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.PathUtils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DiskCache {

    private static DiskLruCache diskLruCache;


    //文件缓存路径（/data/data/package/cache）
    public static String diskCacheDir = PathUtils.getInternalAppCachePath() + File.separator + "disk";


    public DiskCache() {
        try {
            diskLruCache = DiskLruCache.open(new File(diskCacheDir), 1, Integer.MAX_VALUE, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getDiskCacheDir() {
        return diskCacheDir;
    }

    public static void setDiskCacheDir(String diskCacheDir) {
        DiskCache.diskCacheDir = diskCacheDir;
    }


    public static String generateFilePath(String url) {
        return DiskCache.diskCacheDir + "/" + urlMd5(url);
    }

    public static String urlMd5(String url) {
        return EncryptUtils.encryptMD5ToString(url);
    }

    public static void put(String url) throws IOException {
        //创建Editor 对象
        DiskLruCache.Editor edit = diskLruCache.edit(urlMd5(url));

        if (edit != null) {
            OutputStream outputStream = edit.newOutputStream(0);
            downloadUrlToStream(url, outputStream);
        }
    }


    /**
     * 从网络中下载图片，并写到缓存中
     *
     * @param urlString
     * @param outputStream
     * @return
     */
    private static boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
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
