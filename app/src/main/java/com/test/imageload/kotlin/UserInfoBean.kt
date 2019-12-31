package com.test.imageload.kotlin

import java.io.Serializable

/**
 * 创建一个类、构造函数（一级构造函数、二级构造函数）、初始化代码、方法
 */
class UserInfoBean constructor(name: String) : Serializable {

    var name: String = ""
    var age: Int = 0
    var headIcon: String = ""

    //二级构造函数
    constructor(name: String, age: Int) : this(name) {
        this.age = age
    }

    init {
        this.name = name
    }

    //函数
    override fun toString(): String {
        return "UserInfoBean(name='$name', age=$age, headIcon='$headIcon')"
    }
}