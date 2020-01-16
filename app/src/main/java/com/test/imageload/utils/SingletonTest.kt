package com.test.imageload.utils

/**
 * 双重校验锁式
 */
class SingletonTest private constructor() {

    companion object {
        val instance: SingletonTest by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SingletonTest()
        }
    }
}