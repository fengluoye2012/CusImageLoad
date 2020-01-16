package com.test.imageload.utils

import com.test.imageload.kotlin.UserInfoBean

/**
 * companion object {}中用来修饰 静态常量，或者静态方法，单例等等
 *
 * 单例模式
 * https://www.jianshu.com/p/5797b3d0ebd0
 */
public class DataUtil private constructor() {//私有构造函数

    //饿汉模式--线程安全
    companion object {

        private var instance: DataUtil? = null
            get() {
                if (field == null) {
                    field = DataUtil()
                }
                return field
            }

        @Synchronized
        fun get(): DataUtil {
            return instance!!
        }
    }

    public fun generateData(): MutableList<UserInfoBean> {
        var list: ArrayList<UserInfoBean> = ArrayList()
        for (i in 1..15) {
            var str = "风落叶$i"
            list.add(UserInfoBean(str))
        }
        return list
    }


}