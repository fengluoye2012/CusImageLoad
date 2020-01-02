package com.test.imageload.picture.cache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.LruCache;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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

//        Activity activity = null;
//        RequestBuilder<Bitmap> bitmapRequestBuilder = Glide.with(activity).addDefaultRequestListener(new RequestListener<Object>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Object> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Object resource, Object model, Target<Object> target, DataSource dataSource, boolean isFirstResource) {
//                return false;
//            }
//        }).asBitmap().addListener(new RequestListener<Bitmap>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                return false;
//            }
//        });

        return super.sizeOf(key, value);
    }
}
