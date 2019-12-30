package com.test.imageload.kotlin

import java.io.Serializable

/**
 * 创建一个只包含数据的类
 *
 * 数据类需要满足以下条件：
 * 主构造函数至少包含一个参数。
 * 所有的主构造函数的参数必须标识为val 或者 var ;
 * 数据类不可以声明为 abstract, open, sealed 或者 inner;
 * 数据类不能继承其他类 (但是可以实现接口)。
 */
data class User constructor(var name: String = "") : Serializable {

    constructor(address: Address, isNonProfit: Boolean, links: List<Link>,
                name: String,
                page: Int,
                url: String) : this(name)

    init {

    }
}

data class Address(
        var city: String = "",
        var country: String = "",
        var street: String = ""
)

data class Link(
        var name: String = "",
        var url: String = ""
)