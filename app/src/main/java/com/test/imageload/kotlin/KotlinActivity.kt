package com.test.imageload.kotlin

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.test.imageload.R

class KotlinActivity : Activity() {

    //可变变量定义：var 关键字
    private val age: Int = 12

    //不可变变量定义：val 关键字，只能赋值一次的变量(类似Java中final修饰的变量)
    val name: String = "风落叶"

    //类型后面加?表示可为null；
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_konlin)
        textView = findViewById<TextView>(R.id.textView)
        textView?.setText(name)


        var user: User = User("fengluoye")
        LogUtils.i(user.name)
    }

    //参数是Int 类型的a,b相加 返回
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    fun printStr(a: Int) {
        var str: String = "" + a
        LogUtils.i(str)
    }

}
