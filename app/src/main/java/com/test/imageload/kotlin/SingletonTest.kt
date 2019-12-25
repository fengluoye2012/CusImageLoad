package com.test.imageload.kotlin

/**
 * kotlin 单例
 *
 * 私有构造函数
 */
class SingletonTest private constructor() {

    //初始化变量
    private var instance: SingletonTest? = null

    /**
     * 懒汉式单例模式，双重锁
     */
    public fun getInstance(): SingletonTest? {
        if (instance == null) {
            synchronized(SingletonTest::class) {
                if (instance == null) {
                    instance = SingletonTest()
                }
            }
        }
        return instance
    }

    private var instance1: SingletonTest = SingletonTest();

    public fun getInstance1(): SingletonTest {
        return instance1;
    }


}