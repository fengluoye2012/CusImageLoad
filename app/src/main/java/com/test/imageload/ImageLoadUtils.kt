package com.test.imageload

import com.blankj.utilcode.util.EncryptUtils

object ImageLoadUtils {

    fun urlToMd5(url: String): String {
        return EncryptUtils.encryptMD5ToString(url).toLowerCase()
    }
}
