package com.test.imageload.kotlin

import com.blankj.utilcode.util.LogUtils

//类 构造函数 get、set 方法
class User constructor(name: String) {

    constructor(name: String, age: Int, headUrl: String) : this(name) {

    }

    var name: String? = null
        get() = field
        set

    var age: Int = 0
        get() = field
        set(value) {
            if (value < 0) {
                field = 0
            } else {
                field = value
            }
        }

    var headUrl: String? = null
        get() = field
        private set


    init {
        LogUtils.i("init 方法")
    }

    fun printName() {
        LogUtils.i(name)
    }


}