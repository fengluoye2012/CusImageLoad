package com.test.imageload.picture.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapLruCache<Q, K> extends LruCache<Q, K> {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }


    @Override
    protected int sizeOf(Q key, K value) {
        //如果是BitMap 则返回字节数；
        if (value instanceof Bitmap) {
            return ((Bitmap) value).getByteCount();
        }
        return super.sizeOf(key, value);
    }
}
