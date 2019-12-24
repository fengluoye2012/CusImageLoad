package com.test.imageload.kotlin

/**
 *
 */
data class User(
        var address: Address = Address(),
        var isNonProfit: Boolean = false,
        var links: List<Link> = listOf(),
        var name: String = "",
        var page: Int = 0,
        var url: String = ""
)

data class Address(
        var city: String = "",
        var country: String = "",
        var street: String = ""
)

data class Link(
        var name: String = "",
        var url: String = ""
)