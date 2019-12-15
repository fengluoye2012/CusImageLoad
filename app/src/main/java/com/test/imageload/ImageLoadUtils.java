package com.test.imageload;

import com.blankj.utilcode.util.EncryptUtils;

public class ImageLoadUtils {

    public static String urlToMd5(String url) {
        return EncryptUtils.encryptMD5ToString(url).toLowerCase();
    }
}
